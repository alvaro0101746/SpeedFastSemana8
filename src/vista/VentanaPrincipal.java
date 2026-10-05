package vista;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {
        setTitle("SpeedFast - Sistema de Gestión");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

        JLabel lblTitulo = new JLabel("Sistema de Gestión SpeedFast", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblTitulo, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel(new GridLayout(3, 1, 10, 10));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JButton btnRepartidores = new JButton("Gestión de Repartidores");
        JButton btnPedidos = new JButton("Gestión de Pedidos");
        JButton btnEntregas = new JButton("Gestión de Entregas");

        panelBotones.add(btnRepartidores);
        panelBotones.add(btnPedidos);
        panelBotones.add(btnEntregas);

        add(panelBotones, BorderLayout.CENTER);

        btnRepartidores.addActionListener(e -> new VentanaRepartidores().setVisible(true));
        btnPedidos.addActionListener(e -> new VentanaPedidos().setVisible(true));
        btnEntregas.addActionListener(e -> new VentanaEntregas().setVisible(true));
    }
}