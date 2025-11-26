package edu.uga.cs.finalproject.models;

/**
 * Represents an item listed for sale or donation.
 * User Story 8: Name, date/time, category, posted by, price/free.
 */
public class Item {
    private String id;
    private String name; // "title" in previous version, "name" in requirements
    private String description;
    private double price; // Storing as double for simplicity, or could use cents
    private boolean isFree;
    private String categoryId;
    private String categoryName; // Denormalized for display
    private String sellerId; // "postedBy"
    private String sellerName;
    private long createdAt; // timestamp
    private String status; // "AVAILABLE", "PENDING", "SOLD"

    public Item() {
    }

    public Item(String id, String name, String description, double price, boolean isFree, String categoryId,
            String categoryName, String sellerId, String sellerName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.isFree = isFree;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.sellerId = sellerId;
        this.sellerName = sellerName;
        this.createdAt = System.currentTimeMillis();
        this.status = "AVAILABLE";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean free) {
        isFree = free;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
