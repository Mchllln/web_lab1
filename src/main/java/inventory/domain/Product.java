package inventory.domain;

import java.util.Objects;

public class Product {

    private String sku;
    private String name;
    private double price;
    private int quantity;

    public Product() {}

    public Product(String sku, String name, double price, int quantity) {
        this.sku = sku;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public void reduceStock(int amount) {
        if (amount > quantity) {
            throw new IllegalStateException("Not enough stock");
        }
        quantity -= amount;
    }

    public void addStock(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        quantity += amount;
    }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product that = (Product) o;
        return Double.compare(that.price, price) == 0
                && quantity == that.quantity
                && Objects.equals(sku, that.sku)
                && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sku, name, price, quantity);
    }

    @Override
    public String toString() {
        return "Product{sku='" + sku + "', name='" + name
                + "', price=" + price + ", quantity=" + quantity + "}";
    }
}
