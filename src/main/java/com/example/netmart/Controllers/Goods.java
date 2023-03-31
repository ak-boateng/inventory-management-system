package com.example.netmart.Controllers;

public class Goods {
    Integer good_id;
    String good_name;
    Integer quantity;
    Integer buying_price;
    Integer selling_price;
    Integer gross_price;
    String date;

    public Goods(Integer good_id, String good_name, Integer quantity, Integer buying_price, Integer selling_price, Integer gross_price, String date) {
        this.good_id = good_id;
        this.good_name = good_name;
        this.quantity = quantity;
        this.buying_price = buying_price;
        this.selling_price = selling_price;
        this.gross_price = gross_price;
        this.date = date;
    }


    public Integer getGood_id() {
        return good_id;
    }

    public String getGood_name() {
        return good_name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getBuying_price() {
        return buying_price;
    }

    public Integer getSelling_price() {
        return selling_price;
    }

    public Integer getGross_price() {
        return gross_price;
    }

    public String getDate() {
        return date;
    }


    public void setGood_id(Integer good_id) {
        this.good_id = good_id;
    }

    public void setGood_name(String good_name) {
        this.good_name = good_name;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setBuying_price(Integer buying_price) {
        this.buying_price = buying_price;
    }

    public void setSelling_price(Integer selling_price) {
        this.selling_price = selling_price;
    }

    public void setGross_price(Integer gross_price) {
        this.gross_price = gross_price;
    }

    public void setDate_price(String date) {
        this.date = date;
    }
}