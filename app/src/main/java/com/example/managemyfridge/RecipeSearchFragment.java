package com.example.managemyfridge;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static android.widget.AbsListView.CHOICE_MODE_MULTIPLE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipeSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeSearchFragment extends Fragment {

    String[] diet_prefs;
    String[] meal_type;

    //String[] ingredients;
    List<String> ingredientGroups;
    HashMap<String, List<String>> ingredients;

    HashMap<Integer, HashMap<Integer, Integer>> checkedStates;

    IngredientExpListAdapter adapter;

    MyDBHandler dbHandler;

    //Things I need to implement the find Recipes functionality
    //Get the diet prefs
    //Get the desired meal type
    //Get all the desired checked ingredients

    ArrayList<String> checkedDietPrefs;
    ArrayList<String> checkedMealTypes;
    ArrayList<String> checkedIngredients;

    //ArrayList<String> fridge;
    //String[] ingredients = new String[]{"Eggs", "Bacon", "Chicken", "Milk", "Yogurt", "Apples", "Feta cheese", "Chocolate Milk", "Juice", "Bell Peppers"};
    //String[] fridge = new String[]{"Mayonaise", "Mustard", "Chicken", "Yogurt", "Juice"};


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "fridge";
    private static final String ARG_PARAM2 = "param2";

    private Fridge fridge;

    RecyclerView diet_prefs_recyclerview;
    RecyclerView meal_type_recyclerview;

    CheckboxRecyclerAdapter dietprefsAdapter;
    CheckboxRecyclerAdapter mealtypeAdapter;

    //ListView diet_prefs_listView;
    ListView meal_type_listView;
    ExpandableListView ingredients_list;

    Button clearButton;
    Button importfromfridge;
    Button findRecipes;
    Button findAllRecipes;

    public RecipeSearchFragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment Recipes.
     */

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
            diet_prefs = getResources().getStringArray(R.array.diet_preference);
            meal_type = getResources().getStringArray(R.array.meal_type);
            ingredientGroups = Arrays.asList(getResources().getStringArray(R.array.ingredient_groups));

            ingredients = new HashMap<>();
            ingredients.put(ingredientGroups.get(0), Arrays.asList(getResources().getStringArray(R.array.dairy_group)));
            ingredients.put(ingredientGroups.get(1), Arrays.asList(getResources().getStringArray(R.array.meats_group)));
            ingredients.put(ingredientGroups.get(2), Arrays.asList(getResources().getStringArray(R.array.vegetables_group)));
            ingredients.put(ingredientGroups.get(3), Arrays.asList(getResources().getStringArray(R.array.fruits_group)));
            ingredients.put(ingredientGroups.get(4), Arrays.asList(getResources().getStringArray(R.array.grains_group)));
            ingredients.put(ingredientGroups.get(5), Arrays.asList(getResources().getStringArray(R.array.fish_group)));
            ingredients.put(ingredientGroups.get(6), Arrays.asList(getResources().getStringArray(R.array.legumes_group)));
            ingredients.put(ingredientGroups.get(7), Arrays.asList(getResources().getStringArray(R.array.condiments_group)));
            ingredients.put(ingredientGroups.get(8), Arrays.asList(getResources().getStringArray(R.array.dairy_alts_group)));
            ingredients.put(ingredientGroups.get(9), Arrays.asList(getResources().getStringArray(R.array.nuts_group)));

            dbHandler = new MyDBHandler(getActivity(), null, null, 1);

            checkedDietPrefs = new ArrayList<>();
            checkedMealTypes = new ArrayList<>();
            checkedIngredients = new ArrayList<>();

            //ingredients = getResources().getStringArray(R.array.types);
        }
    }

   /* @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        checkedStates = adapter.getGroup_checkedStates();
        outState.putSerializable("ingredient_checked_states", checkedStates);
    }

    */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_recipe_search, container, false);

        CheckBox none = view.findViewById(R.id.no_preference_checkbox);

        clearButton = (Button) view.findViewById(R.id.clearButton);
        importfromfridge = view.findViewById(R.id.fromFridgeButton);
        findRecipes = view.findViewById(R.id.findRecipesButton);
        findAllRecipes = view.findViewById(R.id.findAllRecipesButton);

        diet_prefs_recyclerview = view.findViewById(R.id.diet_prefs_recyclerview);
        meal_type_recyclerview = view.findViewById(R.id.meal_type_recyclerview);

        //diet_prefs_listView = view.findViewById(R.id.diet_prefs_list);
