package id.komorebi.dao;

import id.komorebi.model.OrderItem;
import id.komorebi.model.enums.OrderItemStatus;

import java.sql.SQLException;
import java.util.List;

public interface OrderItemDAO {
    OrderItem findById(int id) throws SQLException;
    List<OrderItem> findByOrderId(int orderId) throws SQLException;
    List<OrderItem> findByStatus(OrderItemStatus status) throws SQLException;
    int insert(OrderItem item) throws SQLException;
    boolean update(OrderItem item) throws SQLException;
    boolean updateStatus(int orderItemId, OrderItemStatus status) throws SQLException;
}
