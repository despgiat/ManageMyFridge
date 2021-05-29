package com.example.managemyfridge;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TITLE = "title";
    private static final String INSTRUCTIONS = "instructions";


    // TODO: Rename and change types of parameters
    private String recipeTitle;
    private String recipeInstructions;
    boolean favourite; //We will check from the database if it was marked as favourite by the user and we will display it as such
    MenuItem fave;

    Menu toolbar;

    public RecipesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecipesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecipesFragment newInstance(String param1, String param2) {
        RecipesFragment fragment = new RecipesFragment();
        Bundle args = new Bundle();
        args.putString(TITLE, param1);
        args.putString(INSTRUCTIONS, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recipeTitle = getArguments().getString(TITLE);
            recipeInstructions = getArguments().getString(INSTRUCTIONS);

            setHasOptionsMenu(true);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_recipes, container, false);
        TextView titleTextView = view.findViewById(R.id.recipeTitle);
        titleTextView.setText(recipeTitle);

        TextView instructions = view.findViewById(R.id.instructionsTextView);
        instructions.setText(recipeInstructions);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu items for use in the action bar
        inflater.inflate(R.menu.recipes_toobar_menu, menu);

        fave = menu.findItem(R.id.fave);
        fave.setIcon(R.drawable.ic_fave_empty);

        fave.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if(!favourite)
                {
                    setAsFavourite();
                }
                else
                {
                    removeFromFavourites();
                }

                return true;
            }
        });


        super.onCreateOptionsMenu(menu, inflater);
    }

    public void setAsFavourite()
    {
        fave.setIcon(R.drawable.ic_fave_filled);

        //Database stuff

        Toast.makeText(getContext(), "This recipe has been added to your favorites!", Toast.LENGTH_SHORT).show();
        favourite = true;
    }

    public void removeFromFavourites()
    {
        fave.setIcon(R.drawable.ic_fave_empty);

        //Database stuff

        Toast.makeText(getContext(), "This recipe has been removed from your favorites!", Toast.LENGTH_SHORT).show();
        favourite = false;
    }


}