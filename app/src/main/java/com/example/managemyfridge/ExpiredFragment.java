package com.example.managemyfridge;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.security.keystore.StrongBoxUnavailableException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.time.LocalDate;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExpiredFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExpiredFragment extends Fragment {

    private Fridge fridge; //We need to access all of the fridge items and we need to know the current Date
    private static final String FRIDGE = "fridge_from_Activity";
    TextView warning;
    HomeFragment.HomeFragmentListener activityCallback; //For communication with the activity
    RecyclerView.Adapter adapterFridgeItems;
    CardView cardView;
    String currentDate;


    public static ExpiredFragment newInstance(Fridge fridge) {
        ExpiredFragment fragment = new ExpiredFragment();

        Bundle args = new Bundle();
        args.putSerializable(FRIDGE, fridge);
        //args.putString(DATE, currentDate);
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

        if (getArguments() != null) {
            fridge = (Fridge) getArguments().getSerializable(FRIDGE);
            //currentDate = getArguments().getString(DATE);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_expired, container, false);
        activityCallback.UpdateData(fridge);

        warning = view.findViewById(R.id.noExpiredTextView);
        //cardView = (CardView) view.findViewById(R.id.productEditableCardLayout);


        if(fridge.checkForExpiredAtDate(currentDate).size() == 0) //We need to pass the currentDate to the fragment too
        {
            warning.setVisibility(View.VISIBLE);
        }
        else
        {
            warning.setVisibility(View.GONE);
        }

        RecyclerView productsRecyclerView = view.findViewById(R.id.expiredProductsRecyclerView);

        LinearLayoutManager linearLayoutManagerToday = new LinearLayoutManager(this.getContext());

        productsRecyclerView.setLayoutManager(linearLayoutManagerToday);
        adapterFridgeItems = new EditableProductRecyclerAdapter(fridge.checkForExpiredAtDate(currentDate), Color.parseColor("#FFFFC107"));
        productsRecyclerView.setAdapter(adapterFridgeItems);

        return  view;
    }


}