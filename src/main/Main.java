package main;

import vista.VentanaPrincipal;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new vista.VentanaPrincipal().setVisible(true);
        });
    }
}