package id.komorebi.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import id.komorebi.dao.OrderDAO;
import id.komorebi.model.CafeTable;
import id.komorebi.model.Order;
import id.komorebi.model.OrderItem;
import id.komorebi.model.User;
import id.komorebi.model.enums.OrderStatus;
import id.komorebi.util.DBConnection;

public class OrderDAOImpl implements OrderDAO {
    private final OrderItemDAOImpl orderItemDao = new OrderItemDAOImpl();

    @Override
    public Optional<Order> findById(int id) throws SQLException {
        Order order = null;
        String sql = "SELECT o.order_id, o.table_id, t.table_name, o.cashier_id, u.username, o.status, o.total, o.created_at " +
                     "FROM orders o LEFT JOIN cafe_tables t ON o.table_id = t.table_id LEFT JOIN users u ON o.cashier_id = u.user_id WHERE o.order_id = ?";
        
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    order = mapRowToOrder(rs); 
                }
            }
        }
        
        if (order != null) {
            try {
                List<OrderItem> items = orderItemDao.findByOrderId(order.getOrderId());
                order.setItems(items);
                order.recalcTotal();
            } catch (Exception ignored) {
                System.err.println("Gagal load item untuk order " + id);
            }
            return Optional.of(order);
        }
        return Optional.empty();
    }

    @Override
    public List<Order> findAll() throws SQLException {
        List<Order> list = new ArrayList<>();
        List<Integer> orderIds = new ArrayList<>();
        String sql = "SELECT o.order_id, o.table_id, t.table_name, o.cashier_id, u.username, o.status, o.total, o.created_at " +
                     "FROM orders o LEFT JOIN cafe_tables t ON o.table_id = t.table_id LEFT JOIN users u ON o.cashier_id = u.user_id " +
                     "ORDER BY o.created_at DESC";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getInt("order_id"));
                orderIds.add(order.getOrderId());
                CafeTable t = new CafeTable(rs.getInt("table_id"), rs.getString("table_name"), 0);
                order.setTable(t);
                int cashierId = rs.getInt("cashier_id");
                if (!rs.wasNull()) {
                    User cashier = new User();
                    cashier.setUserId(cashierId);
                    cashier.setUsername(rs.getString("username"));
                    order.setCashier(cashier);
                }
                order.setStatus(OrderStatus.valueOf(rs.getString("status")));
                order.setTotal(rs.getBigDecimal("total"));
                Timestamp ts = rs.getTimestamp("created_at");
                if (ts != null) order.setCreatedAt(ts.toLocalDateTime());
                list.add(order);
            }
        }
        // load items after ResultSet is closed
        for (Order order : list) {
            try {
                List<OrderItem> items = orderItemDao.findByOrderId(order.getOrderId());
                order.setItems(items);
                order.recalcTotal();
            } catch (Exception ignored) {}
        }
        return list;
    }

    @Override
    public List<Order> findByStatus(OrderStatus status) throws SQLException {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT o.order_id, o.table_id, t.table_name, o.cashier_id, u.username, o.status, o.total, o.created_at " +
                     "FROM orders o LEFT JOIN cafe_tables t ON o.table_id = t.table_id LEFT JOIN users u ON o.cashier_id = u.user_id " +
                     "WHERE o.status = ? ORDER BY o.created_at DESC";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, status.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order();
                    order.setOrderId(rs.getInt("order_id"));
                    CafeTable t = new CafeTable(rs.getInt("table_id"), rs.getString("table_name"), 0);
                    order.setTable(t);
                    int cashierId = rs.getInt("cashier_id");
                    if (!rs.wasNull()) {
                        User cashier = new User();
                        cashier.setUserId(cashierId);
                        cashier.setUsername(rs.getString("username"));
                        order.setCashier(cashier);
                    }
                    order.setStatus(OrderStatus.valueOf(rs.getString("status")));
                    order.setTotal(rs.getBigDecimal("total"));
                    Timestamp ts = rs.getTimestamp("created_at");
                    if (ts != null) order.setCreatedAt(ts.toLocalDateTime());
                    list.add(order);
                }
            }
        }
        // load items after ResultSet is closed
        for (Order order : list) {
            try {
                List<OrderItem> items = orderItemDao.findByOrderId(order.getOrderId());
                order.setItems(items);
                order.recalcTotal();
            } catch (Exception ignored) {}
        }
        return list;
    }

    @Override
    public int insert(Order order) throws SQLException {
        String sql = "INSERT INTO orders (table_id, status, total, created_at) VALUES (?, ?, ?, ?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, order.getTable().getTableId());
            ps.setString(2, order.getStatus().name());
            ps.setBigDecimal(3, order.getTotal());
            ps.setTimestamp(4, Timestamp.valueOf(order.getCreatedAt() != null ? order.getCreatedAt() : LocalDateTime.now()));
            int affected = ps.executeUpdate();
            if (affected == 0) return -1;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int orderId = rs.getInt(1);
                    // insert order items
                    if (order.getItems() != null) {
                        for (OrderItem oi : order.getItems()) {
                            oi.setOrder(order);
                            oi.getOrder().setOrderId(orderId);
                            orderItemDao.insert(oi);
                        }
                    }
                    return orderId;
                }
            }
        }
        return -1;
    }

    @Override
    public boolean update(Order order) throws SQLException {
        String sql = "UPDATE orders SET table_id=?, cashier_id=?, status=?, total=? WHERE order_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, order.getTable().getTableId());
            if (order.getCashier() != null) ps.setInt(2, order.getCashier().getUserId());
            else ps.setNull(2, Types.INTEGER);
            ps.setString(3, order.getStatus().name());
            ps.setBigDecimal(4, order.getTotal());
            ps.setInt(5, order.getOrderId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean markPaid(int orderId, int cashierId) throws SQLException {
        String sql = "UPDATE orders SET status = 'PAID', cashier_id = ? WHERE order_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, cashierId);
            ps.setInt(2, orderId);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean cancelOrder(int orderId) throws SQLException {
        String sql = "UPDATE orders SET status = 'CANCELED' WHERE order_id = ? AND status = 'NEW'";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            return ps.executeUpdate() > 0;
        }
    }

    private Order mapRowToOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setOrderId(rs.getInt("order_id"));
        
        CafeTable t = new CafeTable(rs.getInt("table_id"), rs.getString("table_name"), 0);
        order.setTable(t);
        
        int cashierId = rs.getInt("cashier_id");
        if (!rs.wasNull()) {
            User cashier = new User();
            cashier.setUserId(cashierId);
            cashier.setUsername(rs.getString("username"));
            order.setCashier(cashier);
        }
        
        order.setStatus(OrderStatus.valueOf(rs.getString("status")));
        order.setTotal(rs.getBigDecimal("total"));
        
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) order.setCreatedAt(ts.toLocalDateTime());
        
        return order;
    }
}