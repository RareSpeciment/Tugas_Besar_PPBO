package id.komorebi.util;

import javax.swing.*;
import java.awt.*;

public class ReceiptPrinter {
    public static void showReceiptText(Component parent, String text) {
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(parent), "Receipt", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(350, 500);
        dialog.setLocationRelativeTo(parent);
        
        JTextArea area = new JTextArea(text);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12)); // Font Struk
        area.setEditable(false);
        area.setMargin(new Insets(15, 15, 15, 15));
        
        dialog.add(new JScrollPane(area));
        
        JButton btnClose = new JButton("Close");
        btnClose.addActionListener(e -> dialog.dispose());
        dialog.add(btnClose, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
}