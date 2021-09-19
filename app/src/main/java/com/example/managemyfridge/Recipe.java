package com.example.managemyfridge;

import java.util.ArrayList;

public class Recipe {

    private int _id;
    private String _recipename;
    private String _recipetype;
    private String _instructions;
    //private String _favoured;
    private String _img;
    private String _source;

    //NEW: ADDED INGREDIENTS, diet preference
    private String _ingredients;
    private String _diet_pref;


    //private ArrayList<Ingredient> listofIngr;


    public Recipe() {
    }

   /* public Recipe(int id, String recipename, String recipeType, String instructions, String isitfav) {
        this.set_id(id);
        this._recipename = recipename;
        this._recipetype = recipeType;
        this._instructions = instructions;
        this._favoured = isitfav;
        this.listofIngr = new ArrayList<>();
    }*/

    /*
    //old constructor without ingredients
    public Recipe(int id, String recipename, String recipeType, String instructions, String source) {
        this.set_id(id);
        this._recipename = recipename;
        this._recipetype = recipeType;
        this._instructions = instructions;
        this._source = source;
        this.listofIngr = new ArrayList<>();
    }*/

    //NEW: CONSTRUCTOR WITH INGREDIENTS, diet preference
    public Recipe(int id, String recipename, String diet_pref, String recipeType, String instructions,String ingredients, String source) {
        this.set_id(id);
        this._recipename = recipename;
        this._diet_pref = diet_pref;
        this._recipetype = recipeType;
        this._instructions = instructions;
        this._ingredients = ingredients;
        this._source = source;

    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_recipename() {
        return _recipename;
    }

    public void set_recipename(String _recipename) {
        this._recipename = _recipename;
    }

    public String get_recipetype() {
        return _recipetype;
    }

    public void set_recipetype(String _recipetype) {
        this._recipetype = _recipetype;
    }

    public String get_instructions() {
        return _instructions;
    }

    public void set_instructions(String _instructions) {
        this._instructions = _instructions;
    }

   /* public String get_favoured() {
        return _favoured;
    }

    public void set_favoured(String _favoured) {
        this._favoured = _favoured;
    }*/

    public String get_img() {
        return _img;
    }

    public void set_img(String _img) {
        this._img = _img;
    }


   /* public ArrayList<Ingredient> getListofIngr() {
        return listofIngr;
    }

    public void setListofIngr(ArrayList<Ingredient> listofIngr) {
        this.listofIngr = listofIngr;
    }*/


     public String get_source() {
        return _source;
    }

    public void set_source(String _source) {
        this._source = _source;
    }

    //NEW: ADDED GETTER AND SETTER FOR INGREDIENTS
    public String get_ingredients() {
        return _ingredients;
    }

    public void set_ingredients(String _ingredients) {
        this._ingredients = _ingredients;
    }


    //NEW: ADDED GETTER AND SETTER FOR diet preference
    public String get_diet_pref() {
        return _diet_pref;
    }

    public void set_diet_pref(String _diet_pref) {
        this._diet_pref = _diet_pref;
    }



}