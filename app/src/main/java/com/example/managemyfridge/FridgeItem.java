package com.example.managemyfridge;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FridgeItem { //The fridge item class

    private String name;
    private LocalDate expiry;
    private boolean opened;
    private LocalDate dayOpened;
    //private ProductType

    //Insert shelf life after opening (if specified on the product)


    public FridgeItem()
    {
        setName("");
        setOpened(false);
        setExpiry(null);
        setDayOpened(null);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getExpiry() {
        return expiry;
    }

    public void setExpiry(LocalDate expiry) {
        this.expiry = expiry;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public LocalDate getDayOpened() {
        return dayOpened;
    }

    public void setDayOpened(LocalDate dayOpened) {
        this.dayOpened = dayOpened;
    }
}
