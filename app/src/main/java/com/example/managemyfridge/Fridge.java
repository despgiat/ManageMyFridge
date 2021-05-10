package com.example.managemyfridge;

import java.io.Serializable;
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

    public void removeItem(int id)
    {
        getFridgeItems().remove(id);
    }

    public void openItem(int id)
    {
        getFridgeItems().get(id).setOpened(true);
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
