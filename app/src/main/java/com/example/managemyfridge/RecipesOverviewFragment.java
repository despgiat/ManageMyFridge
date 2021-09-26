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
 *Upon entering the desired products and clicking "Find recipes!", this fragment gets displayed in the screen.
 * Some of the recipes' basic information are displayed as clickable cards. Upon clicking, the user can see the whole recipe, as it is the RecipeFragment gets displayed
 */
public class RecipesOverviewFragment extends Fragment {

    //The recipes are retrieved here.
    ArrayList<Recipe> recipes;

    TextView warning; //No recipes found
    RecyclerView.Adapter recipesAdapter;

    private static final String ARG_PARAM1 = "recipes";

    public RecipesOverviewFragment() {
        // Required empty public constructor
    }

    public static RecipesOverviewFragment newInstance(ArrayList<Recipe> recipes) {
        RecipesOverviewFragment fragment = new RecipesOverviewFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, recipes);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recipes = (ArrayList<Recipe>) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Recipes");

        View view = inflater.inflate(R.layout.fragment_recipes_overview, container, false);
        RecyclerView recipesRecyclerView = view.findViewById(R.id.recipesFoundRecyclerView);
        LinearLayoutManager linearLayoutManagerToday = new LinearLayoutManager(this.getContext());

        recipesRecyclerView.setLayoutManager(linearLayoutManagerToday);
        recipesAdapter = new ContentRecyclerAdapter(getContext(), this, recipes);
        recipesRecyclerView.setAdapter(recipesAdapter);

        warning = view.findViewById(R.id.recipesWarningTextView);

        if(recipes.isEmpty())
        {
            warning.setVisibility(View.VISIBLE);
        }
        else
        {
            warning.setVisibility(View.GONE);
        }

        // Inflate the layout for this fragment
        return view;
    }
}