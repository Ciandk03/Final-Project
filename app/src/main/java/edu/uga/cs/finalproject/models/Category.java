package edu.uga.cs.finalproject.models;

/*
 * This file defines the data model for a 'Category' in the TradeIt application.
 * A Category object represents a classification for items, such as "Transportation",
 * "Household", or "Clothing".
 *
 * This class is a simple POJO (Plain Old Java Object) that will be used to serialize and
 * deserialize data from Google's Firebase Realtime Database. It contains fields for the
 * category's unique ID and its name.
 *
 * TODO:
 * - The project requirements state that categories should be ordered alphabetically.
 *   This ordering logic will need to be implemented in the UI layer (e.g., when querying
 *   and displaying the categories), not within this model class.
 */
public class Category {
    public String id;
    public String name;
    public Category() {}
}
