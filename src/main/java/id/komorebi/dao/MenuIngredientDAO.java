package id.komorebi.dao;

import id.komorebi.model.MenuIngredient;
import java.sql.SQLException;
import java.util.List;

public interface MenuIngredientDAO {
    MenuIngredient findById(int id) throws SQLException;
    List<MenuIngredient> findByMenuId(int menuId) throws SQLException;
    int insert(MenuIngredient mi) throws SQLException;
    boolean update(MenuIngredient mi) throws SQLException;
    boolean delete(int id) throws SQLException;
}
