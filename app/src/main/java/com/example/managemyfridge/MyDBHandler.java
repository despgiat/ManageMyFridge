package com.example.managemyfridge;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MyDBHandler extends SQLiteOpenHelper {

    //Σταθερές για τη ΒΔ (όνομα ΒΔ, έκδοση, πίνακες κλπ)

    //New: Added PATH, mDataBase, mContext, mNeedUpdate and changed the name of the DB to MMFDB3
    private static String DATABASE_PATH =  "";
    private SQLiteDatabase mDataBase;
    private final Context mContext;
    private boolean mNeedUpdate = false;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MMFDB3.db";
    public static final String TABLE_PRODUCTS = "PRODUCT";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PRODUCTNAME = "Name";
    public static final String COLUMN_EXDATE = "Expiration_Date";
    public static final String COLUMN_IS_IT_OPEN = "Is_it_open";
    public static final String COLUMN_TYPE = "PRODUCT_Type";
    public static final String COLUMN_DATE_OF_OPENING = "Date_of_opening";
    public static final String COLUMN_QUANTITY = "Quantity";
    public static final String COLUMN_UNIT = "Unit";

    //NEW: IDOFUSER
    private static final String COLUMN_ID_OF_USER = "_idofUSER";

    public static final String TABLE_USERS = "USERS";
    public static final String COLUMN_USERNAME = "Username";
    public static final String COLUMN_EMAIL = "Email";
    public static final String COLUMN_PASSWORD = "User_Password";
    public static final String COLUMN_FAVORITES = "Fav_Recipes";
    public static final String COLUMN_TIPS = "Fav_Tips";

    public static final String TABLE_RECIPES = "RECIPE";
   /* public static final String COLUMN_RECIPENAME = "Name";
    public static final String COLUMN_RECIPE_TYPE = "Type_of_Recipe";
    public static final String COLUMN_INSTRUCTIONS = "Instructions";
    public static final String COLUMN_IS_IT_FAV = "Is_it_fav";

    public static final String COLUMN_SOURCE = "Source"; //NEWLY ADDED*/

   //NEW: COLUMN FOR DIET PREFERENCE
   public static final String COLUMN_DIET_PREF = "Diet_Preference";

    public static final String TABLE_TIPS = "TIP";
   /* public static final String COLUMN_TIPNAME = "Name";
    public static final String COLUMN_DESCRIPTION = "Description";
    public static final String COLUMN_RELATED_PRODUCT = "Related_Product";*/
    public static final String COLUMN_IMAGE = "Img";

   /* public static final String TABLE_INGREDIENTS = "INGREDIENT";
    public static final String COLUMN_INGREDIENTNAME = "Ingredient_Name";
    public static final String COLUMN_ID_OF_RECIPE = "_idofRECIPE"; */



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

    /*public boolean openDataBase() throws SQLException {
        mDataBase = SQLiteDatabase.openDatabase(DATABASE_PATH + DATABASE_NAME, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        return mDataBase != null;
    }*/

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





    //PRODUCT METHODS


    //Μέθοδος για προσθήκη ενός προϊόντος στη ΒΔ
    public void addProduct(Product product, int idofuser) {
        //added: exdate, isopened, prodtype, dateofopening, img, unit
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCTNAME, product.getProductName());
        values.put(COLUMN_QUANTITY, product.getQuantity());
        values.put(COLUMN_EXDATE, product.get_exdate());
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


    //adding user to data base
    public void addUser(){
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, LoginScreen.user.getEmail());
        values.put(COLUMN_USERNAME, LoginScreen.user.getUsername());
        values.put(COLUMN_PASSWORD, LoginScreen.user.getPassword());
        values.put(COLUMN_FAVORITES, LoginScreen.user.getRecipeString());
        values.put(COLUMN_TIPS, LoginScreen.user.getRecipeString());

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
          //  LoginScreen.user.setImg(cursor.getString(4));
            LoginScreen.user.setFavRecipeString(cursor.getString(5));
            LoginScreen.user.setFavoriteTipsString(cursor.getString(6));
            //if the favorite's string is not empty, then we fill the favorite's array
            if (!LoginScreen.user.getTipString().equals("")){
                LoginScreen.user.setFavoriteTipsArray(LoginScreen.user.stringToArray(LoginScreen.user.getTipString()));
            }
            if (!LoginScreen.user.getRecipeString().equals("")){
                LoginScreen.user.setFavRecipeArray(LoginScreen.user.stringToArray(LoginScreen.user.getRecipeString()));
                System.out.println(LoginScreen.user.getRecipeString());
            }
            cursor.close();
            flag = true;
        } else {
            LoginScreen.user = null; //otherwise we delete everything from user object
        }
        db.close();
        return flag;
    }

    /*

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
    }*/

    //update user
    public void updateUser (){

        ContentValues values = new ContentValues();
        values.put(COLUMN_PASSWORD, LoginScreen.user.getPassword());
        values.put(COLUMN_USERNAME, LoginScreen.user.getUsername());
        values.put(COLUMN_EMAIL, LoginScreen.user.getEmail());
        values.put(COLUMN_FAVORITES, LoginScreen.user.getRecipeString());
        values.put(COLUMN_TIPS, LoginScreen.user.getTipString());

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




    //RECIPE METHODS

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

    //returns all recipes of certain diet preference, meal type (and ingredients later).
    public ArrayList<Recipe> getallRecipesofCertainPref(ArrayList<String> listofpreferences, ArrayList<String> listofmealtype){

        int i = 0;
        int j = 0;


        // on below line we are creating a new array list for chosen recipes.
        ArrayList<Recipe> listofRecipes = new ArrayList<>();

        // on below line we are creating a new helping array list for temporarily chosen recipes.
        ArrayList<Recipe> temp = new ArrayList<>();


        //First loop
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




                    // on below line we are adding the data from cursor to our temp recipe array list.
                    //listofRecipes.add(recipe);
                    temp.add(recipe);

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


        //Second loop
        //Adding the recipes based on meal type
        while (j < listofmealtype.size()){

            //meal type chosen, changes until all meal types chosen are selected and related recipes are searched for
            String meal_type = listofmealtype.get(j);



            // on below line we are adding the recipes of chosen meal type to our recipe array list.

            for (Recipe rec : temp){
                if (rec.get_recipetype().equals(meal_type)){
                    listofRecipes.add(rec);

                }
            }



            //Incrementing i in order to move to next meal type if there is any.
            j++;



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