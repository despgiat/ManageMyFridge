package com.example.managemyfridge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MainActivity extends AppCompatActivity {

    //Things to implement:
    //Warning about incorrect date format (To be made)
    //Warning about not filling all the required fields (DONE)

    EditText itemName;
    EditText expiry;
    EditText openedDate;
    boolean customOpenedDate;
    ConstraintLayout openedLayout;
    RadioGroup radioGroupOpened;
    Intent i;
    Fridge fridge;
    private FridgeItem fridgeItem;
    DateTimeFormatter formatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        itemName = findViewById(R.id.editTextTextPersonName);
        expiry = findViewById(R.id.editTextDate);
        openedDate = findViewById(R.id.editTextDateOpened);

        openedLayout = findViewById(R.id.constraintLayout);
        openedLayout.setVisibility(View.INVISIBLE);
        radioGroupOpened = findViewById(R.id.radioGroup);

        i = getIntent();
        fridge = (Fridge) i.getSerializableExtra("Fridge");
        fridgeItem = new FridgeItem();

        if (savedInstanceState != null) {
            //Retrieve data from the Bundle (other methods include getInt(), getBoolean() etc)
            CharSequence userText = savedInstanceState.getCharSequence("savedUserText");
            //CharSequence displayText = savedInstanceState.getCharSequence("savedDisplayText");
            int userChoice = savedInstanceState.getInt("savedUserChoice");

            //Restore the dynamic state of the UI
            itemName.setText(userText);

            radioGroupOpened.check(userChoice);
        } else {
            //Initialize the UI
            itemName.setText("");
            radioGroupOpened.check(R.id.noButton);

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save data to the Bundle (other methods include putInt(), putBoolean() etc)
        CharSequence userText = itemName.getText();
        outState.putCharSequence("savedUserText", userText);

        //outState.putCharSequence("savedDisplayText", displayText);
        outState.putInt("savedUserChoice", radioGroupOpened.getCheckedRadioButtonId());
    }

    public void addNewItem(View view)
    {
        //If not all fields are filled up, a toast with a warning appears-> return if happens;

        //We process the input from the edittexts (the dates)
        //String textOther = otherDate.getText().toString(); //We get the user's input
        //LocalDate dateOther = LocalDate.parse(textOther, formatter);
        //fridgeItem.setDayOpened(dateOther);

        String itemNameText = itemName.getText().toString();
        fridgeItem.setName(itemNameText);
        String expiryText = expiry.getText().toString();
        fridgeItem.setExpiry(expiryText);
        String openedDateText = openedDate.getText().toString();

        if(customOpenedDate)
        {
            fridgeItem.setDayOpened(openedDateText);
        }

        if(fridgeItem.getName().isEmpty() || fridgeItem.getExpiry().isEmpty() || (fridgeItem.isOpened() && fridgeItem.getDayOpened().isEmpty()))
        {
            Toast.makeText(this, "You're missing some fields!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            fridge.addItem(fridgeItem);
            Intent returnData = new Intent();
            returnData.putExtra("Fridge", fridge);
            setResult(RESULT_OK , returnData);
            finish();
        }

    }

    public void itemOpened(View view)
    {
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        if (view.getId() == R.id.yesButton)
        {
            if (checked)
            {
                openedLayout.setVisibility(View.VISIBLE);
                fridgeItem.setOpened(true);
            }

        }
        if (view.getId() == R.id.noButton)
        {
            if (checked)
            {
                openedLayout.setVisibility(View.INVISIBLE);
                fridgeItem.setOpened(false);
                fridgeItem.setDayOpened("");
            }
        }
    }

    public void dayOpened(View view)
    {
        //Gets the opened date from here
        RadioGroup dateRadioGroup = findViewById(R.id.radioGroup2);
        int choiceId = dateRadioGroup.getCheckedRadioButtonId();
        EditText otherDate = findViewById(R.id.editTextDateOpened);
        TextView dateFormat = findViewById(R.id.textViewDateFormat);

        switch (choiceId)
        {
            case R.id.todayButton:
                LocalDate date = LocalDate.now(); //We retrieve the current system date
                String text = date.format(formatter);
                fridgeItem.setDayOpened(text);
                otherDate.setVisibility(View.INVISIBLE);
                dateFormat.setVisibility(View.INVISIBLE);
                customOpenedDate = false;
                break;
            case R.id.otherDateButton:
                otherDate.setVisibility(View.VISIBLE);
                dateFormat.setVisibility(View.VISIBLE);
                customOpenedDate = true;
                break;
        }

    }
}