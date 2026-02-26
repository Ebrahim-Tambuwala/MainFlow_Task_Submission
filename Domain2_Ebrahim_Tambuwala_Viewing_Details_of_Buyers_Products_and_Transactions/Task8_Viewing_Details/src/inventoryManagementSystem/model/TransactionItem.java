package inventoryManagementSystem.model;

public class TransactionItem {

    private int id;
    private int transactionId;
    private int productId;
    private int quantity;
    private double unitPrice;

    // Default Constructor
    public TransactionItem() {}

    // Constructor for New Item
    public TransactionItem(int productId, int quantity, double unitPrice) {
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    // Full Constructor
    public TransactionItem(int id, int transactionId, int productId, int quantity, double unitPrice) {
        this.id = id;
        this.transactionId = transactionId;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    // Getters
    public int getId() { return id; }
    public int getTransactionId() { return transactionId; }
    public int getProductId() { return productId; }
    public int getQuantity() { return quantity; }
    public double getUnitPrice() { return unitPrice; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setTransactionId(int transactionId) { this.transactionId = transactionId; }
    public void setProductId(int productId) { this.productId = productId; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }
}