package com.example.managemyfridge;

import android.app.Notification;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * ContentRecyclerAdapter is the Adapter which helps the recipes and the tips in the fragments RecipesOverviewFragment and
 * TipsOverviewFragment to be displayed as clickable cards.
 */


public class ContentRecyclerAdapter extends RecyclerView.Adapter<ContentRecyclerAdapter.ViewHolder>{

    //Content from the database
    Context context;
    Fragment fromFragment;
    ArrayList<Recipe> recipeData;

    //The below will be derived from the database
    //String[] recipeNames = {"Porkchops with honey", "Ceasar's Salad", "Chocolate Cake"};
    //String[] recipeDescriptions = {"Delicious porkchops with honey and chili sauce", "Ceasar's Salad with chicken and lettuce", "Decadent chocolate Cake with vanilla buttercream"};
    //Ingredient[] ingredients = {new Ingredient(1,1,  "flour", "3 1/4", "cups"), new Ingredient(2,1,  "flour", "3 1/4", "cups"), new Ingredient(3,1,  "flour", "3 1/4", "cups")};

    public ContentRecyclerAdapter(Context context, Fragment fromFragment, ArrayList<Recipe> recipeData)
    {
        this.context = context;
        this.fromFragment = fromFragment;
        this.recipeData = recipeData;
        System.out.println("WHOAH");
    }

    @NonNull
    @Override
    public ContentRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tips_card_layout, parent, false);
        return new ContentRecyclerAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ContentRecyclerAdapter.ViewHolder holder, int position) {
        holder.title.setText(recipeData.get(position).get_recipename());
        holder.description.setText(recipeData.get(position).get_instructions());

        boolean isFave = LoginScreen.user.getFavoriteRecipesArray().contains(recipeData.get(position).get_id());

        if(isFave)
        {
            holder.fave_icon.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.fave_icon.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return recipeData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView fave_icon;
        TextView title;
        TextView description;
        //ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tip_title);
            description = itemView.findViewById(R.id.tip_subtitle);
            fave_icon = itemView.findViewById(R.id.fave_icon_card);

            itemView.setOnClickListener(new View.OnClickListener() { //When a card is clicked, a new fragment is added to the stack
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    //Retrieve the data for this particular recipe from the database
                    //And load its components in the next screen

                    //replace screen and get to the recipe fragment

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("recipe", (Serializable) recipeData.get(position));
                    bundle.putBoolean("favourite", LoginScreen.user.getFavoriteRecipesArray().contains(recipeData.get(position).get_id()));
                    //bundle.putSerializable("ingredients", ingredients);

                    //Add the image and the ingredients list and we're set

                    RecipesFragment fragment = new RecipesFragment(); //Shows the content fragment, whether is it recipes or tips
                    fragment.setArguments(bundle);

                    fromFragment.getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.screen, fragment).addToBackStack(null).commit();

                }
            });

        }
    }
}
