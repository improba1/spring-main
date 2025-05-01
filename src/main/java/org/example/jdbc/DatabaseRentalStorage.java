package org.example.jdbc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.example.Interfaces.IRentalStorage;

public class DatabaseRentalStorage implements IRentalStorage {

    private static final String DB_URL = System.getenv("DB_URL");

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    // public DatabaseRentalStorage(){
    //     load();
    // }

    public void addRental(String login, String vehicleId, String startTime) {
        String query = "INSERT INTO rental_history (login, vehicle_id, start_time) VALUES (?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
                stmt.setString(1, login);
                stmt.setString(2, vehicleId);
                stmt.setString(3, startTime);
                stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public List<String> getRentalHistory(String vehicleId) throws IOException {
        String query = "SELECT start_time FROM rental_history WHERE vehicle_id = ? ORDER BY login ASC";
        List<String> rentals = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, vehicleId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                rentals.add(rs.getString("start_time"));
            }

        } catch (SQLException e) {
            throw new IOException("Failed to load rental history", e);
        }

        return rentals;
    }

    public void deleteRentals(String vehicleId) {
        String query = "DELETE FROM rental_history WHERE vehicle_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, vehicleId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
