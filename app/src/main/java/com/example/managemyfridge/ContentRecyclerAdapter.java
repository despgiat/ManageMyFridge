package com.example.managemyfridge;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

/**
 * ContentRecyclerAdapter is the Adapter which helps the recipes and the tips in the fragments RecipesOverviewFragment and
 * TipsOverviewFragment to be displayed as clickable cards.
 */


public class ContentRecyclerAdapter extends RecyclerView.Adapter<ContentRecyclerAdapter.ViewHolder>{

    //We will get the recipes from the database

    Context context;
    Fragment fromFragment;

    String[] recipeNames = {"Porkchops with honey", "Ceasar's Salad", "Chocolate Cake"};
    String[] recipeDescriptions = {"Delicious porkchops with honey and chili sauce", "Ceasar's Salad with chicken and lettuce", "Decadent chocolate Cake with vanilla buttercream"};
    Ingredient[] ingredients = {new Ingredient(1, "flour", 3, "cups"), new Ingredient(2, "flour", 3, "cups"), new Ingredient(3, "flour", 3, "cups")};

    public ContentRecyclerAdapter(Context context, Fragment fromFragment)
    {
        this.context = context;
        this.fromFragment = fromFragment;
    }

    @NonNull
    @Override
    public ContentRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tips_card_layout, parent, false);
        return new ContentRecyclerAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ContentRecyclerAdapter.ViewHolder holder, int position) {
        holder.title.setText(recipeNames[position]);
        holder.description.setText(recipeDescriptions[position]);
    }

    @Override
    public int getItemCount() {
        return recipeNames.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView description;
        //ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tip_title);
            description = itemView.findViewById(R.id.tip_subtitle);

            itemView.setOnClickListener(new View.OnClickListener() { //When a card is clicked, a new fragment is added to the stack
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    //Retrieve the data for this particular recipe from the database
                    //And load its components in the next screen

                    //replace screen and get to the recipe fragment

                    Bundle bundle = new Bundle();
                    bundle.putString("title", recipeNames[position]);
                    bundle.putString("instructions", recipeDescriptions[position]);
                    bundle.putSerializable("ingredients", ingredients);

                    //Add the image and the ingredients list and we're set

                    RecipesFragment fragment = new RecipesFragment(); //Shows the content fragment, whether is it recipes or tips
                    fragment.setArguments(bundle);

                    fromFragment.getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.screen, fragment).addToBackStack(null).commit();

                }
            });

        }
    }
}
