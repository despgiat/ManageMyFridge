package com.example.managemyfridge;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

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

        /*LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        //String dateText = date.format(formatter);
         */

        holder.itemName.setText(productData.get(position).getProductName());
        holder.itemOpened.setText(productData.get(position).get_opened().equals("yes") ? "Opened at " + (productData.get(position).get_DateofOpening()) : "");
        /*holder.deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Click clicky clickety!");
            }
        });

         */

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
            //deleteItem = itemView.findViewById(R.id.deleteItemButton);
        }
    }
}
