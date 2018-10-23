package com.example.alex.secondhandcarseller;

/**
 * Created by Alex on 10/23/2018.
 */

public class Car {
    String carNames;
   int carImages;

    public Car(String carNames, int carImages) {
        this.carNames = carNames;
        this.carImages = carImages;
    }

    public Car() {
    }

    public String getCarNames() {
        return carNames;
    }

    public int getCarImages() {
        return carImages;
    }
}
