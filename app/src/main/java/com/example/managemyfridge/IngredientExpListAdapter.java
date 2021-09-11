package com.example.managemyfridge;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class IngredientExpListAdapter extends BaseExpandableListAdapter {

    Context context;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetails;
    HashMap<Integer, Integer> checkedStates; //Integer is the child's id, Boolean is whether it is checked or not
    HashMap<Integer, HashMap<Integer, Integer>> group_checkedStates; //Integer is the group position, the Hashmap is the child's hashmap
    ArrayList<String> allChecked;

    public IngredientExpListAdapter(Context context, List<String> expandableListTitle, HashMap<String, List<String>> expandableListDetails)
    {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetails = expandableListDetails;
        checkedStates = new HashMap<>();
       /* for(int i = 0; i < expandableListTitle.size(); i++)
        {
            for(int j = 0; j < expandableListDetails.get(i).size(); j++)
            {
                checkedStates.put(j, 0);
            }
            group_checkedStates.put(i, checkedStates);
        }

        */

        group_checkedStates = new HashMap<>();
        allChecked = new ArrayList<>();
    }


    @Override
    public int getGroupCount() {
        return expandableListTitle.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return expandableListDetails.get(expandableListTitle.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return expandableListTitle.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return expandableListDetails.get(expandableListTitle.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup viewGroup) {

        String listTitle = (String) getGroup(groupPosition);

        view = LayoutInflater.from(viewGroup.getContext()).inflate(android.R.layout.simple_expandable_list_item_1, viewGroup, false);

        TextView listTitleTextView = (TextView) view.findViewById(android.R.id.text1);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup viewGroup) {

        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ingredient_list_item, viewGroup, false);

        String listChildTitle = (String) getChild(groupPosition, childPosition);
        CheckBox listCheckbox = (CheckBox) view.findViewById(R.id.ingredient_checkbox);
        listCheckbox.setText(listChildTitle);

        try
        {
            if (group_checkedStates != null)
                if (checkedStates.size() > 0)
                    if((group_checkedStates.get(groupPosition)).get(childPosition) == 1)
                    {
                        listCheckbox.setChecked(true);
                    }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        listCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listCheckbox.isChecked())
                {
                    checkedStates.put(childPosition, 1);
                    allChecked.add(listChildTitle);
                    System.out.println("HERRO HERRO");
                }
                else
                {
                    checkedStates.put(childPosition, 0);
                    allChecked.remove(listChildTitle);
                    System.out.println("BAH BUY");
                }
                group_checkedStates.put(groupPosition, checkedStates);
                //notifyDataSetChanged();
            }
        });

        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public ArrayList<String> getAllChecked()
    {
        return allChecked;
    }
}
