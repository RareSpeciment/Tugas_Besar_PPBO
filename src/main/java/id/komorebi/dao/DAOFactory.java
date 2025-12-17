package id.komorebi.dao;

import id.komorebi.dao.impl.*;

public class DAOFactory {
    public static RoleDAO createRoleDao() {
        return new RoleDAOImpl();
    }

    public static UserDAO createUserDao() {
        return new UserDAOImpl();
    }

    public static CategoryDAO createCategoryDao() {
        return new CategoryDAOImpl();
    }

    public static MenuItemDAO createMenuItemDao() {
        return new MenuItemDAOImpl();
    }

    public static MenuIngredientDAO createMenuIngredientDao() {
        return new MenuIngredientDAOImpl();
    }

    public static InventoryDAO createInventoryDao() {
        return new InventoryDAOImpl();
    }

    public static InventoryLogDAO createInventoryLogDao() {
        return new InventoryLogDAOImpl();
    }

    public static CafeTableDAO createCafeTableDao() {
        return new CafeTableDAOImpl();
    }

    public static OrderDAO createOrderDao() {
        return new OrderDAOImpl();
    }

    public static OrderItemDAO createOrderItemDao() {
        return new OrderItemDAOImpl();
    }

    public static PaymentDAO createPaymentDao() {
        return new PaymentDAOImpl();
    }

    public static ActivityLogDAO createActivityLogDao() {
        return new ActivityLogDAOImpl();
    }

    public static ReceiptDAO createReceiptDao() {
        return new ReceiptDAOImpl();
    }
}
