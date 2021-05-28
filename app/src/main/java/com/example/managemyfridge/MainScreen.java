package com.example.managemyfridge;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MainScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, HomeFragment.HomeFragmentListener{

    /**TODO: Make products appear as lists under the Text Views. If the lists are empty, the text views' text
     * will be "There are no products expriring today/tomorrow/soon." (DONE)
     * TODO: If there are not any expired products in the fridge, the yellow text view shall not appear (DONE)
     * TODO: If there are expired products in the fridge, in the navigation drawer the "expired items" choice will have
     * the "danger" icon displayed
     * TODO: If there are products expiring in the following days, display the crockpot message and the crockpot -> Which will lead to the recipes page
     **/

    //Ok, let's make it all over again, from scratch!
    //We want the MainActivty (MainScreen as it is called here, to handle the communication between the fragments
    // and the navigation drawer choices (and other general things in the app))

    private Fridge fridge; //--> We do not need this then (it will be loaded in the fragment) (Or we do, to pass it to other fragments)
    /*new Fridge(new ArrayList<FridgeItem>(){{add(new FridgeItem("Milk", false, "19/05/2021", ""));
            add(new FridgeItem("Eggs", false, "19/05/2021", "")); new FridgeItem("Salami", true, "20/05/2021", "13/05/2021");}} , new ArrayList<FridgeItem>(){{add(new FridgeItem("Milk", false, "19/05/2021", ""));
            add(new FridgeItem("Eggs", false, "19/05/2021", ""));}});

     */
    LocalDate currentDate;
    public static DateTimeFormatter formatter;

    /*ArrayList<FridgeItem> expireToday;
    ArrayList<FridgeItem> expireTomorrow;
    ArrayList<FridgeItem> expireSoon;
    boolean expiredItems;

     */

    //LinearLayout expiredWarning;
    DrawerLayout drawerLayout;
    NavigationView navigationView; //The navigation drawer panel
    //AppBarConfiguration appBarConfiguration;
    //NavController navController; //Not needed as the choices are handled manually

    //RecyclerView.Adapter adapterToday;
    //RecyclerView.Adapter adapterTomorrow;
    //RecyclerView.Adapter adapterSoon;
    //TextView todayTextView;
    //TextView tomorrowTextView;
    //TextView soonTextView;
    //ImageView warningSign;
    View inflatedView;
    ImageView profilePicture;
    private Menu navigationDrawer;

    //Fragments:
    Fragment activeScreen;
    //HomeFragment homeFragment;
    //ExpiredFragment expiredFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        currentDate = LocalDate.now();

        /**----------Finding view IDs-------------**/
        //expiredWarning = findViewById(R.id.linearLayoutWarning);
        //todayTextView = findViewById(R.id.todayTextView);
        //tomorrowTextView = findViewById(R.id.tomorrowTextView);
        //soonTextView = findViewById(R.id.soonTextView);

        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawerLayout);

        //RecyclerView expiredRecyclerView = findViewById(R.id.todayExpireRecyclerView);
        //RecyclerView tomorrowRecyclerView = findViewById(R.id.tomorrowExpireRecyclerView);
        //RecyclerView soonRecyclerView = findViewById(R.id.soonExpireRecyclerView);

        /**----------END OF Finding view IDs-------**/

        Toolbar myToolbar = findViewById(R.id.toolbar);

        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();

