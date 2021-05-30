package com.example.managemyfridge;

import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * EditableProductsRecyclerAdapter is the Adapter which helps user's products in the fragments MyFridge and
 * ExpiredFragment to be displayed as cards containing the products' information and having three buttons which control
 * the user's actions on the products.
 */


public class EditableProductRecyclerAdapter extends RecyclerView.Adapter<EditableProductRecyclerAdapter.ViewHolder>{
    private ArrayList<Product> productData;
    private int cardColor;  //We want to control the card's color (ex. in the ExpiredFragment, the cards appear mustard yellow)
    Fragment fromFragment; //the fragment that is using the adapter.

    public EditableProductRecyclerAdapter(ArrayList<Product> products, int cardColor, Fragment fromFragment)
    {
        productData = products;
        this.cardColor = cardColor;
        this.fromFragment = fromFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_overview_editable_layout, parent, false);

        return new EditableProductRecyclerAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.itemName.setText(productData.get(position).getProductName());
        holder.itemExpiry.setText(productData.get(position).get_exdate());
        holder.itemOpened.setText(productData.get(position).get_opened().equals("yes") ? "Opened at " + (productData.get(position).get_DateofOpening()) : ""); //If the product is opened, it also displays the date it was opened,
        //otherwise displays nothing on this field
        holder.itemType.setText("(" + productData.get(position).get_prodtype() + ")");
        holder.cardView.setCardBackgroundColor(cardColor);

        /**
         * On click listeners for each of the card's three buttons
         */

        holder.deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Remove the item from the list -> And the fridge in general
                // RemoveItem(position);

                new AlertDialog.Builder(fromFragment.getContext()) //When deleting a product, an alert dialogue is displayed to the user.
                        .setTitle("Delete Product")
                        .setMessage("Are you sure you want to delete " + productData.get(position).getProductName() + "?")

                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ((MainScreen) fromFragment.getActivity()).deleteProduct(productData.get(position).getProductName(), fromFragment); //Implemented in the MainScreen Activity

                                productData.remove(position); //To remove each card immediately
                                notifyDataSetChanged(); //So that the recyclerView will be displayed again with the updated data

                            }
                        })

                        //Dismisses the dialog and does nothing else
                        .setNegativeButton(android.R.string.cancel, null)
                        .setIcon(R.drawable.ic_warning)
                        .show();


            }
        });

        holder.openItem.setOnClickListener(new View.OnClickListener() { //The product is marked as opened and the current Date is stored as the opening Date
            @Override
            public void onClick(View v) {
                System.out.println(productData.get(position).getID());
                ((MainScreen) fromFragment.getActivity()).openProduct(productData.get(position).getID(), fromFragment);
                notifyDataSetChanged();
            }
        });

        holder.editItem.setOnClickListener(new View.OnClickListener() { //The AddItemActivity is launched with its fields already filled with the products' information and the user is able to alter them.
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
