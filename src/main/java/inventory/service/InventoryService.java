package inventory.service;

import inventory.domain.Product;
import inventory.domain.Warehouse;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class InventoryService {

    private final Storage storage;

    public InventoryService(Storage storage) {
        this.storage = storage;
    }

    public void exportData(Warehouse warehouse, String filename) throws IOException {
        List<Product> sorted = warehouse.getProducts().stream()
                .sorted(Comparator.comparingInt(Product::getQuantity).reversed())
                .collect(Collectors.toList());
        warehouse.setProducts(sorted);
        storage.saveToFile(warehouse, filename);
    }

    public Warehouse importData(String filename) throws IOException {
        return storage.loadFromFile(filename);
    }
}
