package inventoryManagementSystem.model;

public class Product {

    private int id;
    private String name;
    private String category;
    private double price;
    private int quantity;
    private String description;

    // Default Constructor (Professional practice)
    public Product() {
    }

    // Constructor for Adding New Product
    public Product(String name, String category, double price, int quantity, String description) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
    }

    // Full Constructor (Useful for Update & Advanced Use)
    public Product(int id, String name, String category, double price, int quantity, String description) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public String getDescription() { return description; }

    // Setters (Required for Update Feature)
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setCategory(String category) { this.category = category; }
    public void setPrice(double price) { this.price = price; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setDescription(String description) { this.description = description; }
}