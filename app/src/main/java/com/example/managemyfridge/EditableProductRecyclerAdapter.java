package com.example.managemyfridge;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EditableProductRecyclerAdapter extends RecyclerView.Adapter<EditableProductRecyclerAdapter.ViewHolder>{
    private ArrayList<FridgeItem> productData;
    private int cardColor;

    public EditableProductRecyclerAdapter(ArrayList<FridgeItem> products, int cardColor)
    {
        productData = products;
        this.cardColor = cardColor;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_overview_editable_layout, parent, false);

        return new EditableProductRecyclerAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.itemName.setText(productData.get(position).getName());
        holder.itemExpiry.setText(productData.get(position).getExpiry());
        holder.itemOpened.setText(productData.get(position).isOpened() ? "Opened at " + (productData.get(position).getDayOpened()) : "");
        holder.itemType.setText("(" + "Other" + ")");
        holder.cardView.setBackgroundColor(cardColor);

        holder.deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Remove the item from the list -> And the fridge in general
                // RemoveItem(position);
                //productData.remove(position);
            }
        });

        holder.openItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.editItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

            }
        });


    }

    @Override
    public int getItemCount() {
        return productData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        TextView itemOpened;
        TextView itemType;
        TextView itemExpiry;
        Button deleteItem;
        Button editItem;
        Button openItem;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.editableNameTextView);
            itemOpened = itemView.findViewById(R.id.editableOpenedTextView);
            itemExpiry = itemView.findViewById(R.id.editableExpiryTextView);
            deleteItem = itemView.findViewById(R.id.deleteButton);
            editItem = itemView.findViewById(R.id.editButton);
            openItem = itemView.findViewById(R.id.openButton);
            itemType = itemView.findViewById(R.id.editableItemType);
            cardView = itemView.findViewById(R.id.productEditableCardLayout);

        }
    }
}
