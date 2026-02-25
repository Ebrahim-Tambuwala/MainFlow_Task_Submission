package inventoryManagementSystem.model;

import java.sql.Timestamp;
import java.util.List;

public class Bill {

    private int transactionId;
    private String buyerName;
    private String buyerEmail;
    private String buyerPhone;
    private String buyerAddress;

    private String paymentMethod;
    private Timestamp purchaseDate;

    private List<BillItem> items;
    private double subtotal;

    public Bill(int transactionId,
                String buyerName,
                String buyerEmail,
                String buyerPhone,
                String buyerAddress,
                String paymentMethod,
                Timestamp purchaseDate,
                List<BillItem> items) {

        this.transactionId = transactionId;
        this.buyerName = buyerName;
        this.buyerEmail = buyerEmail;
        this.buyerPhone = buyerPhone;
        this.buyerAddress = buyerAddress;
        this.paymentMethod = paymentMethod;
        this.purchaseDate = purchaseDate;
        this.items = items;

        calculateSubtotal();
    }

    private void calculateSubtotal() {
        subtotal = 0;
        for (BillItem item : items) {
            subtotal += item.getTotalPrice();
        }
    }

    // Getters
    public int getTransactionId() { return transactionId; }
    public String getBuyerName() { return buyerName; }
    public String getBuyerEmail() { return buyerEmail; }
    public String getBuyerPhone() { return buyerPhone; }
    public String getBuyerAddress() { return buyerAddress; }
    public String getPaymentMethod() { return paymentMethod; }
    public Timestamp getPurchaseDate() { return purchaseDate; }
    public List<BillItem> getItems() { return items; }
    public double getSubtotal() { return subtotal; }
}