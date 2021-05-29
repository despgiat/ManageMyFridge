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
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipesOverviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipesOverviewFragment extends Fragment {

    //here we load everything from the database
    //String[] recipeNames = {"Porkchops with honey", "Ceasar's Salad", "Chocolate Cake"};
    //String[] recipeDescriptions = {"Delicious porkchops with honey and chili sauce", "Ceasar's Salad with chicken and lettuce", "Decadent chocolate Cake with vanilla buttercream"};

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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecipesOverviewFragment.
     */
    // TODO: Rename and change types and number of parameters
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