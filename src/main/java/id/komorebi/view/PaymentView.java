package id.komorebi.view;

import id.komorebi.controller.PaymentController;
import id.komorebi.model.Order;
import javax.swing.*;
import java.awt.*;

public class PaymentView extends JFrame {
    private PaymentController controller;
    private JComboBox<String> paymentMethodBox;
    private JButton payBtn;

    public PaymentView(PaymentController controller) {
        this.controller = controller;

        setTitle("Payment");
        setSize(400, 200);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        String[] methods = { "CASH", "CARD", "QRIS", "TRANSFER" };
        paymentMethodBox = new JComboBox<>(methods);

        payBtn = new JButton("Confirm Payment");
        payBtn.addActionListener(e ->
            controller.processPayment((String) paymentMethodBox.getSelectedItem())
        );

        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.add(paymentMethodBox);
        panel.add(payBtn);

        add(panel);
    }

    public void showOrderInfo(Order order) {
        setTitle("Payment - Order #" + order.getOrderId() + " | Total: " + order.getTotal());
    }
}