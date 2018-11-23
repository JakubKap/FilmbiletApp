package com.companysf.filmbilet.appLogic;

public class Movie {
    private String title, languageVersion, description, pictureURL;
    private int runningTimeMin, age;
    private long unixTime;

    public Movie(String title, String languageVersion, String description, String pictureURL, int runningTimeMin, int age, long unixTime) {
        this.title = title;
        this.languageVersion = languageVersion;
        this.description = description;
        this.pictureURL = pictureURL;
        this.runningTimeMin = runningTimeMin;
        this.age = age;
        this.unixTime = unixTime;
    }
}
