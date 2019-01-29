package com.companysf.filmbilet.entities;


import com.companysf.filmbilet.services.DateFormat;

public class CustomerReservation {
    private String seatNumbers;
    private DateFormat reservationDateFormat;
    private Repertoire repertoire;
    private float price;

    public CustomerReservation(String seatNumbers, String reservationDate,
                               Repertoire repertoire, float price) {
        this.seatNumbers = seatNumbers;
        this.reservationDateFormat = new DateFormat(reservationDate);
        this.repertoire = repertoire;
        this.price = price;
    }

    public DateFormat getReservationDateFormat() {
        return reservationDateFormat;
    }

    public Repertoire getRepertoire() {
        return repertoire;
    }

    public float getPrice() {
        return price;
    }

    public String getSeatNumbers() {
        return seatNumbers;
    }
}
