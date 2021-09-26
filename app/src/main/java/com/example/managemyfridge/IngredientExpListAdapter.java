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

/**
 * https://www.journaldev.com/9942/android-expandablelistview-example-tutorial
 * https://medium.com/@makkenasrinivasarao1/expandablelistview-with-checkbox-radiobutton-in-android-af9ec9d81ddf
 * The ExpandableList helps display the ingredients in the RecipeSearchFragment and organize them into categories to make it easier for the user to
 * choose them.
 */


public class IngredientExpListAdapter extends BaseExpandableListAdapter {

    Context context;
    List<String> expandableListTitle; //The group names
    HashMap<String, List<String>> expandableListDetails; //HashMap which has key the name of the group and value the List of the "Ingredients" for that group
    HashMap<Integer, HashMap<Integer, Integer>> group_checkedStates; //Integer is the group position, the Hashmap is the child's hashmap. Contains the checked states of every child of every group
    //Each group has its own hashmap of childs' id and checked state

    ArrayList<String> allChecked; //All of the checked items, regardless of their group

    public IngredientExpListAdapter(Context context, List<String> expandableListTitle, HashMap<String, List<String>> expandableListDetails)
    {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetails = expandableListDetails;

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

    /**
     * Controls how the group is displayed
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup viewGroup) {

        String listTitle = (String) getGroup(groupPosition);
        view = LayoutInflater.from(viewGroup.getContext()).inflate(android.R.layout.simple_expandable_list_item_1, viewGroup, false);

        TextView listTitleTextView = (TextView) view.findViewById(android.R.id.text1);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);
        return view;
    }

    /**
     * Each of the children is a checkbox
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup viewGroup) {

        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ingredient_list_item, viewGroup, false);

        String listChildTitle = (String) getChild(groupPosition, childPosition);
        CheckBox listCheckbox = (CheckBox) view.findViewById(R.id.ingredient_checkbox);
        listCheckbox.setText(listChildTitle);


        try
        {
            if (group_checkedStates != null)
                //if (checkedStates.size() > 0)
                    if((group_checkedStates.get(groupPosition)).get(childPosition) == 1)
                    {
                        listCheckbox.setChecked(true);
                    }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        /**
         * Controls the checking and the unchecking for each item in a child list.
         * */

        listCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Retreives the child map
                HashMap<Integer, Integer> childMap = group_checkedStates.get(groupPosition);
                if(childMap == null)
                {
                    childMap = new HashMap<>();
                }

                //If the item was checked, then it is added to the child map and the allChecked list, else, the item gets removed from the all checked and
                //its value changes to 0 in the HashMap (unchecked)
                if (listCheckbox.isChecked())
                {
                    childMap.put(childPosition, 1);
                    allChecked.add(listChildTitle);
                }
                else
                {
                    childMap.put(childPosition, 0);
                    allChecked.remove(listChildTitle);
                }

                //Updates the HashMap containing the states of all children in all groups
                group_checkedStates.put(groupPosition, childMap);
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

    public HashMap<Integer, HashMap<Integer, Integer>> getGroup_checkedStates() {
        return group_checkedStates;
    }


}
