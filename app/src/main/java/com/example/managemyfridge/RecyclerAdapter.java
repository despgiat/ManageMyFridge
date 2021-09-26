package com.example.managemyfridge;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * The RecylerAdapter to display the products going to expire in the current and following days in the home screen.
 * It shows the product's name and the date that it was opened, if it was opened.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private ArrayList<Product> productData;

    public RecyclerAdapter(ArrayList<Product> products)
    {
        productData = products;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_overview_home_layout, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.itemName.setText(productData.get(position).getProductName());
        holder.itemOpened.setText(productData.get(position).get_opened().equals("yes") ? "Opened at " + (productData.get(position).get_DateofOpening()) : ""); //If the product was opened, the card displays the opening date as well
    }

    @Override
    public int getItemCount() {
        return productData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        TextView itemOpened;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.item_nameTextView);
            itemOpened = itemView.findViewById(R.id.item_openedTextView);
        }
    }
}
