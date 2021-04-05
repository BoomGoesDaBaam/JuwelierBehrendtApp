package com.example.juwelierbehrendt;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class Product {
    private String name;
    private float price;
    private int discount;
    private String brand;
    private String what;
    private String description;
    private ArrayList<Bitmap> pics;
    private Date created;
    private Date updated;
    private String objectId;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public Product()
    {
        name = "-";
        price = 0;
        discount = 0;
        brand = "brandless";
        what = "nothing";
        pics = new ArrayList<>();
        created = new Date();
        updated = new Date();
    }
    public void setPics(ArrayList<Bitmap> pics) {
        this.pics = pics;
    }

    public ArrayList<Bitmap> getPics() {
        return pics;
    }

    public void addPic(Bitmap pic) {
        this.pics.add(pic);
    }
    public void deletePic(int index) {
        this.pics.remove(index);
    }

    public String getWhat() {
        return what;
    }

    public void setWhat(String what) {
        this.what = what;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }
    public float getPriceIncDiscount() {
        return price * ((100f - discount)/100);
    }
    public void setPrice(float price) {
        this.price = price;
    }
}
