package org.example.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.example.Interfaces.IUserStorage;
import org.example.modules.User;
import org.example.modules.Vehicle;

public class UserDatabaseManager implements IUserStorage{
    private static final String DB_URL = System.getenv("DB_URL");
    private ArrayList<Vehicle> allVehicles;

    public UserDatabaseManager(ArrayList<Vehicle> allVehicles){
        this.allVehicles = allVehicles;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public void createTables() {
        String usersTable = "CREATE TABLE IF NOT EXISTS users (" +
                            "login TEXT PRIMARY KEY," +
                            "password TEXT NOT NULL," +
                            "role TEXT NOT NULL" +
                            ");";

        String rentalsTable = " CREATE TABLE IF NOT EXISTS user_rentals (" +
                            "login TEXT NOT NULL," +
                            "vehicle_id TEXT NOT NULL," +
                            "start_time TEXT)" +
                            "FOREIGN KEY (login) REFERENCES users(login)" +
                            ");";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(usersTable);
            stmt.execute(rentalsTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void add(String login, String password, String role) {
        String query = "INSERT INTO users (login, password, role) VALUES (?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, login);
            stmt.setString(2, password);
            stmt.setString(3, role);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(String login) {
        String deleteRentals = "DELETE FROM user_rentals WHERE login = ?";
        String deleteUser = "DELETE FROM users WHERE login = ?";

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement rentalsStmt = conn.prepareStatement(deleteRentals);
                 PreparedStatement userStmt = conn.prepareStatement(deleteUser)) {

                rentalsStmt.setString(1, login);
                rentalsStmt.executeUpdate();

                userStmt.setString(1, login);
                userStmt.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            } finally {
                conn.setAutoCommit(true);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<User> load() {
        ArrayList<User> users = new ArrayList<>();
        Map<String, User> userMap = new HashMap<>();
        Map<String, Vehicle> vehicleMap = new HashMap<>();

        for (Vehicle v : allVehicles) {
            vehicleMap.put(v.getId(), v);
        }

        String userQuery = "SELECT * FROM users";
        String rentalQuery = "SELECT login, vehicle_id, start_time FROM user_rentals";

        try (Connection conn = getConnection();
            Statement userStmt = conn.createStatement();
            Statement rentalStmt = conn.createStatement()) {

            // load users
            ResultSet userRs = userStmt.executeQuery(userQuery);
            while (userRs.next()) {
                String login = userRs.getString("login");
                String password = userRs.getString("password");
                String role = userRs.getString("role");

                User user = new User(role, login, password, new ArrayList<>());
                users.add(user);
                userMap.put(login, user);
            }

            // load rentals
            ResultSet rentalRs = rentalStmt.executeQuery(rentalQuery);
            while (rentalRs.next()) {
                String login = rentalRs.getString("login");
                String vehicleId = rentalRs.getString("vehicle_id");
                String rentTime = rentalRs.getString("start_time");

                User user = userMap.get(login);
                Vehicle vehicle = vehicleMap.get(vehicleId);

                if (user != null && vehicle != null) {
                    vehicle.setIsRented(true);
                    vehicle.setRentalTime(rentTime);
                    if (!user.getVehicles().contains(vehicle)) {
                        user.addVehicle(vehicle);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    return users;
        // ArrayList<User> users = new ArrayList<>();
    
        // String userQuery = "SELECT * FROM Users";
        // String rentalQuery = "SELECT login, rented_vehicle_id FROM User_Rentals";
    
        // try (Connection conn = getConnection();
        //         Statement userStmt = conn.createStatement();
        //         Statement rentalStmt = conn.createStatement()) {
    
        //     ResultSet userRs = userStmt.executeQuery(userQuery);
        //     Map<String, ArrayList<String>> rentalMap = new HashMap<>();
    
        //     // Считываем все аренды в память
        //     ResultSet rentalRs = rentalStmt.executeQuery(rentalQuery);
        //     while (rentalRs.next()) {
        //         String login = rentalRs.getString("login");
        //         String vehicleId = rentalRs.getString("rented_vehicle_id");
        //         String rentTime = rentalRs.getString("rented_vehicle_time");
    
        //         rentalMap.putIfAbsent(login, new ArrayList<>());
        //         rentalMap.get(login).add(vehicleId);
        //     }
    
        //     // Создаем пользователей
        //     while (userRs.next()) {
        //         String login = userRs.getString("login");
        //         String password = userRs.getString("password");
        //         String role = userRs.getString("role");
    
        //         ArrayList<Vehicle> rented = new ArrayList<>();
        //         if (rentalMap.containsKey(login)) {
        //             for (String vehicleId : rentalMap.get(login)) {
        //                 for (Vehicle v : allVehicles) {
        //                     if (v.getId().equals(vehicleId)) {
        //                         v.setIsRented(true);
        //                         rented.add(v);
        //                         break;
        //                     }
        //                 }
        //             }
        //         }
    
        //         users.add(new User(role, login, password, rented));
        //     }
    
        // } catch (SQLException e) {
        //     e.printStackTrace();
        // }
    
        // return users;
    }

    public void saveReturn(String vehicle_id){
        String deleteOld = "DELETE FROM user_rentals WHERE vehicle_id = ?";
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteOld)) {
                deleteStmt.setString(1, vehicle_id);
                deleteStmt.executeUpdate();
            }
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveRent(String login, String vehicle_id, String rentTime) {
        String insertNew = "INSERT INTO user_rentals (login, vehicle_id, start_time) VALUES (?, ?, ?)";

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement insertStmt = conn.prepareStatement(insertNew)) {
                insertStmt.setString(1, login);
                insertStmt.setString(2, vehicle_id);
                insertStmt.setString(3, rentTime);
                insertStmt.addBatch();
                insertStmt.executeBatch();
            }

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
