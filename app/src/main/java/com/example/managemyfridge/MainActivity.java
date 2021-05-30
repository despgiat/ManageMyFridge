package com.example.managemyfridge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
    //TextView idView;
    //EditText productBox;
    EditText quantityBox;
    Spinner unitSpinner;
    Spinner typeSpinner;

    //EditText productTypeBox;
    //EditText dateofOpeningBox;
    EditText unitsBox;

    //should use next line
    //LocalDate currentDate;
    //String currentDate;

    boolean customOpenedDate;
    LinearLayout openedLayout;
    RadioGroup radioGroupOpened;
    Intent i;
    Fridge fridge;
    //private FridgeItem fridgeItem;
    DateTimeFormatter formatter;
    LocalDate currentDate;
    String isOpened;
    String openedDateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        formatter = MainScreen.formatter;
        currentDate = LocalDate.now();

        itemName = findViewById(R.id.editTextTextPersonName);
        expiry = findViewById(R.id.editTextDate);
        openedDate = findViewById(R.id.editTextDateOpened);

        openedLayout = findViewById(R.id.openedLayout);
        openedLayout.setVisibility(View.GONE);
        radioGroupOpened = findViewById(R.id.radioGroup);

        //Get references to view objects
        //idView = findViewById(R.id.productID);
        //productBox = findViewById(R.id.productName);
        quantityBox = findViewById(R.id.productQuantityEditText);

        //added buttons in onCreate
        //buttonadd = findViewById(R.id.button1);
        //buttonfind = findViewById(R.id.button2);
        //buttondel = findViewById(R.id.button3);

        //exDateBox = findViewById(R.id.exDate);
        //productTypeBox = findViewById(R.id.productType);
        //dateofOpeningBox = findViewById(R.id.dateofOpening);
        //unitsBox = findViewById(R.id.autoCompleteTextView);

        typeSpinner = findViewById(R.id.typeSpinner);
        isOpened = "";

        unitSpinner = findViewById(R.id.unitSpinner);
        //or if it doesn't work
        //unitSpinner = (Spinner) findViewById(R.id.spinner1);

        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.units));
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner.setAdapter(unitAdapter);

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.types));
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);


        i = getIntent();
        fridge = (Fridge) i.getSerializableExtra("Fridge");
        //fridgeItem = new FridgeItem();

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
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);

        //If not all fields are filled up, a toast with a warning appears-> return if happens;

        //We process the input from the edittexts (the dates)
        //String textOther = otherDate.getText().toString(); //We get the user's input
        //LocalDate dateOther = LocalDate.parse(textOther, formatter);
        //fridgeItem.setDayOpened(dateOther);

        String itemNameText = itemName.getText().toString();
        //fridgeItem.setName(itemNameText);
        String expiryText = expiry.getText().toString();

        //fridgeItem.setExpiry(expiryText);

        String quantity = quantityBox.getText().toString();
        //String productType = productTypeBox.getText().toString();
        //String unit = unitsBox.getText().toString();

        String unit = unitSpinner.getSelectedItem().toString();
        String productType = typeSpinner.getSelectedItem().toString();

        /*String isitopen = "no";
        /*if( isitopenbutton=="yes"){
            isitopen="yes";
        }*/

        if(customOpenedDate)
        {
            openedDateText = openedDate.getText().toString();
           // fridgeItem.setDayOpened(openedDateText);
        }

        if(itemNameText.isEmpty() || expiryText.isEmpty() || (isOpened.equals("yes") && openedDateText.isEmpty()) || quantity.isEmpty())
       // if(fridgeItem.getName().isEmpty() || fridgeItem.getExpiry().isEmpty() || (fridgeItem.isOpened() && fridgeItem.getDayOpened().isEmpty()))
        {
            Toast.makeText(this, "You're missing some fields!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            try //We check if the inputted dates are valid
            {
                LocalDate expDate = LocalDate.parse(expiryText, formatter);
            }
            catch (DateTimeParseException e)
            {
                Toast.makeText(this, "Incorrect date format!", Toast.LENGTH_SHORT).show();
                return;
            }

            if(isOpened.equals("yes"))
            //if(fridgeItem.isOpened())
            {
                try
                {
                    LocalDate openDate = LocalDate.parse(openedDateText, formatter);
                }
                catch (DateTimeParseException e)
                {
                    Toast.makeText(this, "Incorrect date format!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            if (!itemNameText.equals("") &&  !quantity.equals("")){
                Product found = dbHandler.findProduct(itemNameText);
                if (found == null){
                    //added: exDateBox in product, isitopen, productTypeBox, dateofOpeningBox, unitsBox
                    Product product = new Product(itemNameText, quantity, expiryText, isOpened, productType, openedDateText, unit);
                    //Product product = new Product(productName, Integer.parseInt(quantity));
                    dbHandler.addProduct(product);

                    /* productBox.setText("");
                    quantityBox.setText("");
                    //exDateBox.setText("");
                    //just turn the isitiopen button off
                    //isitopenbutton.setText("");
                    productTypeBox.setText("");
                    //dateofOpeningBox.setText("");
                    //unitsBox.setText("");
                    */

                    //adding toast if product was added
                    Toast.makeText(MainActivity.this, "Product added", Toast.LENGTH_SHORT).show();
                }
                else {
                    //adding toast if product was not added
                    Toast.makeText(MainActivity.this, "Error. Product was not added", Toast.LENGTH_SHORT).show();
                }
            }


           // fridge.addItem(fridgeItem);

            /*String date = currentDate.format(formatter);

            if(fridgeItem.productExpired(date))//Checks if item added was expired
            {
                fridge.AddExpired(fridgeItem);
            }

             */

            //Check the expiration date here and if it is equal to the current system date or lower, add it to the expired items

            Intent returnData = new Intent();
            //returnData.putExtra("Fridge", fridge);
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
                isOpened = "yes";

                //fridgeItem.setOpened(true);
                break;

            case R.id.noButton:
                openedLayout.setVisibility(View.GONE);
                //fridgeItem.setOpened(false);
                isOpened = "no";
                break;
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
                openedDateText = text;
                //fridgeItem.setDayOpened(text);
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