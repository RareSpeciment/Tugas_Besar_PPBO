package id.komorebi.view;

import id.komorebi.controller.AuthController;
import id.komorebi.util.UIHelper;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LoginView extends JPanel {
    private final AuthController controller;
    private final JTextField username = UIHelper.createTextField();
    private final JPasswordField password = new JPasswordField();
    private final JButton loginBtn = UIHelper.createSuccessButton("Login");

    public LoginView(AuthController controller) {
        this.controller = controller;
        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        setBackground(UIHelper.BACKGROUND_WHITE);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIHelper.PRIMARY_BROWN);
        headerPanel.setBorder(new EmptyBorder(30, 20, 30, 20));

        JLabel brandTitle = UIHelper.createNavTitle("Komorèbi Café");
        JLabel brandSubtitle = UIHelper.createNavSubtitle("Premium Coffee Management System");

        JPanel brandPanel = new JPanel();
        brandPanel.setLayout(new BoxLayout(brandPanel, BoxLayout.Y_AXIS));
        brandPanel.setBackground(UIHelper.PRIMARY_BROWN);
        brandPanel.add(brandTitle);
        brandPanel.add(Box.createVerticalStrut(5));
        brandPanel.add(brandSubtitle);

        headerPanel.add(brandPanel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Login form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(UIHelper.BACKGROUND_WHITE);
        formPanel.setBorder(new EmptyBorder(40, 50, 40, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(12, 0, 12, 0);
        gbc.weightx = 1.0;

        // Form Title
        JLabel formTitle = UIHelper.createTitleLabel("Login ke Akun Anda");
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(formTitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.insets = new Insets(20, 0, 8, 0);
        formPanel.add(UIHelper.createLabel("Username"), gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 15, 0);
        username.setPreferredSize(new java.awt.Dimension(350, 40));
        formPanel.add(username, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(15, 0, 8, 0);
        formPanel.add(UIHelper.createLabel("Password"), gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 15, 0);
        stylePasswordField();
        password.setPreferredSize(new java.awt.Dimension(350, 40));
        formPanel.add(password, gbc);

        JPanel buttonPanel = UIHelper.createHorizontalButtonPanel();
        buttonPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(25, 0, 0, 0);
        formPanel.add(buttonPanel, gbc);

        buttonPanel.add(loginBtn);
        
        JButton btnBack = new JButton("Back to MainMenu");
        btnBack.setContentAreaFilled(false);
        btnBack.setBorderPainted(false);
        btnBack.setForeground(Color.GRAY);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(e -> controller.back());
        buttonPanel.add(btnBack);

        add(formPanel, BorderLayout.CENTER);

        // Footer
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(UIHelper.ACCENT_CREAM);
        footerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        JLabel footerLabel = UIHelper.createSmallLabel("© 2024 Komorèbi Café Management System");
        footerPanel.add(footerLabel);
        add(footerPanel, BorderLayout.SOUTH);

        setupListeners();
    }

    private void stylePasswordField() {
        password.setFont(UIHelper.MAIN_FONT);
        password.setBorder(UIHelper.createTextField().getBorder());
        password.setMargin(new Insets(10, 12, 10, 12));
        password.setBackground(Color.WHITE);
        password.setForeground(UIHelper.TEXT_DARK);
        password.setCaretColor(UIHelper.TEXT_DARK);
    }

    private void setupListeners() {
        loginBtn.addActionListener(this::handleLogin);
        password.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleLogin(null);
                }
            }
        });
    }

    private void handleLogin(ActionEvent e) {
        String user = username.getText();
        String pass = String.valueOf(password.getPassword());
        controller.handleLogin(user, pass);
    }

    public void onLoginFailed(String reason) {
        UIHelper.showError(reason); 
        password.setText("");
    }
}