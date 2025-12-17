package id.komorebi.controller;

import id.komorebi.dao.DAOFactory;
import id.komorebi.dao.UserDAO;
import id.komorebi.model.User;
import id.komorebi.service.AuthService;
import id.komorebi.util.UIHelper;

import java.sql.SQLException;
import java.util.List;

public class UserController {

    private final AuthService authService;
    private final UserDAO userDAO;

    public UserController(AuthService authService) {
        this.authService = authService;
        this.userDAO = DAOFactory.createUserDao();
    }

    public List<User> getAllUsers() {
        try {
            return userDAO.findAll();
        } catch (SQLException e) {
            UIHelper.showError("Failed to load users: " + e.getMessage());
            return List.of();
        }
    }

    public boolean deactivateUser(int userId) {
        try {
            authService.deactivateUser(userId);
            UIHelper.showInfo("User deactivated.");
            return true;
        } catch (Exception e) {
            UIHelper.showError("Failed: " + e.getMessage());
            return false;
        }
    }

    public boolean activateUser(int userId) {
        try {
            boolean ok = authService.activateUser(userId);
            if (ok) UIHelper.showInfo("User activated.");
            else UIHelper.showError("Failed to activate user.");
            return ok;
        } catch (Exception e) {
            UIHelper.showError("Failed: " + e.getMessage());
            return false;
        }
    }

    public boolean createUser(String username, String passwordPlain, String fullname, int roleId) {
        try {
            int id = authService.createUser(username, passwordPlain, fullname, roleId);
            if (id > 0) {
                UIHelper.showInfo("User created successfully (ID: " + id + ").");
                return true;
            } else {
                UIHelper.showError("Failed to create user.");
                return false;
            }
        } catch (Exception e) {
            UIHelper.showError("Failed: " + e.getMessage());
            return false;
        }
    }
}
