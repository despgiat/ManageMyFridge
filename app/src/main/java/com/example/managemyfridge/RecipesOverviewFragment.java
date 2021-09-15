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

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "recipes";
    //private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private ArrayList<Recipe> mParam1;
    //private String mParam2;

    public RecipesOverviewFragment() {
        // Required empty public constructor
    }

    public static RecipesOverviewFragment newInstance(ArrayList<Recipe> recipes) {
        RecipesOverviewFragment fragment = new RecipesOverviewFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, recipes);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = (ArrayList<Recipe>) getArguments().getSerializable(ARG_PARAM1);

            //((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Recipes");
            //mParam2 = getArguments().getString(ARG_PARAM2);
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
        recipesAdapter = new ContentRecyclerAdapter(getContext(), this);
        recipesRecyclerView.setAdapter(recipesAdapter);

        // Inflate the layout for this fragment
        return view;
    }
}