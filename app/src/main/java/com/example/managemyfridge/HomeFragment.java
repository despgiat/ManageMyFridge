package com.example.managemyfridge;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * The app's home screen. When the user logs into the app, the Home Fragment is displayed to them.
 * The HomeFragment (home screen) displays the products that are expiring at the current date, as well as the next day and two days later.
 * If there are expired items in the fridge (products that are expiring at the current date and the previous days) the fragment displays a warning
 * message.
 */

public class HomeFragment extends Fragment {

    private Fridge fridge; //We need to access all of the fridge items
    private String currentDate;
    private String tomorrowDate;

    private static final String FRIDGE = "fridge";
    HomeFragmentListener activityCallback; //For communication with the activity

    ArrayList<Product> expireToday; //We want to show the user which of the products in their fridge expire at the current date, one day later and two days later
    ArrayList<Product> expireTomorrow;
    ArrayList<Product> expireSoon;

    TextView todayTextView;
    TextView tomorrowTextView;
    TextView soonTextView;
    LinearLayout expiredWarning; //A warning on top of the screen to warn about expired products already in the fridge that need to be taken out

    RecyclerView.Adapter adapterToday; //Adapters for the recycler views to display the above ArrayLists of products
    RecyclerView.Adapter adapterTomorrow;
    RecyclerView.Adapter adapterSoon;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(Fridge fridge) {
        HomeFragment fragment = new HomeFragment();
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
            activityCallback = (HomeFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement HomeFragmentListener");
        }

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Retrieved the current date
        LocalDate now = LocalDate.now();
        currentDate = now.format(MainScreen.formatter);

        if (getArguments() != null) {
            fridge = (Fridge) getArguments().getSerializable(FRIDGE); //retrieves the fridge from the MainScreen Activity
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Home");

        View view = inflater.inflate(R.layout.home_fragment_layout, container, false);

        activityCallback.UpdateData(fridge);

        //Retrieves the system date and computes the next couple of days.
        LocalDate today = LocalDate.parse(currentDate, MainScreen.formatter);
        LocalDate tomorrow = today.plusDays(1);
        tomorrowDate = tomorrow.format(MainScreen.formatter);
        LocalDate soon = today.plusDays(2); //two days later

        //Assigning the ArrayLists
        expireToday = fridge.expiresAtDate(currentDate);
        expireTomorrow = fridge.expiresAtDate(tomorrow.format(MainScreen.formatter)); //Those that expire the next day
        expireSoon = fridge.expiresAtDate(soon.format(MainScreen.formatter)); //Products that expire in 2 days


        //Assigning the layout's views
        expiredWarning = view.findViewById(R.id.linearLayoutWarning);
        todayTextView = view.findViewById(R.id.todayTextView);
        tomorrowTextView = view.findViewById(R.id.tomorrowTextView);
        soonTextView = view.findViewById(R.id.soonTextView);

        CheckFridge();

        //Reclaring the Recycler Views and Adapters for the products to be displayed
        RecyclerView expiredRecyclerView = view.findViewById(R.id.todayExpireRecyclerView);
        RecyclerView tomorrowRecyclerView = view.findViewById(R.id.tomorrowExpireRecyclerView);
        RecyclerView soonRecyclerView = view.findViewById(R.id.soonExpireRecyclerView);

        LinearLayoutManager linearLayoutManagerToday = new LinearLayoutManager(this.getContext());
        LinearLayoutManager linearLayoutManagerTomorrow = new LinearLayoutManager(this.getContext());
        LinearLayoutManager linearLayoutManagerSoon = new LinearLayoutManager(this.getContext());

        // set LayoutManager to RecyclerView
        expiredRecyclerView.setLayoutManager(linearLayoutManagerToday);
        tomorrowRecyclerView.setLayoutManager(linearLayoutManagerTomorrow);
        soonRecyclerView.setLayoutManager(linearLayoutManagerSoon);


        //Sets my Adapters for the RecyclerView
        adapterToday = new RecyclerAdapter(expireToday);
        expiredRecyclerView.setAdapter(adapterToday);

        adapterTomorrow = new RecyclerAdapter(expireTomorrow);
        tomorrowRecyclerView.setAdapter(adapterTomorrow);

        adapterSoon = new RecyclerAdapter(expireSoon);
        soonRecyclerView.setAdapter(adapterSoon);


        // Inflate the layout for this fragment
        return view;

    }


    /**
     * Checks for expiring products at the current date, at the next day and two days later.
     * If there aren't any, updates the UI appropriately, notifying the user.
     */

    public void CheckFridge()
    {
        ArrayList<Product> products = fridge.checkForExpiredAtDate(currentDate); //The products that have expired up until the current date
        //We need its size to update the screen's UI accordingly

        if(expireToday.isEmpty()) //Controls the views' appearance and text depending on if there are products in the ArrayLists that expire today, tomorrow or soon
        {
            todayTextView.setText("There are no products expiring today!");
            todayTextView.setAllCaps(false);
            todayTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }
        else
        {
            todayTextView.setText("Today to Expire " + "("+ currentDate + ")");
            todayTextView.setAllCaps(true);
        }

        if(expireTomorrow.isEmpty())
        {
            tomorrowTextView.setText("There are no products expiring tomorrow!");
            tomorrowTextView.setAllCaps(false);
            tomorrowTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }
        else {
            tomorrowTextView.setAllCaps(true);
            tomorrowTextView.setText("Tomorrow to expire " + "("+ tomorrowDate + ")");
        }

        if(expireSoon.isEmpty())
        {
            soonTextView.setText("There are no products expiring soon!");
            soonTextView.setAllCaps(false);
            soonTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }
        else {
            soonTextView.setAllCaps(true);
            soonTextView.setText("Soon to expire");
        }

        if(products.size() > 0) //if there are expired products in the fridge, the linear layout containing the warning and the button become visible
        {
            expiredWarning.setVisibility(View.VISIBLE);
        }
        else
        {
            expiredWarning.setVisibility(View.GONE);
        }

    }


    //For the fragment - Activity communication
    public interface HomeFragmentListener {
        public void UpdateData(Fridge fridge);
    }


}
