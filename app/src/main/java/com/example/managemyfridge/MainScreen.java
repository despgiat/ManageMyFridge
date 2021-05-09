package com.example.managemyfridge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainScreen extends AppCompatActivity {

    private Fridge fridge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        fridge = new Fridge();
    }

    public void addNewItem(View view)
    {
        Intent i = new Intent(this, MainActivity.class);
        startActivityForResult(i, 2);
    }

    @Override
    protected void onActivityResult( int requestCode , int resultCode , Intent data ) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 2) && resultCode == RESULT_OK)
        {
            //Create toast when new item has been added
            Toast.makeText(this, "You have added a new product in your fridge", Toast.LENGTH_SHORT).show();
        }
        //Get saved data from the returning Intent
        //CharSequence userTextNew = data.getExtras().getCharSequence( getCharSequence("userTextNew
        //Update the UI
        //objEditTextName.setText(userTextNew
        //objTextViewName.setText( setText("Hello Dear " + userTextNew
    }
//If this is the code assigned to SayHelloNewScreen and returning Intent succeeded

}