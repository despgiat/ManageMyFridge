package com.example.managemyfridge;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static android.widget.AbsListView.CHOICE_MODE_MULTIPLE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipeSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeSearchFragment extends Fragment {

    String[] ingredients;
    //ArrayList<String> fridge;
    //String[] ingredients = new String[]{"Eggs", "Bacon", "Chicken", "Milk", "Yogurt", "Apples", "Feta cheese", "Chocolate Milk", "Juice", "Bell Peppers"};
    //String[] fridge = new String[]{"Mayonaise", "Mustard", "Chicken", "Yogurt", "Juice"};


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "fridge";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private Fridge fridge;

    ListView listView;
    Button clearButton;
    Button importfromfridge;

    public RecipeSearchFragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment Recipes.
     */
    // TODO: Rename and change types and number of parameters
    public static RecipeSearchFragment newInstance(Fridge fridge) {
        RecipeSearchFragment fragment = new RecipeSearchFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, fridge);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            fridge = (Fridge) getArguments().getSerializable(ARG_PARAM1);

            ingredients = getResources().getStringArray(R.array.types);

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
//        listView.setChoiceMode(CHOICE_MODE_MULTIPLE);
        listView.setAdapter(new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_list_item_multiple_choice, ingredients));

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

        //We get all of the fridge's products and for every ingredient type, we check if there is one instance (product) of that type.

        ArrayList<Product> products = fridge.getFridgeItems();

        for (int i = 0; i < ingredients.length; i++) {

            for(Product product : products)
            {
                if(product.get_prodtype().equals(ingredients[i]))
                {
                    listView.setItemChecked(i, true);
                }
            }
        }
        listView.requestLayout();

    }
}