package com.example.managemyfridge;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.managemyfridge.MyDBHandler;
import com.example.managemyfridge.Product;

public class AddItemActivity extends AppCompatActivity {
    TextView idView;
    EditText productBox;
    EditText quantityBox;

    //added the buttons
    Button buttonadd, buttonfind, buttondel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        //Get references to view objects
        idView = findViewById(R.id.productID);
        productBox = findViewById(R.id.productName);
        quantityBox = findViewById(R.id.productQuantity);

        //added buttons in onCreate
        buttonadd = findViewById(R.id.button1);
        buttonfind = findViewById(R.id.button2);
        buttondel = findViewById(R.id.button3);


    }

    //OnClick method for ADD button
    public void newProduct (View view) {
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
        String productName = productBox.getText().toString();
        String quantity = quantityBox.getText().toString();
        if (!productName.equals("") &&  !quantity.equals("")){
            Product found = dbHandler.findProduct(productName);
            if (found == null){
                //Product product = new Product(productName, Integer.parseInt(quantity));
                //dbHandler.addProduct(product);
                productBox.setText("");
                quantityBox.setText("");

                //adding toast if product was added
                Toast.makeText(AddItemActivity.this, "Product added", Toast.LENGTH_SHORT).show();
            }
            else {
                //adding toast if product was not added
                Toast.makeText(AddItemActivity.this, "Error. Product was not added", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //OnClick method for FIND button
    public void lookupProduct (View view) {
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
        Product product = dbHandler.findProduct(productBox.getText().toString());
        if (product != null) {
            idView.setText(String.valueOf(product.getID()));
            quantityBox.setText(String.valueOf(product.getQuantity()));
        } else {
            idView.setText(getString(R.string.no_match_found));
            quantityBox.setText("");
        }
    }

    //OnClick method for DELETE button
    public void removeProduct (View view) {
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
        boolean result = dbHandler.deleteProduct(productBox.getText().toString());
        if (result)
        {
            idView.setText(getString(R.string.record_deleted));
            productBox.setText("");
            quantityBox.setText("");
        }
        else {
            idView.setText(getString(R.string.no_match_found));
            quantityBox.setText("");
        }
    }
}