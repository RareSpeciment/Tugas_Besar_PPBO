package id.komorebi.dao;

import id.komorebi.model.Role;
import java.sql.SQLException;
import java.util.List;

public interface RoleDAO {
    Role findById(int id) throws SQLException;
    Role findByName(String name) throws SQLException;
    List<Role> findAll() throws SQLException;
    int insert(Role role) throws SQLException;
    boolean update(Role role) throws SQLException;
    boolean delete(int id) throws SQLException;
}