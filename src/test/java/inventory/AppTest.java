package inventory;

import inventory.domain.Product;
import inventory.domain.Warehouse;
import inventory.service.InventoryService;
import inventory.service.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppTest {

    private Product product;
    private Warehouse warehouse;
    private Storage storageMock;
    private InventoryService inventoryService;

    @BeforeEach
    void setUp() {
        product = new Product("SKU-001", "Laptop", 999.99, 10);
        warehouse = new Warehouse("Test Warehouse", "Zone A",
                new java.util.ArrayList<>(List.of(product)));
        storageMock = mock(Storage.class);
        inventoryService = new InventoryService(storageMock);
    }

    // --- Product.reduceStock tests ---

    @Test
    @DisplayName("reduceStock decreases quantity correctly")
    void testReduceStock_decreasesQuantity() {
        product.reduceStock(3);
        assertEquals(7, product.getQuantity());
    }

    @Test
    @DisplayName("reduceStock throws IllegalStateException when stock is insufficient")
    void testReduceStock_insufficientStock_throwsException() {
        assertThrows(IllegalStateException.class, () -> product.reduceStock(11));
    }

    @Test
    @DisplayName("reduceStock with exact quantity brings stock to zero")
    void testReduceStock_exactQuantity_bringsToZero() {
        product.reduceStock(10);
        assertEquals(0, product.getQuantity());
    }

    // --- Product.addStock tests ---

    @Test
    @DisplayName("addStock increases quantity correctly")
    void testAddStock_increasesQuantity() {
        product.addStock(5);
        assertEquals(15, product.getQuantity());
    }

    @Test
    @DisplayName("addStock throws IllegalArgumentException for zero amount")
    void testAddStock_zeroAmount_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> product.addStock(0));
    }

    @Test
    @DisplayName("addStock throws IllegalArgumentException for negative amount")
    void testAddStock_negativeAmount_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> product.addStock(-5));
    }

    // --- Warehouse.calculateTotalInventoryValue tests ---

    @Test
    @DisplayName("calculateTotalInventoryValue returns correct total")
    void testCalculateTotalInventoryValue_returnsCorrectTotal() {
        double expected = 999.99 * 10;
        assertEquals(expected, warehouse.calculateTotalInventoryValue(), 0.001);
    }

    @Test
    @DisplayName("calculateTotalInventoryValue returns zero for empty warehouse")
    void testCalculateTotalInventoryValue_emptyWarehouse_returnsZero() {
        Warehouse empty = new Warehouse("Empty", "Zone B", new java.util.ArrayList<>());
        assertEquals(0.0, empty.calculateTotalInventoryValue(), 0.001);
    }

    // --- InventoryService + Mockito tests ---

    @Test
    @DisplayName("exportData calls saveToFile exactly once")
    void testExportData_callsSaveToFileOnce() throws IOException {
        inventoryService.exportData(warehouse, "output.json");
        verify(storageMock, times(1)).saveToFile(any(Warehouse.class), eq("output.json"));
    }

    @Test
    @DisplayName("exportData sorts products by quantity descending before saving")
    void testExportData_sortsByQuantityDescending() throws IOException {
        Product low    = new Product("A", "Low Stock Item",    5.0,  2);
        Product high   = new Product("B", "High Stock Item",   3.0, 50);
        Product medium = new Product("C", "Medium Stock Item", 8.0, 20);

        Warehouse w = new Warehouse("Sort Test", "Zone C",
                new java.util.ArrayList<>(Arrays.asList(low, high, medium)));

        ArgumentCaptor<Warehouse> captor = ArgumentCaptor.forClass(Warehouse.class);

        inventoryService.exportData(w, "sorted.json");

        verify(storageMock).saveToFile(captor.capture(), eq("sorted.json"));
        List<Product> saved = captor.getValue().getProducts();

        assertEquals("B", saved.get(0).getSku());
        assertEquals("C", saved.get(1).getSku());
        assertEquals("A", saved.get(2).getSku());
    }

    @Test
    @DisplayName("importData returns warehouse loaded from storage")
    void testImportData_returnsWarehouseFromStorage() throws IOException {
        when(storageMock.loadFromFile("input.json")).thenReturn(warehouse);

        Warehouse result = inventoryService.importData("input.json");

        assertEquals(warehouse, result);
        verify(storageMock, times(1)).loadFromFile("input.json");
    }
}
