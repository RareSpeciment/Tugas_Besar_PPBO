package id.komorebi.controller;

import id.komorebi.service.PaymentService;
import id.komorebi.util.UIHelper;

public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    public boolean payOrder(int orderId, String method, int cashierId) {
        try {
            service.payOrder(orderId, method, cashierId);
            UIHelper.showInfo("Payment recorded.");
            return true;
        } catch (Exception e) {
            UIHelper.showError("Payment failed: " + e.getMessage());
            return false;
        }
    }

    public boolean processPayment(String method) {
        // TODO: Implement with current order context (orderId, cashierId) from session
        UIHelper.showError("Payment processing requires order context - not yet implemented.");
        return false;
    }
}
