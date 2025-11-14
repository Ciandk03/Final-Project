package edu.uga.cs.finalproject.models;

import java.util.Date;
import java.util.List;

/*
 * Defines the data model for an 'Item' in the TradeIt application. An Item object is a POJO
 * (Plain Old Java Object) that represents a product a user has listed for sale or to be given away.
 * This class is used to serialize and deserialize data from Google's Firebase Realtime Database.
 *
 * It includes fields for the item's details (title, description, price) and metadata (who posted
 * it, posting date, current status).
 *
 * TODO:
 * - The project requires items to be sorted by posting date (newest to oldest). The `createdAt`
 *   field is included for this purpose. The actual sorting logic will be implemented in the Firebase
 *   query within the UI layer (e.g., in ItemListFragment), not in this model class.
 * - When a user agrees to purchase an item, its status should be updated to "pending". This logic
 *   will be handled in the UI layer (e.g., in ItemDetailFragment) by updating this object in Firebase.
 * - For Firebase to automatically map this POJO, ensure the field names here match the keys in your
 *   Realtime Database. If they differ, use annotations like @PropertyName.
 */
public class Item {
    public String id;
    public String title;
    public String description;
    public long priceCents;
    public boolean isFree;
    public String categoryId;
    public String postedBy;
    public String postedByName;
    public List<String> images; // urls
    public String status; // active, pending, completed
    public String transactionId;
    public Date createdAt;
    public Item() {}
}
