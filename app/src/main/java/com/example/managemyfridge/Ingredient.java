package com.example.managemyfridge;

public class Ingredient {
    private int _idofRECIPE;
    private String _ingredientname;
    private int _quantity;
    private String _unit;

    public Ingredient() {
    }

    //Added to product: exDate, isitopen, type, dateofopening, unit
    public Ingredient(int idofRECIPE, String ingredientname, int quantity, String unit) {
        this._idofRECIPE = idofRECIPE;
        this._ingredientname = ingredientname;
        this._quantity = quantity;
        this._unit = unit;
    }

    public int get_idofRECIPE() {
        return _idofRECIPE;
    }

    public void set_idofRECIPE(int _idofRECIPE) {
        this._idofRECIPE = _idofRECIPE;
    }

    public String get_ingredientname() {
        return _ingredientname;
    }

    public void set_ingredientname(String _ingredientname) {
        this._ingredientname = _ingredientname;
    }

    public int get_quantity() {
        return _quantity;
    }

    public void set_quantity(int _quantity) {
        this._quantity = _quantity;
    }

    public String get_unit() {
        return _unit;
    }

    public void set_unit(String _unit) {
        this._unit = _unit;
    }




}