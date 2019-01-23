package com.companysf.filmbilet.Entities;

public class Repertoire {
    private Movie movie;
    private Date date;
    private int id;

    public Repertoire(Movie movie, String dateTime, int id) {
        this.movie = movie;
        this.date = new Date(dateTime);
        this.id = id;
    }

    public Movie getMovie() {
        return movie;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
