package vista;

import dao.EntregaDAO;
import dao.PedidoDAO;
import dao.RepartidorDAO;
import modelo.Entrega;
import modelo.Pedido;
import modelo.Repartidor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class VentanaEntregas extends JFrame {
    private JComboBox<Pedido> cbPedidos;
    private JComboBox<Repartidor> cbRepartidores;
    private JTextField txtFecha;
    private JTextField txtHora;
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    private EntregaDAO entregaDAO = new EntregaDAO();
    private PedidoDAO pedidoDAO = new PedidoDAO();
    private RepartidorDAO repartidorDAO = new RepartidorDAO();
    private int idSeleccionado = -1;

    public VentanaEntregas() {
        setTitle("Gestión de Entregas");
        setSize(650, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel panelForm = new JPanel(new GridLayout(4, 2, 5, 5));
        panelForm.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelForm.add(new JLabel("Pedido:"));
        cbPedidos = new JComboBox<>();
        panelForm.add(cbPedidos);

        panelForm.add(new JLabel("Repartidor:"));
        cbRepartidores = new JComboBox<>();
        panelForm.add(cbRepartidores);

        panelForm.add(new JLabel("Fecha (YYYY-MM-DD):"));
        txtFecha = new JTextField(LocalDate.now().toString());
        panelForm.add(txtFecha);

        panelForm.add(new JLabel("Hora (HH:MM:SS):"));
        txtHora = new JTextField(LocalTime.now().toString().substring(0, 8));
        panelForm.add(txtHora);

        JButton btnGuardar = new JButton("Guardar");
        JButton btnEditar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");

        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.add(btnGuardar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);

        JPanel panelNorte = new JPanel(new BorderLayout());
        panelNorte.add(panelForm, BorderLayout.CENTER);
        panelNorte.add(panelBotones, BorderLayout.SOUTH);
        add(panelNorte, BorderLayout.NORTH);

        String[] columnas = {"ID", "ID Pedido", "ID Repartidor", "Fecha", "Hora"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabla = new JTable(modeloTabla);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        tabla.getSelectionModel().addListSelectionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila >= 0) {
                idSeleccionado = (int) modeloTabla.getValueAt(fila, 0);
                int idPedido = (int) modeloTabla.getValueAt(fila, 1);
                int idRepartidor = (int) modeloTabla.getValueAt(fila, 2);
                txtFecha.setText(modeloTabla.getValueAt(fila, 3).toString());
                txtHora.setText(modeloTabla.getValueAt(fila, 4).toString());

                for (int i = 0; i < cbPedidos.getItemCount(); i++) {
                    if (cbPedidos.getItemAt(i).getId() == idPedido) {
                        cbPedidos.setSelectedIndex(i);
                        break;
                    }
                }

                for (int i = 0; i < cbRepartidores.getItemCount(); i++) {
                    if (cbRepartidores.getItemAt(i).getId() == idRepartidor) {
                        cbRepartidores.setSelectedIndex(i);
                        break;
                    }
                }
            }
        });

        btnGuardar.addActionListener(e -> guardar());
        btnEditar.addActionListener(e -> actualizar());
        btnEliminar.addActionListener(e -> eliminar());

        cargarCombos();
        cargarDatos();
    }

    private void cargarCombos() {
        cbPedidos.removeAllItems();
        List<Pedido> pedidos = pedidoDAO.readAll();
        for (Pedido p : pedidos) {
            cbPedidos.addItem(p);
        }

        cbRepartidores.removeAllItems();
        List<Repartidor> repartidores = repartidorDAO.readAll();
        for (Repartidor r : repartidores) {
            cbRepartidores.addItem(r);
        }
    }

    private void cargarDatos() {
        modeloTabla.setRowCount(0);
        List<Entrega> lista = entregaDAO.readAll();
        for (Entrega e : lista) {
            modeloTabla.addRow(new Object[]{e.getId(), e.getIdPedido(), e.getIdRepartidor(), e.getFecha(), e.getHora()});
        }
        idSeleccionado = -1;
        txtFecha.setText(LocalDate.now().toString());
        txtHora.setText(LocalTime.now().toString().substring(0, 8));
    }

    private void guardar() {
        Pedido pedido = (Pedido) cbPedidos.getSelectedItem();
        Repartidor repartidor = (Repartidor) cbRepartidores.getSelectedItem();

        if (pedido == null || repartidor == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un pedido y un repartidor.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Date fecha = Date.valueOf(txtFecha.getText().trim());
            Time hora = Time.valueOf(txtHora.getText().trim());

            if (entregaDAO.create(new Entrega(pedido.getId(), repartidor.getId(), fecha, hora))) {
                JOptionPane.showMessageDialog(this, "Entrega registrada con éxito.");
                cargarDatos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar entrega.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Formato de fecha u hora inválido. Use YYYY-MM-DD y HH:MM:SS", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizar() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una entrega de la tabla.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Pedido pedido = (Pedido) cbPedidos.getSelectedItem();
        Repartidor repartidor = (Repartidor) cbRepartidores.getSelectedItem();

        if (pedido == null || repartidor == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un pedido y un repartidor.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Date fecha = Date.valueOf(txtFecha.getText().trim());
            Time hora = Time.valueOf(txtHora.getText().trim());

            if (entregaDAO.update(new Entrega(idSeleccionado, pedido.getId(), repartidor.getId(), fecha, hora))) {
                JOptionPane.showMessageDialog(this, "Entrega actualizada con éxito.");
                cargarDatos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar entrega.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Formato de fecha u hora inválido. Use YYYY-MM-DD y HH:MM:SS", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminar() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una entrega de la tabla.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar entrega seleccionada?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (entregaDAO.delete(idSeleccionado)) {
                JOptionPane.showMessageDialog(this, "Entrega eliminada.");
                cargarDatos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar entrega.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}