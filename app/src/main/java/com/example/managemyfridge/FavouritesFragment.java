package com.example.managemyfridge;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    ArrayList<Recipe> recipes;
    ArrayList<Tip> tips;

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

        RecyclerView recipesRecyclerView = view.findViewById(R.id.recipesFoundRecyclerView);
        LinearLayoutManager linearLayoutManagerToday = new LinearLayoutManager(this.getContext());

        recipesRecyclerView.setLayoutManager(linearLayoutManagerToday);

        ArrayList<Integer> recipe_ids = LoginScreen.user.getFavoriteRecipesArray();
        recipes = LoginScreen.dbHandlerlog.getallRecipes();

        //TODO PLACEHOLDER, TO ADD THE USER'S FAVOURITE RECIPES

        fav_recipesAdapter = new ContentRecyclerAdapter(getContext(), this, recipes);
        recipesRecyclerView.setAdapter(fav_recipesAdapter);


        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Favourites");

        return view;
    }
}