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
 * ContentRecyclerAdapter is the Adapter which helps the recipes in the fragment RecipesOverviewFragment to be displayed as clickable cards.
 */


public class ContentRecyclerAdapter extends RecyclerView.Adapter<ContentRecyclerAdapter.ViewHolder>{

    Context context;
    Fragment fromFragment; //The fragment which contains the recycler view
    ArrayList<Recipe> recipeData;

    public ContentRecyclerAdapter(Context context, Fragment fromFragment, ArrayList<Recipe> recipeData)
    {
        this.context = context;
        this.fromFragment = fromFragment;
        this.recipeData = recipeData;
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

        /**
         * Depending on the recipe type (Breakfast, Lunch etc...), the image view displays the correct "thumbnail" image for each recipe
         */
        String meal_type = recipeData.get(position).get_recipetype();
        switch (meal_type)
        {
            case "Breakfast":
                holder.imageView.setImageResource(R.drawable.ic_breakfast);
                break;
            case "Brunch":
                holder.imageView.setImageResource(R.drawable.ic_brunch);
                break;
            case "Lunch":
                holder.imageView.setImageResource(R.drawable.ic_lunch);
                break;
            case "Dinner":
                holder.imageView.setImageResource(R.drawable.ic_dinner);
                break;
            case "Snack":
                holder.imageView.setImageResource(R.drawable.ic_snack);
                break;
            case "Dessert":
                holder.imageView.setImageResource(R.drawable.ic_cake);
                break;
        }

        /**
         * If this particular recipe is favoured by the user, then a filled heart icon also gets displayed on the card at the top right corner
         */
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
        ImageView imageView; //the recipe's thumbnail in the card

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tip_title);
            description = itemView.findViewById(R.id.tip_subtitle);
            fave_icon = itemView.findViewById(R.id.fave_icon_card);
            imageView = itemView.findViewById(R.id.tipImage);

            /**
             * When a card is clicked, a new fragment which will display the recipe's information (Recipe Fragment) is added to the fragments' stack
             * and is displayed on the screen
             */

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("recipe", (Serializable) recipeData.get(position));
                    bundle.putBoolean("favourite", LoginScreen.user.getFavoriteRecipesArray().contains(recipeData.get(position).get_id()));

                    RecipesFragment fragment = new RecipesFragment();
                    fragment.setArguments(bundle);

                    fromFragment.getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.screen, fragment).addToBackStack(null).commit();

                }
            });

        }
    }
}
