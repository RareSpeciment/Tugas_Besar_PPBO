package id.komorebi.dao;
import id.komorebi.model.Receipt;
import java.sql.SQLException;

public interface ReceiptDAO {
    int insert(Receipt receipt) throws SQLException;
    Receipt findByOrderId(int orderId) throws SQLException;
}