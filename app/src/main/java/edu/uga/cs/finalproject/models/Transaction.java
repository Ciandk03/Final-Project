package edu.uga.cs.finalproject.models;

/**
 * Represents a transaction between a buyer and a seller.
 */
public class Transaction {
    private String id;
    private String itemId;
    private String itemTitle;
    private String categoryName; // Added field
    private double itemPrice;
    private String sellerId;
    private String sellerName;
    private String buyerId;
    private String buyerName;
    private boolean sellerConfirmed;
    private boolean buyerConfirmed;
    private long initiatedAt;
    private long completedAt;
    private String status; // "PENDING", "COMPLETED"

    public Transaction() {
    }

    public Transaction(String id, String itemId, String itemTitle, String categoryName, double itemPrice,
            String sellerId, String sellerName, String buyerId, String buyerName) {
        this.id = id;
        this.itemId = itemId;
        this.itemTitle = itemTitle;
        this.categoryName = categoryName;
        this.itemPrice = itemPrice;
        this.sellerId = sellerId;
        this.sellerName = sellerName;
        this.buyerId = buyerId;
        this.buyerName = buyerName;
        this.initiatedAt = System.currentTimeMillis();
        this.status = "PENDING";
        this.sellerConfirmed = false;
        this.buyerConfirmed = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public boolean isSellerConfirmed() {
        return sellerConfirmed;
    }

    public void setSellerConfirmed(boolean sellerConfirmed) {
        this.sellerConfirmed = sellerConfirmed;
    }

    public boolean isBuyerConfirmed() {
        return buyerConfirmed;
    }

    public void setBuyerConfirmed(boolean buyerConfirmed) {
        this.buyerConfirmed = buyerConfirmed;
    }

    public long getInitiatedAt() {
        return initiatedAt;
    }

    public void setInitiatedAt(long initiatedAt) {
        this.initiatedAt = initiatedAt;
    }

    public long getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(long completedAt) {
        this.completedAt = completedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
