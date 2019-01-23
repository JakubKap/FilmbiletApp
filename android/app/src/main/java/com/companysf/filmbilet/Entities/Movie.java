package com.companysf.filmbilet.Entities;

import java.io.Serializable;

public class Movie implements Serializable {
    private String title, pictureURL, genres;
    private int runningTimeMin, age, id;

    public Movie(int id, String title, int runningTimeMin, int age, String pictureURL, String genres) {
        this.title = title;
        this.runningTimeMin = runningTimeMin;
        this.age = age;
        this.pictureURL = pictureURL;
        this.genres = genres;
        this.id = id;
    }

    public Movie(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public int getRunningTimeMin() {
        return runningTimeMin;
    }

    public int getAge() {
        return age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
