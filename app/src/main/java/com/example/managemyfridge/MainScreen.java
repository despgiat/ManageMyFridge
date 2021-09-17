package com.example.managemyfridge;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
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

    private Fridge fridge;
    LocalDate currentDate;
    public static DateTimeFormatter formatter; //the date formatter, used by different fragments in the app

    //Views
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    View inflatedView;
    ImageView profilePicture;

    Fragment currentFragment;
    MyDBHandler dbHandler;
    //SharedPreferences sharedPreferences; //Geeks for geeks


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        currentDate = LocalDate.now();

        /**----------Finding View IDs-------------**/
        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawerLayout);

        /**----------END OF Finding view IDs-------**/

        //Setting up the custom Toolbar
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        inflatedView = navigationView.inflateHeaderView(R.layout.header_navigation_drawer); //inflates the header of the navigation drawer (the section containing the user's profile picture
        // username and email )
        profilePicture = (ImageView) inflatedView.findViewById(R.id.imageViewProfilePic);
        registerForContextMenu(profilePicture); //When the profile picture on the navigation drawer header gets long-clicked, a context menu will appear

        //Setting up the navigation drawer - See Android Material Design on creating the Navigation Drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, myToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();
        navigationView.getMenu().getItem(0).setChecked(true);

        //To change the username in the header
        SharedPreferences sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);
        String headerUsername = sharedPreferences.getString("username", "Username");
        TextView header = inflatedView.findViewById(R.id.headerUsername);
        header.setText(headerUsername);

        boolean darkMode = sharedPreferences.getBoolean("dark_mode", false);
        System.out.println("Dark Mode is " + darkMode);
        if (darkMode)
        {
            //Switch to Dark Mode
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else
        {
            //Switch to Light Mode
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }


        //Desired date formatter
        formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        //Loads the databases:
        dbHandler = new MyDBHandler(this, null, null, 1);
        ArrayList<Product> products = dbHandler.showallProducts(LoginScreen.user.getID());
        fridge = new Fridge(products);

        //Some fragments in the app get added to the backstack, and we want the "burger" button in the navigation drawer to become the arrow, and to allow the navigation only backward
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if(getSupportFragmentManager().getBackStackEntryCount() > 0){
                    toggle.setDrawerIndicatorEnabled(false); //disables the drawer icon and sets it as the back arrow
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                    toggle.setToolbarNavigationClickListener(new View.OnClickListener() { //https://stackoverflow.com/questions/17258020/switching-between-android-navigation-drawer-image-and-up-caret-when-using-fragme
                        @Override
                        public void onClick(View v) {
                            getSupportFragmentManager().popBackStack();
                        } //When the arrow is clicked, the back stack is popped and the "older" fragment gets displayed again
                    });

                }
                else {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    toggle.setDrawerIndicatorEnabled(true);
                }
            }
        });


        if(savedInstanceState == null) //The Home Fragment is the default fragment when where is no saved Instance state
        {
            HomeFragment home = new HomeFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("fridge", fridge);
            home.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.screen, home).commit();
            navigationView.setCheckedItem(R.id.homeItem);
            currentFragment = home;
           // getSupportActionBar().setTitle("Home");
        }

    }

    @Override
    public void onResume() {

        super.onResume();
/*
        //read preferences
        SharedPreferences sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);
        //SharedPreferences.Editor editor = sharedPreferences.edit();

        String headerUsername = sharedPreferences.getString("username", "Username");
        //int a = sharedPreferences.getInt("age", 0);

        // Setting the fetched data
        // in the EditTexts

        TextView header = inflatedView.findViewById(R.id.headerUsername);
        header.setText(headerUsername);

        //name.setText(s1);
        //age.setText(String.valueOf(a));

 */
        TextView header = inflatedView.findViewById(R.id.headerUsername);
        header.setText(LoginScreen.user.getUsername());

        TextView header1 = inflatedView.findViewById(R.id.headerEmail);
        header1.setText(LoginScreen.user.getEmail());

    }

    //Starts the MainActivity, which adds new items to the product database
    public void addNewItem()
    {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("Fridge", fridge);
        startActivityForResult(i, 2);
    }

    public void editProduct(Product product)
    {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("Fridge", fridge);
        i.putExtra("Product", product);
        //i.putExtra("Edit", true);
        startActivityForResult(i, 3);
    }



    @Override
    protected void onActivityResult( int requestCode , int resultCode , Intent data ) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK)
        {
            switch(requestCode)
            {
                case 2: //case 2 and case 3 merged
                case 3:
                    //Update the fridge from the database:
                    fridge.setFridgeItems(dbHandler.showallProducts(LoginScreen.user.getID()));
                    //Updates the Home Fragment with the new fridge and displays it
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("fridge", fridge);
                    MyFridge myFridge = new MyFridge();
                    myFridge.setArguments(bundle);
                    currentFragment = myFridge;
                    getSupportFragmentManager().beginTransaction().replace(R.id.screen, myFridge).commit();
                    break;
                //Updates the Home Fragment with the new fridge and displays it

                case 22:
                    // Get the url of the image from data
                    Uri selectedImageUri = data.getData();
                    if (null != selectedImageUri) {
                        // update the preview image in the layout
                        profilePicture.setImageURI(selectedImageUri);
                    }
                    break;
            }
        }


        /*if ((requestCode == 2) && resultCode == RESULT_OK)
        {



        }

        if (resultCode == RESULT_OK) { //https://www.geeksforgeeks.org/how-to-select-an-image-from-gallery-in-android/

            if (requestCode == 22) {

            }
        }
         */
    }

    @Override
    public void onBackPressed() {

        //When the drawer is open, clicking back causes it to close
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        }

        //If the fragment backstack isn't empty, pop the top fragment
        else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        }

        //if the current fragment displayed is not the home fragment and there are no other fragments in the backstack -> for ex. when the user is at the myfridge screen
        else if(getSupportFragmentManager().getBackStackEntryCount() == 0 && !(currentFragment instanceof HomeFragment))
        {
            Bundle bundle = new Bundle();
            bundle.putSerializable("fridge", fridge);
            HomeFragment homeFragment = new HomeFragment();
            homeFragment.setArguments(bundle);
            currentFragment = homeFragment;
            navigationView.setCheckedItem(R.id.homeItem);
            getSupportFragmentManager().beginTransaction().replace(R.id.screen, homeFragment).commit(); //"returns to the home page", the home fragment gets displayed
        }
        else
        {
            super.onBackPressed();
        }

    }

    //Context menu for changing the user's profile picture
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
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


    /**Navigation drawer menu choices handler
     * On click on each menu item, the appropriate fragment is displayed in the main screen (thus the user navigated to the app's different screens)
     *
     */
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
                //getSupportActionBar().setTitle("My Fridge");
                break;

            case R.id.homeItem:
                bundle.putSerializable("fridge", fridge);
                //bundle.putString("date", currentDate.format(formatter));

                HomeFragment homeFragment = new HomeFragment();
                homeFragment.setArguments(bundle);
                currentFragment = homeFragment;
                getSupportFragmentManager().beginTransaction().replace(R.id.screen, homeFragment).commit();
                //getSupportActionBar().setTitle("Home");
                break;

            case R.id.expiredItem:
                bundle.putSerializable("fridge", fridge);
                ExpiredFragment expiredFragment = new ExpiredFragment();
                expiredFragment.setArguments(bundle);
                currentFragment = expiredFragment;
                getSupportFragmentManager().beginTransaction().replace(R.id.screen, expiredFragment).commit();
                //getSupportActionBar().setTitle("Expired");
                break;

            case R.id.recipesItem:
                RecipeSearchFragment recipeSearchFragment = new RecipeSearchFragment();
                //bundle.putSerializable("recipes" );
                recipeSearchFragment.setArguments(bundle);
                currentFragment = recipeSearchFragment;
                getSupportFragmentManager().beginTransaction().replace(R.id.screen, recipeSearchFragment).commit();
                //getSupportActionBar().setTitle("Recipes");
                break;

            case R.id.zeroWasteItem:
                TipsOverviewFragment tipsOverviewFragment = new TipsOverviewFragment();
                currentFragment = tipsOverviewFragment;
                getSupportFragmentManager().beginTransaction().replace(R.id.screen, tipsOverviewFragment).commit();
                //getSupportActionBar().setTitle("Zero Waste Tips");
                break;

            case R.id.settingsItem:
                SettingsFragment settingsFragment = new SettingsFragment();
                currentFragment = settingsFragment;
                getSupportFragmentManager().beginTransaction().replace(R.id.screen, settingsFragment).commit();
                //getSupportActionBar().setTitle("Settings");
                break;

            case R.id.favouritesItem:
                FavouritesFragment favouritesFragment = new FavouritesFragment();
                currentFragment = favouritesFragment;
                getSupportFragmentManager().beginTransaction().replace(R.id.screen, favouritesFragment).commit();
               // getSupportActionBar().setTitle("Favourites");
                break;

            case R.id.profileItem:
                ProfileFragment profileFragment = new ProfileFragment();
                currentFragment = profileFragment;
                getSupportFragmentManager().beginTransaction().replace(R.id.screen, profileFragment).commit();
                //getSupportActionBar().setTitle("Profile");
                break;
        }

        drawerLayout.close(); //when the user makes the choice, the navigation drawer closes
        return true;
    }


    public void LogOut()
    {
        new AlertDialog.Builder(this)
                .setTitle("Log out")
                .setMessage("Are you sure you want to logout?")

                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(getApplicationContext(), LoginScreen.class); //Starts the LoginScreen Activity
                        startActivity(i);
                        finish(); //So that we don't go back to the login activity on back pressed
                    }
                })

                .setNegativeButton(android.R.string.cancel, null)
                .setIcon(R.drawable.ic_warning)
                .show();

    }


    @Override
    public void UpdateData(Fridge fridge)
    {
        System.out.println("FRIDGE UPDATED");
        this.fridge = fridge;
    }

    /**
     * The onClick method when the user clicks the yellow warning sign on the HomeFragment, when there are expired products in the fridge.
     * The user immediately gets transfered to the Expired Products screen
     * @param view
     */
    public void goToExpired(View view)
    {
        navigationView.getMenu().findItem(R.id.expiredItem).setChecked(true);

        Bundle bundle = new Bundle();
        bundle.putSerializable("fridge", fridge);
        ExpiredFragment expiredFragment = new ExpiredFragment();
        expiredFragment.setArguments(bundle);
        currentFragment = expiredFragment;
        getSupportFragmentManager().beginTransaction().replace(R.id.screen, expiredFragment).commit();
    }

    //Opens a product of id in the database
    public void openProduct(int id, Fragment fromFragment)
    {
        boolean opened = dbHandler.makeProductOpen(id, currentDate.format(formatter));

        if(opened)
        {
            Toast.makeText(this, "You opened this product!", Toast.LENGTH_SHORT).show();
            fridge.setFridgeItems(dbHandler.showallProducts(LoginScreen.user.getID())); //Updates the fridge from the database
        }
        else
        {
            Toast.makeText(this, "Something went wrong...", Toast.LENGTH_SHORT).show();
        }
        getSupportFragmentManager().beginTransaction().detach(fromFragment).attach(fromFragment).commit(); //To refresh the Fragment UI
    }

    //Deletes a product from the database
    public void deleteProduct(String productName, Fragment fromFragment)
    {
        boolean deleted = dbHandler.deleteProduct(productName);
        if (deleted)
        {
            Toast.makeText(this, productName + " was successfully deleted", Toast.LENGTH_SHORT).show();
            fridge.setFridgeItems(dbHandler.showallProducts(LoginScreen.user.getID()));
        }
        else
        {
            Toast.makeText(this, "Could not delete " + productName, Toast.LENGTH_SHORT).show();
        }
    }

    public void usernameChange()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);
        String headerUsername = sharedPreferences.getString("username", "Username");
        TextView header = inflatedView.findViewById(R.id.headerUsername);
        header.setText(headerUsername);
    }

    public void getMealType()
    {

    }

    //public


}