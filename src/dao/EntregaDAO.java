package dao;

import modelo.Entrega;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EntregaDAO {

    public boolean create(Entrega e) {
        String sql = "INSERT INTO entregas (id_pedido, id_repartidor, fecha, hora) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, e.getIdPedido());
            stmt.setInt(2, e.getIdRepartidor());
            stmt.setDate(3, e.getFecha());
            stmt.setTime(4, e.getHora());
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public List<Entrega> readAll() {
        List<Entrega> lista = new ArrayList<>();
        String sql = "SELECT * FROM entregas";
        try (Connection conn = ConexionDB.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Entrega(
                        rs.getInt("id"),
                        rs.getInt("id_pedido"),
                        rs.getInt("id_repartidor"),
                        rs.getDate("fecha"),
                        rs.getTime("hora")
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    public boolean update(Entrega e) {
        String sql = "UPDATE entregas SET id_pedido = ?, id_repartidor = ?, fecha = ?, hora = ? WHERE id = ?";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, e.getIdPedido());
            stmt.setInt(2, e.getIdRepartidor());
            stmt.setDate(3, e.getFecha());
            stmt.setTime(4, e.getHora());
            stmt.setInt(5, e.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM entregas WHERE id = ?";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}