package inventory.service;

import inventory.domain.Warehouse;

import java.io.IOException;

public interface Storage {

    void saveToFile(Warehouse warehouse, String filename) throws IOException;

    Warehouse loadFromFile(String filename) throws IOException;
}
