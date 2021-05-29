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

public class LoginScreen extends AppCompatActivity {
    static Users user;
    EditText username;
    EditText password;
    Button register;
    Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        user = new Users();
        setupUI();
        setupListeners();
    }

    private void setupListeners() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUsername();
            }
        });

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
        boolean isValid = true;
        boolean isemail = false;
        if (isEmpty(username)) {
            username.setError("Please enter an username or email address to login");
            isValid = false;
        }
        if (isEmpty(password)) {
            password.setError("Please enter the password to login");
            isValid = false;
        }
        if (isValid) {
            String usernameValue = username.getText().toString();
            String passwordValue = password.getText().toString();
            /**TODO: Check if the username/email exists in our data base, if it does, check if the password is matching and create the user!
             *
             */
            if (usernameValue.equals("dev") && passwordValue.equals("1234")) {//we need to check from db
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
            }
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
    private void setupUI() {
        username = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);
        register = findViewById(R.id.button3);
        login = findViewById(R.id.button2);
    }

}