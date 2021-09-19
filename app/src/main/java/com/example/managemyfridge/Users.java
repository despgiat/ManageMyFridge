package com.example.managemyfridge;


import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.HashMap;

public class Users {

    private int _id;
    private String username;
    private  String email;
    private String password;
    private String img;
    private Product[] fridgeItems;
    private ArrayList<Integer> favorites;
    private String favs;

    public Users(){
        this.favorites = new ArrayList<>();
        favs = null;
    }


    public int getID() {
        return _id;
    }

    public void setID(int _id) {
        this._id = _id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFavs(){ arrayToString(); return favs; }

    public void setFavs(String favs){ this.favs=favs; }

    public void setFavorites(ArrayList<Integer> favorites){this.favorites = favorites;}

    public ArrayList<Integer> getFavorites(){ return this.favorites;}

    public void addFavorite(int a){this.favorites.add(a);}

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void removeFavorite(int a){this.favorites.removeIf(s -> s.equals(a));}

    public void arrayToString()
    {
        StringBuffer sb = new StringBuffer();

        for (Integer s : this.favorites) {
            sb.append(s);
            sb.append(",");
        }
        this.favs= sb.toString();
    }

    public void stringToArray()
    {
        String[] splitArray = this.favs.split(",");

        //parsing the String argument as a signed decimal integer object and
        //storing that integer into the array
        for(int i=0;i<splitArray.length;i++)
        {
            this.favorites.add(Integer.parseInt(splitArray[i]));
        }

    }

}