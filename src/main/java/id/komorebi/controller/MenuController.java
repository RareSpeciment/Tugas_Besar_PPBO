package id.komorebi.controller;

import id.komorebi.dao.DAOFactory;
import id.komorebi.dao.MenuItemDAO;
import id.komorebi.dao.CategoryDAO;
import id.komorebi.model.MenuItem;
import id.komorebi.model.Category;
import id.komorebi.util.UIHelper;

import java.sql.SQLException;
import java.util.List;

public class MenuController {

    private final MenuItemDAO menuItemDAO;
    private final CategoryDAO categoryDAO;

    public MenuController() {
        this.menuItemDAO = DAOFactory.createMenuItemDao();
        this.categoryDAO = DAOFactory.createCategoryDao();
    }

    public List<MenuItem> getAllMenu() {
        try {
            return menuItemDAO.findAll();
        } catch (SQLException e) {
            UIHelper.showError("Failed to load menu: " + e.getMessage());
            return List.of();
        }
    }

    public List<Category> getCategories() {
        try {
            return categoryDAO.findAll();
        } catch (SQLException e) {
            UIHelper.showError("Failed to load categories: " + e.getMessage());
            return List.of();
        }
    }

    public boolean createMenu(MenuItem item) {
        try {
            int id = menuItemDAO.insert(item);
            if (id > 0) {
                UIHelper.showInfo("Menu item added.");
                return true;
            }
            return false;
        } catch (SQLException e) {
            UIHelper.showError("Error: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteMenu(int id) {
        try {
            if (menuItemDAO.delete(id)) {
                UIHelper.showInfo("Menu item deleted.");
                return true;
            }
            return false;
        } catch (SQLException e) {
            UIHelper.showError("Error: " + e.getMessage());
            return false;
        }
    }
}