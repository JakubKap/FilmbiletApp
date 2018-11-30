package com.companysf.filmbilet.appLogic;

import java.text.SimpleDateFormat;

public class Reservation {

    private int customerId, hall, seatNumber, row, seatTypeId, repertoireId;
    SimpleDateFormat date;

    public Reservation(int customerId, int hall, int seatNumber, int row, int seatTypeId, int repertoireId, SimpleDateFormat date) {
        this.customerId = customerId;
        this.hall = hall;
        this.seatNumber = seatNumber;
        this.row = row;
        this.seatTypeId = seatTypeId;
        this.repertoireId = repertoireId;
        this.date = date;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getHall() {
        return hall;
    }

    public void setHall(int hall) {
        this.hall = hall;
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

    public int getRepertoireId() {
        return repertoireId;
    }

    public void setRepertoireId(int repertoireId) {
        this.repertoireId = repertoireId;
    }

    public SimpleDateFormat getDate() {
        return date;
    }

    public void setDate(SimpleDateFormat date) {
        this.date = date;
    }
}
