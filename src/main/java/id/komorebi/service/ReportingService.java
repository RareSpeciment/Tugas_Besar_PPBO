package id.komorebi.service;

import id.komorebi.dao.*;
import id.komorebi.model.ReportEntry;

import java.util.List;
import java.time.LocalDate;

public class ReportingService {
    private final PaymentDAO paymentDAO;
    private final MenuItemDAO menuItemDAO;

    public ReportingService() {
        this.paymentDAO = DAOFactory.createPaymentDao();
        this.menuItemDAO = DAOFactory.createMenuItemDao();
    }

    public double getTotalSales(LocalDate date) {
        try {
            return paymentDAO.getTotalSalesByDate(date);
        } catch (java.sql.SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getTransactionCount(LocalDate date) {
        try {
            return paymentDAO.getDailyTransactionCount(date);
        } catch (java.sql.SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ReportEntry> getTopMenuItems(LocalDate date) {
        try {
            return menuItemDAO.getTopSellingItems(date);
        } catch (java.sql.SQLException e) {
            throw new RuntimeException(e);
        }
    }
}