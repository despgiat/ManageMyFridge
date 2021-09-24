package com.example.managemyfridge;


import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

public class Users {
    private int _id;
    private String username;
    private  String email;
    private String password;
   // private String img;
    private ArrayList<Integer> favoriteRecipes;
    private String favoriteRecipesString;
    private ArrayList<Integer> favoriteTips;
    private String favoriteTipsString;

    //user constructor
    public Users(){
        favoriteRecipes = new ArrayList<>();
        favoriteRecipesString = "";
        favoriteTipsString = "";
        favoriteTips = new ArrayList<>();
    }

    //getter and setter for user id
    public int getID() {
        return _id;
    }
    public void setID(int _id) {
        this._id = _id;
    }

    //getter and setter for user name
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    //getter and setter for user password
    public String getPassword() {return password;}
    public void setPassword(String password) { this.password = password; }

    /*
    public String getImg() {return img; }
    public void setImg(String img) {this.img = img;}
    */
    //getter and setter for email
    public String getEmail() { return email;}
    public void setEmail(String email) {this.email = email;}

    /*
        method for converting an int array to string in the following format:
        if [1,2,3] => string = "1,2,3". The numbers are representing ids.
    */
      public String arrayToString(ArrayList<Integer> favorites)
    {
        StringBuilder sb = new StringBuilder();

        for (Integer s : favorites) {
            sb.append(s);
            sb.append(",");
        }
        return sb.toString();
    }
    /*
        method for converting a string of the following format:
        string = "1,2,3" to an array of integers : [1,2,3]. The numbers are representing ids.
    */
    public ArrayList<Integer> stringToArray(String favorites)
    {
        String[] splitArray = favorites.split(",");
        ArrayList<Integer> tempFavs = new ArrayList<>();
        //parsing the String argument as a signed decimal integer object and
        //storing that integer into the array
        for (String s : splitArray) {
            tempFavs.add(Integer.parseInt(s));
        }
        return tempFavs;
    }

    // getter and setter for favorite recipe string of the format : "1,2,3" . The numbers are representing ids.
    public String getRecipeString(){return favoriteRecipesString;}
    public void setFavRecipeString(String favs){ this.favoriteRecipesString=favs; }

    //getter and setter for favorite recipe array [1,2,3]
    public void setFavRecipeArray(ArrayList<Integer> favorites){this.favoriteRecipes = favorites;}
    public ArrayList<Integer> getFavoriteRecipesArray(){ return this.favoriteRecipes;}

    /* method for adding a recipe to user's favorites.
        1: we add the recipe's id to the favorite recipe array
        2: we update the favorite recipe string by converting the new array to string.
     */
    public void addFavoriteRecipe(int a){this.favoriteRecipes.add(a); favoriteRecipesString = arrayToString(this.favoriteRecipes);}

    /* method for removing a recipe from user's favorites.
        1: we remove the recipe's id from the favorite recipe array
        2: we update the favorite recipe string by converting the new array to string.
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void removeFavoriteRecipe(int a){this.favoriteRecipes.removeIf(s -> s.equals(a)); favoriteRecipesString = arrayToString(this.favoriteRecipes);}

    // getter and setter for favorite tip string of the format : "1,2,3" . The numbers are representing ids.
    public String getTipString(){return favoriteTipsString;}
    public void setFavoriteTipsString(String favs){ this.favoriteTipsString=favs; }

    //getter and setter for favorite tip array [1,2,3]
    public void setFavoriteTipsArray(ArrayList<Integer> favorites){this.favoriteTips = favorites;}
    public ArrayList<Integer> getFavoriteTipsArray(){ return this.favoriteTips;}

    /* method for adding a tip to user's favorites.
            1: we add the tip's id to the favorite recipe array
            2: we update the favorite tip string by converting the new array to string.
         */
    public void addFavoriteTip(int a){this.favoriteTips.add(a); favoriteTipsString = arrayToString(this.favoriteTips);}

    /* method for removing a tip from user's favorites.
        1: we remove the tip's id from the favorite recipe array
        2: we update the favorite tip string by converting the new array to string.
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void removeFavoriteTip(int a){this.favoriteTips.removeIf(s -> s.equals(a)); favoriteTipsString = arrayToString(this.favoriteTips);}



}