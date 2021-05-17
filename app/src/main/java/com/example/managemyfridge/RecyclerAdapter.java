package com.example.managemyfridge;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private ArrayList<FridgeItem> productData;

    public RecyclerAdapter(ArrayList<FridgeItem> products)
    {
        productData = products;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_overview_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        /*LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        //String dateText = date.format(formatter);
         */

        holder.itemName.setText(productData.get(position).getName());
        holder.itemOpened.setText(productData.get(position).isOpened() ? "Opened" : "");
        //holder.deleteItem.setImageResource("@drawable/ic_delete_item");

    }

    @Override
    public int getItemCount() {
        return productData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        TextView itemOpened;
        ImageButton deleteItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.item_nameTextView);
            itemOpened = itemView.findViewById(R.id.item_openedTextView);
            deleteItem = itemView.findViewById(R.id.deleteItemButton);
        }
    }
}
