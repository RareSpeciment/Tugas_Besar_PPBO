package id.komorebi.dao;

import id.komorebi.model.Category;
import java.sql.SQLException;
import java.util.List;

public interface CategoryDAO {
    Category findById(int id) throws SQLException;
    List<Category> findAll() throws SQLException;
    int insert(Category category) throws SQLException;
    boolean update(Category category) throws SQLException;
    boolean delete(int id) throws SQLException;
}