//       NavigationUI.setupWithNavController(navigationView, navController);


        inflatedView = navigationView.inflateHeaderView(R.layout.header_navigation_drawer);
        profilePicture = (ImageView) inflatedView.findViewById(R.id.imageViewProfilePic);
        registerForContextMenu(profilePicture);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, myToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();
        navigationView.getMenu().getItem(0).setChecked(true);

        //When the activity is created it will search for the expired items in the fridge and make them appear in the recyclerview on the front screen
        //First get the currect date

        formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");



        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if(getSupportFragmentManager().getBackStackEntryCount() > 0){
                    toggle.setDrawerIndicatorEnabled(false);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                    toggle.setToolbarNavigationClickListener(new View.OnClickListener() { //https://stackoverflow.com/questions/17258020/switching-between-android-navigation-drawer-image-and-up-caret-when-using-fragme
                        @Override
                        public void onClick(View v) {
                            getSupportFragmentManager().popBackStack();
                        }
                    });

                }
                else {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    toggle.setDrawerIndicatorEnabled(true);
                }
            }
        });



        /*getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() { //https://stackoverflow.com/questions/48185871/navigation-drawer-back-button-in-fragments
            @Override
            public void onBackStackChanged() {
                if(getSupportFragmentManager().getBackStackEntryCount() == 0){
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    toggle.setDrawerIndicatorEnabled(true);

                }else{
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    toggle.setDrawerIndicatorEnabled(false);


                }
            }
        });

         */


        ;

       /* fridge = new Fridge(); //Placeholder -> It will derive the fridge's insides from the database
        FridgeItem product1 = new FridgeItem();
        FridgeItem product2 = new FridgeItem();
        FridgeItem product3 = new FridgeItem();
        FridgeItem product4 = new FridgeItem();

        product1.setName("Milk"); //Placeholder for testing
        product1.setExpiry("19/05/2021");
        product1.setOpened(true);
        product2.setName("Eggs"); //Placeholder for testing
        product2.setExpiry("19/05/2021");
        product2.setOpened(false);
        product3.setName("Salami"); //Placeholder for testing
        product3.setExpiry("20/05/2021");
        product3.setOpened(true);
        product4.setName("Sweet Yoghurt"); //Placeholder for testing
        product4.setExpiry("21/05/2021");
        product4.setOpened(false);

        fridge.addItem(product1);
        fridge.addItem(product2);
        fridge.addItem(product3);
        fridge.addItem(product4);

        */



       /* Bundle bundle = new Bundle();
        bundle.putSerializable("fridge", fridge);
        bundle.putString("date", currentDate.format(formatter));

        */

        //myFragment.setArguments(bundle);

        //CheckFridge(); //-> The main activity should look for expired products once when app is launched and continuously
        //(when it is resumed for ex. after we add a new product and it was expired)

        // set a LinearLayoutManager with default orientation

        //LinearLayoutManager linearLayoutManagerToday = new LinearLayoutManager(this);
        //LinearLayoutManager linearLayoutManagerTomorrow = new LinearLayoutManager(this);
        //LinearLayoutManager linearLayoutManagerSoon = new LinearLayoutManager(this);
        // set LayoutManager to RecyclerView
        //expiredRecyclerView.setLayoutManager(linearLayoutManagerToday);
        //tomorrowRecyclerView.setLayoutManager(linearLayoutManagerTomorrow);
        //soonRecyclerView.setLayoutManager(linearLayoutManagerSoon);

        //Set my Adapter for the RecyclerView
       // adapterToday = new RecyclerAdapter(expireToday);
        //expiredRecyclerView.setAdapter(adapterToday);

        //adapterTomorrow = new RecyclerAdapter(expireTomorrow);
        //tomorrowRecyclerView.setAdapter(adapterTomorrow);

        //adapterSoon = new RecyclerAdapter(expireSoon);
        //soonRecyclerView.setAdapter(adapterSoon);

        //homeFragment = new HomeFragment();
        //expiredFragment = new ExpiredFragment();

        /**
         * When there are expired products, the HomeFragment will send the expires Items arraylist to this activity,
         * and this activity will send the ExpiredFragment the data
         */

        if(savedInstanceState == null)
        {
            //HomeFragment home = new HomeFragment();
            //home.setArguments(bundle);
            //homeFragment = new HomeFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.screen, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.homeItem);
        }

    }


    @Override
    public void onResume()
    {
        super.onResume();




       /* if(fridge.getExpiredItems().size() > 0)
        {
            //expiredItems = true;
            //expriredProductsWarning();
        }
        else
        {
            //expiredItems = false;
        }
        //expiredWarning.setVisibility(View.INVISIBLE);

        */


    }

    //This will be accessed from the Home Fragment and the MyFridge Fragment, so it should be implemented here
    public void addNewItem()
    {
        System.out.println("ADD NEW ITEM!!!");
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

            //Updates all of the fragments with the new fridge
            //Since it will be called only from MyFridge, it will return there

            Bundle bundle = new Bundle(); //Sends the fridge back to the fragment (It doesn't unfortunately...) Let's fix that real quick
            bundle.putSerializable("fridge", fridge);
            MyFridge myFridge = new MyFridge();
            myFridge.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.screen, myFridge).commit();

            //HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.homeFragment);
            //homeFragment.GetFridgeFromMain(fridge);

            System.out.println("Fridge Items");
            for (FridgeItem fridgeItem:fridge.getFridgeItems())
            {
                System.out.println(fridgeItem.getName());
            }

            System.out.println("Expired Items");
            for (FridgeItem fridgeItem:fridge.checkForExpiredAtDate(currentDate.format(formatter)))
            {
                System.out.println(fridgeItem.getName());
            }
        }

        if (resultCode == RESULT_OK) { //For changing the profile picture

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == 22) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    profilePicture.setImageURI(selectedImageUri);
                }
            }
        }
    }

    @Override
    public void onBackPressed() { //Stays
        // 5 - Handle back click to close menu
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        }
        else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        }
        else
        {
            super.onBackPressed();
        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, //Stays as it is
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.new_profile_pic_floating_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) { //Profile picture choices handler
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.take_photo:
                System.out.println("TAKE PHOTO FROM CAMERA");
                return true;
            case R.id.from_gallery:
                System.out.println("CHOOSE PIC FROM GALLERY");
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);

                // pass the constant to compare it
                // with the returned requestCode
                startActivityForResult(Intent.createChooser(i, "Select Picture"), 22);
                return true;

            case R.id.remove_photo:
                System.out.println("REMOVE PHOTO");
                profilePicture.setImageResource(R.drawable.ic_profile_pic);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) { //Navigation drawer choices handler
        item.setChecked(true);
        Bundle bundle = new Bundle();
        switch (item.getItemId())
        {
            case R.id.myFridgeItem:
                bundle.putSerializable("fridge", fridge);
                MyFridge myFridge = new MyFridge();
                myFridge.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.screen, myFridge).commit();
                /*Intent i = new Intent(this, MyFridge.class);
                i.putExtra("Fridge", fridge);
                startActivity(i);
                 */
                break;

            case R.id.homeItem:
                bundle.putSerializable("fridge", fridge);
                //bundle.putString("date", currentDate.format(formatter));

                HomeFragment homeFragment = new HomeFragment();
                homeFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.screen, homeFragment).commit();

                //getSupportFragmentManager().beginTransaction().replace(R.id.screen, new HomeFragment()).commit();
                break;

            case R.id.expiredItem:
                bundle.putSerializable("fridge_from_Activity", fridge);
