package com.example.managemyfridge;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * CheckboxRecyclerAdapter is the adapter which helps display checkbox lists in the RecipeSearchFragment for the user's choices.
 */

public class CheckboxRecyclerAdapter extends RecyclerView.Adapter<CheckboxRecyclerAdapter.ViewHolder> {

    String[] choices; //The data to be displayed
    ArrayList<String> checked; //All of the checked checkboxes
    ArrayList<Integer> checkedIds; // ...and their ids

    public CheckboxRecyclerAdapter(String[] choices)
    {
        this.choices = choices;
        checked = new ArrayList<>();
        checkedIds = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_list_item, parent, false);
        return new CheckboxRecyclerAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.checkBox.setText(choices[position]);
    }

    @Override
    public int getItemCount() {
        return choices.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            checkBox = itemView.findViewById(R.id.ingredient_checkbox);

            /**
             * By clicking on an item, it gets added or removed from the list of checked items.
             */
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition(); //The position of the item which was clicked in the recycler view

                    if(!checkedIds.contains(position))
                    {
                        checkedIds.add(position);
                        checked.add((String) checkBox.getText());
                    }
                    else
                    {
                        checkedIds.remove((Integer) position);
                        checked.remove((String) checkBox.getText());
                    }

                }
            });

        }
    }

    public ArrayList<String> getChecked()
    {
        return checked;
    }
}
