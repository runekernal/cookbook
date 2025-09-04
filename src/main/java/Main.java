
import com.formdev.flatlaf.FlatDarculaLaf;
import ui.MainFrame;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Run the GUI on the Event Dispatch Thread (EDT)
        try {
            FlatDarculaLaf.setup();
        } catch (Exception e) {
            System.err.println("Failed ot initialize LaF");
        }
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}