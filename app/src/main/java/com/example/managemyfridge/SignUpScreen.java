package com.example.managemyfridge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpScreen extends AppCompatActivity {
    EditText username;
    EditText confirmpassword;
    EditText password;
    EditText email;
    Button register;
    Button login;
  //  MyDBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //finding views and assigning them to specific variables
        setContentView(R.layout.activity_sign_up_screen);
        username = findViewById(R.id.editTextTextPersonName2);
        password = findViewById(R.id.editTextTextPassword6);
        confirmpassword = findViewById(R.id.editTextTextPassword7);
        email = findViewById(R.id.editTextTextEmailAddress5);
        register = findViewById(R.id.button11);
        login = findViewById(R.id.button10);

        //db handler creation
        //dbHandler = new MyDBHandler(this, null, null, 1);
        //listener for sign up button that takes you to home screen if the registration was successful
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkDataEntered();
            } //checks the data entered and creates a user.
        });
        //listener for log in button that takes you to log in screen
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LoginScreen.class);
                startActivity(i);
                finish(); //So that we don't go back to the login activity on back pressed
            }
        });

    }

    /**
     * In this function we are checking the data that is entered.
     * In case there is some mistake, either an empty field, wrong email format, or miss matching password
     * the registration button is not taking you to the next screen (home screen)
     * When all conditions are met, the variable proceed will be true and we are going to be allowed to
     * move on to next screen
     */
    private void checkDataEntered() {
        boolean proceed = true;

        //checking if the fields are empty and printing the proper message in such case
        if (isEmpty(username)) {
            username.setError("You must enter a username to register!");
            proceed = false;
        }

        if (isEmpty(password)) {
            password.setError("You must enter a password to register!");
            proceed = false;
        }
        if (isEmpty(email)) {
            email.setError("You must enter an email address to register!");
            proceed = false;

        }

        if (isEmpty(confirmpassword)) {
            confirmpassword.setError("You must re-enter your password to register!");
            proceed = false;
        }

        //checks if the passwords entered are matching
        if (!(confirmpassword.getText().toString().equals(password.getText().toString()))) {
            confirmpassword.setError("The passwords you have entered are not matching, please re-enter them to register!");
            proceed = false;

        }

        //checks if the email field is filled with text that has email format
        if (!isEmail(email)){
            email.setError("Please make sure you enter a valid email format (username@example.com)");
        proceed = false;
        }
        if (proceed) {
            /*/checking if username is available
            if(LoginScreen.dbHandlerlog.findUser(username)){
                username.setError("The username you have entered is not available, please try something else!");
                proceed = false;
            }
            //checking if email is taken
            if(LoginScreen.dbHandlerlog.findUser(email)){
                email.setError("The email you have entered is not available, please try something else!");
                proceed = false;
            }*/
            //creating the user
            LoginScreen.user.setUsername(username.getText().toString());
            LoginScreen.user.setPassword(password.getText().toString());
            LoginScreen.user.setEmail(email.getText().toString());
            //adding user to data base
            LoginScreen.dbHandlerlog.addUser();
            //moving to home screen
            Intent i = new Intent(getApplicationContext(), MainScreen.class);
            startActivity(i);
            finish(); //So that we don't go back to the login activity on back pressed
        }
    }
    //checks if the text format is email format
    boolean isEmail(EditText text) {
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }
    //checks if text field is empty or not
    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }
}