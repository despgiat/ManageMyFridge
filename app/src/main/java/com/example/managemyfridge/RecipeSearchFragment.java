package com.example.managemyfridge;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipeSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeSearchFragment extends Fragment {

    ArrayList<String> ingredients;
    ArrayList<String> fridge;
    //String[] ingredients = new String[]{"Eggs", "Bacon", "Chicken", "Milk", "Yogurt", "Apples", "Feta cheese", "Chocolate Milk", "Juice", "Bell Peppers"};
    //String[] fridge = new String[]{"Mayonaise", "Mustard", "Chicken", "Yogurt", "Juice"};


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ListView listView;
    Button clearButton;
    Button importfromfridge;

    public RecipeSearchFragment() {
        // Required empty public constructor

        ingredients = new ArrayList<>();
        fridge = new ArrayList<>();
        ingredients.add("Eggs");
        ingredients.add("Bacon");
        ingredients.add("Yogurt");
        ingredients.add("Chicken");
        ingredients.add("Beef");
        ingredients.add("Juice");
        ingredients.add("Apples");
        ingredients.add("Milk");

        fridge.add("Mustard");
        fridge.add("Mayonaise");
        fridge.add("Chicken");
        fridge.add("Yogurt");
        fridge.add("Bacon");
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Recipes.
     */
    // TODO: Rename and change types and number of parameters
    public static RecipeSearchFragment newInstance(String param1, String param2) {
        RecipeSearchFragment fragment = new RecipeSearchFragment();
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
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_recipe_search, container, false);
        clearButton = (Button) view.findViewById(R.id.clearButton);
        importfromfridge = view.findViewById(R.id.fromFridgeButton);

        listView = view.findViewById(R.id.ingredient_list);
        //listView.setChoiceMode(CHOICE_MODE_MULTIPLE);
        listView.setAdapter(new ArrayAdapter<String>(this.getContext(),
                android.R.layout.simple_list_item_multiple_choice, ingredients));

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearSelection();
            }
        });

        importfromfridge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImportFromFridge();
            }
        });


        return view;
    }

    public void ClearSelection() {
        listView.clearChoices();
        listView.requestLayout();
    }

    public void ImportFromFridge() {
        //Check which items are in the fridge
        for (int i = 0; i < ingredients.size(); i++) {
            if (fridge.contains(ingredients.get(i))) {
                listView.setItemChecked(i, true);
            }
        }
        listView.requestLayout();
    }
}