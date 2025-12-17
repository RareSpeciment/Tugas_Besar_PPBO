package id.komorebi.App;

import id.komorebi.controller.OrderController;
import id.komorebi.service.OrderService;
import id.komorebi.util.DBConnection;
import id.komorebi.util.UIHelper;
import id.komorebi.view.MainFrame;
import id.komorebi.view.MainMenuView;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                if (DBConnection.getConnection() == null) {
                    UIHelper.showError("Database Connection Failed!");
                    return;
                }
                UIHelper.setupCommonStyles();
                MainFrame mainFrame = MainFrame.getInstance();

                OrderController controller = new OrderController(new OrderService());

                mainFrame.addView("MainMenu", new MainMenuView(controller));
                mainFrame.showView("MainMenu");

                mainFrame.setVisible(true);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}