package id.komorebi.view;

import id.komorebi.controller.OrderController;
import id.komorebi.controller.AuthController;
import id.komorebi.dao.CafeTableDAO;
import id.komorebi.dao.DAOFactory;
import id.komorebi.model.CafeTable;
import id.komorebi.model.MenuItem;
import id.komorebi.util.UIHelper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class MainMenuView extends JPanel {
    private final OrderController controller;

    private JPanel menuPanel;
    private JPanel cartPanel;
    private JPanel cartListPanel;
    private JLabel lblTotalParams;
    
    private Map<Integer, Integer> cart = new HashMap<>();
    private Map<Integer, MenuItem> menuIndex = new HashMap<>();

    public MainMenuView(OrderController controller) {
        this.controller = controller;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        
        // --- HEADER ---
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UIHelper.PRIMARY_BROWN);
        header.setPreferredSize(new Dimension(0, 70));
        header.setBorder(new EmptyBorder(0, 25, 0, 25));

        JLabel title = new JLabel("Komorebi Café");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(UIHelper.ACCENT_CREAM);

        JButton btnStaffLogin = new JButton("Login");
        btnStaffLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnStaffLogin.setForeground(UIHelper.PRIMARY_BROWN);
        btnStaffLogin.setBackground(UIHelper.ACCENT_CREAM);
        btnStaffLogin.setFocusPainted(false);
        btnStaffLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnStaffLogin.setPreferredSize(new Dimension(100, 35)); 

        JPanel btnWrapper = new JPanel(new GridBagLayout()); 
        btnWrapper.setOpaque(false);
        btnWrapper.add(btnStaffLogin);
        
        header.add(title, BorderLayout.WEST);
        header.add(btnWrapper, BorderLayout.EAST); 
        add(header, BorderLayout.NORTH);
        
        // AKSI LOGIN
        btnStaffLogin.addActionListener(e -> openStaffLogin());
        
        // --- CONTENT ---
        JPanel mainContent = createMainContent();
        add(mainContent, BorderLayout.CENTER);

        LoadMenu();
    }

    private JPanel createMainContent() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Color.WHITE);

        menuPanel = new JPanel(new GridLayout(0, 4, 15, 15)); 
        menuPanel.setBackground(Color.WHITE);
        menuPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JScrollPane menuScroll = new JScrollPane(menuPanel);
        menuScroll.setBorder(null);
        menuScroll.getVerticalScrollBar().setUnitIncrement(16);

        cartPanel = new JPanel(new BorderLayout());
        cartPanel.setPreferredSize(new Dimension(360, 0)); 
        cartPanel.setBackground(UIHelper.LIGHT_CREAM);
        cartPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.LIGHT_GRAY));
        
        JLabel cartTitle = new JLabel("Your Order", SwingConstants.CENTER);
        cartTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        cartTitle.setBorder(new EmptyBorder(20, 0, 10, 0));
        cartTitle.setForeground(UIHelper.PRIMARY_BROWN);
        
        cartListPanel = new JPanel();
        cartListPanel.setLayout(new BoxLayout(cartListPanel, BoxLayout.Y_AXIS));
        cartListPanel.setBackground(UIHelper.LIGHT_CREAM);
        
        JScrollPane cartScroll = new JScrollPane(cartListPanel);
        cartScroll.setBorder(null);
        cartScroll.getViewport().setBackground(UIHelper.LIGHT_CREAM);

        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(UIHelper.LIGHT_CREAM);
        footerPanel.setBorder(new EmptyBorder(10, 15, 15, 15));

        lblTotalParams = new JLabel("Total: Rp 0", SwingConstants.RIGHT);
        lblTotalParams.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTotalParams.setForeground(UIHelper.PRIMARY_BROWN);
        lblTotalParams.setBorder(new EmptyBorder(0, 0, 15, 0));

        JButton btnSubmit = UIHelper.createSuccessButton("ORDER NOW");
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnSubmit.setPreferredSize(new Dimension(0, 50));
        btnSubmit.addActionListener(e -> handleSubmitOrder());

        footerPanel.add(lblTotalParams, BorderLayout.NORTH);
        footerPanel.add(btnSubmit, BorderLayout.CENTER);

        cartPanel.add(cartTitle, BorderLayout.NORTH);
        cartPanel.add(cartScroll, BorderLayout.CENTER);
        cartPanel.add(footerPanel, BorderLayout.SOUTH);

        container.add(menuScroll, BorderLayout.CENTER);
        container.add(cartPanel, BorderLayout.EAST);
        
        return container;
    }

    private void LoadMenu() {
        SwingUtilities.invokeLater(() -> {
            try {
                List<MenuItem> items = controller.getAvailableMenuItems();
                menuPanel.removeAll();
                menuIndex.clear();
                
                for (MenuItem mi : items) {
                    menuIndex.put(mi.getMenuId(), mi);
                    boolean isStockReady = controller.isMenuAvailable(mi.getMenuId());
                    menuPanel.add(createMenuCard(mi, isStockReady));
                }
                menuPanel.revalidate();
                menuPanel.repaint();
            } catch (Exception e) { e.printStackTrace(); }
        });
    }

    private JButton createMenuCard(MenuItem mi, boolean isStockReady) {
        JButton btn = new JButton();
        btn.setLayout(new BorderLayout(0, 5));
        btn.setBackground(Color.WHITE);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            new EmptyBorder(10, 10, 10, 10)
        ));
        btn.setPreferredSize(new Dimension(180, 200));

        JLabel imgLabel = new JLabel();
        imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imgLabel.setPreferredSize(new Dimension(0, 100));
        
        if (mi.getImagePath() != null && !mi.getImagePath().isEmpty()) {
            try {
                // Cek path gambar, pastikan diawali slash jika di resources
                String path = mi.getImagePath().startsWith("/") ? mi.getImagePath() : "/" + mi.getImagePath();
                java.net.URL imageUrl = getClass().getResource(path);
                
                if (imageUrl != null) {
                    ImageIcon icon = new ImageIcon(imageUrl);
                    Image img = icon.getImage().getScaledInstance(120, 90, Image.SCALE_SMOOTH);
                    imgLabel.setIcon(new ImageIcon(img));
                } else {
                    // Fallback jika resource null
                    imgLabel.setText("No Image");
                }
            } catch (Exception e) { 
                imgLabel.setText("No Image"); 
            }
        } else { 
            imgLabel.setText("No Image"); 
        }

        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setOpaque(false);

        JLabel nameLabel = new JLabel(mi.getName(), SwingConstants.CENTER);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nameLabel.setForeground(UIHelper.PRIMARY_BROWN);

        String subText = isStockReady ? "Rp " + (int)mi.getPrice().doubleValue() : "SOLD OUT";
        JLabel priceLabel = new JLabel(subText, SwingConstants.CENTER);
        priceLabel.setFont(UIHelper.SMALL_FONT);
        
        if (!isStockReady) {
            priceLabel.setForeground(Color.RED);
            btn.setEnabled(false);
            btn.setBackground(new Color(245, 245, 245));
        } else {
            priceLabel.setForeground(Color.DARK_GRAY);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.addActionListener(e -> addToCart(mi));
            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btn.setBorder(BorderFactory.createLineBorder(UIHelper.PRIMARY_BROWN, 2));
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btn.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                        new EmptyBorder(10, 10, 10, 10)
                    ));
                }
            });
        }

        infoPanel.add(nameLabel);
        infoPanel.add(priceLabel);
        btn.add(imgLabel, BorderLayout.CENTER);
        btn.add(infoPanel, BorderLayout.SOUTH);

        return btn;
    }

    private void addToCart(MenuItem mi) {
        cart.put(mi.getMenuId(), cart.getOrDefault(mi.getMenuId(), 0) + 1);
        updateCartDisplay();
    }

    private void updateCartDisplay() {
        cartListPanel.removeAll();
        double grandTotal = 0;
        
        for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
            MenuItem mi = menuIndex.get(entry.getKey());
            int qty = entry.getValue();
            double totalItemPrice = mi.getPrice().doubleValue() * qty;
            grandTotal += totalItemPrice;
            
            JPanel row = new JPanel(new BorderLayout());
            row.setBackground(UIHelper.LIGHT_CREAM);
            row.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(5, 10, 5, 10),
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220,220,220))
            ));
            row.setMaximumSize(new Dimension(400, 60));
            
            String html = String.format("<html><b>%s</b><br><font color='gray' size='3'>Rp %d x %d = <b>Rp %d</b></font></html>", 
                    mi.getName(), (int)mi.getPrice().doubleValue(), qty, (int)totalItemPrice);
            JLabel lbl = new JLabel(html);
            lbl.setFont(UIHelper.MAIN_FONT);
            
            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
            btnPanel.setBackground(UIHelper.LIGHT_CREAM);

            JButton btnMinus = new JButton("-");
            styleMiniButton(btnMinus, Color.BLACK);
            btnMinus.addActionListener(e -> {
                if (qty > 1) cart.put(entry.getKey(), qty - 1);
                else cart.remove(entry.getKey());
                updateCartDisplay();
            });

            JButton btnRemove = new JButton("x");
            styleMiniButton(btnRemove, Color.RED);
            btnRemove.addActionListener(e -> {
                cart.remove(entry.getKey());
                updateCartDisplay();
            });

            btnPanel.add(btnMinus);
            btnPanel.add(Box.createHorizontalStrut(8));
            btnPanel.add(btnRemove);

            row.add(lbl, BorderLayout.CENTER);
            row.add(btnPanel, BorderLayout.EAST);
            
            cartListPanel.add(row);
        }
        
        lblTotalParams.setText("Total: Rp " + (int)grandTotal);
        
        cartListPanel.revalidate();
        cartListPanel.repaint();
    }

    private void styleMiniButton(JButton btn, Color fgColor) {
        btn.setForeground(fgColor);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btn.setMargin(new Insets(0, 5, 0, 5));
    }

    private void handleSubmitOrder() {
        if (cart.isEmpty()) {
            UIHelper.showError("Your cart is empty!");
            return;
        }
        try {
            CafeTableDAO tableDAO = DAOFactory.createCafeTableDao();
            List<CafeTable> tables = tableDAO.findAll();

            // 3. FIX POPUP DIALOG: Karena 'this' adalah JPanel, kita butuh window ancestor
            Window parentWindow = SwingUtilities.getWindowAncestor(this);
            JDialog dialog = new JDialog(parentWindow, "Select Table", Dialog.ModalityType.APPLICATION_MODAL);
            
            dialog.setSize(350, 200);
            dialog.setLocationRelativeTo(this);
            dialog.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 30));

            String[] opts = new String[tables.size()];
            for (int i = 0; i < tables.size(); i++) opts[i] = tables.get(i).getTableName();

            JComboBox<String> cb = new JComboBox<>(opts);
            cb.setPreferredSize(new Dimension(200, 35));
            
            JButton btnOk = UIHelper.createSuccessButton("CONFIRM");
            final int[] selectedIdx = { -1 };

            btnOk.addActionListener(e -> {
                selectedIdx[0] = cb.getSelectedIndex();
                dialog.dispose();
            });

            dialog.add(new JLabel("Please select your table: "));
            dialog.add(cb);
            dialog.add(btnOk);
            dialog.setVisible(true);

            if (selectedIdx[0] != -1) {
                int tid = tables.get(selectedIdx[0]).getTableId();
                int oid = controller.createOrder(tid);
                if (oid > 0) {
                    for (var en : cart.entrySet())
                        controller.addItem(oid, en.getKey(), en.getValue());
                    
                    id.komorebi.util.Logger.log(null, "SELF_ORDER", "New Order #" + oid + " created via Kiosk");
                    UIHelper.showInfo("Order Placed Successfully!\nPlease wait at your table.");
                    cart.clear();
                    updateCartDisplay();
                }
            }
        } catch (Exception e) { UIHelper.showError(e.getMessage()); }
    }

    // 4. FIX LOGIN: Gunakan MainFrame
    private void openStaffLogin() {
        // Cek apakah AuthController sudah menangani navigasi MainFrame
        // Atau panggil langsung MainFrame jika LoginView sudah didaftarkan
        new AuthController().showLogin(); 
    }
}