package com.example.managemyfridge;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FridgeItem implements Serializable { //The fridge item class

    private String name;
    private String expiry;
    private boolean opened;
    private String dayOpened;
    //private ProductType

    //Insert shelf life after opening (if specified on the product)

    public FridgeItem()
    {

        setName("");
        setOpened(false);
        setExpiry("");
        setDayOpened("");
    }

    public boolean productExpired(String date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate localDate = LocalDate.parse(date, formatter);
        LocalDate expDate = LocalDate.parse(expiry, formatter);

        return localDate.compareTo(expDate) >= 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public String getDayOpened() {
        return dayOpened;
    }

    public void setDayOpened(String dayOpened) {
        this.dayOpened = dayOpened;
    }



}
