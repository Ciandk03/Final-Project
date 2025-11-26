package edu.uga.cs.finalproject.models;

import java.util.Date;

/**
 * Represents a classification for items.
 * User Story 5: Must include date/time of creation.
 * User Story 7: User can delete a category they created.
 */
public class Category {
    private String id;
    private String name;
    private String createdBy; // userId of creator
    private long createdAt; // timestamp

    public Category() {
        // Default constructor required for calls to
        // DataSnapshot.getValue(Category.class)
    }

    public Category(String id, String name, String createdBy) {
        this.id = id;
        this.name = name;
        this.createdBy = createdBy;
        this.createdAt = System.currentTimeMillis();
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
