package id.komorebi.dao.impl;

import id.komorebi.dao.PaymentDAO;
import id.komorebi.model.Payment;
import id.komorebi.model.Order;
import id.komorebi.model.User;
import id.komorebi.model.enums.PaymentMethod;
import id.komorebi.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;


public class PaymentDAOImpl implements PaymentDAO {
    @Override
    public Payment findById(int id) throws SQLException {
        String sql = "SELECT p.payment_id, p.order_id, p.payment_method, p.payment_time, p.cashier_id, u.username " +
                     "FROM payments p LEFT JOIN users u ON p.cashier_id = u.user_id WHERE p.payment_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Payment p = new Payment();
                    p.setPaymentId(rs.getInt("payment_id"));
                    Order o = new Order();
                    o.setOrderId(rs.getInt("order_id"));
                    p.setOrder(o);
                    p.setPaymentMethod(PaymentMethod.valueOf(rs.getString("payment_method")));
                    p.setPaymentTime(rs.getTimestamp("payment_time").toLocalDateTime());
                    User u = new User();
                    u.setUserId(rs.getInt("cashier_id"));
                    u.setUsername(rs.getString("username"));
                    p.setCashier(u);
                    return p;
                }
            }
        }
        return null;
    }

    @Override
    public double getTotalSalesByDate(LocalDate date) throws SQLException {
        String sql = "SELECT SUM(total) FROM orders WHERE status IN ('PAID', 'DONE', 'FULLY_SERVED') AND DATE(created_at) = ?";
        
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(date));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        }
        return 0.0;
    }

    @Override
    public int getDailyTransactionCount(LocalDate date) throws SQLException {
        String sql = "SELECT COUNT(*) AS cnt FROM payments p WHERE DATE(p.payment_time) = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(date));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("cnt");
            }
        }
        return 0;
    }

    @Override
    public List<Payment> findByOrderId(int orderId) throws SQLException {
        List<Payment> list = new ArrayList<>();
        String sql = "SELECT p.payment_id, p.order_id, p.payment_method, p.payment_time, p.cashier_id, u.username " +
                     "FROM payments p LEFT JOIN users u ON p.cashier_id = u.user_id WHERE p.order_id = ? ORDER BY p.payment_time";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Payment p = new Payment();
                    p.setPaymentId(rs.getInt("payment_id"));
                    Order o = new Order();
                    o.setOrderId(rs.getInt("order_id"));
                    p.setOrder(o);
                    p.setPaymentMethod(PaymentMethod.valueOf(rs.getString("payment_method")));
                    p.setPaymentTime(rs.getTimestamp("payment_time").toLocalDateTime());
                    User u = new User();
                    u.setUserId(rs.getInt("cashier_id"));
                    u.setUsername(rs.getString("username"));
                    p.setCashier(u);
                    list.add(p);
                }
            }
        }
        return list;
    }

    @Override
    public int insert(Payment payment) throws SQLException {
        String sql = "INSERT INTO payments (order_id, payment_method, payment_time, cashier_id) VALUES (?, ?, NOW(), ?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, payment.getOrder().getOrderId());
            ps.setString(2, payment.getPaymentMethod().name());
            ps.setInt(3, payment.getCashier().getUserId());
            
            int affected = ps.executeUpdate();
            if (affected == 0) return -1;
            
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }
}