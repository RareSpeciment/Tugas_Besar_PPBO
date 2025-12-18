package id.komorebi.view;

import id.komorebi.controller.OrderController;
import id.komorebi.controller.LogoutCallback;
import id.komorebi.model.Order;
import id.komorebi.model.OrderItem;
import id.komorebi.util.UIHelper;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class OrderListView extends JPanel {
    private final OrderController controller;
    private final id.komorebi.model.User currentUser;

    private JTable orderTable;
    private JComboBox<String> filterBox;

    private JTable itemDetailTable;
    private JButton btnDetailAction;
    private LogoutCallback logoutCallback;

    public OrderListView(OrderController controller, id.komorebi.model.User user, LogoutCallback logoutCallback, Object ignored) {
        this.controller = controller;
        this.currentUser = user;
        this.logoutCallback = logoutCallback;

        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        
        add(UIHelper.createHeaderPanel("Order Management (" + currentUser.getRole().getRoleName() + ")", e -> handleLogout()), BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(15, 15));
        content.setBackground(UIHelper.BACKGROUND_WHITE);
        content.setBorder(new javax.swing.border.EmptyBorder(15, 15, 15, 15));

        JPanel filterPanel = createFilterBar();
        content.add(filterPanel, BorderLayout.NORTH);

        // Master Table
        orderTable = new JTable();
        orderTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        UIHelper.styleTable(orderTable);
        JScrollPane orderScroll = new JScrollPane(orderTable);
        orderScroll.setBorder(BorderFactory.createEmptyBorder());

        if (isChefOrBarista() || isCashier()) { 
            // kasir dan cheff: Tampilan Split (Kiri Order, Kanan Detail Item)
            JComponent detailPanel = createDetailPanel();
            
            orderTable.getSelectionModel().addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) loadOrderDetails();
            });

            JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, orderScroll, detailPanel);
            splitPane.setDividerLocation(350); 
            splitPane.setResizeWeight(0.5);
            splitPane.setBorder(null);
            
            content.add(splitPane, BorderLayout.CENTER);
            
            if (isCashier()) filterBox.setSelectedItem("NEW");
            else filterBox.setSelectedItem("PAID");

        } else if (isWaiter()) {
            // WAITER: Hanya List Order Saja
            content.add(orderScroll, BorderLayout.CENTER);
            filterBox.setSelectedItem("DONE"); 

        } else {
            content.add(orderScroll, BorderLayout.CENTER);
        }
        
        add(content, BorderLayout.CENTER);
        loadByFilter();
    }

    private JPanel createFilterBar() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UIHelper.DIVIDER));
        
        p.add(UIHelper.createLabel("Filter Status:"));
        
        String[] filterOptions;
        if (isChefOrBarista()) {
            filterOptions = new String[] { "ALL", "PAID", "DONE" };
        } else if (isWaiter()) {
            filterOptions = new String[] { "ALL", "NEW", "DONE", "FULLY_SERVED" };
        } else if (isCashier()) {
            filterOptions = new String[] { "ALL", "NEW", "PAID", "CANCELED" };
        } else {
            filterOptions = new String[] { "ALL", "NEW", "PAID", "DONE", "FULLY_SERVED", "CANCELED" };
        }
        filterBox = new JComboBox<>(filterOptions);
        filterBox.setFont(UIHelper.MAIN_FONT);
        filterBox.setBackground(Color.WHITE);
        p.add(filterBox);
        
        JButton refresh = UIHelper.createButton("Refresh");
        refresh.setPreferredSize(new Dimension(100, 30));
        refresh.addActionListener(e -> loadByFilter());
        p.add(refresh);

        if (isCashier()) {
            JButton payBtn = UIHelper.createSuccessButton("Process Payment");
            payBtn.setPreferredSize(new Dimension(150, 30));
            payBtn.addActionListener(e -> processPayment());
            p.add(payBtn);
        }
        if (isWaiter()) {
            JButton servedBtn = UIHelper.createSuccessButton("Mark Order Served");
            servedBtn.setPreferredSize(new Dimension(160, 30));
            servedBtn.addActionListener(e -> markOrderServed());
            p.add(servedBtn);
        }
        
        return p;
    }

    private boolean isChefOrBarista() {
        String r = currentUser.getRole().getRoleName().toLowerCase();
        return r.contains("chef") || r.contains("barista");
    }

    private boolean isWaiter() {
        String r = currentUser.getRole().getRoleName().toLowerCase();
        return r.contains("waiter") || r.contains("server");
    }

    private boolean isCashier() {
        String r = currentUser.getRole().getRoleName().toLowerCase();
        return r.contains("cashier") || r.contains("kasir");
    }

    private void loadChefOrderDetails() {
        int row = orderTable.getSelectedRow();
        if (row < 0) {
            itemDetailTable.setModel(new DefaultTableModel());
            return;
        }

        int orderId = Integer.parseInt(orderTable.getValueAt(row, 0).toString());
        List<OrderItem> items = controller.getOrderItems(orderId);

        String[] cols = { "Item ID", "Menu Name", "Qty", "Status" };
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };

        for (OrderItem oi : items) {
            model.addRow(new Object[] {
                    oi.getOrderItemId(),
                    oi.getMenuItem().getName(),
                    oi.getQuantity(),
                    oi.getStatus()
            });
        }
        itemDetailTable.setModel(model);
    }

    private void markItemReady() {
        int[] selectedRows = itemDetailTable.getSelectedRows();
        if (selectedRows.length == 0) return;
        
        int successCount = 0;
        for (int row : selectedRows) {
            int itemId = Integer.parseInt(itemDetailTable.getValueAt(row, 0).toString());
            String currentStatus = itemDetailTable.getValueAt(row, 3).toString();

            if ("DONE".equals(currentStatus) || "SERVED".equals(currentStatus)) continue;
            
            if (controller.updateItemStatus(itemId, "DONE")) {
                successCount++;
            }
        }

        if (successCount > 0) {
            UIHelper.showInfo(successCount + " Item(s) Marked Ready!");
            loadChefOrderDetails();
            loadByFilter();
        } else {
            UIHelper.showInfo("No eligible items updated.");
        }
    }

    private JComponent createDetailPanel() {
        JPanel panel = UIHelper.createCardPanel();
        String titleText = isCashier() ? "Order Items (Select to Cancel)" : "Order Items (Select to Cook)";
        JLabel title = UIHelper.createSectionLabel(titleText);
        panel.add(title, BorderLayout.NORTH);

        itemDetailTable = new JTable();
        itemDetailTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        UIHelper.styleTable(itemDetailTable);
        
        JScrollPane scroll = new JScrollPane(itemDetailTable);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnDetailAction = UIHelper.createButton("Action"); 
        btnDetailAction.setEnabled(false);
        
        if (isCashier()) {
            btnDetailAction.setText("Cancel Selected Items");
            btnDetailAction.setBackground(UIHelper.ERROR_RED);
            btnDetailAction.setForeground(Color.BLACK);
            btnDetailAction.addActionListener(e -> cancelSelectedItems());
        } else {
            btnDetailAction.setText("Mark Selected READY");
            btnDetailAction.setBackground(UIHelper.WARNING_YELLOW);
            btnDetailAction.setForeground(Color.BLACK);
            btnDetailAction.addActionListener(e -> markItemReady());
        }
        
        btnPanel.add(btnDetailAction);
        btnPanel.setBackground(Color.WHITE);
        
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);
        
        itemDetailTable.getSelectionModel().addListSelectionListener(e -> 
            btnDetailAction.setEnabled(itemDetailTable.getSelectedRows().length > 0)
        );
        return panel;
    }

    public void loadByFilter() {
        String sel = (String) filterBox.getSelectedItem();
        try {
            List<Order> orders;
            if (sel == null || sel.equals("ALL")) {
                if (isChefOrBarista()) {
                    orders = controller.getOrdersByStatus(id.komorebi.model.enums.OrderStatus.PAID);
                    orders.addAll(controller.getOrdersByStatus(id.komorebi.model.enums.OrderStatus.DONE));
                } else if (isCashier()) {
                    orders = controller.getOrdersByStatus(id.komorebi.model.enums.OrderStatus.NEW);
                    orders.addAll(controller.getOrdersByStatus(id.komorebi.model.enums.OrderStatus.PAID));
                } else {
                    orders = controller.getActiveOrders();
                }
            } else {
                orders = controller.getOrdersByStatus(id.komorebi.model.enums.OrderStatus.valueOf(sel));
            }
            orders.sort((o1, o2) -> Integer.compare(o2.getOrderId(), o1.getOrderId()));
            setOrders(orders);
        } catch (Exception e) {}
    }

    public void setOrders(List<Order> orders) {
        String[] cols = { "ID", "Table", "Status", "Total", "Time" };
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        for (Order o : orders) {
            model.addRow(new Object[] {
                    o.getOrderId(),
                    o.getTable() != null ? o.getTable().getTableName() : "?",
                    o.getStatus(),
                    "Rp " + (o.getTotal() != null ? o.getTotal().intValue() : 0),
                    o.getCreatedAt()
            });
        }
        orderTable.setModel(model);
    }

    private void loadOrderDetails() {
        int row = orderTable.getSelectedRow();
        if (row < 0) {
            itemDetailTable.setModel(new DefaultTableModel());
            return;
        }

        int orderId = Integer.parseInt(orderTable.getValueAt(row, 0).toString());
        List<OrderItem> items = controller.getOrderItems(orderId);

        String[] cols = { "Item ID", "Menu Name", "Qty", "Price", "Status" };
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        for (OrderItem oi : items) {
            if (isChefOrBarista() && "CANCELED".equals(oi.getStatus().toString())) continue;

            model.addRow(new Object[] {
                    oi.getOrderItemId(),
                    oi.getMenuItem().getName(),
                    oi.getQuantity(),
                    "Rp " + (int)oi.getPriceEach().doubleValue(),
                    oi.getStatus()
            });
        }
        itemDetailTable.setModel(model);
    }

    private void cancelSelectedItems() {
        int[] selectedRows = itemDetailTable.getSelectedRows();
        if (selectedRows.length == 0) return;

        if (!UIHelper.confirm("Are you sure you want to CANCEL " + selectedRows.length + " item(s)?\nTotal price will decrease.")) {
            return;
        }

        int successCount = 0;
        for (int row : selectedRows) {
            int itemId = Integer.parseInt(itemDetailTable.getValueAt(row, 0).toString());
            String status = itemDetailTable.getValueAt(row, 4).toString();
            
            if ("CANCELED".equals(status)) continue;

            if (controller.cancelOrderItem(itemId, currentUser.getUserId())) {
                successCount++;
            }
        }

        if (successCount > 0) {
            UIHelper.showInfo(successCount + " items canceled & Total Updated!");
            loadOrderDetails();
            loadByFilter();
        }
    }

    private void processPayment() {
        int r = orderTable.getSelectedRow();
        if (r < 0) {
            UIHelper.showError("Please select an order first.");
            return;
        }
        
        int oid = Integer.parseInt(orderTable.getValueAt(r, 0).toString());
        String totalStr = orderTable.getValueAt(r, 3).toString(); 

        // 3. FIX: Ambil Window induk (karena 'this' sekarang Panel, bukan Frame)
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(parentWindow, "Process Payment - Order #" + oid, Dialog.ModalityType.APPLICATION_MODAL);
        
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(UIHelper.BACKGROUND_WHITE);

        // Header
        JPanel header = new JPanel(new GridLayout(2, 1));
        header.setBackground(UIHelper.PRIMARY_BROWN);
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel lblTitle = new JLabel("Total Amount to Pay:", SwingConstants.CENTER);
        lblTitle.setFont(UIHelper.MAIN_FONT);
        lblTitle.setForeground(UIHelper.ACCENT_CREAM);
        
        JLabel lblTotal = new JLabel(totalStr, SwingConstants.CENTER);
        lblTotal.setFont(UIHelper.TITLE_FONT);
        lblTotal.setForeground(Color.WHITE);
        
        header.add(lblTitle);
        header.add(lblTotal);

        // Form
        JPanel form = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        form.setBackground(UIHelper.BACKGROUND_WHITE);
        
        JLabel lblMethod = new JLabel("Select Payment Method:");
        lblMethod.setFont(UIHelper.MAIN_FONT);
        
        String[] methods = { "CASH", "QRIS", "CARD", "TRANSFER" };
        JComboBox<String> cbMethod = new JComboBox<>(methods);
        cbMethod.setFont(UIHelper.BUTTON_FONT);
        cbMethod.setPreferredSize(new Dimension(200, 40));
        
        form.add(lblMethod);
        form.add(cbMethod);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        btnPanel.setBackground(UIHelper.BACKGROUND_WHITE);
        
        JButton btnConfirm = UIHelper.createSuccessButton("CONFIRM PAYMENT");
        JButton btnCancel = UIHelper.createErrorButton("CANCEL");
        
        btnConfirm.addActionListener(e -> {
            String selectedMethod = (String) cbMethod.getSelectedItem();
            boolean success = controller.processPayment(oid, selectedMethod, currentUser.getUserId());
            
            if (success) {
                dialog.dispose();
                String receiptText = controller.getReceiptText(oid);
                
                if (parentWindow instanceof Frame) {
                     id.komorebi.util.ReceiptPrinter.showReceiptText((Frame)parentWindow, receiptText);
                } else {
                     JOptionPane.showMessageDialog(this, receiptText, "Receipt", JOptionPane.INFORMATION_MESSAGE);
                }
                
                UIHelper.showInfo("Payment Successful! Order #" + oid + " is now PAID.");
                id.komorebi.util.Logger.log(currentUser, "PAYMENT", "Processed Payment for Order #" + oid);
                loadByFilter();
            } else {
                UIHelper.showError("Payment Failed. Please try again.");
            }
        });
        
        btnCancel.addActionListener(e -> dialog.dispose());
        
        btnPanel.add(btnCancel);
        btnPanel.add(btnConfirm);

        dialog.add(header, BorderLayout.NORTH);
        dialog.add(form, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }

    private void markOrderServed() {
        int r = orderTable.getSelectedRow();
        if (r < 0) return;
        int oid = Integer.parseInt(orderTable.getValueAt(r, 0).toString());

        if (UIHelper.confirm("Mark Order #" + oid + " FULLY SERVED?")) {
            controller.updateOrderStatus(oid, "FULLY_SERVED");
            controller.markOrderItemsStatus(oid, "SERVED");
            loadByFilter();
        }
    }

    private void handleLogout() {
        if (logoutCallback != null) {
            logoutCallback.onLogout();
        } else {
            MainFrame.getInstance().showView("MainMenuView");
        }
    }
}