package id.komorebi.util;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ImageUtil {

    // Folder tempat menyimpan gambar di dalam proyek
    private static final String UPLOAD_DIR = "user_images";

    public static String selectAndSaveImage(Component parent) {
        File folder = new File(UPLOAD_DIR);
        if (!folder.exists()) {
            folder.mkdir();
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select Menu Image");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        // Filter hanya file gambar
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Images (JPG, PNG)", "jpg", "jpeg", "png");
        chooser.setFileFilter(filter);

        int result = chooser.showOpenDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            
            String ext = getFileExtension(selectedFile);
            String newName = "img_" + System.currentTimeMillis() + "." + ext;
            File destFile = new File(folder, newName);

            try {
                Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                
                return destFile.getPath(); 
            } catch (IOException e) {
                UIHelper.showError("Gagal upload gambar: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return null;
    }

    private static String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return "";
        }
        return name.substring(lastIndexOf + 1);
    }

    public static ImageIcon loadResizedIcon(String path, int width, int height) {
        if (path == null || path.isEmpty()) return null;
        try {
            ImageIcon icon = new ImageIcon(path);
            Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } catch (Exception e) {
            return null;
        }
    }
}