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

    Context context;
    Fragment fromFragment; //The fragment which called the adapter

    MyDBHandler dbHandler;
    ArrayList<Tip> tipsData; //The list of tips to be displayed by the recycler view

    public TipRecyclerAdapter(Context context, Fragment fromFragment, ArrayList<Tip> tipsData)
    {
        this.context = context;
        this.fromFragment = fromFragment;
        dbHandler = new MyDBHandler(context, null, null, 1);
        this.tipsData = tipsData;

    }

    @NonNull
    @Override
    public TipRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tips_card_layout, parent, false);
        return new TipRecyclerAdapter.ViewHolder(v);
    }

    //The card's
    @Override
    public void onBindViewHolder(@NonNull TipRecyclerAdapter.ViewHolder holder, int position) {
        holder.title.setText(tipsData.get(position).get_tipname());
        holder.description.setText(tipsData.get(position).get_description());
        holder.imageView.setImageResource(R.drawable.ic_tips_idea_image); //The tip's thumbnail image

        //If the tip is a favourite, then a filled heart icon is also displayed at the top right corner of the card
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
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tip_title);
            description = itemView.findViewById(R.id.tip_subtitle);
            fave_icon = itemView.findViewById(R.id.fave_icon_card);
            imageView = itemView.findViewById(R.id.tipImage);

            /**
             * When a card is clicked, a new fragment which will display the tips's information (TipsFragment) is added to the fragments' stack
             * and is displayed on the screen
             */
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("tip", (Serializable) tipsData.get(position)); //Pumps the Tip's data and a boolean indicating whether if it's a favourite or not
                    bundle.putBoolean("favourite", LoginScreen.user.getFavoriteTipsArray().contains(tipsData.get(position).get_id()));

                    TipsFragment fragment = new TipsFragment();
                    fragment.setArguments(bundle);

                    fromFragment.getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.screen, fragment).addToBackStack(null).commit();

                }
            });

        }
    }
}
