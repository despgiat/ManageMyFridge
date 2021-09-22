package com.example.managemyfridge;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Array;
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

    RecyclerView.Adapter fav_recipesAdapter;
    RecyclerView.Adapter fav_tipsAdapter;

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

            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_favourites, container, false);

        ArrayList<Integer> recipe_ids = LoginScreen.user.getFavoriteRecipesArray();
        ArrayList<Integer> tips_ids = LoginScreen.user.getFavoriteTipsArray();

        //The full catalog of recipes
        //The other implementation can be that the adapters can have the data themselves and the input can be the ids of the recipes we want to show each time
        all_recipes = LoginScreen.dbHandlerlog.getallRecipes();
        all_tips = LoginScreen.dbHandlerlog.getallTips();

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


        //TODO PLACEHOLDER, TO ADD THE USER'S FAVOURITE RECIPES


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