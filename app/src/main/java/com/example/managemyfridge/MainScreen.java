package com.example.managemyfridge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MainScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    /**TODO: Make products appear as lists under the Text Views. If the lists are empty, the text views' text
     * will be "There are no products expriring today/tomorrow/soon." (DONE)
     * TODO: If there are not any expired products in the fridge, the yellow text view shall not appear (DONE)
     * TODO: If there are expired products in the fridge, in the navigation drawer the "expired items" choice will have
     * the "danger" icon displayed
     * TODO: If there are products expiring in the following days, display the crockpot message and the crockpot -> Which will lead to the recipes page
     **/

    private Fridge fridge;
    LocalDate currentDate;
    DateTimeFormatter formatter;
    ArrayList<FridgeItem> expireToday;
    ArrayList<FridgeItem> expireTomorrow;
    ArrayList<FridgeItem> expireSoon;
    boolean expiredItems;

    LinearLayout expiredWarning;
    DrawerLayout drawerLayout;
    NavigationView navigationView; //The navigation drawer panel
    AppBarConfiguration appBarConfiguration;
    NavController navController;

    RecyclerView.Adapter adapterToday;
    RecyclerView.Adapter adapterTomorrow;
    RecyclerView.Adapter adapterSoon;
    TextView todayTextView;
    TextView tomorrowTextView;
    TextView soonTextView;
    ImageView warningSign;
    View inflatedView;
    private Menu navigationDrawer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        /**----------Finding view IDs-------------**/
        expiredWarning = findViewById(R.id.linearLayoutWarning);
        todayTextView = findViewById(R.id.todayTextView);
        tomorrowTextView = findViewById(R.id.tomorrowTextView);
        soonTextView = findViewById(R.id.soonTextView);

        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawerLayout);

        RecyclerView expiredRecyclerView = findViewById(R.id.todayExpireRecyclerView);
        RecyclerView tomorrowRecyclerView = findViewById(R.id.tomorrowExpireRecyclerView);
        RecyclerView soonRecyclerView = findViewById(R.id.soonExpireRecyclerView);

        /**----------END OF Finding view IDs-------**/

        Toolbar myToolbar = findViewById(R.id.toolbar);

        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();

