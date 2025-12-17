package id.komorebi.dao.impl;

import id.komorebi.dao.ReceiptDAO;
import id.komorebi.model.Receipt;
import id.komorebi.util.DBConnection;
import java.sql.*;

public class ReceiptDAOImpl implements ReceiptDAO {

    @Override
    public int insert(Receipt receipt) throws SQLException {
        String sql = "INSERT INTO receipts (order_id, receipt_code, receipt_content, created_at) VALUES (?, ?, ?, NOW())";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, receipt.getOrder().getOrderId());
            ps.setString(2, receipt.getReceiptCode());
            ps.setString(3, receipt.getReceiptContent());
            
            if (ps.executeUpdate() > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    return rs.next() ? rs.getInt(1) : -1;
                }
            }
        }
        return -1;
    }

    @Override
    public Receipt findByOrderId(int orderId) throws SQLException {
        String sql = "SELECT * FROM receipts WHERE order_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Receipt r = new Receipt();
                    r.setReceiptId(rs.getInt("receipt_id"));
                    r.setReceiptCode(rs.getString("receipt_code"));
                    r.setReceiptContent(rs.getString("receipt_content"));
                    return r;
                }
            }
        }
        return null;
    }
}