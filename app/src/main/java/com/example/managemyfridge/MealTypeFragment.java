package com.example.managemyfridge;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MealTypeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MealTypeFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    ListView listView;
    String[] types = {"Breakfast", "Brunch", "Lunch", "Dinner", "Snack", "Dessert"};

    public MealTypeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MealTypeFragment.
     */

    public static MealTypeFragment newInstance(String param1, String param2) {
        MealTypeFragment fragment = new MealTypeFragment();
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
        View view = inflater.inflate(R.layout.fragment_meal_type, container, false);

        //types = getResources().getStringArray(R.array.meal_type);

        listView = view.findViewById(R.id.meal_type_list);
       // listView.setChoiceMode(CHOICE_MODE_MULTIPLE);
        listView.setAdapter(new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_list_item_multiple_choice, types));

        return view;
    }
}