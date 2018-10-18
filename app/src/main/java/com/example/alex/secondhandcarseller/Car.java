package com.example.alex.secondhandcarseller;

/**
 * Created by Alex on 10/15/2018.
 */

public class Car {
    private String name;
    private String imgURL;

    public Car(String name, String imgURL) {
        this.name = name;
        this.imgURL = imgURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }
}

