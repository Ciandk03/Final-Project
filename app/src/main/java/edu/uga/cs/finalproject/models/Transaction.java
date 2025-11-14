package edu.uga.cs.finalproject.models;

import java.util.Date;

/*
 * This file defines the data model for a 'Transaction' in the TradeIt application.
 * A Transaction object represents the agreement between a seller and a buyer for an item.
 * It tracks the status of the transaction from initiation to completion.
 *
 * This class is a POJO (Plain Old Java Object) for serializing and deserializing data
 * from Google's Firebase Realtime Database. It includes details about the item, seller,
 * and buyer, as well as the status of the transaction (pending or completed) and
 * confirmation from both parties.
 *
 * TODO:
 * - The project requires that once a transaction is agreed upon, the item is removed
 *   from its category and added to the buyer's and seller's pending lists. The logic
 *   for this will be handled in the UI/ViewModel layer, not in this model.
 * - The logic for both users to confirm the transaction and update the status to
 *   "completed" will also be handled in the UI/ViewModel layer.
 */
public class Transaction {
    public String id;
    public String itemId;
    public String itemTitle;
    public String sellerId;
    public String sellerName;
    public String buyerId;
    public String buyerName;
    public boolean sellerConfirmed;
    public boolean buyerConfirmed;
    public Date initiatedAt;
    public Date completedAt;
    public String status;
    public Transaction() {}
}
