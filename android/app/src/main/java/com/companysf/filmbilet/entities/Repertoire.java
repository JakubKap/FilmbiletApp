package com.companysf.filmbilet.entities;

import com.companysf.filmbilet.services.DateFormat;

public class Repertoire {
    private Movie movie;
    private DateFormat dateFormat;
    private int id;

    public Repertoire(Movie movie, String dateTime, int id) {
        this.movie = movie;
        this.dateFormat = new DateFormat(dateTime);
        this.id = id;
    }

    public Repertoire(int id,String dateTime){
        this.id = id;
        this.dateFormat = new DateFormat(dateTime);
    }

    public Movie getMovie() {
        return movie;
    }

    public DateFormat getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