// set Fragmentclass Arguments
                //ExpiredFragment expiredFragment = new ExpiredFragment();
                ExpiredFragment expiredFragment = new ExpiredFragment();
                expiredFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.screen, expiredFragment).commit();
                break;

            case R.id.recipesItem:
                RecipesOverviewFragment recipesSearchFragment = new RecipesOverviewFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.screen, recipesSearchFragment).commit();
                break;

            case R.id.settingsItem:
                SettingsFragment settingsFragment = new SettingsFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.screen, settingsFragment).commit();
        }

        drawerLayout.close();
        return true;
    }


   /* public void expriredProductsWarning()
    {
        expiredWarning.setVisibility(View.VISIBLE);
    }

    */

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


    /*public void CheckFridge() //It should only check for expired products. The rest will be handled by the fragment
    {
        currentDate = LocalDate.now();
        //LocalDate tomorrow = currentDate.plusDays(1);
        //LocalDate soon = currentDate.plusDays(2); //three days later

        String dateText = currentDate.format(formatter);

        /*
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





        ArrayList<FridgeItem> products = fridge.checkForExpiredAtDate(dateText);
        for (FridgeItem product: products)
        {
            if(!fridge.getExpiredItems().contains(product))
                fridge.AddExpired(product);
        }


        for (FridgeItem fridgeItem:fridge.getFridgeItems())
        {
            System.out.println("Fridge Items");
            System.out.println(fridgeItem.getName());
        }


        for (FridgeItem fridgeItem:fridge.getExpiredItems())
        {
            System.out.println("Expired Items");
            System.out.println(fridgeItem.getName());
        }

    }

     */


    /*
    public Fridge GetFridge()
    {
        return fridge;
    }

    public String GetCurrentDate()
    {
        return currentDate.format(formatter);
    }

     */

    @Override
    public void UpdateData(Fridge fridge)
    {
        //HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("HomeFragment");
        System.out.println("FRIDGE UPDATED");

        for (FridgeItem product:fridge.getFridgeItems())
        {
            System.out.println(product.getName());
        }

        this.fridge = fridge;
    }

    public void goToExpired(View view)
    {
        navigationView.getMenu().findItem(R.id.expiredItem).setChecked(true);

        Bundle bundle = new Bundle();
        bundle.putSerializable("fridge_from_Activity", fridge);
        // set Fragmentclass Arguments
        ExpiredFragment expiredFragment = new ExpiredFragment();
        expiredFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.screen, expiredFragment).commit();
    }

    public void openProduct(int id)
    {
        if(!fridge.getFridgeItems().get(id).isOpened())
        {
            fridge.getFridgeItems().get(id).setOpened(true);
            fridge.getFridgeItems().get(id).setDayOpened(currentDate.format(formatter));
        }

    }

    public void deleteProduct(int id)
    {
        fridge.removeItem(id);
    }

    public void MoveUp(Fragment fromFragment)
    {

    }


    //public void editProduct(int id)
    //{

    //}
}