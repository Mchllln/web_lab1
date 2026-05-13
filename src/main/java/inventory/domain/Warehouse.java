package inventory.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Warehouse {

    private String name;
    private String location;
    private List<Product> products;

    public Warehouse() {
        this.products = new ArrayList<>();
    }

    public Warehouse(String name, String location, List<Product> products) {
        this.name = name;
        this.location = location;
        this.products = products != null ? products : new ArrayList<>();
    }

    public double calculateTotalInventoryValue() {
        return products.stream()
                .mapToDouble(p -> p.getPrice() * p.getQuantity())
                .sum();
    }

    public Optional<Product> findBySku(String sku) {
        return products.stream()
                .filter(p -> p.getSku().equalsIgnoreCase(sku))
                .findFirst();
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> products) { this.products = products; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Warehouse)) return false;
        Warehouse that = (Warehouse) o;
        return Objects.equals(name, that.name)
                && Objects.equals(location, that.location)
                && Objects.equals(products, that.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, location, products);
    }

    @Override
    public String toString() {
        return "Warehouse{name='" + name + "', location='" + location
                + "', products=" + products.size() + "}";
    }
}
