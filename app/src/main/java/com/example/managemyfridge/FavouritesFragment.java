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

    //We will get this info from the user's class.

    private static final String FAV_RECIPES = "fav_recipes";
    private static final String FAV_TIPS = "fav_tips";

    ArrayList<Recipe> all_recipes;
    ArrayList<Tip> all_tips;

    ArrayList<Integer> recipe_ids;
    ArrayList<Integer> tips_ids;

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
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
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
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_favourites, container, false);

        recipesTextView = view.findViewById(R.id.fav_recipes_textview);
        tipsTextView = view.findViewById(R.id.faveTipsTextView);

        //The full catalog of recipes
        //The other implementation can be that the adapters can have the data themselves and the input can be the ids of the recipes we want to show each time

        ArrayList<Recipe> recipes = new ArrayList<>();
        ArrayList<Tip> tips = new ArrayList<>();

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


        //TODO PLACEHOLDER, TO ADD THE USER'S FAVOURITE RECIPES (DONE)


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