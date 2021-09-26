package com.example.managemyfridge;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * The TipsFragment contains all of the information regarding a tip -> Its title, instructions and source.
 */
public class TipsFragment extends Fragment {

    public static final String FAVOURITE = "favourite";
    public static final String TIPINFO = "tip";
    private Tip tip;

    private String tipTitle;
    private String tipDescription;
    boolean favourite;
    MenuItem fave;

    public TipsFragment() {
        // Required empty public constructor
    }

    public static TipsFragment newInstance(String param1, Boolean param2) {
        TipsFragment fragment = new TipsFragment();
        Bundle args = new Bundle();
        args.putSerializable(TIPINFO, param1);
        args.putBoolean(FAVOURITE, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            tip = (Tip) getArguments().getSerializable(TIPINFO);
            tipTitle = tip.get_tipname();
            tipDescription = tip.get_description();
            favourite = getArguments().getBoolean(FAVOURITE);

            setHasOptionsMenu(true); //So that we can add the "Favourite" button to the toolbar

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_tips, container, false);

        TextView titleTextView = view.findViewById(R.id.tipTitleTextView);
        titleTextView.setText(tipTitle);

        TextView description = view.findViewById(R.id.tip_instructionsTextView);
        description.setText(tipDescription);

        TextView source = view.findViewById(R.id.tip_sourceTextView);
        source.setText(tip.get_source());

        //Sets the toolbar title to the tip's title
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(tipTitle);

        return view;
    }

    /**
     * In the toolbar we add the "Favourite" button, to mark/unmark the tip as favourite.
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.recipes_toobar_menu, menu);

        fave = menu.findItem(R.id.fave);

        //If the tip is a favourite, the displayed button will be a filled heart, otherwise, an empty heart
        if(favourite)
        {
            fave.setIcon(R.drawable.ic_fave_filled);
        }
        else
        {
            fave.setIcon(R.drawable.ic_fave_empty);
        }

        fave.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if(!favourite)
                {
                    setAsFavourite();
                }
                else
                {
                    removeFromFavourites();
                }

                return true;
            }
        });


        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * Methods to handle the adding/removing tips from favourites
     * They alter the button's icon, add/remove the tip to/from the list of favourite tips of the user and update the user in the database to save the changes
     */

    public void setAsFavourite()
    {
        fave.setIcon(R.drawable.ic_fave_filled);
        LoginScreen.user.addFavoriteTip(tip.get_id());
        LoginScreen.dbHandlerlog.updateUser();
        favourite = true;
        Toast.makeText(getContext(), "This recipe has been added to your favorites!", Toast.LENGTH_SHORT).show();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void removeFromFavourites()
    {
        fave.setIcon(R.drawable.ic_fave_empty);
        LoginScreen.user.removeFavoriteTip(tip.get_id());
        LoginScreen.dbHandlerlog.updateUser();
        favourite = false;
        Toast.makeText(getContext(), "This recipe has been removed from your favorites!", Toast.LENGTH_SHORT).show();
    }


}