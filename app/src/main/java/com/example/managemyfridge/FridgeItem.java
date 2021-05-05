package com.example.managemyfridge;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class FridgeItem { //The fridge item class

    private String name;
    private LocalDate expiry;
    private boolean opened;
    private LocalDate dayOpened;

    public FridgeItem()
    {
        name = "";
        opened = false;

        LocalDate date = LocalDate.now(); //We retrieve the current system date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String text = date.format(formatter);

        expiry = LocalDate.parse(text, formatter);
        //DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

        name = "";
        dayOpened = null;
    }

    public FridgeItem(String name, LocalDate expiry, boolean opened, LocalDate dayOpened)
    {
        this.name = name;
        this.expiry = expiry;
        this.opened = opened;
        this.dayOpened = dayOpened;
    }

}
