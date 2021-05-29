package com.example.managemyfridge;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MyDBHandler extends SQLiteOpenHelper {

    //Σταθερές για τη ΒΔ (όνομα ΒΔ, έκδοση, πίνακες κλπ)
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MMFDB.db";
    public static final String TABLE_PRODUCTS = "PRODUCT";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PRODUCTNAME = "Name";
    public static final String COLUMN_EXDATE = "Expiration_Date";
    public static final String COLUMN_IS_IT_OPEN = "Is_it_open";
    public static final String COLUMN_TYPE = "PRODUCT_Type";
    public static final String COLUMN_DATE_OF_OPENING = "Date_of_opening";
    public static final String COLUMN_QUANTITY = "Quantity";
    public static final String COLUMN_UNIT = "Unit";

    public static final String TABLE_USERS = "USERS";
    public static final String COLUMN_USERNAME = "Username";
    public static final String COLUMN_PASSWORD = "User_Password";
    public static final String COLUMN_IMAGE = "Img";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-mm-yyyy");

    //Constructor
    public MyDBHandler(Context context, String name,
                       SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    //Δημιουργία του σχήματος της ΒΔ (πίνακας products)
    //this is called the first time a database is accessed
    //Added autoincrement in id key
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " +
                TABLE_PRODUCTS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_PRODUCTNAME + " TEXT NOT NULL," +
                COLUMN_QUANTITY + " INTEGER NOT NULL," +
                COLUMN_EXDATE + " TEXT," +
                COLUMN_IS_IT_OPEN + " TEXT," +
                COLUMN_TYPE + " TEXT  ," + //NOT NULL Here
                COLUMN_DATE_OF_OPENING + " TEXT," +
                COLUMN_IMAGE + " TEXT," +
                COLUMN_UNIT + " TEXT" +
                ")";
        db.execSQL(CREATE_PRODUCTS_TABLE);
    }


    //Αναβάθμιση ΒΔ: εδώ τη διαγραφώ και τη ξαναδημιουργώ ίδια
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        onCreate(db);
    }

    //Μέθοδος για προσθήκη ενός προϊόντος στη ΒΔ
    public void addProduct(Product product) {
        //added: exdate, isopened, prodtype, dateofopening, img, unit

        System.out.println("PRODUCT TO BE ADDED");

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
            product.setQuantity(Integer.parseInt(cursor.getString(2)));
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

    //add user, needs changes
   /* public void addUser(Users user){
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCTNAME, product.getProductName());
        values.put(COLUMN_QUANTITY, product.getQuantity());
        SQLiteDatabase db = this.getWritableDatabase();
        //original
        db.insert(TABLE_PRODUCTS, null, values);
        db.close();

    }

    //find user, needs changes (query)
    public Users findUser(String username) {
        String query = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " +
                COLUMN_PRODUCTNAME + " = '" + username + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Users user = new Users();
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            user.setID(Integer.parseInt(cursor.getString(0)));
            user.setUsername(cursor.getString(1));
            user.setPassword(cursor.getString(2));
            user.setImg(cursor.getString(3));
            cursor.close();
        } else {
            user = null;
        }
        db.close();
        return user;
    }

    //delete user, needs changes
    public boolean deleteUser(String username) {
        boolean result = false;
        String query = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " +
                COLUMN_PRODUCTNAME + " = '" + username + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Users user = new Users();
        if (cursor.moveToFirst()) {
            user.setID(Integer.parseInt(cursor.getString(0)));
            db.delete(TABLE_PRODUCTS, COLUMN_ID + " = ?",
                    new String[] { String.valueOf(user.getID()) });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    } */

    // we have created a new method that returns all the products in the fridge.
    public ArrayList<Product> showallProducts() {
        // on below line we are creating a
        // database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS, null);

        // on below line we are creating a new array list.
        ArrayList<Product> fridgeProducts = new ArrayList<>();

        // moving our cursor to first position.
        if (cursor.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                fridgeProducts.add(new Product(cursor.getInt(0), //id
                        cursor.getString(1), //name
                        cursor.getInt(2), //quantity
                        cursor.getString(3), //expiration date
                        cursor.getString(4), //is_it_open
                        cursor.getString(5), //type
                        cursor.getString(6), //date of opening
                        //image      cursor.getString(7),
                        cursor.getString(8) //unit

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

}