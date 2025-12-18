package id.komorebi.controller;

import id.komorebi.view.MainFrame;

public class AdminController {
    private final UserController userController;
    private final MenuController menuController;
    private final CategoryController categoryController;
    private final ActivityLogController activityLogController;

    //Terima 4 Parameter agar cocok dengan AuthController
    public AdminController(UserController userController, MenuController menuController, CategoryController categoryController, ActivityLogController activityLogController) {
        this.userController = userController;
        this.menuController = menuController;
        this.categoryController = categoryController;
        this.activityLogController = activityLogController;
    }

    //
    public UserController getUserController() { return userController; }
    public MenuController getMenuController() { return menuController; }
    public CategoryController getCategoryController() { return categoryController; }
    public ActivityLogController getActivityLogController() { return activityLogController; }

    public void openUserManagement() {
        // Jangan 'new AdminUserView', tapi panggil view yang sudah didaftarkan di AdminPanelView
        MainFrame.getInstance().showView("ADMIN_USER");
    }

    public void openMenuManagement() {
        MainFrame.getInstance().showView("ADMIN_MENU");
    }

    public void openCategoryManagement() {
        MainFrame.getInstance().showView("ADMIN_CATEGORY");
    }

    public void openLogs() {
        MainFrame.getInstance().showView("ADMIN_LOGS");
    }

    public void showRevenueReport() {
        try {
            id.komorebi.dao.PaymentDAO paymentDao = id.komorebi.dao.DAOFactory.createPaymentDao();
            double total = paymentDao.getTotalSalesByDate(java.time.LocalDate.now());
            String formatted = "Rp " + java.text.NumberFormat.getInstance().format(total);
            id.komorebi.util.UIHelper.showInfo("Total Pendapatan Hari Ini:\n\n" + formatted);
        } catch (Exception e) {}
    }
}