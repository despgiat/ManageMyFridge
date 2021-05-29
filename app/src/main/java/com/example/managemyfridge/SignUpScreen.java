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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen);
        username = findViewById(R.id.editTextTextPersonName2);
        password = findViewById(R.id.editTextTextPassword6);
        confirmpassword = findViewById(R.id.editTextTextPassword7);
        email = findViewById(R.id.editTextTextEmailAddress5);
        register = findViewById(R.id.button11);
        login = findViewById(R.id.button10);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkDataEntered();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LoginScreen.class);
                startActivity(i);
                finish(); //So that we don't go back to the login activity on back pressed
            }
        });

    }

    private void checkDataEntered() {
        boolean proceed = true;
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
        if (!(confirmpassword.getText().toString().equals(password.getText().toString()))) {
            confirmpassword.setError("The passwords you have entered are not matching, please re-enter them to register!");
            proceed = false;

        }
        if (!isEmail(email)){
            email.setError("Please make sure you enter a valid email format (username@example.com)");
        proceed = false;
        }
        if (proceed) {
            LoginScreen.user.setUsername(username.getText().toString());
            LoginScreen.user.setPassword(password.getText().toString());
            LoginScreen.user.setEmail(email.getText().toString());
            /**TODO: Add the user to database!
             *
             */
            Intent i = new Intent(getApplicationContext(), MainScreen.class);
            startActivity(i);
            finish(); //So that we don't go back to the login activity on back pressed
        }
    }
    boolean isEmail(EditText text) {
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }
    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }
}