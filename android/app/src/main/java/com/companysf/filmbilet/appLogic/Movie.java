package com.companysf.filmbilet.appLogic;

public class Movie {
    private String title, pictureURL, genres;
    private int runningTimeMin, age;

    public Movie(String title, int runningTimeMin, int age, String pictureURL, String genres) {
        this.title = title;
        this.runningTimeMin = runningTimeMin;
        this.age = age;
        this.pictureURL = pictureURL;
        this.genres = genres;
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
}