//
//       listView.setChoiceMode(CHOICE_MODE_MULTIPLE);
        LinearLayoutManager linearLayoutManagerPrefs = new LinearLayoutManager(this.getContext());
        diet_prefs_recyclerview.setLayoutManager(linearLayoutManagerPrefs);
        dietprefsAdapter = new CheckboxRecyclerAdapter(diet_prefs);
        diet_prefs_recyclerview.setAdapter(dietprefsAdapter);


        LinearLayoutManager linearLayoutManagerType = new LinearLayoutManager(this.getContext());
        meal_type_recyclerview.setLayoutManager(linearLayoutManagerType);
        mealtypeAdapter = new CheckboxRecyclerAdapter(meal_type);
        meal_type_recyclerview.setAdapter(mealtypeAdapter);


        //diet_prefs_listView.setAdapter(new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_list_item_multiple_choice, diet_prefs));
/*
        diet_prefs_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String item = (String) parent.getItemAtPosition(position);
                if(!checkedDietPrefs.contains(item))
                {
                    checkedDietPrefs.add(item);
                }
                else
                {
                    checkedDietPrefs.remove(item);
                }
            }
        });


        none.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(none.isChecked())
                {
                    diet_prefs_listView.clearChoices();
                    adapter.notifyDataSetChanged();
                    checkedDietPrefs.addAll(Arrays.asList(diet_prefs)); //If NONE diet preferences is checked, it means we want every type of recipe, there's no diet preference
                }
            }
        });



        meal_type_listView = view.findViewById(R.id.meal_type_list);
//        listView.setChoiceMode(CHOICE_MODE_MULTIPLE);
        meal_type_listView.setAdapter(new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_list_item_multiple_choice, meal_type));

        meal_type_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String item = (String) parent.getItemAtPosition(position);
                if(!checkedMealTypes.contains(item))
                {
                    checkedMealTypes.add(item);
                }
                else
                {
                    checkedMealTypes.remove(item);
                }
            }
        });

 */

        ingredients_list = view.findViewById(R.id.ingredient_expandablelist);

        adapter = new IngredientExpListAdapter(this.getContext(), ingredientGroups, ingredients);

        ingredients_list.setAdapter(adapter);

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ClearSelection();
            }
        });

        importfromfridge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ImportFromFridge();
            }
        });

        findRecipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<Recipe> foundRecipes = findRecipes();

                for(int i = 0; i < foundRecipes.size(); i++)
                {
                    System.out.println(foundRecipes.get(i).get_recipename());
                }

                    Bundle bundle;
                    bundle = new Bundle();
                    bundle.putSerializable("recipes", foundRecipes);
                    RecipesOverviewFragment fragment = new RecipesOverviewFragment(); //Shows the content fragment, whether is it recipes or tips
                    fragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.screen, fragment).addToBackStack(null).commit();
               // }
               // else
               // {
                 //   Toast.makeText(getActivity(), "Please choose at least one item of each list to proceed!", Toast.LENGTH_SHORT).show();
               // }

            }
        });

        findAllRecipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle;

                ArrayList<Recipe> recipes = LoginScreen.dbHandlerlog.getallRecipes();

                bundle = new Bundle();
                bundle.putSerializable("recipes", recipes);
                RecipesOverviewFragment fragment = new RecipesOverviewFragment(); //Shows the content fragment, whether is it recipes or tips
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.screen, fragment).addToBackStack(null).commit();

            }
        });

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Recipes");

        return view;
    }

   /* public void ClearSelection() {
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

    */

    //TODO The actual findRecipes function. This is a placeholder

    public ArrayList<Recipe> findRecipes()
    {
       // ArrayList<Recipe> foundRecipes = new ArrayList<>();

        //checkedIngredients = adapter.getAllChecked();

        ArrayList<String> prefs = dietprefsAdapter.getChecked();
        ArrayList<String> types = mealtypeAdapter.getChecked();

        ArrayList<Recipe> recipesFound = LoginScreen.dbHandlerlog.getallRecipesofCertainPref(prefs, types);

        System.out.println("RECIPES FOUND:");
        for(int i = 0; i < recipesFound.size(); i++)
        {
            System.out.println(recipesFound.get(i).get_recipename());
        }

        return recipesFound;

    }

   /*public ArrayList<Recipe> findRecipes()
    {
        ArrayList<Recipe> foundRecipes; //= new ArrayList<>();
        //ArrayList<Recipe> allRecipes = dbHandler.getallRecipes();

        ArrayList<String> prefs = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();
        types.add("Lunch");
        types.add("Snack");
        prefs.add("Vegan");
        prefs.add("Vegetarian");

        foundRecipes = dbHandler.getallRecipesofCertainPref(prefs,types);
        //System.out.println(foundRecipes.size());

        if(foundRecipes.size() > 0)
        {
            for(int i = 0; i < foundRecipes.size(); i++)
            {
                System.out.println(foundRecipes.get(i).get_recipename());
            }
        }
        else
        {
            System.out.println("No recipes found!");
        }


        return foundRecipes;
    }

    */

}