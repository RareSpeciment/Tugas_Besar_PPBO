package id.komorebi.model;

import java.time.LocalDateTime;

import id.komorebi.model.enums.PaymentMethod;

public class Payment {
    private int paymentId;
    private Order order;
    private PaymentMethod paymentMethod;
    private LocalDateTime paymentTime;
    private User cashier;

    public Payment() {
        this.paymentTime = LocalDateTime.now();
    }

    public Payment(int paymentId, Order order, PaymentMethod paymentMethod, LocalDateTime paymentTime, User cashier) {
        this.paymentId = paymentId;
        this.order = order;
        this.paymentMethod = paymentMethod;
        this.paymentTime = paymentTime;
        this.cashier = cashier;
    }

    public int getPaymentId() { return paymentId; }
    public void setPaymentId(int paymentId) { this.paymentId = paymentId; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }

    public LocalDateTime getPaymentTime() { return paymentTime; }
    public void setPaymentTime(LocalDateTime paymentTime) { this.paymentTime = paymentTime; }

    public User getCashier() { return cashier; }
    public void setCashier(User cashier) { this.cashier = cashier; }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", orderId=" + (order != null ? order.getOrderId() : null) +
                ", method=" + paymentMethod +
                ", paymentTime=" + paymentTime +
                ", cashier=" + (cashier != null ? cashier.getUsername() : null) +
                '}';
    }
}