package id.komorebi.view;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

public class StockUpdateDialog extends JDialog {
    private JSpinner quantitySpinner;
    private JTextArea noteArea;
    private boolean confirmed = false;

    public StockUpdateDialog(Window parent, String ingredientName) {
        super(parent, "Update Stock: " + ingredientName, ModalityType.APPLICATION_MODAL); 
        initUI();
        setSize(400, 300);
        setLocationRelativeTo(parent);
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Input Quantity
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Change Amount (+/-):"), gbc);

        gbc.gridx = 1; 
        quantitySpinner = new JSpinner(new SpinnerNumberModel(0.0, -10000.0, 10000.0, 1.0));
        formPanel.add(quantitySpinner, gbc);

        // Input Note
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(new JLabel("Note / Reason:"), gbc);

        gbc.gridx = 1; 
        noteArea = new JTextArea(4, 20);
        noteArea.setLineWrap(true);
        formPanel.add(new JScrollPane(noteArea), gbc);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelBtn = new JButton("Cancel");
        JButton saveBtn = new JButton("Update Stock");

        cancelBtn.addActionListener(e -> dispose());
        saveBtn.addActionListener(e -> {
            confirmed = true;
            dispose();
        });

        btnPanel.add(cancelBtn);
        btnPanel.add(saveBtn);

        add(formPanel, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
        ((JPanel)getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    public boolean isConfirmed() { return confirmed; }

    public BigDecimal getQuantityChange() {
        Double val = (Double) quantitySpinner.getValue();
        return BigDecimal.valueOf(val);
    }

    public String getNote() { return noteArea.getText().trim(); }
}