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

public class RecipesFragment extends Fragment {

    private static final String RECIPEINFO = "recipe";
    private static final String FAVOURITE = "favourite";

    private Recipe recipe; //The recipe's info which we want to display
    boolean favourite; //Is the recipe a favourite
    MenuItem fave; //the favourite button in the toolbar

    public RecipesFragment() {
        // Required empty public constructor
    }


    public static RecipesFragment newInstance(String param1, Boolean param2) {
        RecipesFragment fragment = new RecipesFragment();
        Bundle args = new Bundle();
        args.putSerializable(RECIPEINFO, param1);
        args.putBoolean(FAVOURITE, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recipe = (Recipe) getArguments().getSerializable(RECIPEINFO);
            favourite = getArguments().getBoolean(FAVOURITE);
            setHasOptionsMenu(true); //To display the toolbar menu in where the favourite button sits
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //Setting up the textviews with the recipe's information
        View view = inflater.inflate(R.layout.fragment_recipes, container, false);
        TextView titleTextView = view.findViewById(R.id.recipeTitle);
        titleTextView.setText(recipe.get_recipename());

        TextView instructions = view.findViewById(R.id.instructionsTextView);
        instructions.setText(recipe.get_instructions());

        TextView ingredients = view.findViewById(R.id.ingredientsListTextView);
        ingredients.setText(formatIngredients(recipe.get_ingredients())); //Formats the ingredients to be displayed correctly on screen

        TextView source = view.findViewById(R.id.recipe_sourceTextView);
        source.setText(recipe.get_source());

        TextView diet = view.findViewById(R.id.recipe_diet_textview);
        diet.setText(recipe.get_diet_pref());

        TextView type = view.findViewById(R.id.recipe_mealtype_textview);
        type.setText(recipe.get_recipetype());

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(recipe.get_recipename());

        return view;
    }

    /**
     * In the toolbar we add the "Favourite" button, to mark/unmark the recipe as favourite.
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu items for use in the action bar
        inflater.inflate(R.menu.recipes_toobar_menu, menu);

        //If the tip is a favourite, the displayed button will be a filled heart, otherwise, an empty heart
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

    /**
     * Methods to handle the adding/removing tips from favourites
     * They alter the button's icon, add/remove the tip to/from the list of favourite tips of the user and update the user in the database to save the changes
     */
    public void setAsFavourite()
    {
        fave.setIcon(R.drawable.ic_fave_filled);
        LoginScreen.user.addFavoriteRecipe(recipe.get_id());
        LoginScreen.dbHandlerlog.updateUser();
        Toast.makeText(getContext(), "This recipe has been added to your favorites!", Toast.LENGTH_SHORT).show();
        favourite = true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void removeFromFavourites()
    {
        fave.setIcon(R.drawable.ic_fave_empty);
        LoginScreen.user.removeFavoriteRecipe(recipe.get_id());
        LoginScreen.dbHandlerlog.updateUser();
        Toast.makeText(getContext(), "This recipe has been removed from your favorites!", Toast.LENGTH_SHORT).show();
        favourite = false;

    }

    /**
     * Uses a simple string formatter to format the ingredients string as the list of ingredients from the database are displayed at one text
     */
    public String formatIngredients(String ingredients)
    {
        return ingredients.replace(". ", "\n");
    }


}