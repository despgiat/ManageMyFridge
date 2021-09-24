package com.example.managemyfridge;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.nio.charset.StandardCharsets;

/**
 * In the Profile Fragment the user is able to see the information regarding them, such as their username, e-mail, password and profile picture.
 */
public class ProfileFragment extends Fragment {

    Button logoutButton;
    TextView changePic;
    TextView removePic;
    ImageView profilePicture;
    TextView username;
    TextView email;
    Button changeUsername;
    Button saveUsername;
    Button discardUsername;
    EditText editTextUsername;
    TextView passwordTextView;
    Button changePassword;
    EditText new_passowrd;
    EditText confirm_passowrd;
    TextView password_match;
    Button save;
    Button cancel;

    boolean editMode = false;


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Profile");

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //profilePicture = view.findViewById(R.id.profilePic);

        username = view.findViewById(R.id.usernameTextView);
        email = view.findViewById(R.id.emailTextView);

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String name = sharedPreferences.getString("username", "Username");
        username.setText(LoginScreen.user.getUsername());
        email.setText(LoginScreen.user.getEmail());

        changePassword = view.findViewById(R.id.change_password);
        confirm_passowrd = view.findViewById(R.id.passwordConfirm);
        passwordTextView = view.findViewById(R.id.passwordTextView);


        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword.setVisibility(View.GONE);
                passwordTextView.setVisibility(View.GONE);
                new_passowrd = view.findViewById(R.id.newPassword);
                new_passowrd.setVisibility(View.VISIBLE);

                confirm_passowrd = view.findViewById(R.id.passwordConfirm);
                confirm_passowrd.setVisibility(View.VISIBLE);




                save = view.findViewById(R.id.save_new_password);
                save.setVisibility(View.VISIBLE);
                cancel = view.findViewById(R.id.discard);
                cancel.setVisibility(View.VISIBLE);

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String newpass, confpass;
                        newpass= new_passowrd.getText().toString();
                        confpass = confirm_passowrd.getText().toString();

                        if (!(newpass.equals(confpass))) {
                            confirm_passowrd.setError("The passwords you have entered are not matching, please re-enter them to register!");
                        }
                        else{
                            LoginScreen.user.setPassword(newpass);
                            LoginScreen.dbHandlerlog.updateUser();

                            save.setVisibility(View.GONE);
                            cancel.setVisibility(View.GONE);
                            new_passowrd.setVisibility(View.GONE);
                            confirm_passowrd.setVisibility(View.GONE);
                            changePassword.setVisibility(View.VISIBLE);
                            passwordTextView.setVisibility(View.VISIBLE);
                            confirm_passowrd.getText().clear();
                            new_passowrd.getText().clear();
                        }
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        confirm_passowrd.getText().clear();
                        new_passowrd.getText().clear();
                        save.setVisibility(View.GONE);
                        cancel.setVisibility(View.GONE);
                        new_passowrd.setVisibility(View.GONE);
                        confirm_passowrd.setVisibility(View.GONE);
                        changePassword.setVisibility(View.VISIBLE);
                        passwordTextView.setVisibility(View.VISIBLE);
                    }
                });

            }
        });
        /*TODO: Change button to Save, after click*/

        saveUsername = view.findViewById(R.id.save_username);
        discardUsername = view.findViewById(R.id.discard_username);
        changeUsername = view.findViewById(R.id.editUsernameButton);
        changeUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //String usernameText = (String) username.getText();

                changeUsername.setVisibility(View.GONE);
                //editMode = !editMode;
               // if(editMode)
                //{
                    username.setVisibility(View.GONE);
                    editTextUsername = view.findViewById(R.id.editTextUsername);
                    editTextUsername.setVisibility(View.VISIBLE);
                    editTextUsername.setText(username.getText());
                    saveUsername.setVisibility(View.VISIBLE);
                    discardUsername.setVisibility(View.VISIBLE);

                    saveUsername.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(editTextUsername.getText() != null)
                            {
                                username.setText(editTextUsername.getText());
                                editTextUsername.setVisibility(View.GONE);
                                username.setVisibility(View.VISIBLE);
                                saveUsername.setVisibility(View.GONE);
                                discardUsername.setVisibility(View.GONE);
                                changeUsername.setVisibility(View.VISIBLE);
                                //set this as the general username

                                editor.putString("username", String.valueOf(editTextUsername.getText()));
                                editor.apply();



                                //change the Username everywhere

                                ((MainScreen)getActivity()).usernameChange();

                                LoginScreen.user.setUsername(username.getText().toString());

                                //update DB
                                LoginScreen.dbHandlerlog.updateUser();
                            }

                        }
                    });
                    discardUsername.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            editTextUsername.getText().clear();
                            editTextUsername.setVisibility(View.GONE);
                            saveUsername.setVisibility(View.GONE);
                            discardUsername.setVisibility(View.GONE);
                            changeUsername.setVisibility(View.VISIBLE);
                            username.setVisibility(View.VISIBLE);
                        }
                    });
               // }
             //   else
             //   {

              //  }

            }
        });

        email = view.findViewById(R.id.emailTextView);

        logoutButton = view.findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainScreen) getActivity()).LogOut();
            }
        });

        changePic = view.findViewById(R.id.changePic);
        changePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainScreen) getActivity()).changeProfilePic();
            }
        });

        removePic = view.findViewById(R.id.removePicButton);
        removePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainScreen) getActivity()).removeProfilePic();
            }
        });

        return view;

    }

}