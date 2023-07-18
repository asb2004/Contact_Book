package com.example.contactbook.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.contactbook.MainActivity;
import com.example.contactbook.R;

import java.io.Serializable;

public class Contact implements Serializable {
    private int id;
    private String name;
    private String number;
    private transient Bitmap image;

    public Contact() {

    }

    public Contact(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public Contact(String name, String number, Bitmap image) {
        this.name = name;
        this.number = number;
        this.image = image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Bitmap getImage() {
        return image;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
