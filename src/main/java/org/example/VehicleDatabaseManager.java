package org.example;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VehicleDatabaseManager {
    private static final String DB_URL = System.getenv("DB_URL");

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public void addVehicle(Vehicle vehicle) {
        String query = "INSERT INTO \"Vehicles\" (id, model, brand, year, price, type, extra, \"isRented\", \"startTime\") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, vehicle.getId());
            preparedStatement.setString(2, vehicle.getModel());
            preparedStatement.setString(3, vehicle.getBrand());
            preparedStatement.setInt(4, vehicle.getYear());
            preparedStatement.setInt(5, vehicle.getPrice());
            preparedStatement.setString(6, vehicle.getType());
            preparedStatement.setString(7, vehicle.getExtra());
            preparedStatement.setBoolean(8, vehicle.getIsRented());
            preparedStatement.setString(9, vehicle.getRentalTime());
            preparedStatement.executeUpdate();

            if(vehicle.getType().equals("motorcycle")){
                query = "UPDATE \"Vehicles\" SET category = ? WHERE id = ?";
                try (PreparedStatement updateStmt = connection.prepareStatement(query)) {
                    updateStmt.setString(1, ((Motorcycle) vehicle).getCategory()); 
                    updateStmt.setString(2, vehicle.getId());
                    updateStmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeVehicle(String vehicleId) {
        String query = "DELETE FROM \"Vehicles\" WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, vehicleId);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 0) {
                System.out.println("Vehicle is not found");
            } else {
                System.out.println("Vehicle " + vehicleId + " removed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateVehicleRentalStatus(String vehicleId, boolean isRented, String startTime) {
        String query = "UPDATE \"Vehicles\" SET \"isRented\" = ?, \"startTime\" = ? WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setBoolean(1, isRented);
            preparedStatement.setString(2, startTime);
            preparedStatement.setString(3, vehicleId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Vehicle getVehicleById(String vehicleId) throws IOException {
        String query = "SELECT * FROM \"Vehicles\" WHERE id = ?";
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
                        resultSet.getInt("price"), 
                        resultSet.getBoolean("\"isRented\""),
                        resultSet.getString("id"), 
                        resultSet.getString("extra")
                    );
                }else if(type.equalsIgnoreCase("motorcycle")){
                    vehicle = new Motorcycle(
                        resultSet.getString("brand"), 
                        resultSet.getString("model"), 
                        resultSet.getInt("year"),
                        resultSet.getInt("price"), 
                        resultSet.getString("category"),
                        resultSet.getBoolean("\"isRented\""),
                        resultSet.getString("id"), 
                        resultSet.getString("extra")
                    );
                }else{
                    vehicle = new UnknownVehicle(
                        resultSet.getString("brand"), 
                        resultSet.getString("model"), 
                        resultSet.getInt("year"),
                        resultSet.getInt("price"), 
                        type,
                        resultSet.getString("id"), 
                        resultSet.getString("extra")
                    );
                    vehicle.setIsRented(resultSet.getBoolean("\"isRented\""));
                }
                vehicle.setRentalTime(resultSet.getString("\"startTime\""));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vehicle;
    }
}
