package com.example.managemyfridge;

import java.util.ArrayList;

public class Fridge {

    private ArrayList<FridgeItem> fridgeItems;
    private ArrayList<FridgeItem> expiredItems;

    public Fridge()
    {
        fridgeItems = null;
        expiredItems = null;
    }

    public Fridge(ArrayList<FridgeItem> fridgeItems, ArrayList<FridgeItem> expiredItems )
    {
        this.fridgeItems = fridgeItems;
        this.expiredItems = expiredItems;
    }

    public void addItem(FridgeItem item)
    {
        fridgeItems.add(item);
    }

    public void removeItem(int id)
    {
        fridgeItems.remove(id);
    }

    public void openItem(int id)
    {
        fridgeItems.get(id).setOpened(true);
    }


}
