package id.komorebi.service;

import id.komorebi.dao.DAOFactory;
import id.komorebi.dao.MenuItemDAO;
import id.komorebi.dao.OrderDAO;
import id.komorebi.dao.OrderItemDAO;
import id.komorebi.model.CafeTable;
import id.komorebi.model.MenuItem;
import id.komorebi.model.Order;
import id.komorebi.model.OrderItem;
import id.komorebi.model.enums.OrderItemStatus;
import id.komorebi.model.enums.OrderStatus;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class OrderService {
    private final OrderDAO orderDAO;
    private final OrderItemDAO orderItemDAO;
    private final MenuItemDAO menuItemDAO;
    private final id.komorebi.dao.PaymentDAO paymentDAO;
    private final id.komorebi.dao.InventoryDAO inventoryDAO;
    private final id.komorebi.dao.ReceiptDAO receiptDAO = DAOFactory.createReceiptDao();
    
    public OrderService() {
        this.orderDAO = DAOFactory.createOrderDao();
        this.orderItemDAO = DAOFactory.createOrderItemDao();
        this.menuItemDAO = DAOFactory.createMenuItemDao();
        this.paymentDAO = DAOFactory.createPaymentDao();
        this.inventoryDAO = DAOFactory.createInventoryDao();
    }

    public int createOrder(int tableId) {
        try {
            Order o = new Order();
            CafeTable t = new CafeTable();
            t.setTableId(tableId);
            o.setTable(t);
            return orderDAO.insert(o);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean processPayment(int orderId, String paymentMethodStr, int cashierId) {
        try {
            id.komorebi.model.enums.PaymentMethod method = id.komorebi.model.enums.PaymentMethod.valueOf(paymentMethodStr);
            id.komorebi.model.Payment payment = new id.komorebi.model.Payment();
            
            id.komorebi.model.Order order = new id.komorebi.model.Order();
            order.setOrderId(orderId);
            payment.setOrder(order);
            
            payment.setPaymentMethod(method);
            
            id.komorebi.model.User cashier = new id.komorebi.model.User();
            cashier.setUserId(cashierId);
            payment.setCashier(cashier);

            int payId = paymentDAO.insert(payment);
            
            if (payId > 0) {
                boolean updated = orderDAO.markPaid(orderId, cashierId);
                
                if (updated) {
                    generateAndSaveReceipt(orderId, paymentMethodStr);
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void generateAndSaveReceipt(int orderId, String paymentMethod) {
        try {
            Order order = getOrder(orderId);
            if (order == null) return;

            StringBuilder sb = new StringBuilder();
            sb.append("========== KOMOREBI CAFE ==========\n");
            sb.append("Receipt Code: RCP-" + System.currentTimeMillis() + "\n");
            sb.append("Date: " + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + "\n");
            sb.append("-----------------------------------\n");
            
            for (OrderItem item : order.getItems()) {
                String name = item.getMenuItem().getName();
                if (name.length() > 20) name = name.substring(0, 20);
                String line = String.format("%-20s x%d  %,.0f\n", name, item.getQuantity(), item.getPriceEach());
                sb.append(line);
            }
            
            sb.append("-----------------------------------\n");
            sb.append(String.format("TOTAL:                 Rp %,.0f\n", order.getTotal()));
            sb.append("PAYMENT: " + paymentMethod + "\n");
            sb.append("===================================\n");
            sb.append("      Thank you for visiting!      \n");

            // C. Simpan ke Database Receipts
            id.komorebi.model.Receipt receipt = new id.komorebi.model.Receipt();
            receipt.setOrder(order);
            receipt.setReceiptCode("RCP-" + orderId + "-" + System.currentTimeMillis());
            receipt.setReceiptContent(sb.toString());
            
            receiptDAO.insert(receipt);
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Gagal menyimpan receipt: " + e.getMessage());
        }
    }
    
    public String getReceiptContent(int orderId) {
        try {
            id.komorebi.model.Receipt r = receiptDAO.findByOrderId(orderId);
            return (r != null) ? r.getReceiptContent() : "Receipt not found.";
        } catch (Exception e) { return "Error loading receipt."; }
    }

    public boolean cancelOrderItem(int orderItemId, int userId) {
        try {
            OrderItem item = orderItemDAO.findById(orderItemId);
            if (item == null) return false;

            if (item.getStatus() == OrderItemStatus.CANCELED) return false;
            boolean updated = orderItemDAO.updateStatus(orderItemId, OrderItemStatus.CANCELED);

            if (!updated) return false;
            inventoryDAO.restoreStockForMenu(item.getMenuItem().getMenuId(), item.getQuantity(), userId);

            calculateOrderTotal(item.getOrder().getOrderId());
            checkAndUpdateParentStatus(item.getOrder().getOrderId());

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addItem(int orderId, int menuId, int qty) {
        try {
            MenuItem menu = menuItemDAO.findById(menuId);
            if (menu == null) return false;

            if (!inventoryDAO.checkAvailability(menuId)) {
                System.out.println("Stock insufficient for menu: " + menuId);
                return false; 
            }

            OrderItem oi = new OrderItem();
            Order o = new Order();
            o.setOrderId(orderId);
            oi.setOrder(o);
            oi.setMenuItem(menu);
            oi.setQuantity(qty);
            oi.setPriceEach(menu.getPrice());
            
            int id = orderItemDAO.insert(oi);
            
            if (id > 0) {
                calculateOrderTotal(orderId); 
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean isMenuStockAvailable(int menuId) {
        try {
            return inventoryDAO.checkAvailability(menuId);
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean updateItemStatus(int orderItemId, String status) {
        try {
            boolean updated = orderItemDAO.updateStatus(orderItemId, OrderItemStatus.valueOf(status));
            if (!updated) return false;

            OrderItem item = orderItemDAO.findById(orderItemId);
            if (item != null) {
                checkAndUpdateParentStatus(item.getOrder().getOrderId());
            }
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    private void checkAndUpdateParentStatus(int orderId) throws SQLException {
        List<OrderItem> allItems = orderItemDAO.findByOrderId(orderId);
        if (allItems.isEmpty()) return;

        boolean allDone = true;
        boolean allServed = true;
        int activeItemCount = 0;

        for (OrderItem it : allItems) {
            if (it.getStatus() == OrderItemStatus.CANCELED) {
                continue; 
            }
            
            activeItemCount++;
            if (it.getStatus() != OrderItemStatus.DONE && it.getStatus() != OrderItemStatus.SERVED) {
                allDone = false; 
            }
            if (it.getStatus() != OrderItemStatus.SERVED) {
                allServed = false;
            }
        }

        // Jika semua item dicancel (activeItemCount 0), jangan ubah jadi DONE
        if (activeItemCount == 0) return;
        Optional<Order> optOrder = orderDAO.findById(orderId);
        if (optOrder.isPresent()) {
            Order parent = optOrder.get();
            boolean changed = false;

            if (allServed && parent.getStatus() != OrderStatus.FULLY_SERVED) {
                parent.setStatus(OrderStatus.FULLY_SERVED);
                changed = true;
            } 
            else if (allDone && parent.getStatus() != OrderStatus.FULLY_SERVED && parent.getStatus() != OrderStatus.DONE) {
                parent.setStatus(OrderStatus.DONE);
                changed = true;
            }

            if (changed) {
                orderDAO.update(parent);
            }
        }
    }

    public boolean calculateOrderTotal(int orderId) {
        try {
            Optional<Order> oo = orderDAO.findById(orderId);
            if (!oo.isPresent()) return false;
            Order o = oo.get();
            List<OrderItem> items = orderItemDAO.findByOrderId(orderId);
            o.setItems(items);
            o.recalcTotal();
            return orderDAO.update(o);
        } catch (SQLException e) {
            return false;
        }
    }

    public List<OrderItem> getOrderItems(int orderId) {
        try { return orderItemDAO.findByOrderId(orderId); } 
        catch (SQLException e) { throw new RuntimeException(e); }
    }

    public boolean updateOrderStatus(int orderId, String status) {
        try {
            Optional<Order> oo = orderDAO.findById(orderId);
            if (!oo.isPresent()) return false;
            Order o = oo.get();
            o.setStatus(OrderStatus.valueOf(status));
            return orderDAO.update(o);
        } catch (SQLException e) { return false; }
    }

    public Order getOrder(int id) {
        try { return orderDAO.findById(id).orElse(null); } 
        catch (SQLException e) { throw new RuntimeException(e); }
    }

    public List<Order> getActiveOrders() {
        try { return orderDAO.findByStatus(OrderStatus.NEW); } 
        catch (SQLException e) { throw new RuntimeException(e); }
    }

    public List<MenuItem> getAvailableMenuItems() {
        try { return menuItemDAO.findAvailable(); } 
        catch (SQLException e) { throw new RuntimeException(e); }
    }

    public List<Order> getOrdersByStatus(OrderStatus status) {
        try { return orderDAO.findByStatus(status); } 
        catch (SQLException e) { throw new RuntimeException(e); }
    }
    
    public int createCompleteOrder(java.util.Map<Integer, Integer> cart) {
        try {
            Order o = new Order();
            int orderId = orderDAO.insert(o);
            if (orderId <= 0) return -1;

            for (java.util.Map.Entry<Integer, Integer> entry : cart.entrySet()) {
                int menuId = entry.getKey();
                int qty = entry.getValue();
                MenuItem menu = menuItemDAO.findById(menuId);
                if (menu == null) continue;

                OrderItem oi = new OrderItem();
                Order orderRef = new Order();
                orderRef.setOrderId(orderId);
                oi.setOrder(orderRef);
                oi.setMenuItem(menu);
                oi.setQuantity(qty);
                oi.setPriceEach(menu.getPrice());

                orderItemDAO.insert(oi);
            }

            calculateOrderTotal(orderId);
            return orderId;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}