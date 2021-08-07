package com.example.managemyfridge;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.security.keystore.StrongBoxUnavailableException;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.time.LocalDate;

public class ExpiredFragment extends Fragment {

    private Fridge fridge; //We need to access the fridge products
    private static final String FRIDGE = "fridge";
    TextView warning; //warning that displays that there are no expired products in the fridge at the moment
    HomeFragment.HomeFragmentListener activityCallback; //For communication with the activity
    RecyclerView.Adapter adapterFridgeItems;
    String currentDate;


    public static ExpiredFragment newInstance(Fridge fridge) {
        ExpiredFragment fragment = new ExpiredFragment();
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

        LocalDate now = LocalDate.now();
        currentDate = now.format(MainScreen.formatter);

        if (getArguments() != null) { //Gets the fridge from the MainScreen Activity
            fridge = (Fridge) getArguments().getSerializable(FRIDGE);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Expired");

        View view = inflater.inflate(R.layout.fragment_expired, container, false);

        warning = view.findViewById(R.id.noExpiredTextView);

        if(fridge.checkForExpiredAtDate(currentDate).size() == 0) //if there are no expired products at the moment, the warning will be displayed
        {
            warning.setVisibility(View.VISIBLE);
        }
        else
        {
            warning.setVisibility(View.GONE);
        }

        RecyclerView productsRecyclerView = view.findViewById(R.id.expiredProductsRecyclerView); //The recycler view for the products to be displayed as cards
        LinearLayoutManager linearLayoutManagerToday = new LinearLayoutManager(this.getContext());
        productsRecyclerView.setLayoutManager(linearLayoutManagerToday);

        TypedValue value = new TypedValue(); //To retrieve the secondary color variant from the attributes: https://stackoverflow.com/questions/17277618/get-color-value-programmatically-when-its-a-reference-theme
        getContext().getTheme().resolveAttribute(R.attr.colorSecondaryVariant, value, true);

        adapterFridgeItems = new EditableProductRecyclerAdapter(fridge.checkForExpiredAtDate(currentDate), value.data, this); //value.data is the color we want each card in the card view to be
        productsRecyclerView.setAdapter(adapterFridgeItems);

        return  view;
    }



}