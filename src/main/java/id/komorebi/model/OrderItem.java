package id.komorebi.model;

import java.math.BigDecimal;

import id.komorebi.model.enums.OrderItemStatus;

public class OrderItem {
    private int orderItemId;
    private Order order;
    private MenuItem menuItem;
    private int quantity;
    private BigDecimal priceEach;
    private OrderItemStatus status;
    private String note;

    public OrderItem() {
        this.status = OrderItemStatus.NEW;
    }

    public OrderItem(int orderItemId, Order order, MenuItem menuItem, int quantity, BigDecimal priceEach, OrderItemStatus status, String note) {
        this.orderItemId = orderItemId;
        this.order = order;
        this.menuItem = menuItem;
        this.quantity = quantity;
        this.priceEach = priceEach;
        this.status = status;
        this.note = note;
    }

    public int getOrderItemId() { return orderItemId; }
    public void setOrderItemId(int orderItemId) { this.orderItemId = orderItemId; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    public MenuItem getMenuItem() { return menuItem; }
    public void setMenuItem(MenuItem menuItem) { this.menuItem = menuItem; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public java.math.BigDecimal getPriceEach() { return priceEach; }
    public void setPriceEach(java.math.BigDecimal priceEach) { this.priceEach = priceEach; }

    public OrderItemStatus getStatus() { return status; }
    public void setStatus(OrderItemStatus status) { this.status = status; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    @Override
    public String toString() {
        return "OrderItem{" +
                "orderItemId=" + orderItemId +
                ", menuItem=" + (menuItem != null ? menuItem.getName() : null) +
                ", qty=" + quantity +
                ", priceEach=" + priceEach +
                ", status=" + status +
                '}';
    }
}
