package id.komorebi.view;

import id.komorebi.util.UIHelper;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MainFrame extends JFrame {
    private static MainFrame instance;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private Map<String, JPanel> views = new HashMap<>();

    private MainFrame() {
        UIHelper.applyFrameSettings(this, "Komorebi Café System", 1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        add(mainPanel);
    }

    public static MainFrame getInstance() {
        if (instance == null) instance = new MainFrame();
        return instance;
    }

    public void addView(String name, JPanel viewPanel) { 
        views.put(name, viewPanel);
        mainPanel.add(viewPanel, name);
    }

    public void showView(String name) {
        if (views.containsKey(name)) {
            cardLayout.show(mainPanel, name);
        } else {
            System.err.println("View not found: " + name);
        }
    }
}