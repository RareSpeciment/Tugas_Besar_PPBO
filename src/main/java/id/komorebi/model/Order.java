package id.komorebi.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import id.komorebi.model.enums.OrderStatus;

public class Order {
    private int orderId;
    private CafeTable table;
    private User cashier; // nullable until paid
    private OrderStatus status;
    private BigDecimal total;
    private LocalDateTime createdAt;
    private List<OrderItem> items = new ArrayList<>();

    public Order() {
        this.status = OrderStatus.NEW;
        this.createdAt = LocalDateTime.now();
        this.total = BigDecimal.ZERO;
    }

    public Order(int orderId, CafeTable table, User cashier, OrderStatus status, BigDecimal total, LocalDateTime createdAt) {
        this.orderId = orderId;
        this.table = table;
        this.cashier = cashier;
        this.status = status;
        this.total = total;
        this.createdAt = createdAt;
    }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public CafeTable getTable() { return table; }
    public void setTable(CafeTable table) { this.table = table; }

    public User getCashier() { return cashier; }
    public void setCashier(User cashier) { this.cashier = cashier; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }

    public void addItem(OrderItem item) {
        items.add(item);
        recalcTotal();
    }

    // public void recalcTotal() {
    //     BigDecimal sum = BigDecimal.ZERO;
    //     for (OrderItem oi : items) {
    //         sum = sum.add(oi.getPriceEach().multiply(new BigDecimal(oi.getQuantity())));
    //     }
    //     this.total = sum;
    // }

    public void recalcTotal() {
    BigDecimal sum = BigDecimal.ZERO;
    for (OrderItem oi : items) {
        if (oi.getStatus() != id.komorebi.model.enums.OrderItemStatus.CANCELED) { 
            sum = sum.add(oi.getPriceEach().multiply(new BigDecimal(oi.getQuantity())));
        }
    }
    this.total = sum;
}

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", table=" + (table != null ? table.getTableName() : null) +
                ", status=" + status +
                ", total=" + total +
                ", createdAt=" + createdAt +
                '}';
    }
}
