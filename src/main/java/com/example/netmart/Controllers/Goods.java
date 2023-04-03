package com.example.netmart.Controllers;

public class Goods {
    String good_id;
    String good_name;
    String quantity;
    String buying_price;
    String selling_price;
    String gross_price;
    String date;

    public Goods(String good_id, String good_name, String quantity, String buying_price, String selling_price, String gross_price, String date) {
        this.good_id = good_id;
        this.good_name = good_name;
        this.quantity = quantity;
        this.buying_price = buying_price;
        this.selling_price = selling_price;
        this.gross_price = gross_price;
        this.date = date;
    }


    public String getGood_id() {
        return good_id;
    }

    public String getGood_name() {
        return good_name;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getBuying_price() {
        return buying_price;
    }

    public String getSelling_price() {
        return selling_price;
    }

    public String getGross_price() {
        return gross_price;
    }

    public String getDate() {
        return date;
    }


    public void setGood_id(String good_id) {
        this.good_id = good_id;
    }

    public void setGood_name(String good_name) {
        this.good_name = good_name;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setBuying_price(String buying_price) {
        this.buying_price = buying_price;
    }

    public void setSelling_price(String selling_price) {
        this.selling_price = selling_price;
    }

    public void setGross_price(String gross_price) {
        this.gross_price = gross_price;
    }

    public void setDate_price(String date) {
        this.date = date;
    }
}