package id.komorebi.controller;

import id.komorebi.dao.CategoryDAO;
import id.komorebi.dao.DAOFactory;
import id.komorebi.model.Category;
import id.komorebi.util.UIHelper;

import java.util.List;
import java.util.Collections;

public class CategoryController {

    private final CategoryDAO categoryDAO;

    public CategoryController() {
        this.categoryDAO = DAOFactory.createCategoryDao();
    }

    public List<Category> getAllCategories() {
        try {
            return categoryDAO.findAll();
        } catch (Exception e) {
            UIHelper.showError("Failed to load categories: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public boolean createCategory(String name) {
        try {
            Category c = new Category();
            c.setName(name);
            int id = categoryDAO.insert(c);
            return id > 0;
        } catch (Exception e) {
            UIHelper.showError("Failed to create category: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteCategory(int id) {
        try {
            return categoryDAO.delete(id);
        } catch (Exception e) {
            UIHelper.showError("Failed to delete category (Might be used in Menu Items): " + e.getMessage());
            return false;
        }
    }
}