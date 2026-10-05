package vista;

import dao.RepartidorDAO;
import modelo.Repartidor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentanaRepartidores extends JFrame {
    private JTextField txtNombre;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private RepartidorDAO repartidorDAO = new RepartidorDAO();
    private int idSeleccionado = -1;

    public VentanaRepartidores() {
        setTitle("Gestión de Repartidores");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel panelForm = new JPanel(new FlowLayout());
        panelForm.add(new JLabel("Nombre:"));
        txtNombre = new JTextField(20);
        panelForm.add(txtNombre);

        JButton btnGuardar = new JButton("Guardar");
        JButton btnEditar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");

        panelForm.add(btnGuardar);
        panelForm.add(btnEditar);
        panelForm.add(btnEliminar);
        add(panelForm, BorderLayout.NORTH);

        String[] columnas = {"ID", "Nombre"};
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
                txtNombre.setText((String) modeloTabla.getValueAt(fila, 1));
            }
        });

        btnGuardar.addActionListener(e -> guardar());
        btnEditar.addActionListener(e -> actualizar());
        btnEliminar.addActionListener(e -> eliminar());

        cargarDatos();
    }

    private void cargarDatos() {
        modeloTabla.setRowCount(0);
        List<Repartidor> lista = repartidorDAO.readAll();
        for (Repartidor r : lista) {
            modeloTabla.addRow(new Object[]{r.getId(), r.getNombre()});
        }
        idSeleccionado = -1;
        txtNombre.setText("");
    }

    private void guardar() {
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (repartidorDAO.create(new Repartidor(nombre))) {
            JOptionPane.showMessageDialog(this, "Repartidor registrado con éxito.");
            cargarDatos();
        } else {
            JOptionPane.showMessageDialog(this, "Error al registrar repartidor.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizar() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un repartidor de la tabla.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (repartidorDAO.update(new Repartidor(idSeleccionado, nombre))) {
            JOptionPane.showMessageDialog(this, "Repartidor actualizado con éxito.");
            cargarDatos();
        } else {
            JOptionPane.showMessageDialog(this, "Error al actualizar repartidor.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminar() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un repartidor de la tabla.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar repartidor seleccionado?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (repartidorDAO.delete(idSeleccionado)) {
                JOptionPane.showMessageDialog(this, "Repartidor eliminado.");
                cargarDatos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar repartidor.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}