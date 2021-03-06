package com.example.managemyfridge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * The MyFridge fragment, which displays all of the user's fridge products as cards.
 */

public class MyFridge extends Fragment {

    Fridge fridge;
    private static final String FRIDGE = "fridge";
    ImageButton addItemButton;
    TextView warning; //The warning that there are no items in the fridge
    HomeFragment.HomeFragmentListener activityCallback; //For communication with the activity
    RecyclerView.Adapter adapterFridgeItems;

    public static MyFridge newInstance(Fridge fridge) {
        MyFridge fragment = new MyFridge();
        Bundle args = new Bundle();
        args.putSerializable(FRIDGE, fridge);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        try {
            activityCallback = (HomeFragment.HomeFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement HomeFragmentListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            fridge = (Fridge) getArguments().getSerializable(FRIDGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.my_fridge_fragment, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("My Fridge");

        addItemButton = view.findViewById(R.id.addItemButton);
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainScreen) getActivity()).addNewItem();
            } //Calls the addNewItem implemented at the MainScreen
        });

        warning = view.findViewById(R.id.noProductsTextView);

        if(fridge.getFridgeItems().size() == 0) //The warning is shown when there are no products in the fridge
        {
            warning.setVisibility(View.VISIBLE);
        }
        else
        {
            warning.setVisibility(View.GONE);
        }

        //The RecyclerView so that the products are displayed at cards
        RecyclerView productsRecyclerView = view.findViewById(R.id.fridgeProductsRecyclerView);
        LinearLayoutManager linearLayoutManagerToday = new LinearLayoutManager(this.getContext());

        TypedValue value = new TypedValue(); //To retrieve the primary color from the attributes
        getContext().getTheme().resolveAttribute(R.attr.colorPrimary, value, true); //https://stackoverflow.com/questions/45218271/how-to-get-an-attr-value-programmatically

        productsRecyclerView.setLayoutManager(linearLayoutManagerToday);
        adapterFridgeItems = new EditableProductRecyclerAdapter(fridge.getFridgeItems(), value.data, this); //value.data is the card's background color
        productsRecyclerView.setAdapter(adapterFridgeItems);

        return  view;
    }

}