package com.example.managemyfridge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MainActivity extends AppCompatActivity {

    EditText itemName;
    ConstraintLayout openedLayout;
    RadioGroup radioGroupOpened;

    private Fridge fridge;
    private FridgeItem fridgeItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemName = findViewById(R.id.editTextTextPersonName);
        openedLayout = findViewById(R.id.constraintLayout);
        openedLayout.setVisibility(View.INVISIBLE);
        radioGroupOpened = findViewById(R.id.radioGroup);

        fridge = new Fridge();
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
        //If not all fields are filled up, a toast with a warning appears

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
                fridgeItem.setDayOpened(null);
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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");


        switch (choiceId)
        {
            case R.id.todayButton:
                LocalDate date = LocalDate.now(); //We retrieve the current system date
                String text = date.format(formatter);
                fridgeItem.setDayOpened((LocalDate.parse(text, formatter)));
                otherDate.setVisibility(View.INVISIBLE);
                dateFormat.setVisibility(View.INVISIBLE);
                break;
            case R.id.otherDateButton:
                otherDate.setVisibility(View.VISIBLE);
                dateFormat.setVisibility(View.VISIBLE);
                //String textOther = otherDate.getText().toString(); //We get the user's input
                //LocalDate dateOther = LocalDate.parse(textOther, formatter);
                //fridgeItem.setDayOpened(dateOther);
                break;
        }

    }
}