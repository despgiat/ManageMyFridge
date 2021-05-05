package com.example.managemyfridge;

public class User {

    private String username;
    private String password;
    private FridgeItem[] fridgeItems;

    public User(String username, String password, FridgeItem[] fridgeItems)
    {
        this.username = username;
        this.password = password;
        this.fridgeItems = fridgeItems;
    }

}
