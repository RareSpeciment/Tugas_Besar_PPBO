package id.komorebi.model;

import java.math.BigDecimal;

public class ReportEntry {
    private int menuId;
    private String name;
    private long quantitySold;
    private BigDecimal revenue;

    public ReportEntry() {}

    public ReportEntry(int menuId, String name, long quantitySold, BigDecimal revenue) {
        this.menuId = menuId;
        this.name = name;
        this.quantitySold = quantitySold;
        this.revenue = revenue;
    }

    //setters and getters
    public void setMenuId(int menuId) {this.menuId = menuId;}
    public void setName(String name) {this.name = name;}
    public void setQuantitySold(long quantitySold) {this.quantitySold = quantitySold;}
    public void setRevenue(BigDecimal revenue) {this.revenue = revenue;}

    public int getMenuId() { return menuId; }
    public String getName() { return name; }
    public long getQuantitySold() { return quantitySold; }
    public BigDecimal getRevenue() { return revenue; }

    @Override
    public String toString() {
        return "ReportEntry{" +
                "menuId=" + menuId +
                ", name='" + name + '\'' +
                ", quantitySold=" + quantitySold +
                ", revenue=" + revenue +
                '}';
    }
}
