package id.komorebi.dao.impl;

import id.komorebi.dao.OrderItemDAO;
import id.komorebi.model.MenuItem;
import id.komorebi.model.OrderItem;
import id.komorebi.model.Order;
import id.komorebi.model.enums.OrderItemStatus;
import id.komorebi.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDAOImpl implements OrderItemDAO {

    @Override
    public OrderItem findById(int id) throws SQLException {
        String sql = "SELECT oi.order_item_id, oi.order_id, oi.menu_id, m.name as menu_name, oi.quantity, oi.price_each, oi.status " +
                     "FROM order_items oi LEFT JOIN menu_items m ON oi.menu_id = m.menu_id WHERE oi.order_item_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    OrderItem oi = new OrderItem();
                    oi.setOrderItemId(rs.getInt("order_item_id"));
                    Order order = new Order();
                    order.setOrderId(rs.getInt("order_id"));
                    oi.setOrder(order);
                    MenuItem menu = new MenuItem();
                    menu.setMenuId(rs.getInt("menu_id"));
                    menu.setName(rs.getString("menu_name"));
                    oi.setMenuItem(menu);
                    oi.setQuantity(rs.getInt("quantity"));
                    oi.setPriceEach(rs.getBigDecimal("price_each"));
                    oi.setStatus(OrderItemStatus.valueOf(rs.getString("status")));
                    return oi;
                }
            }
        }
        return null;
    }

    @Override
    public List<OrderItem> findByOrderId(int orderId) throws SQLException {
        List<OrderItem> list = new ArrayList<>();
        String sql = "SELECT oi.order_item_id, oi.order_id, oi.menu_id, m.name as menu_name, oi.quantity, oi.price_each, oi.status " +
                     "FROM order_items oi LEFT JOIN menu_items m ON oi.menu_id = m.menu_id WHERE oi.order_id = ? ORDER BY oi.order_item_id";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderItem oi = new OrderItem();
                    oi.setOrderItemId(rs.getInt("order_item_id"));
                    Order order = new Order();
                    order.setOrderId(rs.getInt("order_id"));
                    oi.setOrder(order);
                    MenuItem menu = new MenuItem();
                    menu.setMenuId(rs.getInt("menu_id"));
                    menu.setName(rs.getString("menu_name"));
                    oi.setMenuItem(menu);
                    oi.setQuantity(rs.getInt("quantity"));
                    oi.setPriceEach(rs.getBigDecimal("price_each"));
                    oi.setStatus(OrderItemStatus.valueOf(rs.getString("status")));
                    list.add(oi);
                }
            }
        }
        return list;
    }

    @Override
    public List<OrderItem> findByStatus(OrderItemStatus status) throws SQLException {
        List<OrderItem> list = new ArrayList<>();
        String sql = "SELECT oi.order_item_id, oi.order_id, oi.menu_id, m.name as menu_name, oi.quantity, oi.price_each, oi.status " +
                     "FROM order_items oi LEFT JOIN menu_items m ON oi.menu_id = m.menu_id WHERE oi.status = ? ORDER BY oi.order_item_id";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, status.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderItem oi = new OrderItem();
                    oi.setOrderItemId(rs.getInt("order_item_id"));
                    Order order = new Order();
                    order.setOrderId(rs.getInt("order_id"));
                    oi.setOrder(order);
                    MenuItem menu = new MenuItem();
                    menu.setMenuId(rs.getInt("menu_id"));
                    menu.setName(rs.getString("menu_name"));
                    oi.setMenuItem(menu);
                    oi.setQuantity(rs.getInt("quantity"));
                    oi.setPriceEach(rs.getBigDecimal("price_each"));
                    oi.setStatus(OrderItemStatus.valueOf(rs.getString("status")));
                    list.add(oi);
                }
            }
        }
        return list;
    }

    @Override
    public int insert(OrderItem item) throws SQLException {
        String sql = "INSERT INTO order_items (order_id, menu_id, quantity, price_each, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, item.getOrder().getOrderId());
            ps.setInt(2, item.getMenuItem().getMenuId());
            ps.setInt(3, item.getQuantity());
            ps.setBigDecimal(4, item.getPriceEach());
            ps.setString(5, item.getStatus().name());
            // debug: log parameters
            System.out.println("[DEBUG] Inserting OrderItem: orderId=" + item.getOrder().getOrderId()
                    + " menuId=" + item.getMenuItem().getMenuId()
                    + " qty=" + item.getQuantity()
                    + " priceEach=" + item.getPriceEach()
                    + " status=" + item.getStatus().name());
            int affected = ps.executeUpdate();
            if (affected == 0) return -1;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    @Override
    public boolean update(OrderItem item) throws SQLException {
        String sql = "UPDATE order_items SET menu_id=?, quantity=?, price_each=?, status=? WHERE order_item_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, item.getMenuItem().getMenuId());
            ps.setInt(2, item.getQuantity());
            ps.setBigDecimal(3, item.getPriceEach());
            ps.setString(4, item.getStatus().name());
            ps.setInt(5, item.getOrderItemId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean updateStatus(int orderItemId, OrderItemStatus status) throws SQLException {
        String sql = "UPDATE order_items SET status = ? WHERE order_item_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setInt(2, orderItemId);
            return ps.executeUpdate() > 0;
        }
    }
}