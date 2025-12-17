package id.komorebi.dao;

import id.komorebi.model.User;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UserDAO {
    Optional<User> findById(int id) throws SQLException;
    Optional<User> findByUsername(String username) throws SQLException;
    List<User> findAll() throws SQLException;
    int insert(User user) throws SQLException;
    boolean update(User user) throws SQLException;
    boolean deactivate(int id) throws SQLException;
    boolean activate(int id) throws SQLException;
}