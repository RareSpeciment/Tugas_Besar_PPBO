package id.komorebi.dao;

import id.komorebi.model.Order;
import id.komorebi.model.enums.OrderStatus;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface OrderDAO {
Optional<Order> findById(int id) throws SQLException;
    List<Order> findAll() throws SQLException;
    List<Order> findByStatus(OrderStatus status) throws SQLException;
    int insert(Order order) throws SQLException; // returns generated id
    boolean update(Order order) throws SQLException;
    boolean markPaid(int orderId, int cashierId) throws SQLException;
    boolean cancelOrder(int orderId) throws SQLException;
}
