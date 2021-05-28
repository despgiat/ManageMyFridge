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

public class ContentRecyclerAdapter extends RecyclerView.Adapter<ContentRecyclerAdapter.ViewHolder>{

    //We will get the recipes from the database

    Context context;
    Fragment fromFragment;

    String[] recipeNames = {"Porkchops with honey", "Ceasar's Salad", "Chocolate Cake"};
    String[] recipeDescriptions = {"Delicious porkchops with honey and chili sauce", "Ceasar's Salad with chicken and lettuce", "Decadent chocolate Cake with vanilla buttercream"};

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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    //Retrieve the data for this particular recipe from the database
                    //And load its components in the next screen

                    //replace screen and get to the recipe fragment

                    RecipesFragment fragment = new RecipesFragment();
                    //Bundle args = new Bundle();
                    //args.putString("data", "This data has sent to FragmentTwo");
                    //fragment.setArguments(args);

                    fromFragment.getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.screen, fragment).addToBackStack(null).commit();
                    //transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    //transaction.addToBackStack(null);
                    //transaction.commit();

                }
            });

        }
    }
}
