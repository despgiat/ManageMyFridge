package com.example.managemyfridge;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * The IngredientAdapter helps the ingredients be displayed at the corrent format in the RecipeFragment.
 */

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {

    ArrayList<String> ingredients;

    public IngredientAdapter(ArrayList<String> ingredients)
    {
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public IngredientAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredients_layout, parent, false);
        return new IngredientAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientAdapter.ViewHolder holder, int position) {

        holder.ingredient.setText(ingredients.get(position));
        //holder.amount.setText(ingredients[position].get_quantity());
        //holder.unit.setText(ingredients[position].get_unit());
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView ingredient;
        //TextView unit;
        //TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredient = itemView.findViewById(R.id.ingredientTextView);
            //unit = itemView.findViewById(R.id.unitTextView);
            //name = itemView.findViewById(R.id.ingredientTextView);

        }
    }

}
