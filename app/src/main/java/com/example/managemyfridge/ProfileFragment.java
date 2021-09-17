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
    ImageButton changeUsername;
    Button changePassword;
    EditText editTextUsername;
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

        changeUsername = view.findViewById(R.id.editUsernameButton);
        changeUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //String usernameText = (String) username.getText();


                editMode = !editMode;
                if(editMode)
                {
                    username.setVisibility(View.GONE);
                    editTextUsername = view.findViewById(R.id.editTextUsername);
                    editTextUsername.setVisibility(View.VISIBLE);
                    editTextUsername.setText(username.getText());
                }
                else
                {
                    if(editTextUsername.getText() != null)
                    {
                        username.setText(editTextUsername.getText());
                        editTextUsername.setVisibility(View.GONE);
                        username.setVisibility(View.VISIBLE);

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