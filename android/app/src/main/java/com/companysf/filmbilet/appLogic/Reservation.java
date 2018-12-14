package com.companysf.filmbilet.appLogic;

import java.text.SimpleDateFormat;

public class Reservation {

    private int id, customerId, seatNumber, row, seatTypeId;
    String datePom;

    public Reservation(int id, int customerId, int hall, int seatNumber, int row, String datePom, int seatTypeId) {
        this.id = id;

        this.customerId = customerId;
        this.seatNumber = seatNumber;
        this.row = row;
        this.datePom = datePom;
        this.seatTypeId = seatTypeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getSeatTypeId() {
        return seatTypeId;
    }

    public void setSeatTypeId(int seatTypeId) {
        this.seatTypeId = seatTypeId;
    }

    public String getDatePom() {
        return datePom;
    }

    public void setDatePom(String datePom) {
        this.datePom = datePom;
    }


}
