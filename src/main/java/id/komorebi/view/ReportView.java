package id.komorebi.view;

import id.komorebi.controller.ReportController;
import javax.swing.*;
import java.awt.*;

public class ReportView extends JFrame {

    private ReportController controller;

    private JTextArea reportArea;

    public ReportView(ReportController controller) {
        this.controller = controller;

        setTitle("Sales Reports");
        setSize(600, 500);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        add(new JScrollPane(reportArea), BorderLayout.CENTER);

        JButton refreshBtn = new JButton("Refresh Report");
        refreshBtn.addActionListener(e -> {
            String report = controller.loadReport();
            displayReport(report);
        });

        add(refreshBtn, BorderLayout.SOUTH);
        
        String initialReport = controller.loadReport();
        displayReport(initialReport);
    }

    public void displayReport(String text) {
        reportArea.setText(text);
    }
}