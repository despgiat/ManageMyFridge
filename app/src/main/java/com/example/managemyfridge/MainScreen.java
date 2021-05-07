package com.example.managemyfridge;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainScreen extends AppCompatActivity {

    private Fridge fridge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        fridge = new Fridge();
    }
}