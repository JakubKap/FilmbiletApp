package com.companysf.filmbilet.entities;

public class Sector {

    private int freeSeats;
    private int[] seatNumbers;
    private int startSecSeat;
    private int sectorPrice;

    public Sector(int freeSeats, int[] seatNumbers, int startSecSeat, int sectorPrice) {
        this.freeSeats = freeSeats;
        this.seatNumbers = seatNumbers;
        this.startSecSeat = startSecSeat;
    }

    public int getFreeSeats() {
        return freeSeats;
    }

    public void setFreeSeats(int freeSeats) {
        this.freeSeats = freeSeats;
    }

    public int[] getSeatNumbers() {
        return seatNumbers;
    }

    public void setSeatNumbers(int index, int value) {
        this.seatNumbers[index] = value;
    }
    public void setSeatNumbers(int [] values) {
        this.seatNumbers = values;
    }

    public int getStartSecSeat() {
        return startSecSeat;
    }

    public void setStartSecSeat(int startSecSeat) {
        this.startSecSeat = startSecSeat;
    }

    public int getSectorPrice() {
        return sectorPrice;
    }

    public void setSectorPrice(int sectorPrice) {
        this.sectorPrice = sectorPrice;
    }
}
