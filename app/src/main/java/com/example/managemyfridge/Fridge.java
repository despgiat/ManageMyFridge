package com.example.managemyfridge;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Fridge implements Serializable {

    private ArrayList<Product> fridgeItems;
    //private ArrayList<FridgeItem> expiredItems;

    public Fridge()
    {
        setFridgeItems(new ArrayList<>());
        //setExpiredItems(new ArrayList<>());
    }

    public Fridge(ArrayList<Product> fridgeItems)
    {
        this.setFridgeItems(fridgeItems);
        //this.setExpiredItems(expiredItems);
    }

    public void addItem(Product item)
    {
        getFridgeItems().add(item);
    }

    public void addItems(ArrayList<Product> products)
    {
        fridgeItems.addAll(products);
    }

    public void removeItem(int id)
    {
        getFridgeItems().remove(id);
    }

    public void openItem(int id)
    {
        //getFridgeItems().get(id).setOpened(true);
        //getExpiredItems().get(getFridgeItems().get(id)).setOpened(true);
    }

    public ArrayList<Product> checkForExpiredAtDate(String date)
    {
        ArrayList<Product> products = new ArrayList<>();

        for (Product product: fridgeItems)
        {
            if(product.productExpired(date)) //Not just the .equals, it is deemed at expired if it is EQUAL OR LESS
            {
                products.add(product);
            }
        }
        return products;
    }

    public ArrayList<Product> expiresAtDate(String date)
    {
        ArrayList<Product> products = new ArrayList<>();

        for (Product product: fridgeItems)
        {
            if(product.get_exdate().equals(date)) //Not just the .equals, it is deemed at expired if it is EQUAL OR LESS
            {
                products.add(product);
            }
        }
        return products;
    }

   // public void AddExpired(FridgeItem product)
    //{
    //    expiredItems.add(product);
   // }

    //public void AddExpired(ArrayList<FridgeItem> products)
    //{
     //   expiredItems.addAll(products);
   // }

    public ArrayList<Product> getFridgeItems() {
        return fridgeItems;
    }

    public void setFridgeItems(ArrayList<Product> fridgeItems) {
        this.fridgeItems = fridgeItems;
    }

    //public ArrayList<FridgeItem> getExpiredItems() {
        //return expiredItems;
    //}

    //public void setExpiredItems(ArrayList<FridgeItem> expiredItems) {
        //this.expiredItems = expiredItems;
    //}

}
