package com.example.managemyfridge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MainScreen extends AppCompatActivity {

    private Fridge fridge;
    LocalDate currentDate;
    DateTimeFormatter formatter;
    ArrayList<FridgeItem> expiredProducts;
    //Arraylist<FridgeItem> expireTomorrow;
    //Arratlist<FridgeItem> expireSoon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        //When the activity is created it will search for the expired items in the fridge and make them appear in the recyclerview on the front screen
        //First get the currect date

        formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        currentDate = LocalDate.now();
        //tomorrow
        //three days later

        String dateText = currentDate.format(formatter);

        fridge = new Fridge(); //Placeholder -> It will derive the fridge's insides from the database
        FridgeItem product1 = new FridgeItem();
        FridgeItem product2 = new FridgeItem();
        FridgeItem product3 = new FridgeItem();
        FridgeItem product4 = new FridgeItem();

        product1.setName("Milk"); //Placeholder for testing
        product1.setExpiry("11/05/2021");
        product1.setOpened(false);
        product2.setName("Eggs"); //Placeholder for testing
        product2.setExpiry("11/05/2021");
        product2.setOpened(false);
        product3.setName("Salami"); //Placeholder for testing
        product3.setExpiry("11/05/2021");
        product3.setOpened(false);
        product4.setName("Sweet Yoghurt"); //Placeholder for testing
        product4.setExpiry("11/05/2021");
        product4.setOpened(false);

        fridge.addItem(product1);
        fridge.addItem(product2);
        fridge.addItem(product3);
        fridge.addItem(product4);

        expiredProducts = fridge.checkExpiredAtDate(dateText); //Only the ones which expire at the currect Date will be added to the expired products
        fridge.AddExpired(expiredProducts);

        System.out.println("Fridge Items");
        for (FridgeItem fridgeItem:fridge.getFridgeItems())
        {
            System.out.println(fridgeItem.getName());
        }

        System.out.println("Expired Items");
        for (FridgeItem fridgeItem:fridge.getExpiredItems())
        {
            System.out.println(fridgeItem.getName());
        }
    }

    public void addNewItem(View view)
    {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("Fridge", fridge);
        startActivityForResult(i, 2);
    }

    @Override
    protected void onActivityResult( int requestCode , int resultCode , Intent data ) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 2) && resultCode == RESULT_OK)
        {
            //Create toast when new item has been added
            Toast.makeText(this, "You have added a new product in your fridge", Toast.LENGTH_SHORT).show();
            fridge = (Fridge) data.getExtras().getSerializable("Fridge");

            System.out.println("Fridge Items");
            for (FridgeItem fridgeItem:fridge.getFridgeItems())
            {
                System.out.println(fridgeItem.getName());
            }

            System.out.println("Expired Items");
            for (FridgeItem fridgeItem:fridge.getExpiredItems())
            {
                System.out.println(fridgeItem.getName());
            }
        }
    }

}