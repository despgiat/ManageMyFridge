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
                Intent i = new Intent(getApplicationContext(), MainScreen.class);
                startActivity(i);
                finish(); //So that we don't go back to the login activity on back pressed

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
        if (isEmpty(username)) {
            Toast t = Toast.makeText(this, "You must enter a username or email address to register!", Toast.LENGTH_SHORT);
            t.show();
        }
        if (isEmpty(password)) {
            Toast t = Toast.makeText(this, "You must enter a password to register!", Toast.LENGTH_SHORT);
            t.show();
        }
        if (isEmpty(confirmpassword)) {
            Toast t = Toast.makeText(this, "You must re-enter your password to register!", Toast.LENGTH_SHORT);
            t.show();
        }
        if (!confirmpassword.equals(password)) {
            Toast t = Toast.makeText(this, "The passwords you have entered are not matching, please re-enter them to register!", Toast.LENGTH_SHORT);
            t.show();
        }
        if(isEmail(username))
            LoginScreen.user.setEmail(username.toString());
        else
            LoginScreen.user.setUsername(username.toString());
        LoginScreen.user.setPassword(password.toString());
        /**TODO: Add the user to database!
         *
         */

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