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

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private Fridge fridge; //We need to access all of the fridge items and we need to know the current Date
    private String currentDate;
    private String tomorrowDate;


    private static final String FRIDGE = "fridge";
    private static final String DATE = "date";
    HomeFragmentListener activityCallback; //For communication with the activity

    ArrayList<FridgeItem> expireToday;
    ArrayList<FridgeItem> expireTomorrow;
    ArrayList<FridgeItem> expireSoon;
    boolean expiredItems;

    // TODO: Rename and change types of parameters
    TextView todayTextView;
    TextView tomorrowTextView;
    TextView soonTextView;
    //ImageView warningSign;
    LinearLayout expiredWarning;
    //Button warning;

    RecyclerView.Adapter adapterToday;
    RecyclerView.Adapter adapterTomorrow;
    RecyclerView.Adapter adapterSoon;

    ImageButton addItemButton;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param fridge Parameter 1.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(Fridge fridge) {
        HomeFragment fragment = new HomeFragment();

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
            activityCallback = (HomeFragmentListener) context;
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

        LocalDate today = LocalDate.parse(currentDate, MainScreen.formatter);
        LocalDate tomorrow = today.plusDays(1);
        tomorrowDate = tomorrow.format(MainScreen.formatter);
        LocalDate soon = today.plusDays(2); //two and three days later (will be implemented later)

        fridge = new Fridge(); //Placeholder -> It will derive the fridge's insides from the database
        FridgeItem product1 = new FridgeItem();
        FridgeItem product2 = new FridgeItem();
        FridgeItem product3 = new FridgeItem();
        FridgeItem product4 = new FridgeItem();
        FridgeItem product5 = new FridgeItem();

        product1.setName("Milk"); //Placeholder for testing
        product1.setExpiry("29/05/2021");
        product1.setOpened(true);
        product1.setDayOpened("22/05/2021");

        product2.setName("Eggs"); //Placeholder for testing
        product2.setExpiry("29/05/2021");
        product2.setOpened(false);
        product3.setName("Salami"); //Placeholder for testing
        product3.setExpiry("31/05/2021");
        product3.setOpened(true);
        product3.setDayOpened("15/05/2021");

        product4.setName("Sweet Yoghurt"); //Placeholder for testing
        product4.setExpiry("30/05/2021");
        product4.setOpened(false);
        product5.setName("Chicken"); //Placeholder for testing
        product5.setExpiry("31/05/2021");
        product5.setOpened(true);
        product5.setDayOpened("20/05/2021");


        fridge.addItem(product1);
        fridge.addItem(product2);
        fridge.addItem(product3);
        fridge.addItem(product4);
        fridge.addItem(product5);

        if (getArguments() != null) {
            fridge = (Fridge) getArguments().getSerializable(FRIDGE);
            //currentDate = getArguments().getString(DATE);

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.home_fragment_layout, container, false);
        //warning = view.findViewById(R.id.warningExpiredButton);

        activityCallback.UpdateData(fridge);
        view.requestLayout();

        LocalDate today = LocalDate.parse(currentDate, MainScreen.formatter);
        LocalDate tomorrow = today.plusDays(1);
        LocalDate soon = today.plusDays(2); //two and three days later (will be implemented later)
        expireToday = fridge.expiresAtDate(currentDate); //Only the ones which expire at the currect Date will be added to the expired products
        expireTomorrow = fridge.expiresAtDate(tomorrow.format(MainScreen.formatter)); //Those that expire the next day
        expireSoon = fridge.expiresAtDate(soon.format(MainScreen.formatter)); //Products that expire in 2 days


        //fridge = (Fridge) getArguments().getSerializable(FRIDGE);
        //currentDate = getArguments().getString(DATE);

        expiredWarning = view.findViewById(R.id.linearLayoutWarning);
        todayTextView = view.findViewById(R.id.todayTextView);
        tomorrowTextView = view.findViewById(R.id.tomorrowTextView);
        soonTextView = view.findViewById(R.id.soonTextView);

        CheckFridge();

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

        /*for (FridgeItem product: fridge.getFridgeItems()) {
            System.out.println(product.getName());
        }

         */

        //Set my Adapter for the RecyclerView
        adapterToday = new RecyclerAdapter(expireToday);
        expiredRecyclerView.setAdapter(adapterToday);

        adapterTomorrow = new RecyclerAdapter(expireTomorrow);
        tomorrowRecyclerView.setAdapter(adapterTomorrow);

        adapterSoon = new RecyclerAdapter(expireSoon);
        soonRecyclerView.setAdapter(adapterSoon);

        /*addItemButton = view.findViewById(R.id.itemActions);
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainScreen) getActivity()).addNewItem();
            }
        });

         */


        // Inflate the layout for this fragment
        return view;

    }

   /* public void addNewItem(View view)
    {
        Intent i = new Intent(this.getActivity(), MainActivity.class);
        i.putExtra("Fridge", fridge);
        startActivityForResult(i, 2);
    }

    */

    public void CheckFridge()
    {
        ArrayList<FridgeItem> products = fridge.checkForExpiredAtDate(currentDate);
        /*for (FridgeItem product: products)
        {
            if(!fridge.getExpiredItems().contains(product))
                fridge.AddExpired(product);
        }

         */

        //expiredItems = false;
        //fridge.AddExpired(fridge.checkForExpiredAtDate(currentDate));

        if(expireToday.isEmpty())
        {
            todayTextView.setText("There are no products expiring today!");
            todayTextView.setAllCaps(false);
            todayTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }
        else
        {
            todayTextView.setText("Today to Expire " + "("+ currentDate + ")");
            todayTextView.setAllCaps(true);
            expiredItems = true;
            //fridge.AddExpired(expireToday);
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

        if(products.size() > 0) //fridge.getExpiredItems().size() > 0
        {
            expiredItems = true;
            expiredWarning.setVisibility(View.VISIBLE);
            //expriredProductsWarning();
        }
        else
        {
            expiredItems = false;
            expiredWarning.setVisibility(View.GONE);
        }
        //expiredWarning.setVisibility(View.INVISIBLE);


/*
        for (FridgeItem fridgeItem:fridge.getFridgeItems())
        {
            System.out.println("Fridge Items");
            System.out.println(fridgeItem.getName());
        }


        for (FridgeItem fridgeItem:fridge.getExpiredItems())
        {
            System.out.println("Expired Items");
            System.out.println(fridgeItem.getName());
        }

 */
    }


    public interface HomeFragmentListener {
        public void UpdateData(Fridge fridge);
    }

    /*
    @Override
    public void onActivityResult( int requestCode , int resultCode , Intent data ) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 2) && resultCode == getActivity().RESULT_OK) {
            //Create toast when new item has been added
            Toast.makeText(this.getContext(), "You have added a new product in your fridge", Toast.LENGTH_SHORT).show();
            fridge = (Fridge) data.getExtras().getSerializable("Fridge");

            System.out.println("Fridge Items");
            for (FridgeItem fridgeItem : fridge.getFridgeItems()) {
                System.out.println(fridgeItem.getName());
            }

            System.out.println("Expired Items");
            for (FridgeItem fridgeItem : fridge.getExpiredItems()) {
                System.out.println(fridgeItem.getName());
            }
        }
        else
        {
            System.out.println("Something went wrong");
        }
    }

     */

    public void GetFridgeFromMain(Fridge fridge)
    {
        this.fridge = fridge;
    }

}
