package dao;

import modelo.Repartidor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RepartidorDAO {

    public boolean create(Repartidor r) {
        String sql = "INSERT INTO repartidores (nombre) VALUES (?)";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, r.getNombre());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Repartidor> readAll() {
        List<Repartidor> lista = new ArrayList<>();
        String sql = "SELECT * FROM repartidores";
        try (Connection conn = ConexionDB.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Repartidor(rs.getInt("id"), rs.getString("nombre")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean update(Repartidor r) {
        String sql = "UPDATE repartidores SET nombre = ? WHERE id = ?";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, r.getNombre());
            stmt.setInt(2, r.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM repartidores WHERE id = ?";
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