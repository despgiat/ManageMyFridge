package com.example.managemyfridge;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.w3c.dom.ls.LSOutput;

import java.util.ArrayList;

/**
 * In the TipsOverviewFragment the user is able to see the array of tips saved in the database in the form of clickable cards.
 */
public class TipsOverviewFragment extends Fragment {

    //Load articles from database
    ArrayList<Tip> all_tips;
    RecyclerView.Adapter tipsAdapter;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public TipsOverviewFragment() {
        // Required empty public constructor
    }

    public static TipsOverviewFragment newInstance(String param1, String param2) {
        TipsOverviewFragment fragment = new TipsOverviewFragment();
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
        View view = inflater.inflate(R.layout.fragment_tips_overview, container, false);

        all_tips = LoginScreen.dbHandlerlog.getallTips();

        //Recycler view for the display of tips
        RecyclerView tipsRecyclerView = view.findViewById(R.id.tipsCardView);
        LinearLayoutManager linearLayoutManagerToday = new LinearLayoutManager(this.getContext());

        tipsRecyclerView.setLayoutManager(linearLayoutManagerToday);
        tipsAdapter = new TipRecyclerAdapter(getContext(), this, all_tips);
        tipsRecyclerView.setAdapter(tipsAdapter);

        System.out.println(tipsAdapter.getItemCount());

        // Inflate the layout for this fragment

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Zero Waste Tips");

        return view;
    }
}