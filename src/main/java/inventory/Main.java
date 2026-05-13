package inventory;

import inventory.domain.Product;
import inventory.domain.Warehouse;
import inventory.service.InventoryService;
import inventory.service.JsonStorage;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static final String DEFAULT_FILE = "warehouse.json";
    private static final InventoryService service = new InventoryService(new JsonStorage());
    private static Warehouse warehouse = new Warehouse("Main Warehouse", "Building A", new ArrayList<>());

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("=== Inventory Management System ===");

        while (running) {
            printMenu();
            System.out.print("Your choice: ");
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1" -> addProduct(scanner);
                case "2" -> sellProduct(scanner);
                case "3" -> restockProduct(scanner);
                case "4" -> viewInventory();
                case "5" -> exportData();
                case "6" -> importData();
                case "0" -> {
                    System.out.println("Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }

        scanner.close();
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("--- Menu ---");
        System.out.println("1. Add new product to warehouse");
        System.out.println("2. Sell product");
        System.out.println("3. Restock product");
        System.out.println("4. View inventory and total value");
        System.out.println("5. Export to JSON");
        System.out.println("6. Import from JSON");
        System.out.println("0. Exit");
    }

    private static void addProduct(Scanner scanner) {
        System.out.print("SKU: ");
        String sku = scanner.nextLine().trim();

        System.out.print("Name: ");
        String name = scanner.nextLine().trim();

        double price = readDouble(scanner, "Price: ");
        if (price < 0) { System.out.println("Price cannot be negative."); return; }

        int quantity = readInt(scanner, "Quantity: ");
        if (quantity < 0) { System.out.println("Quantity cannot be negative."); return; }

        warehouse.getProducts().add(new Product(sku, name, price, quantity));
        System.out.println("Product '" + name + "' added successfully.");
    }

    private static void sellProduct(Scanner scanner) {
        System.out.print("SKU: ");
        String sku = scanner.nextLine().trim();

        warehouse.findBySku(sku).ifPresentOrElse(product -> {
            int amount = readInt(scanner, "Quantity to sell: ");
            try {
                product.reduceStock(amount);
                System.out.println("Sold " + amount + " unit(s) of '" + product.getName()
                        + "'. Remaining stock: " + product.getQuantity());
            } catch (IllegalStateException e) {
                System.out.println("Error: " + e.getMessage()
                        + " (available: " + product.getQuantity() + ")");
            }
        }, () -> System.out.println("Product with SKU '" + sku + "' not found."));
    }

    private static void restockProduct(Scanner scanner) {
        System.out.print("SKU: ");
        String sku = scanner.nextLine().trim();

        warehouse.findBySku(sku).ifPresentOrElse(product -> {
            int amount = readInt(scanner, "Quantity to add: ");
            try {
                product.addStock(amount);
                System.out.println("Restocked '" + product.getName()
                        + "'. New stock: " + product.getQuantity());
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }, () -> System.out.println("Product with SKU '" + sku + "' not found."));
    }

    private static void viewInventory() {
        if (warehouse.getProducts().isEmpty()) {
            System.out.println("Warehouse is empty. Add products first (option 1).");
            return;
        }

        System.out.println();
        System.out.printf("%-12s %-28s %10s %10s%n", "SKU", "Name", "Price", "Qty");
        System.out.println("-".repeat(64));

        for (Product p : warehouse.getProducts()) {
            System.out.printf("%-12s %-28s %10.2f %10d%n",
                    p.getSku(), p.getName(), p.getPrice(), p.getQuantity());
        }

        System.out.println("-".repeat(64));
        System.out.printf("Total inventory value: $%.2f%n",
                warehouse.calculateTotalInventoryValue());
    }

    private static void exportData() {
        if (warehouse.getProducts().isEmpty()) {
            System.out.println("No data available for export.");
            return;
        }
        try {
            service.exportData(warehouse, DEFAULT_FILE);
            System.out.println("Warehouse exported to: " + DEFAULT_FILE
                    + " (sorted by quantity descending)");
        } catch (Exception e) {
            System.out.println("Export failed: " + e.getMessage());
        }
    }

    private static void importData() {
        try {
            warehouse = service.importData(DEFAULT_FILE);
            System.out.println("Warehouse imported from: " + DEFAULT_FILE
                    + " (" + warehouse.getProducts().size() + " products)");
        } catch (Exception e) {
            System.out.println("Import failed: " + e.getMessage());
        }
    }

    private static int readInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a whole number.");
            }
        }
    }

    private static double readDouble(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a numeric value.");
            }
        }
    }
}
