package com.example.managemyfridge;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.EditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MyDBHandler extends SQLiteOpenHelper {

    //Σταθερές για τη ΒΔ (όνομα ΒΔ, έκδοση, πίνακες κλπ)

    //New: Added PATH, mDataBase, mContext, mNeedUpdate and changed the name of the DB to MMFDB1
    private static String DATABASE_PATH =  "";
    private SQLiteDatabase mDataBase;
    private final Context mContext;
    private boolean mNeedUpdate = false;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MMFDB2.db";
    public static final String TABLE_PRODUCTS = "PRODUCT";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PRODUCTNAME = "Name";
    public static final String COLUMN_EXDATE = "Expiration_Date";
    public static final String COLUMN_IS_IT_OPEN = "Is_it_open";
    public static final String COLUMN_TYPE = "PRODUCT_Type";
    public static final String COLUMN_DATE_OF_OPENING = "Date_of_opening";
    public static final String COLUMN_QUANTITY = "Quantity";
    public static final String COLUMN_UNIT = "Unit";

    //NEW: IDOFUSER = 0 for starters
    private static String COLUMN_ID_OF_USER = "_idofUSER";

    public static final String TABLE_USERS = "USERS";
    public static final String COLUMN_USERNAME = "Username";
    public static final String COLUMN_EMAIL = "Email";
    public static final String COLUMN_PASSWORD = "User_Password";
    public static final String COLUMN_FAVORITES = "Fav_Recipes";

    public static final String TABLE_RECIPES = "RECIPE";
    public static final String COLUMN_RECIPENAME = "Name";
    public static final String COLUMN_RECIPE_TYPE = "Type_of_Recipe";
    public static final String COLUMN_INSTRUCTIONS = "Instructions";
    public static final String COLUMN_IS_IT_FAV = "Is_it_fav";

    public static final String COLUMN_SOURCE = "Source"; //NEWLY ADDED


    public static final String TABLE_TIPS = "TIP";
    public static final String COLUMN_TIPNAME = "Name";
    public static final String COLUMN_DESCRIPTION = "Description";
    public static final String COLUMN_RELATED_PRODUCT = "Related_Product";
    public static final String COLUMN_IMAGE = "Img";

    public static final String TABLE_INGREDIENTS = "INGREDIENT";
    public static final String COLUMN_INGREDIENTNAME = "Ingredient_Name";
    public static final String COLUMN_ID_OF_RECIPE = "_idofRECIPE";

    //NEW: COLUMN FOR DIET PREFERENCE
    public static final String COLUMN_DIET_PREF = "Diet_Preference";


    //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-mm-yyyy");


    //NEW: constructor and new methods for DB handling


    public MyDBHandler(Context context, String name,
                       SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);

        if (android.os.Build.VERSION.SDK_INT >= 17)
            DATABASE_PATH = context.getApplicationInfo().dataDir + "/databases/";
        else
            DATABASE_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        this.mContext = context;


        copyDataBase();

        this.getReadableDatabase();
    }

    //Αναβάθμιση ΒΔ: Ελέγχει αν χρειάζεται αναβάθμιση (mNeedUpdate = true) και αν ναι,
    // κάνει την αντίστοιχη διαγραφή και μετά επαναδημιουργία με την copyDataBase()/copyDBFile().
    public void updateDataBase() throws IOException {
        if (mNeedUpdate) {
            File dbFile = new File(DATABASE_PATH + DATABASE_NAME);
            if (dbFile.exists())
                dbFile.delete();

            copyDataBase();

            mNeedUpdate = false;
        }
    }

    private boolean checkDataBase() {
        File dbFile = new File(DATABASE_PATH + DATABASE_NAME);
        return dbFile.exists();
    }

    private void copyDataBase() {
        if (!checkDataBase()) {
            this.getReadableDatabase();
            this.close();
            try {
                copyDBFile();
            } catch (IOException mIOException) {
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }

    private void copyDBFile() throws IOException {
        InputStream mInput = mContext.getAssets().open(DATABASE_NAME);

        OutputStream mOutput = new FileOutputStream(DATABASE_PATH + DATABASE_NAME);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0)
            mOutput.write(mBuffer, 0, mLength);
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    public boolean openDataBase() throws SQLException {
        mDataBase = SQLiteDatabase.openDatabase(DATABASE_PATH + DATABASE_NAME, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        return mDataBase != null;
    }

    @Override
    public synchronized void close() {
        if (mDataBase != null)
            mDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }


    //Αναβάθμιση ΒΔ: Λέω πως χρειάζεται αναβάθμιση αν η παλιά version είναι μικρότερη της νέας.
    // Ελέγχεται και κάνει την αντίστοιχη διαγραφή και επαναδημιουργία στην updateDataBase().
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion)
            mNeedUpdate = true;
    }

    //ORIGINAL CODE
    /*

    //Constructor
    public MyDBHandler(Context context, String name,
                       SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    //Δημιουργία του σχήματος της ΒΔ (πίνακας products, Ingredients, Recipes)
    //this is called the first time a database is accessed
    //Added autoincrement in id key
    @Override
    public void onCreate(SQLiteDatabase db) {
        //create products table
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " +
                TABLE_PRODUCTS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_PRODUCTNAME + " TEXT NOT NULL," +
                COLUMN_QUANTITY + " TEXT NOT NULL," + //QUANTINY no longer integer
                COLUMN_EXDATE + " TEXT," +
                COLUMN_IS_IT_OPEN + " TEXT," +
                COLUMN_TYPE + " TEXT  ," + //NOT NULL Here
                COLUMN_DATE_OF_OPENING + " TEXT," +
                COLUMN_IMAGE + " TEXT," +
                COLUMN_UNIT + " TEXT" +
                ")";
        db.execSQL(CREATE_PRODUCTS_TABLE);

        //create ingredients table
        String CREATE_INGREDIENTS_TABLE = "CREATE TABLE " +
                TABLE_INGREDIENTS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_ID_OF_RECIPE + " INTEGER NOT NULL," +
                COLUMN_INGREDIENTNAME + " TEXT NOT NULL," +
                COLUMN_QUANTITY + " TEXT NOT NULL," +
                COLUMN_UNIT + " TEXT" +
                ")";
        db.execSQL(CREATE_INGREDIENTS_TABLE);

        //create recipes table
        String CREATE_RECIPES_TABLE = "CREATE TABLE " +
                TABLE_RECIPES + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_RECIPENAME + " TEXT NOT NULL," +
                COLUMN_RECIPE_TYPE + " TEXT  ," +
                COLUMN_INSTRUCTIONS + " TEXT  ," +
                //COLUMN_IS_IT_FAV + " TEXT  ," +
                COLUMN_SOURCE + " TEXT  ," +
                COLUMN_IMAGE + " TEXT" +
                ")";
        db.execSQL(CREATE_RECIPES_TABLE);

        //create tips table
        String CREATE_TIPS_TABLE = "CREATE TABLE " +
                TABLE_TIPS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_TIPNAME + " TEXT NOT NULL," +
                COLUMN_DESCRIPTION + " TEXT  ," +
                COLUMN_RELATED_PRODUCT + " TEXT  ," +
                //COLUMN_IS_IT_FAV + " TEXT  ," +
                COLUMN_SOURCE + " TEXT  ," + //NEWLY ADDED
                COLUMN_IMAGE + " TEXT" +
                ")";
        db.execSQL(CREATE_TIPS_TABLE);

        //create users table
        String CREATE_USERS_TABLE = "CREATE TABLE " +
                TABLE_USERS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_USERNAME + " TEXT ," +
                COLUMN_EMAIL + " TEXT  ," +
                COLUMN_PASSWORD + " TEXT  ," +
                COLUMN_IMAGE + " TEXT" +
                ")";
        db.execSQL(CREATE_USERS_TABLE);
    }




    //Αναβάθμιση ΒΔ: εδώ τη διαγραφώ και τη ξαναδημιουργώ ίδια
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

     */



    //PRODUCT METHODS

    //TODO: Change the product methods, new idofuser needed. DONE

    //Μέθοδος για προσθήκη ενός προϊόντος στη ΒΔ
    public void addProduct(Product product, int idofuser) {
        //added: exdate, isopened, prodtype, dateofopening, img, unit
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCTNAME, product.getProductName());
        values.put(COLUMN_QUANTITY, product.getQuantity());
        values.put(COLUMN_EXDATE, product.get_exdate());
        //values.put(COLUMN_IS_IT_OPEN, product.isOpened());
        values.put(COLUMN_IS_IT_OPEN, product.get_opened());
        values.put(COLUMN_TYPE, product.get_prodtype());
        values.put(COLUMN_DATE_OF_OPENING, product.get_DateofOpening());
        values.put(COLUMN_IMAGE, product.get_img());
        values.put(COLUMN_UNIT, product.get_unit());
        values.put(COLUMN_ID_OF_USER, idofuser); //NEW:added idofuser
        SQLiteDatabase db = this.getWritableDatabase();
        //original
        db.insert(TABLE_PRODUCTS, null, values);
        db.close();


        //change to make sure it works
        /*long insert = db.insert(TABLE_PRODUCTS, null, values);
        db.close();
        if(insert==-1){
            return false;
        }
        else {
            return true;
        }*/

    }

    //Μέθοδος για εύρεση προϊόντος βάσει ονομασίας του
    public Product findProduct(String productname) {
        String query = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " +
                COLUMN_PRODUCTNAME + " = '" + productname + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Product product = new Product();
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            product.setID(Integer.parseInt(cursor.getString(0)));
            product.setProductName(cursor.getString(1));
            product.setQuantity(cursor.getString(2));
            cursor.close();
        } else {
            product = null;
        }
        db.close();
        return product;
    }

    //Μέθοδος για εύρεση προϊόντος βάσει ονομασίας του
    public Product newfindProduct(String productname) {
        String query = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " +
                COLUMN_PRODUCTNAME + " = '" + productname + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Product product = new Product();
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            product.setID(Integer.parseInt(cursor.getString(0)));
            product.setProductName(cursor.getString(1));
            product.setQuantity(cursor.getString(2));
            product.set_exdate(cursor.getString(3));
            product.set_opened(cursor.getString(4));
            product.set_prodtype(cursor.getString(5));
            product.set_DateofOpening(cursor.getString(6));
            //product.set_img(cursor.getString(7)); //the img changes based on type, not user's choice.
            product.set_unit(cursor.getString(8));
           //product.set_idofUSER(Integer.parseInt(cursor.getString(9))); //ID of user doesn't change
            cursor.close();

        } else {
            product = null;
        }
        db.close();
        return product;
    }

    //Μέθοδος για διαγραφή προϊόντος βάσει ονομασίας του
    public boolean deleteProduct(String productname) {
        boolean result = false;
        String query = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " +
                COLUMN_PRODUCTNAME + " = '" + productname + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Product product = new Product();
        if (cursor.moveToFirst()) {
            product.setID(Integer.parseInt(cursor.getString(0)));
            db.delete(TABLE_PRODUCTS, COLUMN_ID + " = ?",
                    new String[] { String.valueOf(product.getID()) });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }

    // we have created a new method that returns all the products in the fridge of the current user.
    public ArrayList<Product> showallProducts(int idofuser) {
        // on below line we are creating a
        // database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS +" WHERE " +
                COLUMN_ID_OF_USER + " = '" + idofuser + "'", null);

        // on below line we are creating a new array list.
        ArrayList<Product> fridgeProducts = new ArrayList<>();

        // moving our cursor to first position.
        if (cursor.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                fridgeProducts.add(new Product(cursor.getInt(0), //id
                        cursor.getString(1), //name
                        cursor.getString(2), //quantity
                        cursor.getString(3), //expiration date
                        cursor.getString(4), //is_it_open
                        cursor.getString(5), //type
                        cursor.getString(6), //date of opening
                        //image      cursor.getString(7),
                        cursor.getString(8), //unit
                        cursor.getInt(9)  //NEW:ADDED idofuser, maybe not needed and perhaps we'll make a different constructor to leave this out

                ));
            } while (cursor.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor and db
        // and returning our array list.
        cursor.close();
        db.close();
        return fridgeProducts;
    }

    //method to change/update the values of  the is_it_open and dateofopening variables
    public boolean makeProductOpen(int id, String todaydate){
        boolean result;
        //String query = "SELECT _id, Is_it_open, Date_of_opening FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_ID + " = '" + id + "'";
        String query = "SELECT _id FROM " + TABLE_PRODUCTS + " WHERE " +
                COLUMN_ID + " = '" + id + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        ContentValues values = new ContentValues();
        Product product = new Product();
        if (cursor.moveToFirst()) {
            product.setID(Integer.parseInt(cursor.getString(0)));
            product.set_opened("yes");
            product.set_DateofOpening(todaydate);

            values.put(COLUMN_IS_IT_OPEN, product.get_opened());
            values.put(COLUMN_DATE_OF_OPENING, product.get_DateofOpening());

            db.update(TABLE_PRODUCTS, values ,COLUMN_ID + " = ?", new String[] { String.valueOf(product.getID()) });
            cursor.close();
            result = true;
        }
        else {
            result = false;
        }

        db.close();
        return result;

    }


    //method to update product properties
    //FIRST IN MAINACTIVITY A findProduct METHOD NEEDS TO BE CALLED TO SHOW ALL PRODUCT PROPERTIES. THEN CALL THIS METHOD  WITH THAT FOUND PRODUCT AS THE VARIABLE
    public void updateProduct(Product product){

        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCTNAME, product.getProductName());
        values.put(COLUMN_QUANTITY, product.getQuantity());
        values.put(COLUMN_EXDATE, product.get_exdate());
        //values.put(COLUMN_IS_IT_OPEN, product.isOpened());
        values.put(COLUMN_IS_IT_OPEN, product.get_opened());
        values.put(COLUMN_TYPE, product.get_prodtype());
        values.put(COLUMN_DATE_OF_OPENING, product.get_DateofOpening());
        values.put(COLUMN_IMAGE, product.get_img()); //is this necessary?
        values.put(COLUMN_UNIT, product.get_unit());
        SQLiteDatabase db = this.getWritableDatabase();
        //original
        db.update(TABLE_PRODUCTS, values ,COLUMN_ID + " = ?", new String[] { String.valueOf(product.getID()) });
        db.close();


    }

    //USER METHODS

    //TODO: Add method to get fav_tips and fav_recipes, old methods may need changes

    //adding user to data base
    public void addUser(){
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, LoginScreen.user.getEmail());
        values.put(COLUMN_USERNAME, LoginScreen.user.getUsername());
        values.put(COLUMN_PASSWORD, LoginScreen.user.getPassword());
        values.put(COLUMN_FAVORITES, LoginScreen.user.getFavs());

        SQLiteDatabase db = this.getWritableDatabase();
        //original
        db.insert(TABLE_USERS, null, values);
        db.close();

    }
    //finding user from database
    public boolean findUser(EditText name) {
        String username = name.getText().toString();
        String query;
        //if the text field had an email address the is searching by email, otherwise is searching by username
        if(isEmail(name)){
             query = "SELECT * FROM " + TABLE_USERS + " WHERE " +
                    COLUMN_EMAIL + " = '" + username + "'";
        }
        else{
             query = "SELECT * FROM " + TABLE_USERS + " WHERE " +
                    COLUMN_USERNAME + " = '" + username + "'";
        }
        SQLiteDatabase db = this.getWritableDatabase();
        boolean flag=false;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) { //if we found a matching to the text field user then we create the user in the programm
            cursor.moveToFirst();   //so we can use later on for checks
            LoginScreen.user.setID(Integer.parseInt(cursor.getString(0)));
            LoginScreen.user.setUsername(cursor.getString(1));
            LoginScreen.user.setEmail(cursor.getString(2));
            LoginScreen.user.setPassword(cursor.getString(3));
            LoginScreen.user.setFavs(cursor.getString(3));
            LoginScreen.user.setImg(cursor.getString(4));
            cursor.close();
            flag = true;
        } else {
            LoginScreen.user = null; //otherwise we delete everything from user object
        }
        db.close();
        return flag;
    }


    //delete user
    public boolean deleteUser(String username) {
        boolean result = false;
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " +
                COLUMN_USERNAME + " = '" + username + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            LoginScreen.user.setID(Integer.parseInt(cursor.getString(0)));
            db.delete(TABLE_PRODUCTS, COLUMN_ID + " = ?",
                    new String[] { String.valueOf(LoginScreen.user.getID()) });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }

    //update user
    public void updateUser (){

        ContentValues values = new ContentValues();
        values.put(COLUMN_PASSWORD, LoginScreen.user.getPassword());
        values.put(COLUMN_USERNAME, LoginScreen.user.getUsername());
        values.put(COLUMN_EMAIL, LoginScreen.user.getEmail());
        values.put(COLUMN_FAVORITES, LoginScreen.user.getFavs());

        SQLiteDatabase db = this.getWritableDatabase();
        //original
        db.update(TABLE_USERS, values ,COLUMN_ID + " = ?", new String[] { String.valueOf(LoginScreen.user.getID()) });
        db.close();

    }

    //checks if the text entered is email format
    boolean isEmail(EditText text) {
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    //TODO: Make the fav_recipes and fav_tips methods
    //NEW: Methods to get favorite recipes and tips.
    public void fav_recipes(){}

    public void fav_tips(){}







    //INGREDIENT METHODS

    //TODO: Might need to alter or delete all methods


    // we have created a new method that returns all the ingredients of a recipe.
    public ArrayList<Ingredient> getallIngredients() {
        // on below line we are creating a
        // database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_INGREDIENTS, null);

        // on below line we are creating a new array list.
        ArrayList<Ingredient> listofIngredients = new ArrayList<>();

        // moving our cursor to first position.
        if (cursor.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                listofIngredients.add(new Ingredient(cursor.getInt(0), //id
                        cursor.getInt(1), //idofRECIPE
                        cursor.getString(2), //ingredientname
                        cursor.getString(3), //quantity
                        cursor.getString(4) //unit

                ));
            } while (cursor.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor and db
        // and returning our array list.
        cursor.close();
        db.close();
        return listofIngredients;
    }

    //NEW: PROBABLY WE'LL STICK WITH THIS
    // we have created a new method that returns all the ingredients of a recipe.
    public String newgetallIngredientsofRecipe(int idofrecipe) {

        // on below line we are creating a
        // database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor cursor = db.rawQuery("SELECT Ingredients FROM " + TABLE_RECIPES + " WHERE " +
                COLUMN_ID_OF_RECIPE + " = '" + idofrecipe + "'", null);

        String Ingredients = cursor.getString(0);

        // at last closing our cursor and db
        // and returning our array list.
        cursor.close();
        db.close();
        return Ingredients;

    }



    // we have created a new method that returns all the ingredients of a recipe.
    public ArrayList<Ingredient> getallIngredientsofRecipe(int idofrecipe) {
        // on below line we are creating a
        // database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_INGREDIENTS + " WHERE " +
                COLUMN_ID_OF_RECIPE + " = '" + idofrecipe + "'", null);

        // on below line we are creating a new array list.
        ArrayList<Ingredient> listofIngredients = new ArrayList<>();

        // moving our cursor to first position.
        if (cursor.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                listofIngredients.add(new Ingredient(cursor.getInt(0), //id
                        cursor.getInt(1), //idofRECIPE
                        cursor.getString(2), //ingredientname
                        cursor.getString(3), //quantity
                        cursor.getString(4) //unit

                ));
            } while (cursor.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor and db
        // and returning our array list.
        cursor.close();
        db.close();
        return listofIngredients;
    }



    //RECIPE METHODS

    //TODO: Changes may be needed, added ingredient in table RECIPE

    // we have created a new method that returns all the recipes.
    public ArrayList<Recipe> getallRecipes() {
        // on below line we are creating a
        // database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_RECIPES, null);

        // on below line we are creating a new array list for recipes.
        ArrayList<Recipe> listofRecipes = new ArrayList<>();
       // ArrayList<Ingredient> listofIng = new ArrayList<>();


        // moving our cursor to first position.
        if (cursor.moveToFirst()) {
            do {
                Recipe recipe = new Recipe(cursor.getInt(0), //id
                        cursor.getString(1), //recipename
                        cursor.getString(2), //recipediet_pref //newly added
                        cursor.getString(3), //recipetype
                        cursor.getString(4), //instructions
                        cursor.getString(5), //ingredients
                        cursor.getString(6)); //source



                /*
                //fill the recipe's list of ingredients by calling getallIngredientsofRecipe method
                listofIng = getallIngredientsofRecipe(recipe.get_id());

                recipe.setListofIngr(listofIng);*/

                // on below line we are adding the data from cursor to our recipe array list.
                listofRecipes.add(recipe);

            } while (cursor.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor and db
        // and returning our array list.
        cursor.close();
        db.close();
        return listofRecipes;
    }

    public ArrayList<Recipe> getallRecipesofCertainPref(ArrayList<String> listofpreferences, ArrayList<String> listofmealtype){

        int i = 0;
        int j = 0;

        //boolean to see if recipe with certain id already exists
        boolean exists;

        // on below line we are creating a new array list for chosen recipes.
        ArrayList<Recipe> listofRecipes = new ArrayList<>();


        //adding the recipes based on diet preference
        while (i<listofpreferences.size()){

            //diet preference chosen, changes until all preferences chosen are selected and related recipes are searched for
            String diet_pref = listofpreferences.get(i);


            // on below line we are creating a
            // database for reading our database.
            SQLiteDatabase db = this.getReadableDatabase();

            // on below line we are creating a cursor with query to read data from database.

            //searches for all recipes with certain diet preference
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_RECIPES +  " WHERE " +
                    COLUMN_DIET_PREF + " = '" + diet_pref + "'", null);





            // moving our cursor to first position.
            if (cursor.moveToFirst()) {
                do {
                    Recipe recipe = new Recipe(cursor.getInt(0), //id
                            cursor.getString(1), //recipename
                            cursor.getString(2), //recipediet_pref //newly added
                            cursor.getString(3), //recipetype
                            cursor.getString(4), //instructions
                            cursor.getString(5), //ingredients
                            cursor.getString(6)); //source




                    // on below line we are adding the data from cursor to our recipe array list.
                    /*boolean exists = false;
                    for (Customer customer : customers) {
                        if (customer.getName().equals(name)) {
                            return customer;
                        }
                    }
                    if(!exists) {

                    }*/
                    listofRecipes.add(recipe);

                } while (cursor.moveToNext());
                // moving our cursor to next.
            }

            //Incrementing i in order to move to next meal type if there is any.
            i++;

            // at last closing our cursor and db
            // and returning our array list.
            cursor.close();
            db.close();


        }



        //Adding the recipes based on meal type
        while (j < listofmealtype.size()){

            exists = false;

            //meal type chosen, changes until all meal types chosen are selected and related recipes are searched for
            String meal_type = listofmealtype.get(j);

            // on below line we are creating a
            // database for reading our database.
            SQLiteDatabase db = this.getReadableDatabase();

            // on below line we are creating a cursor with query to read data from database.

            //searches for all recipes with certain meal type
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_RECIPES +  " WHERE " +
                    COLUMN_RECIPE_TYPE + " = '" + meal_type + "'", null);





            // moving our cursor to first position.
            if (cursor.moveToFirst()) {
                do {
                    Recipe recipe = new Recipe(cursor.getInt(0), //id
                            cursor.getString(1), //recipename
                            cursor.getString(2), //recipediet_pref //newly added
                            cursor.getString(3), //recipetype
                            cursor.getString(4), //instructions
                            cursor.getString(5), //ingredients
                            cursor.getString(6)); //source




                    // on below line we are adding the data from cursor to our recipe array list.
                    /*boolean exists = false;
                    for (Customer customer : customers) {
                        if (customer.getName().equals(name)) {
                            return customer;
                        }
                    }
                    if(!exists) {

                    }*/

                    //checking if the recipe found is already on the list of recipes we're going to return.
                    // If it is we make boolean exists true and don't add that recipe in the list

                    //soon to be id of the recipe. We need it to check if the recipe is already in the list.
                    int stbid = recipe.get_id();

                    for (Recipe rec : listofRecipes){
                        if (rec.get_id() == stbid){
                            exists = true;
                            break;
                        }
                        else {
                            exists = false;
                        }
                    }

                    if(!exists){
                        listofRecipes.add(recipe);
                    }


                } while (cursor.moveToNext());
                // moving our cursor to next.
            }

            //Incrementing i in order to move to next meal type if there is any.
            j++;

            // at last closing our cursor and db
            // and returning our array list.
            cursor.close();
            db.close();


        }
        return listofRecipes;

    }

    //TIP METHODS

    // we have created a new method that returns all tips.
    public ArrayList<Tip> getallTips() {
        // on below line we are creating a
        // database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TIPS, null);

        // on below line we are creating a new array list.
        ArrayList<Tip> listofTips = new ArrayList<>();

        // moving our cursor to first position.
        if (cursor.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                listofTips.add(new Tip(cursor.getInt(0), //id

                        cursor.getString(1), //tipname
                        cursor.getString(2), //description
                        cursor.getString(3), //related product
                        cursor.getString(4) //source

                ));
            } while (cursor.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor and db
        // and returning our array list.
        cursor.close();
        db.close();
        return listofTips;
    }

}