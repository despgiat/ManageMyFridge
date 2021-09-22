package com.example.managemyfridge;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;

public class TipRecyclerAdapter extends RecyclerView.Adapter<TipRecyclerAdapter.ViewHolder>{

    //Content from the database
    Context context;
    Fragment fromFragment;

    //The below will be derived from the database
    MyDBHandler dbHandler;
    ArrayList<Tip> tipsData;

    //String[] recipeNames = {"Porkchops with honey", "Ceasar's Salad", "Chocolate Cake"};
    //String[] recipeDescriptions = {"Delicious porkchops with honey and chili sauce", "Ceasar's Salad with chicken and lettuce", "Decadent chocolate Cake with vanilla buttercream"};
    //Ingredient[] ingredients = {new Ingredient(1,1,  "flour", "3 1/4", "cups"), new Ingredient(2,1,  "flour", "3 1/4", "cups"), new Ingredient(3,1,  "flour", "3 1/4", "cups")};

    public TipRecyclerAdapter(Context context, Fragment fromFragment, ArrayList<Tip> tipsData)
    {
        this.context = context;
        this.fromFragment = fromFragment;
        dbHandler = new MyDBHandler(context, null, null, 1);
        //tipsData = dbHandler.getallTips();

        this.tipsData = tipsData;

    }

    @NonNull
    @Override
    public TipRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tips_card_layout, parent, false);
        return new TipRecyclerAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TipRecyclerAdapter.ViewHolder holder, int position) {
        holder.title.setText(tipsData.get(position).get_tipname());
        holder.description.setText(tipsData.get(position).get_description());

        boolean isFave = LoginScreen.user.getFavoriteTipsArray().contains(tipsData.get(position).get_id());

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
        return tipsData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView description;
        ImageView fave_icon;
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
                    bundle.putSerializable("tip", (Serializable) tipsData.get(position));
                    bundle.putBoolean("favourite", LoginScreen.user.getFavoriteTipsArray().contains(tipsData.get(position).get_id()));

                    //bundle.putString("title", tipsData.get(position).get_tipname());
                    //bundle.putString("description", tipsData.get(position).get_description());
                    //bundle.putBoolean("fave", LoginScreen.user.getFavoriteTipsArray().contains(position)); //True if this particular tip's id is in the favourites list or not

                    //bundle.putSerializable("ingredients", ingredients);

                    //Add the image and the ingredients list and we're set

                    TipsFragment fragment = new TipsFragment(); //Shows the content fragment, whether is it recipes or tips
                    fragment.setArguments(bundle);

                    fromFragment.getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.screen, fragment).addToBackStack(null).commit();

                }
            });

        }
    }
}
