package dao;

import modelo.Pedido;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    public boolean create(Pedido p) {
        String sql = "INSERT INTO pedidos (direccion, tipo, estado) VALUES (?, ?, ?)";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getDireccion());
            stmt.setString(2, p.getTipo());
            stmt.setString(3, p.getEstado());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Pedido> readAll() {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT * FROM pedidos";
        try (Connection conn = ConexionDB.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Pedido(
                        rs.getInt("id"),
                        rs.getString("direccion"),
                        rs.getString("tipo"),
                        rs.getString("estado")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean update(Pedido p) {
        String sql = "UPDATE pedidos SET direccion = ?, tipo = ?, estado = ? WHERE id = ?";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getDireccion());
            stmt.setString(2, p.getTipo());
            stmt.setString(3, p.getEstado());
            stmt.setInt(4, p.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM pedidos WHERE id = ?";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}