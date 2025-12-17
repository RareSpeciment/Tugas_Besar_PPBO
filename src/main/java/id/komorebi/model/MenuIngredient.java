package id.komorebi.model;

import java.math.BigDecimal;

public class MenuIngredient {
    private int id;
    private MenuItem menuItem;
    private Inventory ingredient;
    private BigDecimal quantityRequired;

    public MenuIngredient() {}

    public MenuIngredient(int id, MenuItem menuItem, Inventory ingredient, BigDecimal quantityRequired) {
        this.id = id;
        this.menuItem = menuItem;
        this.ingredient = ingredient;
        this.quantityRequired = quantityRequired;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public MenuItem getMenuItem() { return menuItem; }
    public void setMenuItem(MenuItem menuItem) { this.menuItem = menuItem; }

    public Inventory getIngredient() { return ingredient; }
    public void setIngredient(Inventory ingredient) { this.ingredient = ingredient; }

    public BigDecimal getQuantityRequired() { return quantityRequired; }
    public void setQuantityRequired(BigDecimal quantityRequired) { this.quantityRequired = quantityRequired; }

    @Override
    public String toString() {
        return "MenuIngredient{" +
                "id=" + id +
                ", menuItem=" + (menuItem != null ? menuItem.getName() : null) +
                ", ingredient=" + (ingredient != null ? ingredient.getIngredientName() : null) +
                ", quantityRequired=" + quantityRequired +
                '}';
    }
}
