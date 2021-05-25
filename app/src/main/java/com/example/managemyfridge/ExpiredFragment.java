package com.example.managemyfridge;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExpiredFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExpiredFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "fridge_from_Activity";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private Fridge fridge;
    //private String mParam2;

    public ExpiredFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExpiredFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExpiredFragment newInstance(Fridge fridge) {
        ExpiredFragment fragment = new ExpiredFragment();
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
            fridge = (Fridge) getArguments().getSerializable("fridge_from_Activity");
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_expired, container, false);
        System.out.println("LALALALALALALALA");

        for (FridgeItem product:fridge.getExpiredItems())
        {
            System.out.println(product.getName());
        }

        return view;
    }
}