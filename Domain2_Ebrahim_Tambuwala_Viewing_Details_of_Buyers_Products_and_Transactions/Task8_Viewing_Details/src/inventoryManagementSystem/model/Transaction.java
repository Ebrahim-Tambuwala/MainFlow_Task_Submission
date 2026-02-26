package inventoryManagementSystem.model;

import java.sql.Timestamp;

public class Transaction {

    private int transactionId;
    private int buyerId;
    private String paymentMethod;
    private Timestamp purchaseDate;

    // Default Constructor
    public Transaction() {}

    // Constructor for New Transaction
    public Transaction(int buyerId, String paymentMethod) {
        this.buyerId = buyerId;
        this.paymentMethod = paymentMethod;
    }

    // Full Constructor
    public Transaction(int transactionId, int buyerId, String paymentMethod, Timestamp purchaseDate) {
        this.transactionId = transactionId;
        this.buyerId = buyerId;
        this.paymentMethod = paymentMethod;
        this.purchaseDate = purchaseDate;
    }

    // Getters
    public int getTransactionId() { return transactionId; }
    public int getBuyerId() { return buyerId; }
    public String getPaymentMethod() { return paymentMethod; }
    public Timestamp getPurchaseDate() { return purchaseDate; }

    // Setters
    public void setTransactionId(int transactionId) { this.transactionId = transactionId; }
    public void setBuyerId(int buyerId) { this.buyerId = buyerId; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public void setPurchaseDate(Timestamp purchaseDate) { this.purchaseDate = purchaseDate; }
}