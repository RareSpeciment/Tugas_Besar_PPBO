package id.komorebi.service;

import id.komorebi.dao.DAOFactory;
import id.komorebi.dao.UserDAO;
import id.komorebi.model.User;
import id.komorebi.model.enums.UserStatus;
import id.komorebi.util.PasswordUtil;

import java.sql.SQLException;
import java.util.Optional;

public class AuthService {

    private final UserDAO userDAO;

    public AuthService() {
        this.userDAO = DAOFactory.createUserDao();
    }

    public User login(String username, String password) {
        try {
            Optional<User> ou = userDAO.findByUsername(username);
            if (!ou.isPresent()) return null;
            User user = ou.get();
            if (user.getStatus() != UserStatus.ACTIVE) return null;
            if (PasswordUtil.verifyPassword(password, user.getPasswordHash())) return user;
            return null;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public boolean deactivateUser(int userId) {
        try {
            return userDAO.deactivate(userId);
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean activateUser(int userId) {
        try {
            return userDAO.activate(userId);
        } catch (SQLException e) {
            return false;
        }
    }

    public int createUser(String username, String passwordPlain, String fullname, int roleId) {
        try {
            User u = new User();
            u.setUsername(username);
            u.setPasswordHash(PasswordUtil.hashPassword(passwordPlain));
            u.setFullname(fullname);
            u.setRole(new id.komorebi.model.Role(roleId, null));
            // status and createdAt are set by default in User constructor
            return userDAO.insert(u);
        } catch (SQLException e) {
            return -1;
        }
    }

    public boolean changePassword(int userId, String newPassword) {
        try {
            Optional<User> ou = userDAO.findById(userId);
            if (!ou.isPresent()) return false;
            User user = ou.get();
            user.setPasswordHash(PasswordUtil.hashPassword(newPassword));
            return userDAO.update(user);
        } catch (SQLException e) {
            return false;
        }
    }
}
