package id.komorebi.model;
import java.math.BigDecimal;

public class MenuItem {
    private int menuId;
    private String name;
    private String description;
    private BigDecimal price;
    private Category category;
    private boolean available;
    private String imagePath;

    public MenuItem() {}

    public MenuItem(int menuId, String name, String description, BigDecimal price,
                    Category category, boolean available, String imagePath) {
        this.menuId = menuId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.available = available;
        this.imagePath = imagePath;
    }

    public int getMenuId() { return menuId; }
    public void setMenuId(int menuId) { this.menuId = menuId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    @Override
    public String toString() {
        return "MenuItem{" +
                "menuId=" + menuId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", available=" + available +
                '}';
    }
}
