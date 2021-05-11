package com.example.managemyfridge;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Fridge implements Serializable {

    private ArrayList<FridgeItem> fridgeItems;
    private ArrayList<FridgeItem> expiredItems;

    public Fridge()
    {
        setFridgeItems(new ArrayList<>());
        setExpiredItems(new ArrayList<>());
    }

    public Fridge(ArrayList<FridgeItem> fridgeItems, ArrayList<FridgeItem> expiredItems )
    {
        this.setFridgeItems(fridgeItems);
        this.setExpiredItems(expiredItems);
    }

    public void addItem(FridgeItem item)
    {
        getFridgeItems().add(item);
    }

    public void addItems(ArrayList<FridgeItem> products)
    {
        fridgeItems.addAll(products);
    }

    public void removeItem(int id)
    {
        getFridgeItems().remove(id);
    }

    public void openItem(int id)
    {
        getFridgeItems().get(id).setOpened(true);
    }

    public ArrayList<FridgeItem> checkExpiredAtDate(String date)
    {
        ArrayList<FridgeItem> products = new ArrayList<>();

        for (FridgeItem product: fridgeItems)
        {
            if(product.getExpiry().equals(date))
            {
                products.add(product);
            }
        }
        return  products;
    }

    public void AddExpired(FridgeItem product)
    {
        expiredItems.add(product);
    }

    public void AddExpired(ArrayList<FridgeItem> products)
    {
        expiredItems.addAll(products);
    }

    public ArrayList<FridgeItem> getFridgeItems() {
        return fridgeItems;
    }

    public void setFridgeItems(ArrayList<FridgeItem> fridgeItems) {
        this.fridgeItems = fridgeItems;
    }

    public ArrayList<FridgeItem> getExpiredItems() {
        return expiredItems;
    }

    public void setExpiredItems(ArrayList<FridgeItem> expiredItems) {
        this.expiredItems = expiredItems;
    }
}
