package id.komorebi.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Inventory {
    private int ingredientId;
    private String ingredientName;
    private BigDecimal quantity;
    private String unit;
    private BigDecimal minStock;
    private LocalDateTime updatedAt;

    public Inventory() {}

    public Inventory(int ingredientId, String ingredientName, BigDecimal quantity, String unit, BigDecimal minStock, LocalDateTime updatedAt) {
        this.ingredientId = ingredientId;
        this.ingredientName = ingredientName;
        this.quantity = quantity;
        this.unit = unit;
        this.minStock = minStock;
        this.updatedAt = updatedAt;
    }

    public int getIngredientId() { return ingredientId; }
    public void setIngredientId(int ingredientId) { this.ingredientId = ingredientId; }

    public String getIngredientName() { return ingredientName; }
    public void setIngredientName(String ingredientName) { this.ingredientName = ingredientName; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public BigDecimal getMinStock() { return minStock; }
    public void setMinStock(BigDecimal minStock) { this.minStock = minStock; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "Inventory{" +
                "ingredientId=" + ingredientId +
                ", ingredientName='" + ingredientName + '\'' +
                ", quantity=" + quantity +
                ", unit='" + unit + '\'' +
                '}';
    }
}
