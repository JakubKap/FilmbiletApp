package com.companysf.filmbilet.appLogic;


public class CustomerReservation {
    private String seatNumbers;
    private Date reservationDate;
    private Repertoire repertoire;
    private float price;

    public CustomerReservation(String seatNumbers, Date reservationDate,
                               Repertoire repertoire, float price) {
        this.seatNumbers = seatNumbers;
        this.reservationDate = reservationDate;
        this.repertoire = repertoire;
        this.price = price;
    }

    public CustomerReservation(String seatNumbers, String reservationDate,
                               Repertoire repertoire, float price) {
        this.seatNumbers = seatNumbers;
        this.reservationDate = new Date(reservationDate);
        this.repertoire = repertoire;
        this.price = price;
    }

    public Date getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(Date reservationDate) {
        this.reservationDate = reservationDate;
    }

    public Repertoire getRepertoire() {
        return repertoire;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getSeatNumbers() {
        return seatNumbers;
    }

    public void setSeatNumbers(String seatNumbers) {
        this.seatNumbers = seatNumbers;
    }
}
