package com.example.managemyfridge;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Contains all of the user's fridge products.
 */

public class Fridge implements Serializable {

    private ArrayList<Product> fridgeItems;

    public Fridge()
    {
        setFridgeItems(new ArrayList<>());
    }

    public Fridge(ArrayList<Product> fridgeItems)
    {
        this.setFridgeItems(fridgeItems);
    }

    /**
     * Checks for expired products up until the given date and returns them.
     * @param date is the date we want to check for expired products up until.
     * @return returns a Product ArrayList of the expired products up until the given date.
     */
    public ArrayList<Product> checkForExpiredAtDate(String date)
    {
        ArrayList<Product> products = new ArrayList<>();

        for (Product product: fridgeItems)
        {
            if(product.productExpired(date)) //productExpired is implemented at the Product class
            {
                products.add(product);
            }
        }
        return products;
    }

    /**
     * Checks which of the fridge's products expire AT the given date.
     * @param date is the date we want to check for expired products
     * @return ArrayList<Products> of the products that expire at date.
     */

    public ArrayList<Product> expiresAtDate(String date)
    {
        ArrayList<Product> products = new ArrayList<>();

        for (Product product: fridgeItems)
        {
            if(product.get_exdate().equals(date))
            {
                products.add(product);
            }
        }
        return products;
    }

    public ArrayList<Product> getFridgeItems() {
        return fridgeItems;
    }

    public void setFridgeItems(ArrayList<Product> fridgeItems) {
        this.fridgeItems = fridgeItems;
    }

}
