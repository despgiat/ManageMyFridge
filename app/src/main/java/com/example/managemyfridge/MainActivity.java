package com.example.managemyfridge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView itemShowName;
    EditText itemName;
    ConstraintLayout openedLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemShowName = findViewById(R.id.textView2);
        itemName = findViewById(R.id.editTextTextPersonName);
        openedLayout = findViewById(R.id.constraintLayout);

        if (savedInstanceState != null) {
            //Retrieve data from the Bundle (other methods include getInt(), getBoolean() etc)
            CharSequence userText = savedInstanceState.getCharSequence("savedUserText");
            CharSequence displayText = savedInstanceState.getCharSequence("savedDisplayText");
            //Restore the dynamic state of the UI
            itemName.setText(userText);
            itemShowName.setText(displayText);
        } else {
            //Initialize the UI
            itemName.setText("");
            itemShowName.setHint("Name");
            itemShowName.setText("TextView");



        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save data to the Bundle (other methods include putInt(), putBoolean() etc)
        CharSequence userText = itemName.getText();
        outState.putCharSequence("savedUserText", userText);
        CharSequence displayText = itemShowName.getText();
        outState.putCharSequence("savedDisplayText", displayText);
    }

    public void addNewItem(View view)
    {
        itemShowName.setText("You have added " + itemName.getText().toString());
    }

    public void itemOpened(View view)
    {
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        if (view.getId() == R.id.yesButton)
        {
            if (checked)
                openedLayout.setVisibility(View.VISIBLE);
        }
        if (view.getId() == R.id.noButton)
        {
            if (checked)
                    openedLayout.setVisibility(View.INVISIBLE);
        }
    }
}