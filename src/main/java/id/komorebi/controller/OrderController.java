package id.komorebi.controller;

import id.komorebi.model.Order;
import id.komorebi.model.OrderItem;
import id.komorebi.model.MenuItem;
import id.komorebi.service.OrderService;
import id.komorebi.util.UIHelper;

import java.util.List;

public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    public int createOrder(int tableId) {
        return service.createOrder(tableId);
    }

    public List<Order> getActiveOrders() {
        return service.getActiveOrders();
    }

    public List<Order> getOrdersByStatus(id.komorebi.model.enums.OrderStatus status) {
        try {
            return service.getOrdersByStatus(status);
        } catch (Exception e) {
            UIHelper.showError("Failed to load orders: " + e.getMessage());
            return List.of();
        }
    }

    public List<MenuItem> getAvailableMenuItems() {
        try {
            return service.getAvailableMenuItems();
        } catch (Exception e) {
            UIHelper.showError("Failed to load menu items: " + e.getMessage());
            return List.of();
        }
    }

    public List<OrderItem> getOrderItems(int orderId) {
        return service.getOrderItems(orderId);
    }

    public Order getOrder(int id) {
        try {
            return service.getOrder(id);
        } catch (Exception e) {
            UIHelper.showError("Failed to fetch order: " + e.getMessage());
            return null;
        }
    }

    public boolean addItem(int orderId, int menuId, int qty) {
        try {
            return service.addItem(orderId, menuId, qty);
        } catch (Exception e) {
            UIHelper.showError("Cannot add item: " + e.getMessage());
        }
        return false;
    }

    public boolean updateItemStatus(int itemId, String status) {
        try {
            service.updateItemStatus(itemId, status);
            return true;
        } catch (Exception e) {
            UIHelper.showError("Error updating item: " + e.getMessage());
            return false;
        }
    }

    public boolean updateOrderStatus(int orderId, String status) {
        try {
            service.updateOrderStatus(orderId, status);
            return true;
        } catch (Exception e) {
            UIHelper.showError("Failed: " + e.getMessage());
            return false;
        }
    }

    public boolean markOrderItemsStatus(int orderId, String status) {
        try {
            java.util.List<OrderItem> items = service.getOrderItems(orderId);
            if (items == null || items.isEmpty()) return false;
            
            boolean ok = true;
            for (OrderItem it : items) {
                ok &= updateItemStatus(it.getOrderItemId(), status);
            }
            return ok;            
        } catch (Exception e) {
            UIHelper.showError("Failed to update item statuses: " + e.getMessage());
            return false;
        }
    }

    public boolean cancelOrderItem(int itemId, int userId) {
        try {
            return service.cancelOrderItem(itemId, userId);
        } catch (Exception e) {
            UIHelper.showError("Failed to cancel item: " + e.getMessage());
            return false;
        }
    }

    public boolean processPayment(int orderId, String method, int cashierId) {
        try {
            return service.processPayment(orderId, method, cashierId);
        } catch (Exception e) {
            UIHelper.showError("Payment Failed: " + e.getMessage());
            return false;
        }
    }

    public boolean isMenuAvailable(int menuId) {
        return service.isMenuStockAvailable(menuId);
    }

    public String getReceiptText(int orderId) {
        return service.getReceiptContent(orderId);
    }
}