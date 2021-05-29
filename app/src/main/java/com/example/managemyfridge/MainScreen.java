package com.example.managemyfridge;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
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
import android.content.DialogInterface;
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
import android.widget.PopupMenu;
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

    DrawerLayout drawerLayout;
    NavigationView navigationView; //The navigation drawer panel

    View inflatedView;
    ImageView profilePicture;
    //private Menu navigationDrawer;

    Fragment currentFragment;

    //Fragments:
    //Fragment activeScreen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        currentDate = LocalDate.now();

        /**----------Finding view IDs-------------**/
        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawerLayout);

        /**----------END OF Finding view IDs-------**/

        Toolbar myToolbar = findViewById(R.id.toolbar);

        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

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


        /**
         * When there are expired products, the HomeFragment will send the expires Items arraylist to this activity,
         * and this activity will send the ExpiredFragment the data
         */

        if(savedInstanceState == null)
        {
            HomeFragment home = new HomeFragment();
            //home.setArguments(bundle);
            //homeFragment = new HomeFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.screen, home).commit();
            navigationView.setCheckedItem(R.id.homeItem);
            currentFragment = home;
        }

    }


    @Override
    public void onResume()
    {
        super.onResume();

    }

    //This will be accessed from the Home Fragment and the MyFridge Fragment, so it should be implemented here
    public void addNewItem()
    {
        /*
        System.out.println("ADD NEW ITEM!!!");
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("Fridge", fridge);
        startActivityForResult(i, 2);
        */

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);

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
            currentFragment = myFridge;
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
        else if(getSupportFragmentManager().getBackStackEntryCount() == 0 && !(currentFragment instanceof HomeFragment))
        {
            Bundle bundle = new Bundle();
            bundle.putSerializable("fridge", fridge);
            //bundle.putString("date", currentDate.format(formatter));
            HomeFragment homeFragment = new HomeFragment();
            homeFragment.setArguments(bundle);
            currentFragment = homeFragment;
            navigationView.setCheckedItem(R.id.homeItem);
            getSupportFragmentManager().beginTransaction().replace(R.id.screen, homeFragment).commit();
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
                currentFragment = myFridge;
                getSupportFragmentManager().beginTransaction().replace(R.id.screen, myFridge).commit();
                break;

            case R.id.homeItem:
                bundle.putSerializable("fridge", fridge);
                //bundle.putString("date", currentDate.format(formatter));

                HomeFragment homeFragment = new HomeFragment();
                homeFragment.setArguments(bundle);
                currentFragment = homeFragment;
                getSupportFragmentManager().beginTransaction().replace(R.id.screen, homeFragment).commit();

                //getSupportFragmentManager().beginTransaction().replace(R.id.screen, new HomeFragment()).commit();
                break;

            case R.id.expiredItem:
                bundle.putSerializable("fridge_from_Activity", fridge);
// set Fragmentclass Arguments
                //ExpiredFragment expiredFragment = new ExpiredFragment();
                ExpiredFragment expiredFragment = new ExpiredFragment();
                expiredFragment.setArguments(bundle);
                currentFragment = expiredFragment;
                getSupportFragmentManager().beginTransaction().replace(R.id.screen, expiredFragment).commit();
                break;

            case R.id.recipesItem:
                RecipesOverviewFragment recipesSearchFragment = new RecipesOverviewFragment();
                currentFragment = recipesSearchFragment;
                getSupportFragmentManager().beginTransaction().replace(R.id.screen, recipesSearchFragment).commit();
                break;

            case R.id.zeroWasteItem:
                TipsOverviewFragment tipsOverviewFragment = new TipsOverviewFragment();
                currentFragment = tipsOverviewFragment;
                getSupportFragmentManager().beginTransaction().replace(R.id.screen, tipsOverviewFragment).commit();
                break;

            case R.id.settingsItem:
                SettingsFragment settingsFragment = new SettingsFragment();
                currentFragment = settingsFragment;
                getSupportFragmentManager().beginTransaction().replace(R.id.screen, settingsFragment).commit();
                break;

            case R.id.profileItem:
                ProfileFragment profileFragment = new ProfileFragment();
                currentFragment = profileFragment;
                getSupportFragmentManager().beginTransaction().replace(R.id.screen, profileFragment).commit();
                break;
        }

        drawerLayout.close();
        return true;
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

    public void LogOut()
    {
        new AlertDialog.Builder(this)
                .setTitle("Log out")
                .setMessage("Are you sure you want to logout?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(getApplicationContext(), LoginScreen.class);
                        startActivity(i);
                        finish(); //So that we don't go back to the login activity on back pressed
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.cancel, null)
                .setIcon(R.drawable.ic_warning)
                .show();

    }


    @Override
    public void UpdateData(Fridge fridge)
    {
        //HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("HomeFragment");
        System.out.println("FRIDGE UPDATED");
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
        currentFragment = expiredFragment;
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

   /* public void switchFragment(Fragment toFragment)
    {
        currentFragment =
    }

    */


    //public void editProduct(int id)
    //{

    //}
}