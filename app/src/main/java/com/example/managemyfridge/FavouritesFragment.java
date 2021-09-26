package com.example.managemyfridge;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * The Fragment in which the user's favourite recipes and zero waste tips will appear for easy access
 * The recipes and tips will appear as clickable cards, just like the "Zero Waste Tips" and "Recipes" screens of the application.
 */

public class FavouritesFragment extends Fragment {

    ArrayList<Recipe> all_recipes; //All of the database's recipes
    ArrayList<Tip> all_tips; //All of the database's tips

    ArrayList<Integer> recipe_ids; //The ids of the user's favourite recipes
    ArrayList<Integer> tips_ids; //The ids of the user's favourite tips

    RecyclerView.Adapter fav_recipesAdapter;
    RecyclerView.Adapter fav_tipsAdapter;

    TextView recipesTextView;
    TextView tipsTextView;

    public FavouritesFragment() {
        // Required empty public constructor
    }

    public static FavouritesFragment newInstance(String param1, String param2) {
        FavouritesFragment fragment = new FavouritesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }

        recipe_ids = LoginScreen.user.getFavoriteRecipesArray();
        tips_ids = LoginScreen.user.getFavoriteTipsArray();
        all_recipes = LoginScreen.dbHandlerlog.getallRecipes();
        all_tips = LoginScreen.dbHandlerlog.getallTips();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favourites, container, false);

        recipesTextView = view.findViewById(R.id.fav_recipes_textview);
        tipsTextView = view.findViewById(R.id.faveTipsTextView);


        ArrayList<Recipe> recipes = new ArrayList<>(); //Temporary Arraylists to hold the user's favourite recipes and tips.
        ArrayList<Tip> tips = new ArrayList<>();

        /**
         * For each recipe in the user's favourites (recipe_ids), we look for the corresponding recipe id in the database to retrieve the particular recipe.
         * When found, the recipe is added to the temp recipes ArrayList which holds the user's favourite recipes as Recipe instances.
         */
            for(int i = 0; i < recipe_ids.size(); i++)
            {
                for(int j = 0; j < all_recipes.size(); j++)
                {
                    if(recipe_ids.get(i) == all_recipes.get(j).get_id())
                    {
                        recipes.add(all_recipes.get(j));
                    }
                }
            }


        /**
         * For each tip in the user's favourites (tips_ids), we look for the corresponding tip id in the database to retrieve the particular tip.
         * When found, the tip is added to the temp tips ArrayList which holds the user's favourite tips as Tip instances.
         */
            for(int i = 0; i < tips_ids.size(); i++)
            {
                for(int j = 0; j < all_tips.size(); j++)
                {
                    if(tips_ids.get(i) == all_tips.get(j).get_id())
                    {
                        tips.add(all_tips.get(j));
                    }
                }
            }

            //We check for the sizes of the user's favourite Recipes and Tips ids ArrayList to display the correct message
            if(recipe_ids.size() == 0)
            {
                recipesTextView.setText("You don't have any favourite recipes yet!");
            }
            else
            {
                recipesTextView.setText("Your favourite recipes:");
            }
            if(tips_ids.size() == 0)
            {
                tipsTextView.setText("You don't have any favourite tips yet!");
            }
            else
            {
                tipsTextView.setText("Your favourite tips:");
            }

        /**
         * Setting up the recycler views to display the recipes and the tips as cards
         */

        RecyclerView recipesRecyclerView = view.findViewById(R.id.faveRecipesRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        recipesRecyclerView.setLayoutManager(linearLayoutManager);
        fav_recipesAdapter = new ContentRecyclerAdapter(getContext(), this, recipes);
        recipesRecyclerView.setAdapter(fav_recipesAdapter);

        RecyclerView tipsRecyclerView = view.findViewById(R.id.faveTipsRecyclerView);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this.getContext());
        tipsRecyclerView.setLayoutManager(linearLayoutManager1);
        fav_tipsAdapter = new TipRecyclerAdapter(getContext(), this, tips);
        tipsRecyclerView.setAdapter(fav_tipsAdapter);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Favourites");

        return view;
    }
}