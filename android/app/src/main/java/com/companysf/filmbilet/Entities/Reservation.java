package com.companysf.filmbilet.Entities;

public class Reservation {

    private int id;
    private int seatNumber;
    private int row;

    public Reservation(int id, int seatNumber, int row) {
        this.id = id;
        this.seatNumber = seatNumber;
        this.row = row;
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

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }
}
