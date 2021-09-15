package com.example.managemyfridge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginScreen extends AppCompatActivity {
    static Users user;
    EditText username;
    EditText password;
    Button register;
    Button login;
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
        //when logging in there two possible ways, either with username or email
        boolean isValid = true;
        boolean isemail = false;

        //checking if text views are filled
        if (isEmpty(username)) {
            username.setError("Please enter an username or email address to login");
            isValid = false;
        }
        if (isEmpty(password)) {
            password.setError("Please enter the password to login");
            isValid = false;
        }

        if (isValid) {
            String usernameValue = username.getText().toString(); //gets the text that user added in the username container
            String passwordValue = password.getText().toString(); //same for password
            /**TODO: Check if the username/email exists in our data base, if it does, check if the password is matching and create the user!
             *
             */
            boolean f = dbHandlerlog.findUser(username);
            if(f) //checks if the username exists
                if(password.getText().toString().equals(user.getPassword())) { //checks if the password is correct

                    user.setUsername(username.getText().toString());
                    user.setPassword(password.getText().toString());

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
                username.setError("The username you have entered is not correct, please try again or register if you dont have an account");
            }
            /*if (usernameValue.equals("dev") && passwordValue.equals("1234")) {//we need to check from db
                if(isEmail(username))
                    user.setEmail(usernameValue);
                else
                    user.setUsername(usernameValue);
                user.setEmail("dev@gmail.com");

                user.setPassword(passwordValue);
                Intent i = new Intent(getApplicationContext(), MainScreen.class);
                startActivity(i);
                finish(); //So that we don't go back to the login activity on back pressed
            } else {
                Toast t = Toast.makeText(this, "Wrong Username or password", Toast.LENGTH_SHORT);
                t.show();
            }*/
        }

    }

    //checks if the edit textis empty or not
    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }
    //UI set up
    private void setupUI() {
        username = findViewById(R.id.editTextTextEmailAddress);
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