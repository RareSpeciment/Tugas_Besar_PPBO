package id.komorebi.model;

import java.time.LocalDateTime;

public class Receipt {
    private int receiptId;
    private Order order;
    private String receiptCode;
    private String receiptContent;
    private LocalDateTime createdAt;

    public Receipt() {}

    // Getters & Setters
    public int getReceiptId() { return receiptId; }
    public void setReceiptId(int receiptId) { this.receiptId = receiptId; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    public String getReceiptCode() { return receiptCode; }
    public void setReceiptCode(String receiptCode) { this.receiptCode = receiptCode; }

    public String getReceiptContent() { return receiptContent; }
    public void setReceiptContent(String receiptContent) { this.receiptContent = receiptContent; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}