//       NavigationUI.setupWithNavController(navigationView, navController);


        inflatedView = navigationView.inflateHeaderView(R.layout.header_navigation_drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, myToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();
        navigationView.getMenu().getItem(0).setChecked(true);

        //When the activity is created it will search for the expired items in the fridge and make them appear in the recyclerview on the front screen
        //First get the currect date

        formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        fridge = new Fridge(); //Placeholder -> It will derive the fridge's insides from the database
        FridgeItem product1 = new FridgeItem();
        FridgeItem product2 = new FridgeItem();
        FridgeItem product3 = new FridgeItem();
        FridgeItem product4 = new FridgeItem();

        product1.setName("Milk"); //Placeholder for testing
        product1.setExpiry("16/05/2021");
        product1.setOpened(true);
        product2.setName("Eggs"); //Placeholder for testing
        product2.setExpiry("17/05/2021");
        product2.setOpened(false);
        product3.setName("Salami"); //Placeholder for testing
        product3.setExpiry("30/05/2021");
        product3.setOpened(true);
        product4.setName("Sweet Yoghurt"); //Placeholder for testing
        product4.setExpiry("29/05/2021");
        product4.setOpened(false);

        fridge.addItem(product1);
        fridge.addItem(product2);
        fridge.addItem(product3);
        fridge.addItem(product4);

        CheckFridge();

        // set a LinearLayoutManager with default orientation

        LinearLayoutManager linearLayoutManagerToday = new LinearLayoutManager(this);
        LinearLayoutManager linearLayoutManagerTomorrow = new LinearLayoutManager(this);
        LinearLayoutManager linearLayoutManagerSoon = new LinearLayoutManager(this);
        // set LayoutManager to RecyclerView
        expiredRecyclerView.setLayoutManager(linearLayoutManagerToday);
        tomorrowRecyclerView.setLayoutManager(linearLayoutManagerTomorrow);
        soonRecyclerView.setLayoutManager(linearLayoutManagerSoon);

        //Set my Adapter for the RecyclerView
        adapterToday = new RecyclerAdapter(expireToday);
        expiredRecyclerView.setAdapter(adapterToday);

        adapterTomorrow = new RecyclerAdapter(expireTomorrow);
        tomorrowRecyclerView.setAdapter(adapterTomorrow);

        adapterSoon = new RecyclerAdapter(expireSoon);
        soonRecyclerView.setAdapter(adapterSoon);
    }

    @Override
    public void onResume(){
        super.onResume();
        if(fridge.getExpiredItems().size() > 0)
        {
            expiredItems = true;
            expriredProductsWarning();

        }
        else
        {
            expiredWarning.setVisibility(View.INVISIBLE);
        }

    }

    public void addNewItem(View view)
    {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("Fridge", fridge);
        startActivityForResult(i, 2);
    }

    @Override
    protected void onActivityResult( int requestCode , int resultCode , Intent data ) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 2) && resultCode == RESULT_OK)
        {
            //Create toast when new item has been added
            Toast.makeText(this, "You have added a new product in your fridge", Toast.LENGTH_SHORT).show();
            fridge = (Fridge) data.getExtras().getSerializable("Fridge");

            System.out.println("Fridge Items");
            for (FridgeItem fridgeItem:fridge.getFridgeItems())
            {
                System.out.println(fridgeItem.getName());
            }

            System.out.println("Expired Items");
            for (FridgeItem fridgeItem:fridge.getExpiredItems())
            {
                System.out.println(fridgeItem.getName());
            }
        }
    }

    @Override
    public void onBackPressed() {
        // 5 - Handle back click to close menu
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        drawerLayout.close();
        return true;
    }

    public void expriredProductsWarning()
    {
        expiredWarning.setVisibility(View.VISIBLE);
    }

   /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation_drawer, menu);
        return true;
    }


    public boolean onPrepareOptionsMenu(Menu menu) {

        if(expiredItems)
        {
            menu.getItem(2).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_warning));
        }
        return true;
    }

    */

    public void CheckFridge()
    {
        currentDate = LocalDate.now();
        LocalDate tomorrow = currentDate.plusDays(1);
        LocalDate soon = currentDate.plusDays(2); //three days later

        String dateText = currentDate.format(formatter);

        expireToday = fridge.expiresAtDate(dateText); //Only the ones which expire at the currect Date will be added to the expired products
        expireTomorrow = fridge.expiresAtDate(tomorrow.format(formatter)); //Those that expire the next day
        expireSoon = fridge.expiresAtDate(soon.format(formatter)); //Products that expire in 2 days

        if(expireToday.isEmpty())
        {
            todayTextView.setText("There are no products expiring today!");
            todayTextView.setAllCaps(false);
            todayTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }
        else
        {
            todayTextView.setText("Today");
            expiredItems = true;
            fridge.AddExpired(expireToday);
        }

        if(expireTomorrow.isEmpty())
        {
            tomorrowTextView.setText("There are no products expiring tomorrow!");
            tomorrowTextView.setAllCaps(false);
            tomorrowTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }
        else
            tomorrowTextView.setText("Tomorrow");

        if(expireSoon.isEmpty())
        {
            soonTextView.setText("There are no products expiring soon!");
            soonTextView.setAllCaps(false);
            soonTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }
        else
            soonTextView.setText("Soon");



        System.out.println("Fridge Items");
        for (FridgeItem fridgeItem:fridge.getFridgeItems())
        {
            System.out.println(fridgeItem.getName());
        }

        System.out.println("Expired Items");
        for (FridgeItem fridgeItem:fridge.getExpiredItems())
        {
            System.out.println(fridgeItem.getName());
        }
    }


}