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
    private ArrayList<Integer> favoriteRecipes;
    private String favoriteRecipesString;
    private ArrayList<Integer> favoriteTips;
    private String favoriteTipsString;

    public Users(){
        this.favoriteRecipes = new ArrayList<>();
        favoriteRecipesString = null;
        favoriteTipsString = null;
        favoriteTips = null;
        favoriteRecipes = null;
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

    public String getFavoriteRecipesString(){ favoriteRecipesString = arrayToString(favoriteRecipes); return favoriteRecipesString; }

    public void setFavRecipeString(String favs){ this.favoriteRecipesString=favs; }

    public void setFavRecipeArray(ArrayList<Integer> favorites){this.favoriteRecipes = favorites;}

    public ArrayList<Integer> getFavoriteRecipesArray(){ return this.favoriteRecipes;}

    public void addFavoriteRecipe(int a){this.favoriteTips.add(a);}

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void removeFavoriteRecipe(int a){this.favoriteRecipes.removeIf(s -> s.equals(a));}

    public String arrayToString(ArrayList<Integer> favorites)
    {
        StringBuffer sb = new StringBuffer();

        for (Integer s : favorites) {
            sb.append(s);
            sb.append(",");
        }
        return sb.toString();
    }

    public ArrayList<Integer> stringToArray(String favorites)
    {
        String[] splitArray = favorites.split(",");
        ArrayList<Integer> tempFavs = new ArrayList<>();
        //parsing the String argument as a signed decimal integer object and
        //storing that integer into the array
        for(int i=0;i<splitArray.length;i++)
        {
            tempFavs.add(Integer.parseInt(splitArray[i]));
        }
        return tempFavs;
    }

    public String getFavoriteTipsString(){ favoriteTipsString = arrayToString(favoriteTips); return favoriteTipsString; }

    public void setFavoriteTipsString(String favs){ this.favoriteTipsString=favs; }

    public void setFavoriteTipsArray(ArrayList<Integer> favorites){this.favoriteTips = favorites;}

    public ArrayList<Integer> getFavoriteTipsArray(){ return this.favoriteTips;}

    public void addFavoriteTip(int a){this.favoriteTips.add(a);}

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void removeFavoriteTip(int a){this.favoriteTips.removeIf(s -> s.equals(a));}



}