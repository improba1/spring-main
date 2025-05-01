package org.example.jdbc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.example.Interfaces.IVehicleStorage;
import org.example.modules.Car;
import org.example.modules.Motorcycle;
import org.example.modules.UnknownVehicle;
import org.example.modules.Vehicle;

public class VehicleDatabaseManager implements IVehicleStorage{
    private static final String DB_URL = System.getenv("DB_URL");

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public void addVehicle(Vehicle vehicle) {
        String query = "INSERT INTO vehicles (id, model, brand, year, price, type, extra, is_rented, category) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, vehicle.getId());
            preparedStatement.setString(2, vehicle.getModel());
            preparedStatement.setString(3, vehicle.getBrand());
            preparedStatement.setInt(4, vehicle.getYear());
            preparedStatement.setDouble(5, vehicle.getPrice());
            preparedStatement.setString(6, vehicle.getType());
            preparedStatement.setString(7, vehicle.getExtra());
            preparedStatement.setBoolean(8, vehicle.getIsRented());
            preparedStatement.setString(9, vehicle.getCategory());
            preparedStatement.executeUpdate();

            // if(vehicle.getType().equals("motorcycle")){
            //     query = "UPDATE vehicles SET category = ? WHERE id = ?";
            //     try (PreparedStatement updateStmt = connection.prepareStatement(query)) {
            //         updateStmt.setString(1, ((Motorcycle) vehicle).getCategory()); 
            //         updateStmt.setString(2, vehicle.getId());
            //         updateStmt.executeUpdate();
            //     }
            // }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeVehicle(String vehicleId) {
        String query = "DELETE FROM vehicles WHERE id = ?";
        String deleteHistoryQuery = "DELETE FROM rental_history WHERE vehicle_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             PreparedStatement deleteHistoryStmt = connection.prepareStatement(deleteHistoryQuery)) {

            preparedStatement.setString(1, vehicleId);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 0) {
                System.out.println("Vehicle is not found");
            } else {
                System.out.println("Vehicle " + vehicleId + " removed");
                deleteHistoryStmt.setString(1, vehicleId);
                deleteHistoryStmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // public void updateVehicleRentalStatus(String vehicleId, boolean isRented) {
    //     String query = "UPDATE vehicles SET IS_RENTED = ? WHERE id = ?";

    //     try (Connection connection = getConnection();
    //          PreparedStatement preparedStatement = connection.prepareStatement(query)) {

    //         preparedStatement.setBoolean(1, isRented);
    //         preparedStatement.setString(2, vehicleId);

    //         preparedStatement.executeUpdate();
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }
    // }

    public Vehicle getVehicleById(String vehicleId) throws IOException {
        String query = "SELECT * FROM vehicles WHERE id = ?";
        Vehicle vehicle = null;

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, vehicleId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String type = resultSet.getString("type");
                if(type.equalsIgnoreCase("car")){
                    vehicle = new Car(
                        resultSet.getString("brand"), 
                        resultSet.getString("model"), 
                        resultSet.getInt("year"),
                        resultSet.getDouble("price"), 
                        "car",
                        resultSet.getString("id"), 
                        resultSet.getString("extra"),
                        resultSet.getString("category"),
                        resultSet.getBoolean("is_rented")
                    );
                }else if(type.equalsIgnoreCase("motorcycle")){
                    vehicle = new Motorcycle(
                        resultSet.getString("brand"), 
                        resultSet.getString("model"), 
                        resultSet.getInt("year"),
                        resultSet.getDouble("price"), 
                        "motorcycle",
                        resultSet.getString("id"), 
                        resultSet.getString("extra"),
                        resultSet.getString("category"),
                        resultSet.getBoolean("is_rented")
                    );
                }else{
                    vehicle = new UnknownVehicle(
                        resultSet.getString("brand"), 
                        resultSet.getString("model"), 
                        resultSet.getInt("year"),
                        resultSet.getDouble("price"), 
                        resultSet.getString("type"),
                        resultSet.getString("id"), 
                        resultSet.getString("extra"),
                        resultSet.getString("category"),
                        resultSet.getBoolean("is_rented")
                    );
                }
                //vehicle.setRentalTime(resultSet.getString("start_time"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vehicle;
    }

    public ArrayList<Vehicle> load() {
        ArrayList<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT id, model, brand, year, price, type, extra, is_rented, category FROM vehicles";

        try (Connection conn = getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String id = rs.getString("id");
                String model = rs.getString("model");
                String brand = rs.getString("brand");
                int year = rs.getInt("year");
                double price = rs.getDouble("price");
                String type = rs.getString("type");
                String extra = rs.getString("extra");
                boolean isRented = rs.getBoolean("is_rented");
                String category = rs.getString("category");

                Vehicle vehicle;
                switch (type) {
                    case "car":
                        vehicle = new Car(brand, model, year, price, type, id, extra, category, isRented);
                        break;
                    case "motorcycle":
                        vehicle = new Motorcycle(brand, model, year, price, type, id, extra, category, isRented);
                        break;
                    default:
                        vehicle = new UnknownVehicle(brand, model, year, price, type, id, extra, category, isRented);
                        break;
                }
                vehicles.add(vehicle);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vehicles;
    }

    public void updateRentalStatus(String vehicleId, boolean isRented, String time) {
        String sql = "UPDATE vehicles SET is_rented = ? WHERE id = ?";
    
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
    
            pstmt.setBoolean(1, isRented);
            pstmt.setString(2, vehicleId);
            pstmt.executeUpdate();
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
