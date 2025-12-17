package id.komorebi.service;

import id.komorebi.dao.DAOFactory;
import id.komorebi.dao.OrderDAO;
import id.komorebi.dao.PaymentDAO;
import id.komorebi.model.Order;
import id.komorebi.model.Payment;
import id.komorebi.model.User;
import id.komorebi.model.enums.PaymentMethod;

import java.sql.SQLException;

public class PaymentService {

    private final PaymentDAO paymentDAO;
    private final OrderDAO orderDAO;

    public PaymentService() {
        this.paymentDAO = DAOFactory.createPaymentDao();
        this.orderDAO = DAOFactory.createOrderDao();
    }

    public boolean payOrder(int orderId, String method, int cashierId) {
        try {
            Payment p = new Payment();
            Order o = new Order();
            o.setOrderId(orderId);
            p.setOrder(o);
            p.setPaymentMethod(PaymentMethod.valueOf(method));
            User u = new User();
            u.setUserId(cashierId);
            p.setCashier(u);
            int id = paymentDAO.insert(p);
            if (id <= 0) return false;
            return orderDAO.markPaid(orderId, cashierId);
        } catch (SQLException ex) {
            return false;
        }
    }

    public Payment getPayment(int paymentId) {
        try {
            return paymentDAO.findById(paymentId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}