package com.example.managemyfridge;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

/**
 * The app's settings.
 * We use the Shared Preferences to store the application settings, like the dark mode.
 * https://www.geeksforgeeks.org/shared-preferences-in-android-with-examples/#:~:text=Shared%20Preferences%20is%20the%20way,app%20on%20the%20device%20storage.
 */

public class SettingsFragment extends PreferenceFragmentCompat {

    //The application preferences displayed on the Settings Screen
    Preference darkMode;
    Preference feedback;
    Preference version;
    boolean darkModeEnabled;

    public SettingsFragment() {
        // Required empty public constructor
    }


    public static SettingsFragment newInstance(String param1, String param2) {

        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
          //  mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState); //We don't want to inflate a new XML layout, so we call super
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Settings");
        return view;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.app_settings, rootKey);

            //Retrieves the SharedPreferences to check their values
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //Retrieves the user's "dark mode" preference
        darkModeEnabled = sharedPreferences.getBoolean("dark_mode", false);

        /**
         * When the "feedback" preference is clicked, the user is prompt to an email composer with some fields already filled (the email receivers and the subject)
         * so that they can write and send feedback to the app's developers
         */
        feedback = (Preference) findPreference("send_feedback");
        feedback.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                composeEmail(new String[]{"despgiat@csd.auth.gr", "aftzalanc@csd.auth.gr", "alexandrak@csd.auth.gr"} , "Manage My Fridge Feedback");
                return true;
            }
        });


        /**
         * When clicked, a toast will be displayed showing the app's version
         */
        version = findPreference("version");
        version.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Toast.makeText(getContext(), R.string.app_version, Toast.LENGTH_SHORT).show();
                return true;
            }
        });


        darkMode = (SwitchPreference) findPreference("darkMode");
        darkMode.setDefaultValue(darkModeEnabled); //Its value is the one we retrieved from the SharedPreferences
        darkMode.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                if ((boolean) newValue) //if the preference changed to true (on)
                {
                    //Switch to Dark Mode
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                else //if the preference changes to false (off)
                {
                    //Switch to Light Mode
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }

                darkModeEnabled = (boolean) newValue; //The darkModeEnabled value changes to the new preference value and is stored in the Shared Preferences

                editor.putBoolean("dark_mode", darkModeEnabled);
                editor.apply();
                return true;
            }
        });

    }

    public void composeEmail(String[] addresses, String subject) { //See Android Developer Guides on the most used intents - Compose Email intent
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        startActivity(Intent.createChooser(intent, "Send feedback"));

    }

}