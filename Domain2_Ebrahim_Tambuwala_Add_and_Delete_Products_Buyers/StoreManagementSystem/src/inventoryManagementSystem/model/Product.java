package inventoryManagementSystem.model;

public class Product {

    private int id;
    private String name;
    private String category;
    private double price;
    private int quantity;
    private String description;

    public Product(String name, String category, double price, int quantity, String description) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public String getDescription() { return description; }
}