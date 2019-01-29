package com.companysf.filmbilet.entities;

public class Reservation {

    private int id;
    private int seatNumber;

    public Reservation(int id, int seatNumber) {
        this.id = id;
        this.seatNumber = seatNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

}
