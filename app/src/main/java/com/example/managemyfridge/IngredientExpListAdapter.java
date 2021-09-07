package com.example.managemyfridge;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class IngredientExpListAdapter extends BaseExpandableListAdapter {

    Context context;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetails;

    public IngredientExpListAdapter(Context context, List<String> expandableListTitle, HashMap<String, List<String>> expandableListDetails)
    {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetails = expandableListDetails;
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

        view = LayoutInflater.from(viewGroup.getContext()).inflate(android.R.layout.simple_list_item_multiple_choice, viewGroup, false);


        String listChildTitle = (String) getChild(groupPosition, childPosition);
        TextView listTitleTextView = (TextView) view.findViewById(android.R.id.text1);
        //listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listChildTitle);

        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
