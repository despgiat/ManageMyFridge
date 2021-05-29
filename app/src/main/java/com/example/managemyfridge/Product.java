package com.example.managemyfridge;

public class Product {
    private int _id;
    private String _productname;
    private int _quantity;


    private String _exdate;
    private boolean _opened;
    private String _prodtype;
    private String _DateofOpening;
    private String _img;
    private String _unit;

    public Product() {
    }

    public Product(int id, String productname, int quantity) {
        this.setID(id);
        this._productname = productname;
        this._quantity = quantity;
    }

    public Product(String productname, int quantity) {
        this._productname = productname;
        this._quantity = quantity;
    }

    public int getID() {
        return _id;
    }

    public void setID(int _id) {
        this._id = _id;
    }

    public void setProductName(String productname) {
        this._productname = productname;
    }

    public String getProductName() {
        return this._productname;
    }

    public void setQuantity(int quantity) {
        this._quantity = quantity;
    }

    public int getQuantity() {
        return this._quantity;
    }

    public String get_exdate() {
        return _exdate;
    }

    public void set_exdate(String _exdate) {
        this._exdate = _exdate;
    }

    public boolean isOpened() {
        return _opened;
    }

    public void setOpened(boolean _opened) {
        this._opened = _opened;
    }

    public String get_prodtype() {
        return _prodtype;
    }

    public void set_prodtype(String _prodtype) {
        this._prodtype = _prodtype;
    }

    public String get_DateofOpening() {
        return _DateofOpening;
    }

    public void set_DateofOpening(String _DateofOpening) {
        this._DateofOpening = _DateofOpening;
    }

    public String get_img() {
        return _img;
    }

    public void set_img(String _img) {
        this._img = _img;
    }

    public String get_unit() {
        return _unit;
    }

    public void set_unit(String _unit) {
        this._unit = _unit;
    }


    //toString method
    @Override
    public String toString() {
        return "Product{" +
                "id=" + _id +
                ", product name='" + _productname + '\'' +
                ", quantity=" + _quantity + '\'' +
                ", expiration date='" + _exdate + '\'' +
                ", is it open?='" + _opened + '\'' +
                ", type='" + _prodtype + '\'' +
                ", date of opening='" + _DateofOpening + '\'' +
                ", image='" + _img + '\'' +
                ", unit='" + _unit +
                '}';
    }
}