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
import java.time.format.DateTimeParseException;

public class MainActivity extends AppCompatActivity {

    //Things to add:
    //When the user adds an item which expires today or has already expired, add it to the expired items list of the fridge
    //Make the toast at the end a Snackbar and implement the undo button (Προαιρετικά)

    EditText itemName;
    EditText expiry;
    EditText openedDate;
    boolean customOpenedDate;
    ConstraintLayout openedLayout;
    //RadioGroup radioGroupOpened;
    Intent i;
    Fridge fridge;
    private FridgeItem fridgeItem;
    DateTimeFormatter formatter;
    LocalDate currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        currentDate = LocalDate.now();

        itemName = findViewById(R.id.editTextTextPersonName);
        expiry = findViewById(R.id.editTextDate);
        openedDate = findViewById(R.id.editTextDateOpened);

        openedLayout = findViewById(R.id.constraintLayout);
        openedLayout.setVisibility(View.INVISIBLE);
        //radioGroupOpened = findViewById(R.id.radioGroup);

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

            //radioGroupOpened.check(userChoice);
        } else {
            //Initialize the UI
            itemName.setText("");
            //radioGroupOpened.check(R.id.noButton);

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save data to the Bundle (other methods include putInt(), putBoolean() etc)
        CharSequence userText = itemName.getText();
        outState.putCharSequence("savedUserText", userText);

        //outState.putCharSequence("savedDisplayText", displayText);
        //outState.putInt("savedUserChoice", radioGroupOpened.getCheckedRadioButtonId());
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
            try //We check if the inputted dates are valid
            {
                LocalDate expDate = LocalDate.parse(fridgeItem.getExpiry(), formatter);
            }
            catch (DateTimeParseException e)
            {
                Toast.makeText(this, "Incorrect date format!", Toast.LENGTH_SHORT).show();
                return;
            }

            if(fridgeItem.isOpened())
            {
                try
                {
                    LocalDate openDate = LocalDate.parse(fridgeItem.getDayOpened(), formatter);
                }
                catch (DateTimeParseException e)
                {
                    Toast.makeText(this, "Incorrect date format!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            fridge.addItem(fridgeItem);

            /*String date = currentDate.format(formatter);

            if(fridgeItem.productExpired(date))//Checks if item added was expired
            {
                fridge.AddExpired(fridgeItem);
            }

             */

            //Check the expiration date here and if it is equal to the current system date or lower, add it to the expired items

            Intent returnData = new Intent();
            returnData.putExtra("Fridge", fridge);
            setResult(RESULT_OK , returnData);
            finish();
        }

    }

    public void itemOpened(View view)
    {
        RadioGroup openedRadioGroup = findViewById(R.id.radioGroup);
        int checked = openedRadioGroup.getCheckedRadioButtonId();

        switch (checked)
        {
            case R.id.yesButton:
                openedLayout.setVisibility(View.VISIBLE);
                fridgeItem.setOpened(true);
                break;

            case R.id.noButton:
                openedLayout.setVisibility(View.GONE);
                fridgeItem.setOpened(false);
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
                String text = currentDate.format(formatter);
                fridgeItem.setDayOpened(text);
                otherDate.setVisibility(View.GONE);
                dateFormat.setVisibility(View.GONE);
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