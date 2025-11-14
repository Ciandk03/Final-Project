package edu.uga.cs.finalproject.models;

import java.util.Date;

/*
 * This file defines the data model for a 'User' in the TradeIt application.
 * A User object represents a registered user of the app.
 *
 * This class is a POJO (Plain Old Java Object) for serializing and deserializing data
 * from Google's Firebase Realtime Database. It includes basic user information such as
 * their unique ID (uid), display name, email, and phone number.
 *
 * TODO:
 * - The project requires user registration, login, logout, and password changing.
 *   This class will be used in conjunction with Firebase Authentication to manage
 *   user sessions and retrieve user information.
 */
public class User {
    public String uid;
    public String displayName;
    public String email;
    public String phone;
    public Date createdAt;
    public User() {}
}
