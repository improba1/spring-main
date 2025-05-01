package org.example.Interfaces;

import java.io.IOException;
import java.util.List;

import org.example.modules.Rental;

public interface IRentalStorage {
    void addRental(String login, String vehicleId, String startTime);

    // Загружает весь архив аренд по конкретному транспорту
    List<String> getRentalHistory(String vehicleId) throws IOException;

    // Удаляет все записи об аренде по ID транспорта
    void deleteRentals(String vehicleId) ;

    // Загружает все архивы аренд для всех машин (если нужно массово)
    // Можно не реализовывать, если не требуется
    default List<Rental> loadAllRentals() throws IOException {
        throw new UnsupportedOperationException("Not implemented");
    }
}
