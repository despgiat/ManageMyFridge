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
import android.content.SharedPreferences;
import android.content.res.Configuration;
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


    private Fridge fridge; //--> We do not need this then (it will be loaded in the fragment) (Or we do, to pass it to other fragments)

    LocalDate currentDate;
    public static DateTimeFormatter formatter;

    DrawerLayout drawerLayout;
    NavigationView navigationView; //The navigation drawer panel

    View inflatedView;
    ImageView profilePicture;
    //private Menu navigationDrawer;

    Fragment currentFragment;
    MyDBHandler dbHandler;
    SharedPreferences sharedPreferences; //Geeks for geeks

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

        //Load the database:

        dbHandler = new MyDBHandler(this, null, null, 1);
        ArrayList<Product> products = dbHandler.showallProducts();
        fridge = new Fridge(products);

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

        sharedPreferences = this.getSharedPreferences("com.example.managemyfridge", MODE_PRIVATE);

        boolean isDarkModeOn = false;
       /* switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) { //Check which mode the system is currently using
            case Configuration.UI_MODE_NIGHT_YES:
                System.out.println("DARK MODE");
                isDarkModeOn = true;
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                System.out.println("LIGHT MODE");
                isDarkModeOn = false;
                break;
        }

        */

        boolean darkModeEnabled = sharedPreferences.getBoolean("com.example.darkModeEnabled", false);
        //final SharedPreferences.Editor editor = sharedPreferences.edit();


       /* if (isDarkModeOn) {
            AppCompatDelegate
                    .setDefaultNightMode(
                            AppCompatDelegate
                                    .MODE_NIGHT_YES);
            btnToggleDark.setText(
                    "Disable Dark Mode");
        }
        else {
            AppCompatDelegate
                    .setDefaultNightMode(
                            AppCompatDelegate
                                    .MODE_NIGHT_NO);
            btnToggleDark
                    .setText(
                            "Enable Dark Mode");
        }

        */





        /**
         * When there are expired products, the HomeFragment will send the expires Items arraylist to this activity,
         * and this activity will send the ExpiredFragment the data
         */

        if(savedInstanceState == null)
        {
            HomeFragment home = new HomeFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("fridge", fridge);
            home.setArguments(bundle);
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

        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("Fridge", fridge);
        startActivityForResult(i, 2);
    }



    @Override
    protected void onActivityResult( int requestCode , int resultCode , Intent data ) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 2) && resultCode == RESULT_OK)
        {
            //Update the fridge from the database:
            fridge.setFridgeItems(dbHandler.showallProducts());

            //Updates the Home Fragment with the new fridge and displays it

            Bundle bundle = new Bundle(); //Sends the fridge back to the fragment (It doesn't unfortunately...) Let's fix that real quick
            bundle.putSerializable("fridge", fridge);
            MyFridge myFridge = new MyFridge();
            myFridge.setArguments(bundle);
            currentFragment = myFridge;
            getSupportFragmentManager().beginTransaction().replace(R.id.screen, myFridge).commit();

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

    public void changeProfilePic()
    {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), 22);
    }

    public void removeProfilePic()
    {
        profilePicture.setImageResource(R.drawable.ic_profile_pic);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) { //Profile picture choices handler
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.from_gallery:
                changeProfilePic();
                return true;

            case R.id.remove_photo:
                removeProfilePic();
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
                bundle.putSerializable("fridge", fridge);
// set Fragmentclass Arguments
                //ExpiredFragment expiredFragment = new ExpiredFragment();
                ExpiredFragment expiredFragment = new ExpiredFragment();
                expiredFragment.setArguments(bundle);
                currentFragment = expiredFragment;
                getSupportFragmentManager().beginTransaction().replace(R.id.screen, expiredFragment).commit();
                break;

            case R.id.recipesItem:
                RecipeSearchFragment recipesSearchFragment = new RecipeSearchFragment();
                bundle.putSerializable("fridge", fridge);
                recipesSearchFragment.setArguments(bundle);

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

            case R.id.favouritesItem:
                FavouritesFragment favouritesFragment = new FavouritesFragment();
                currentFragment = favouritesFragment;
                getSupportFragmentManager().beginTransaction().replace(R.id.screen, favouritesFragment).commit();
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
        bundle.putSerializable("fridge", fridge);
        // set Fragmentclass Arguments
        ExpiredFragment expiredFragment = new ExpiredFragment();
        expiredFragment.setArguments(bundle);
        currentFragment = expiredFragment;
        getSupportFragmentManager().beginTransaction().replace(R.id.screen, expiredFragment).commit();
    }

    public void openProduct(int id, Fragment fromFragment)
    {
        boolean opened = dbHandler.makeProductOpen(id, currentDate.format(formatter));

        if(opened)
        {
            Toast.makeText(this, "You opened this product!", Toast.LENGTH_SHORT).show();
            fridge.setFridgeItems(dbHandler.showallProducts()); //Updates the fridge from the database
            /*Bundle bundle = new Bundle();
            bundle.putSerializable("fridge", fridge);
            fromFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.screen, fromFragment).commit();

             */
        }
        else
        {
            Toast.makeText(this, "Something went wrong...", Toast.LENGTH_SHORT).show();
        }

    }

    public void deleteProduct(String productName, Fragment fromFragment)
    {
        boolean deleted = dbHandler.deleteProduct(productName);
        if (deleted)
        {
            Toast.makeText(this, productName + " was successfully deleted", Toast.LENGTH_SHORT).show();
            fridge.setFridgeItems(dbHandler.showallProducts());
           /* Bundle bundle = new Bundle();
            bundle.putSerializable("fridge", fridge);
            fromFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.screen, fromFragment).commit();

            */
        }
        else
        {
            Toast.makeText(this, "Could not delete " + productName, Toast.LENGTH_SHORT).show();
        }



    }


}