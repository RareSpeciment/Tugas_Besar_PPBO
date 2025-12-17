package id.komorebi.util;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class UIHelper {
    public static final Color PRIMARY_BROWN = new Color(92, 64, 51);
    public static final Color SECONDARY_BROWN = new Color(120, 85, 65);
    public static final Color DARK_BROWN = new Color(50, 35, 25);
    public static final Color ACCENT_CREAM = new Color(255, 236, 200);
    public static final Color LIGHT_CREAM = new Color(255, 245, 220);
    public static final Color BACKGROUND_WHITE = new Color(250, 248, 240);
    public static final Color TEXT_DARK = new Color(0, 0, 0);
    public static final Color TEXT_LIGHT = new Color(100, 85, 75);
    public static final Color TEXT_LIGHTER = new Color(150, 130, 110);
    public static final Color BUTTON_HOVER = new Color(70, 50, 40);
    public static final Color BORDER_LIGHT = new Color(200, 180, 160);
    public static final Color BORDER_DARK = new Color(150, 130, 110);
    public static final Color DIVIDER = new Color(220, 200, 180);
    public static final Color SUCCESS_GREEN = new Color(76, 175, 80);
    public static final Color SUCCESS_DARK = new Color(56, 142, 60);
    public static final Color ERROR_RED = new Color(244, 67, 54);
    public static final Color ERROR_DARK = new Color(211, 47, 47);
    public static final Color WARNING_YELLOW = new Color(255, 193, 7);
    public static final Color HIGHLIGHT = new Color(255, 250, 230);
    public static final Color SHADOW = new Color(0, 0, 0);

    public static final Font LOGO_FONT = new Font("Segoe UI", Font.BOLD, 32);
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 28);
    public static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font MAIN_FONT = new Font("Segoe UI", Font.PLAIN, 15);
    public static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font CATEGORY_FONT = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font PRICE_FONT = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font SMALL_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font TINY_FONT = new Font("Segoe UI", Font.PLAIN, 11);

    public static JTextArea createDescriptionLabel(String text) {
        JTextArea area = new JTextArea(text);
        area.setFont(SMALL_FONT);
        area.setForeground(TEXT_LIGHT);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(false);
        area.setOpaque(false);
        area.setFocusable(false);
        area.setSize(180, 50);
        return area;
    }

    public static JPanel createHeaderPanel(String title, java.awt.event.ActionListener logoutAction) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARY_BROWN);
        header.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLbl.setForeground(ACCENT_CREAM);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logoutBtn.setBackground(Color.WHITE);
        logoutBtn.setForeground(Color.BLACK);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        logoutBtn.addActionListener(logoutAction);

        header.add(titleLbl, BorderLayout.WEST);
        header.add(logoutBtn, BorderLayout.EAST);
        return header;
    }

    // ===== TABLE STYLING =====
    public static void styleTable(JTable table) {
        table.setRowHeight(35);
        table.setFont(MAIN_FONT);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(ACCENT_CREAM);
        table.setSelectionForeground(TEXT_DARK);
        
        // Header Table
        javax.swing.table.JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(SECONDARY_BROWN);
        header.setForeground(Color.BLACK);
        header.setPreferredSize(new Dimension(0, 40));
        
        // Center Alignment untuk sel tertentu (Opsional)
        javax.swing.table.DefaultTableCellRenderer centerRenderer = new javax.swing.table.DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(String.class, centerRenderer);
    }
    
    public static JPanel createCardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        // Border abu-abu tipis sebagai bingkai
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        return panel;
    }

    public static JLabel createSectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setForeground(PRIMARY_BROWN);
        label.setBorder(new EmptyBorder(15, 0, 10, 0));
        return label;
    }

    public static JPanel createHorizontalButtonPanel(JButton... buttons) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBackground(BACKGROUND_WHITE);
        panel.setBorder(new EmptyBorder(15, 0, 0, 0));

        for (JButton btn : buttons) {
            panel.add(btn);
            panel.add(Box.createHorizontalStrut(10));
        }
        panel.add(Box.createHorizontalGlue());
        return panel;
    }

    public static void setKomorebiTheme(JFrame frame) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
        frame.getContentPane().setBackground(BACKGROUND_WHITE);
    }

    public static void applyFrameSettings(JFrame frame, String title, int width, int height) {
        frame.setTitle(title);
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(BACKGROUND_WHITE);
    }
    
    public static void showInfo(String msg) {
        showCustomDialog("Information", msg, SUCCESS_GREEN);
    }

    public static void showError(String msg) {
        showCustomDialog("Error", msg, ERROR_RED);
    }

    private static void showCustomDialog(String title, String msg, Color color) {
        Window parent = KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow();
        JDialog dialog = new JDialog(parent, title, Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(350, 180);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new BorderLayout());

        // Header Warna
        JPanel header = new JPanel();
        header.setBackground(color);
        header.setPreferredSize(new Dimension(100, 40));
        JLabel titleLbl = new JLabel(title, SwingConstants.CENTER);
        titleLbl.setFont(BUTTON_FONT);
        titleLbl.setForeground(Color.WHITE);
        header.add(titleLbl);

        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(BACKGROUND_WHITE);
        JTextArea area = new JTextArea(msg);
        area.setWrapStyleWord(true);
        area.setLineWrap(true);
        area.setEditable(false);
        area.setFont(MAIN_FONT);
        area.setBackground(BACKGROUND_WHITE);
        area.setSize(280, 100);
        content.add(area);

        JPanel footer = new JPanel();
        footer.setBackground(BACKGROUND_WHITE);
        JButton okBtn = createButton("OK");
        okBtn.addActionListener(e -> dialog.dispose());
        footer.add(okBtn);

        dialog.add(header, BorderLayout.NORTH);
        dialog.add(content, BorderLayout.CENTER);
        dialog.add(footer, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    public static boolean confirm(String msg) {
        Window parent = KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow();
        JDialog dialog = new JDialog(parent, "Confirmation", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(350, 180);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel();
        header.setBackground(PRIMARY_BROWN);
        JLabel titleLbl = new JLabel("Confirmation", SwingConstants.CENTER);
        titleLbl.setFont(BUTTON_FONT);
        titleLbl.setForeground(Color.WHITE);
        header.add(titleLbl);

        // Pesan
        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(BACKGROUND_WHITE);
        JTextArea area = new JTextArea(msg);
        area.setWrapStyleWord(true);
        area.setLineWrap(true);
        area.setEditable(false);
        area.setFont(MAIN_FONT);
        area.setBackground(BACKGROUND_WHITE);
        content.add(area);

        // Tombol Yes/No
        JPanel footer = new JPanel();
        footer.setBackground(BACKGROUND_WHITE);
        JButton yesBtn = createSuccessButton("YES");
        JButton noBtn = createErrorButton("NO");
        
        final boolean[] result = {false};
        
        yesBtn.addActionListener(e -> { result[0] = true; dialog.dispose(); });
        noBtn.addActionListener(e -> { result[0] = false; dialog.dispose(); });

        footer.add(yesBtn);
        footer.add(noBtn);

        dialog.add(header, BorderLayout.NORTH);
        dialog.add(content, BorderLayout.CENTER);
        dialog.add(footer, BorderLayout.SOUTH);
        dialog.setVisible(true);
        
        return result[0];
    }
    
    public static JTextField createTextField() {
        JTextField f = new JTextField(); 
        f.setFont(MAIN_FONT); 
        f.setBorder(BorderFactory.createLineBorder(BORDER_LIGHT, 1));
        return f; 
    }
    public static JButton createSuccessButton(String t) { 
        JButton b = new JButton(t); 
        b.setBackground(SUCCESS_GREEN); b.setForeground(Color.BLACK); 
        b.setFont(BUTTON_FONT); return b; 
    }
    public static JButton createErrorButton(String t) { 
        JButton b = new JButton(t); 
        b.setBackground(ERROR_RED); b.setForeground(Color.BLACK); 
        b.setFont(BUTTON_FONT); return b; 
    }
    public static JButton createButton(String t) {
        JButton b = new JButton(t); 
        b.setBackground(Color.WHITE); b.setForeground(Color.BLACK); 
        b.setFont(BUTTON_FONT); return b; 
    }
    public static JLabel createNavTitle(String t) { JLabel l = new JLabel(t); l.setFont(LOGO_FONT); l.setForeground(ACCENT_CREAM); return l; }
    public static JLabel createNavSubtitle(String t) { JLabel l = new JLabel(t); l.setFont(SMALL_FONT); l.setForeground(LIGHT_CREAM); return l; }
    public static JLabel createTitleLabel(String t) { JLabel l = new JLabel(t); l.setFont(TITLE_FONT); l.setForeground(PRIMARY_BROWN); return l; }
    public static JLabel createLabel(String t) { JLabel l = new JLabel(t); l.setFont(MAIN_FONT); return l; }
    public static JLabel createSmallLabel(String t) { JLabel l = new JLabel(t); l.setFont(SMALL_FONT); return l; }
    public static JPanel createHorizontalButtonPanel() { JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER)); p.setBackground(BACKGROUND_WHITE); return p; }

    public static void setupCommonStyles() {
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());     
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}