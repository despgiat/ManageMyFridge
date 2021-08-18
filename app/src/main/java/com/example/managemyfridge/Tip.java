package com.example.managemyfridge;

import java.util.ArrayList;

public class Tip {


    private int _id;
    private String _tipname;
    private String _description;
    private String _related_product;
    //private String _favoured;
    private String _source;
    private String _img;

    public Tip() {
    }


    //old constructor
    /*public Tip(int id, String tipname, String description, String related_product, String isitfav) {
        this.set_id(id);
        this._tipname = tipname;
        this._description = description;
        this._related_product = related_product;
        this._favoured = isitfav;
    }*/


    //new constr. with source
    public Tip(int id, String tipname, String description, String related_product, String source) {
        this.set_id(id);
        this._tipname = tipname;
        this._description = description;
        this._related_product = related_product;
        this._source = source;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_tipname() {
        return _tipname;
    }

    public void set_tipname(String _tipname) {
        this._tipname = _tipname;
    }

    public String get_description() {
        return _description;
    }

    public void set_description(String _description) {
        this._description = _description;
    }

    public String get_related_product() {
        return _related_product;
    }

    public void set_related_product(String _related_product) {
        this._related_product = _related_product;
    }

   /* public String get_favoured() {
        return _favoured;
    }

    public void set_favoured(String _favoured) {
        this._favoured = _favoured;
    } */

    public String get_source() {
        return _source;
    }

    public void set_source(String _source) {
        this._source = _source;
    }

    public String get_img() {
        return _img;
    }

    public void set_img(String _img) {
        this._img = _img;
    }
}