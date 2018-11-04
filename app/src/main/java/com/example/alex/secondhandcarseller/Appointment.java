package com.example.alex.secondhandcarseller;

/**
 * Created by Bbao on 05/11/2018.
 */

public class Appointment {
    String appID,appDateNTime,appStatus;

    public Appointment(String appID, String appDateNTime, String appStatus) {
        this.appID = appID;
        this.appDateNTime = appDateNTime;
        this.appStatus = appStatus;
    }

    public String getAppID() {
        return appID;
    }

    public String getAppDateNTime() {
        return appDateNTime;
    }

    public String getAppStatus() {
        return appStatus;
    }
}
