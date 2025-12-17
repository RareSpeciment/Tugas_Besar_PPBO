package id.komorebi.model;

public class CafeTable {
    private int tableId;
    private String tableName;
    private int capacity;

    public CafeTable() {}

    public CafeTable(int tableId, String tableName, int capacity) {
        this.tableId = tableId;
        this.tableName = tableName;
        this.capacity = capacity;
    }

    public int getTableId() { return tableId; }
    public void setTableId(int tableId) { this.tableId = tableId; }

    public String getTableName() { return tableName; }
    public void setTableName(String tableName) { this.tableName = tableName; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    @Override
    public String toString() {
        return "CafeTable{" +
                "tableId=" + tableId +
                ", tableName='" + tableName + '\'' +
                ", capacity=" + capacity +
                '}';
    }
}
