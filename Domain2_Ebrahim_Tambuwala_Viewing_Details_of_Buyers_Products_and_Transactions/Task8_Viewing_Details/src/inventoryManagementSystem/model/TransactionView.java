package inventoryManagementSystem.model;

import java.sql.Timestamp;

public class TransactionView {

    private int transactionId;
    private String buyerName;
    private Timestamp purchaseDate;
    private String paymentMethod;
    private String orderStatus;
    private double totalAmount;

    public TransactionView(int transactionId,
                           String buyerName,
                           Timestamp purchaseDate,
                           String paymentMethod,
                           String orderStatus,
                           double totalAmount) {

        this.transactionId = transactionId;
        this.buyerName = buyerName;
        this.purchaseDate = purchaseDate;
        this.paymentMethod = paymentMethod;
        this.orderStatus = orderStatus;
        this.totalAmount = totalAmount;
    }

    public int getTransactionId() { return transactionId; }
    public String getBuyerName() { return buyerName; }
    public Timestamp getPurchaseDate() { return purchaseDate; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getOrderStatus() { return orderStatus; }
    public double getTotalAmount() { return totalAmount; }
}