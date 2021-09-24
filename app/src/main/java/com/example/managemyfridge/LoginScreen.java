package com.example.managemyfridge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
//import android.widget.Toast;

public class LoginScreen extends AppCompatActivity {
    static Users user;
    EditText email;
    EditText password;
    Button register;
    Button login;
    @SuppressLint("StaticFieldLeak")
    static MyDBHandler dbHandlerlog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        user = new Users();

        //db handler creation

        dbHandlerlog = new MyDBHandler(this, null, null, 1);

        setupUI();
        setupListeners();
    }
    private void setupListeners() {
        //listener for log in button that takes you to home screen if log in was successful
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUsername(); //method for validating username format
            }
        });
        //listener for sign up button that takes you to sign up screen
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), SignUpScreen.class);
                startActivity(i);
                finish(); //So that we don't go back to the login activity on back pressed

            }
        });
    }

    private void checkUsername() {
        //There are two ways to log in, either with username or email
        boolean isValid = true;
       // boolean isemail = false;

        //checking if text views are filled
        if (isEmpty(email)) {
            email.setError("Please enter email address to login");
            isValid = false;
        }
        if (isEmpty(password)) {
            password.setError("Please enter the password to login");
            isValid = false;
        }
        //if the text fields are filled
        if (isValid) {
           // String usernameValue = email.getText().toString(); //gets the text that user added in the username container
           // String passwordValue = password.getText().toString(); //same for password
            
            //checks if the user exists in the data base, if so f gets true and the user variable gets all of his info./
            boolean f = dbHandlerlog.findUser(email); 
            if(f) //checks if the username exists
                if(password.getText().toString().equals(user.getPassword())) { //checks if the password is correct


                    Intent i = new Intent(getApplicationContext(), MainScreen.class); //moves to mainscreen
                    startActivity(i);
                    finish(); //So that we don't go back to the login activity on back pressed
                }
                else
                {
                    //error message in case of wrong password
                    password.setError("The password you entered is not correct, please try again!");

                }
            else
            {
                //error message in case of wrong username
                email.setError("The email you have entered is not correct, please try again or register if you dont have an account");

            }

        }

    }

    //checks if the edit text is empty or not
    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }
    //UI set up
    private void setupUI() {
        email = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);
        register = findViewById(R.id.button3);
        login = findViewById(R.id.button2);

        SharedPreferences sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean darkMode = sharedPreferences.getBoolean("dark_mode", false);

        if (darkMode)
        {
            //Switch to Dark Mode
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else
        {
            //Switch to Light Mode
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

    }

}