package vista;

import dao.PedidoDAO;
import modelo.EstadoPedido;
import modelo.Pedido;
import modelo.TipoPedido;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentanaPedidos extends JFrame {
    private JTextField txtDireccion;
    private JComboBox<TipoPedido> cbTipo;
    private JComboBox<EstadoPedido> cbEstado;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private PedidoDAO pedidoDAO = new PedidoDAO();
    private int idSeleccionado = -1;

    public VentanaPedidos() {
        setTitle("Gestión de Pedidos");
        setSize(600, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel panelForm = new JPanel(new GridLayout(3, 2, 5, 5));
        panelForm.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelForm.add(new JLabel("Dirección:"));
        txtDireccion = new JTextField();
        panelForm.add(txtDireccion);

        panelForm.add(new JLabel("Tipo:"));
        cbTipo = new JComboBox<>(TipoPedido.values());
        panelForm.add(cbTipo);

        panelForm.add(new JLabel("Estado:"));
        cbEstado = new JComboBox<>(EstadoPedido.values());
        panelForm.add(cbEstado);

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

        String[] columnas = {"ID", "Dirección", "Tipo", "Estado"};
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
                txtDireccion.setText((String) modeloTabla.getValueAt(fila, 1));
                cbTipo.setSelectedItem(TipoPedido.valueOf((String) modeloTabla.getValueAt(fila, 2)));
                cbEstado.setSelectedItem(EstadoPedido.valueOf((String) modeloTabla.getValueAt(fila, 3)));
            }
        });

        btnGuardar.addActionListener(e -> guardar());
        btnEditar.addActionListener(e -> actualizar());
        btnEliminar.addActionListener(e -> eliminar());

        cargarDatos();
    }

    private void cargarDatos() {
        modeloTabla.setRowCount(0);
        List<Pedido> lista = pedidoDAO.readAll();
        for (Pedido p : lista) {
            modeloTabla.addRow(new Object[]{p.getId(), p.getDireccion(), p.getTipo(), p.getEstado()});
        }
        idSeleccionado = -1;
        txtDireccion.setText("");
        if (cbTipo.getItemCount() > 0) cbTipo.setSelectedIndex(0);
        if (cbEstado.getItemCount() > 0) cbEstado.setSelectedIndex(0);
    }

    private void guardar() {
        String direccion = txtDireccion.getText().trim();
        if (direccion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La dirección es obligatoria.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        TipoPedido tipo = (TipoPedido) cbTipo.getSelectedItem();
        EstadoPedido estado = (EstadoPedido) cbEstado.getSelectedItem();

        if (pedidoDAO.create(new Pedido(direccion, tipo.name(), estado.name()))) {
            JOptionPane.showMessageDialog(this, "Pedido registrado con éxito.");
            cargarDatos();
        } else {
            JOptionPane.showMessageDialog(this, "Error al registrar pedido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizar() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un pedido de la tabla.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String direccion = txtDireccion.getText().trim();
        if (direccion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La dirección es obligatoria.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        TipoPedido tipo = (TipoPedido) cbTipo.getSelectedItem();
        EstadoPedido estado = (EstadoPedido) cbEstado.getSelectedItem();

        if (pedidoDAO.update(new Pedido(idSeleccionado, direccion, tipo.name(), estado.name()))) {
            JOptionPane.showMessageDialog(this, "Pedido actualizado con éxito.");
            cargarDatos();
        } else {
            JOptionPane.showMessageDialog(this, "Error al actualizar pedido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminar() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un pedido de la tabla.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar pedido seleccionado?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (pedidoDAO.delete(idSeleccionado)) {
                JOptionPane.showMessageDialog(this, "Pedido eliminado.");
                cargarDatos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar pedido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}