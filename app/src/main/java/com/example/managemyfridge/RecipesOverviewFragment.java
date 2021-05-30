package com.example.managemyfridge;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 *Upon entering the desired products and clicking "Find recipes!", this fragment gets displayed in the screen.
 * It contains all of the
 */
public class RecipesOverviewFragment extends Fragment {

    TextView warning;
    RecyclerView.Adapter recipesAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RecipesOverviewFragment() {
        // Required empty public constructor
    }


    public static RecipesOverviewFragment newInstance(String param1, String param2) {
        RecipesOverviewFragment fragment = new RecipesOverviewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

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