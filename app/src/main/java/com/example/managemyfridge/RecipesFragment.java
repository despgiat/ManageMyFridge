package com.example.managemyfridge;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipesFragment extends Fragment {

    private static final String RECIPEINFO = "recipe";
    private static final String FAVOURITE = "favourite";
    //private static final String INSTRUCTIONS = "instructions";
    //private static final String INGREDIENTS = "ingredients";


    private Recipe recipe;
   // private String recipeTitle;
   // private String recipeInstructions;
    //private Ingredient[] ingredients;

    RecyclerView.Adapter ingredientsAdapter;

    //Recipe recipe //The recipe in question, it will be retrieved from the database

    boolean favourite; //We will check from the database if it was marked as favourite by the user and we will display it as such
    MenuItem fave;
    Menu toolbar;

    public RecipesFragment() {
        // Required empty public constructor
    }


    public static RecipesFragment newInstance(String param1, Boolean param2) {
        RecipesFragment fragment = new RecipesFragment();
        Bundle args = new Bundle();
        args.putSerializable(RECIPEINFO, param1);
        args.putBoolean(FAVOURITE, param2);
        //args.putSerializable(INGREDIENTS, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recipe = (Recipe) getArguments().getSerializable(RECIPEINFO);
            favourite = getArguments().getBoolean(FAVOURITE);
            setHasOptionsMenu(true);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_recipes, container, false);
        TextView titleTextView = view.findViewById(R.id.recipeTitle);
        titleTextView.setText(recipe.get_recipename());

        TextView instructions = view.findViewById(R.id.instructionsTextView);
        instructions.setText(recipe.get_instructions());

        TextView ingredients = view.findViewById(R.id.ingredientsListTextView);
        ingredients.setText(recipe.get_ingredients());

        //RecyclerView ingredientsRecyclerView = view.findViewById(R.id.ingredientsRecyclerView);
        //LinearLayoutManager linearLayoutManagerIngredients = new LinearLayoutManager(this.getContext());

        //ingredientsRecyclerView.setLayoutManager(linearLayoutManagerIngredients);
        //ingredientsAdapter = new IngredientAdapter(recipe.get_ingredients());
        //ingredientsRecyclerView.setAdapter(ingredientsAdapter);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(recipe.get_recipename());

        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu items for use in the action bar
        inflater.inflate(R.menu.recipes_toobar_menu, menu);

        //TODO Check from the database if this recipe belongs to the user's favorites and display it accordingly (the heart icon should be filled )

        fave = menu.findItem(R.id.fave);


        if(favourite)
        {
            fave.setIcon(R.drawable.ic_fave_filled);
        }
        else
        {
            fave.setIcon(R.drawable.ic_fave_empty);
        }

        fave.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
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
        LoginScreen.user.addFavoriteRecipe(recipe.get_id());
        Toast.makeText(getContext(), "This recipe has been added to your favorites!", Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void removeFromFavourites()
    {
        fave.setIcon(R.drawable.ic_fave_empty);
        LoginScreen.user.removeFavoriteRecipe(recipe.get_id());
        Toast.makeText(getContext(), "This recipe has been removed from your favorites!", Toast.LENGTH_SHORT).show();

    }


}