package com.example.managemyfridge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
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

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class MainActivity extends AppCompatActivity {

    EditText itemName;
    EditText expiry;
    EditText openedDate;
    EditText quantityBox;
    Spinner unitSpinner;
    Spinner typeSpinner;
    LinearLayout openedLayout;
    RadioGroup radioGroupOpened;
    RadioGroup dateRadioGroup;
    TextView dateFormat;
    Button button;

    Intent i;
    Fridge fridge; //The fridge is retrieved from the MainScreen
    Product product;
    DateTimeFormatter formatter;
    LocalDate currentDate;
    String isOpened;
    String openedDateText;
    boolean customOpenedDate;

    //boolean edit = false;

    //NEW: Added mDBHelper and mDb
    private MyDBHandler mDBHelper;
    private SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //NEW:FOR DB new way of handling things
        mDBHelper = new MyDBHandler(this, null, null, 1);

        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        try {
            mDb = mDBHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        formatter = MainScreen.formatter; //Gets the date formatter from the MainScreen
        currentDate = LocalDate.now();

        //References to view objects
        itemName = findViewById(R.id.editTextTextPersonName);
        expiry = findViewById(R.id.editTextDate);
        openedDate = findViewById(R.id.editTextDateOpened);
        openedLayout = findViewById(R.id.openedLayout);
        openedLayout.setVisibility(View.GONE);
        radioGroupOpened = findViewById(R.id.radioGroup);
        dateRadioGroup = findViewById(R.id.radioGroup2);
        //otherDate = findViewById(R.id.editTextDateOpened);
        dateFormat = findViewById(R.id.textViewDateFormat);
        quantityBox = findViewById(R.id.productQuantityEditText);
        typeSpinner = findViewById(R.id.typeSpinner);
        unitSpinner = findViewById(R.id.unitSpinner);
        button = findViewById(R.id.button);

        //Array Adapters for the spinners (to display the product units and type)
        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.units));
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner.setAdapter(unitAdapter);

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.types));
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);

        isOpened = "";
        i = getIntent();
        fridge = (Fridge) i.getSerializableExtra("Fridge"); //retrieves the fridge from the MainScreen that called it
        product = (Product) i.getSerializableExtra("Product"); //if the product is not null, this means that we are currently editing a product instead of adding a new one
        if(product != null)
            button.setText("SAVE");
        else
            button.setText("ADD PRODUCT");


        //For the changing of the phone's settings (rotation, dark mode and such)
        //if the fields are already filled we want them to stay that way
        if (savedInstanceState != null) {
            //Retrieve data from the Bundle (other methods include getInt(), getBoolean() etc)
           // Product product = (Product) savedInstanceState.getSerializable("Product");
            CharSequence item_name = savedInstanceState.getCharSequence("item_name");
           // int userChoice = savedInstanceState.getInt("savedUserChoice");

            //Restore the dynamic state of the UI
            itemName.setText(item_name);

        } else {
            //Initialize the UI
            if(product != null)
            {
                itemName.setText(product.getProductName());
                itemName.setFocusable(false); //To make it non editable
                itemName.setClickable(false);

                expiry.setText(product.get_exdate());
                if(product.get_opened().equals("no"))
                {
                    openedLayout.setVisibility(View.GONE);
                    radioGroupOpened.check(R.id.noButton);
                }
                else
                {
                    openedLayout.setVisibility(View.VISIBLE);
                    radioGroupOpened.check(R.id.yesButton);

                    if(currentDate.format(formatter).equals(product.get_DateofOpening()))
                    {
                        dateRadioGroup.check(R.id.todayButton);
                    }
                    else
                    {
                        dateRadioGroup.check(R.id.otherDateButton);
                        openedDate.setVisibility(View.VISIBLE);
                        openedDate.setText(product.get_DateofOpening());
                    }

                }

                quantityBox.setText(product.getQuantity());
                typeSpinner.setSelection(typeAdapter.getPosition(product.get_prodtype()));
                unitSpinner.setSelection(unitAdapter.getPosition(product.get_unit()));
                button = findViewById(R.id.button);
            }

        }
    }


    //saves the current state
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save data to the Bundle (other methods include putInt(), putBoolean() etc)
        CharSequence item_name = itemName.getText();
        outState.putCharSequence("item_name", item_name);

        //outState.putCharSequence("savedDisplayText", displayText);
        //outState.putInt("savedUserChoice", radioGroupOpened.getCheckedRadioButtonId());
    }

    public void addNewItem(View view)
    {
        //Load the database
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);

        //Sets the strings' value according to the layout's edit texts
        String itemNameText = itemName.getText().toString();
        String expiryText = expiry.getText().toString();
        String quantity = quantityBox.getText().toString();
        String unit = unitSpinner.getSelectedItem().toString();
        String productType = typeSpinner.getSelectedItem().toString();

        //TODO Editing the custom opening date doesn't work, we should fix it.

        if(customOpenedDate) //If the user had chosen a custom opening date and not the "Today" option in the last radio group (upon checking that the product has been opened)
        {
            openedDateText = openedDate.getText().toString();
        }

        //Checks if any of the required fields is missing. if yes, a toast appears, warning the user
        if(itemNameText.isEmpty() || expiryText.isEmpty() || (isOpened.equals("yes") && openedDateText.isEmpty()) || quantity.isEmpty())
        {
            Toast.makeText(this, "You're missing some fields!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            try //We check if the inputted dates (expiration and/or opening date) are valid. If not, a toast appears, warning the user
            {
                LocalDate expDate = LocalDate.parse(expiryText, formatter);
            }
            catch (DateTimeParseException e)
            {
                Toast.makeText(this, "Incorrect date format!", Toast.LENGTH_SHORT).show();
                return;
            }

            if(isOpened.equals("yes"))
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

            if (!itemNameText.equals("") &&  !quantity.equals("")){ //Checks if the same item already exists in the database.
                Product found = dbHandler.findProduct(itemNameText);
                if (found == null){ //if it doesn't, the new product gets added

                    Product product = new Product(itemNameText, quantity, expiryText, isOpened, productType, openedDateText, unit);

                    //NEW: Find id of user and send it with new product to be created
                    int idofuser = LoginScreen.user.getID();
                    dbHandler.addProduct(product, idofuser);


                    //adding toast if product was added
                    Toast.makeText(MainActivity.this, "Product added", Toast.LENGTH_SHORT).show();
                }
                else {

                    found.setProductName(itemNameText);
                    found.setQuantity(quantity);
                    found.set_exdate(expiryText);
                    found.set_opened(isOpened);
                    found.set_prodtype(productType);
                    found.set_DateofOpening(openedDateText);
                    found.set_unit(unit);

                    dbHandler.updateProduct(found);

                    //adding toast if product was not added
                    Toast.makeText(MainActivity.this, "Changes saved", Toast.LENGTH_SHORT).show();
                }
            }


            Intent returnData = new Intent();
            //returnData.putExtra("Fridge", fridge);
            setResult(RESULT_OK , returnData);
            finish();
        }

    }

    /**
     * on Click method in the radioGroup. Depending on the user's choice, if the product they are trying to add had been opened, displays
     * extra fields for them to enter (what was the product's opening date)
     */
    public void itemOpened(View view)
    {
        RadioGroup openedRadioGroup = findViewById(R.id.radioGroup);
        int checked = openedRadioGroup.getCheckedRadioButtonId();

        switch (checked)
        {
            case R.id.yesButton:
                openedLayout.setVisibility(View.VISIBLE);
                isOpened = "yes";
                break;

            case R.id.noButton:
                openedLayout.setVisibility(View.GONE);
                isOpened = "no";
                break;
        }
    }


    /**
     * on Click method in the radioGroup2. Depending on the user's choice, if the product they are trying to add had been opened at a date other than the current
     * it displays an extra edit text and and an "appropriate" format text view. Either way it sets the date of opening for the product.
     */
    public void dayOpened(View view)
    {
        int choiceId = dateRadioGroup.getCheckedRadioButtonId();

        switch (choiceId)
        {
            case R.id.todayButton:
                String text = currentDate.format(formatter);
                openedDateText = text;
                openedDate.setVisibility(View.GONE);
                dateFormat.setVisibility(View.GONE);
                customOpenedDate = false;
                break;
            case R.id.otherDateButton:
                openedDate.setVisibility(View.VISIBLE);
                dateFormat.setVisibility(View.VISIBLE);
                customOpenedDate = true;
                break;
        }

    }
}