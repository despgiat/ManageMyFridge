package com.example.managemyfridge;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
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

public class RecipeSearchFragment extends Fragment {

    String[] diet_prefs; //the app's supported diet preferences from strings.xml
    String[] meal_type; //the app's supported meal types from strings.xml

    List<String> ingredientGroups; //The ingredient group titles (for the ingredient expandable list adapter)
    HashMap<String, List<String>> ingredients; //the group titles and the list of ingredients HashMap (for the ingredient expandable list adapter)
    IngredientExpListAdapter adapter;

    MyDBHandler dbHandler;
    ArrayList<String> checkedDietPrefs; //All of the user's checked diet preferences
    ArrayList<String> checkedMealTypes; //All of the user's checked meal types
    ArrayList<String> checkedIngredients; //All of the user's checked ingredients

    RecyclerView diet_prefs_recyclerview;
    RecyclerView meal_type_recyclerview;
    CheckboxRecyclerAdapter dietprefsAdapter;
    CheckboxRecyclerAdapter mealtypeAdapter;

    ExpandableListView ingredients_list;

    Button findRecipes; //When clicked, it gets the user's input from the diet preference, meal type and ingredients lists and displays the appropriate recipes
    Button findAllRecipes; //When clicked, all of the database's recipes are displayed

    public RecipeSearchFragment() {
        // Required empty public constructor

    }


    public static RecipeSearchFragment newInstance() {
        RecipeSearchFragment fragment = new RecipeSearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {

            //Gets the string arrays from strings.xml
            diet_prefs = getResources().getStringArray(R.array.diet_preference);
            meal_type = getResources().getStringArray(R.array.meal_type);
            ingredientGroups = Arrays.asList(getResources().getStringArray(R.array.ingredient_groups));

            //For each of the ingredient groups, we retrieve the ingredients "subgroups" from strings.xml
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
            ingredients.put(ingredientGroups.get(10), Arrays.asList(getResources().getStringArray(R.array.baking_group)));

            dbHandler = new MyDBHandler(getActivity(), null, null, 1);

            checkedDietPrefs = new ArrayList<>();
            checkedMealTypes = new ArrayList<>();
            checkedIngredients = new ArrayList<>();

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_recipe_search, container, false);

        //Setting up text views
        findRecipes = view.findViewById(R.id.findRecipesButton);
        findAllRecipes = view.findViewById(R.id.findAllRecipesButton);

        diet_prefs_recyclerview = view.findViewById(R.id.diet_prefs_recyclerview);
        meal_type_recyclerview = view.findViewById(R.id.meal_type_recyclerview);

        //Setting up recycler views and pumping data to them
        LinearLayoutManager linearLayoutManagerPrefs = new LinearLayoutManager(this.getContext());
        diet_prefs_recyclerview.setLayoutManager(linearLayoutManagerPrefs);
        dietprefsAdapter = new CheckboxRecyclerAdapter(diet_prefs);
        diet_prefs_recyclerview.setAdapter(dietprefsAdapter);

        LinearLayoutManager linearLayoutManagerType = new LinearLayoutManager(this.getContext());
        meal_type_recyclerview.setLayoutManager(linearLayoutManagerType);
        mealtypeAdapter = new CheckboxRecyclerAdapter(meal_type);
        meal_type_recyclerview.setAdapter(mealtypeAdapter);

        //Sets up the ingredient expandable list
        ingredients_list = view.findViewById(R.id.ingredient_expandablelist);
        adapter = new IngredientExpListAdapter(this.getContext(), ingredientGroups, ingredients);
        ingredients_list.setAdapter(adapter);


        /**
         * The finding recipes button listeners.
         * findRecipes button gets the data from findRecipes() method, pumps them to the RecipesOverviewFragment so that the recipes are displayed and the
         * findAllRecipes button gets all the recipes from the database and inputs them to the RecipesOverviewFragment to have them displayed
         */
        findRecipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<Recipe> foundRecipes = findRecipes();

                if(foundRecipes != null)
                {
                    Bundle bundle;
                    bundle = new Bundle();
                    bundle.putSerializable("recipes", foundRecipes);
                    RecipesOverviewFragment fragment = new RecipesOverviewFragment(); //Shows the content fragment, whether is it recipes or tips
                    fragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.screen, fragment).addToBackStack(null).commit();
                }

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

    /**
     * Get's all of the user's input from the diet preferences, meal type and ingredients lists and calls the getallRecipesofCertainPref from
     * the database to find the appropriate recipes base on the above preferences.
     * If the input is invalid, it notifies the user with a Toast.
     * @return the list of recipes found in the database that comply with the user's preferences
     */

    public ArrayList<Recipe> findRecipes()
    {
        checkedDietPrefs = dietprefsAdapter.getChecked();
        checkedMealTypes = mealtypeAdapter.getChecked();
        checkedIngredients = adapter.getAllChecked();

        if(checkedDietPrefs.isEmpty() || checkedMealTypes.isEmpty() || checkedIngredients.isEmpty())
        {
            Toast.makeText(getActivity(), "You should choose at least one diet preference, one meal type and one ingredient to proceed!", Toast.LENGTH_SHORT).show();
            return null;
        }
        else
        {
            return LoginScreen.dbHandlerlog.getallRecipesofCertainPref(checkedDietPrefs, checkedMealTypes, checkedIngredients);
        }

    }

}