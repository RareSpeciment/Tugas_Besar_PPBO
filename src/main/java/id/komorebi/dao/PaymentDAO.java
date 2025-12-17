package id.komorebi.dao;

import id.komorebi.model.Payment;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface PaymentDAO {
    Payment findById(int id) throws SQLException;
    List<Payment> findByOrderId(int orderId) throws SQLException;
    int insert(Payment payment) throws SQLException;

    // Reporting aggregates
    double getTotalSalesByDate(LocalDate date) throws SQLException;
    int getDailyTransactionCount(LocalDate date) throws SQLException;
}
