package id.komorebi.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class InventoryLog {
    private int logId;
    private Inventory ingredient;
    private User user;
    private BigDecimal changeAmount;
    private String note;
    private LocalDateTime createdAt;

    public InventoryLog() {}

    public InventoryLog(int logId, Inventory ingredient, User user, BigDecimal changeAmount, String note, LocalDateTime createdAt) {
        this.logId = logId;
        this.ingredient = ingredient;
        this.user = user;
        this.changeAmount = changeAmount;
        this.note = note;
        this.createdAt = createdAt;
    }

    public int getLogId() { return logId; }
    public void setLogId(int logId) { this.logId = logId; }

    public Inventory getIngredient() { return ingredient; }
    public void setIngredient(Inventory ingredient) { this.ingredient = ingredient; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public BigDecimal getChangeAmount() { return changeAmount; }
    public void setChangeAmount(BigDecimal changeAmount) { this.changeAmount = changeAmount; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "InventoryLog{" +
                "logId=" + logId +
                ", ingredient=" + (ingredient != null ? ingredient.getIngredientName() : null) +
                ", user=" + (user != null ? user.getUsername() : null) +
                ", changeAmount=" + changeAmount +
                ", note='" + note + '\'' +
                '}';
    }
}
