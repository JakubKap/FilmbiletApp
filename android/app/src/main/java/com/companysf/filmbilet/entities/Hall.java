package com.companysf.filmbilet.entities;

public class Hall {

    private int numOfSeatsInHall;

    private boolean[] takenSeats;
    private boolean[] choosedSeats;
    private boolean[] choosedSeatsPrev;
    private boolean [] takenYourSeats;

    private int[] seatSector;
    private int[] seatRow;


    public Hall(int numOfSeatsInHall){
        this.numOfSeatsInHall = numOfSeatsInHall;

        takenSeats = new boolean[numOfSeatsInHall];
        choosedSeats = new boolean[numOfSeatsInHall];
        choosedSeatsPrev = new boolean[numOfSeatsInHall];
        takenYourSeats = new boolean[numOfSeatsInHall];

        seatSector = new int[numOfSeatsInHall];
        seatRow = new int[numOfSeatsInHall];

        disableFlags();
    }

    private void disableFlags(){
        for(int i=0; i<numOfSeatsInHall; i++){
            takenSeats[i]=false;
            choosedSeats[i]=false;
            choosedSeatsPrev[i]=false;
            takenYourSeats[i]=false;
        }
    }


    public int[] getSeatSector() {
        return seatSector;
    }

    public void setSeatSector(int index, int value) {
        this.seatSector[index] = value;
    }

    public int[] getSeatRow() {
        return seatRow;
    }

    public void setSeatRow(int index, int value) {
        this.seatRow[index] = value;
    }


    public int getNumOfSeatsInHall() {
        return numOfSeatsInHall;
    }

    public void setNumOfSeatsInHall(int numOfSeatsInHall) {
        this.numOfSeatsInHall = numOfSeatsInHall;
    }

    public boolean[] getTakenSeats() {
        return takenSeats;
    }

    public void setTakenSeats(int index, boolean value) {
        this.takenSeats[index] = value;
    }

    public void setTakenSeats(boolean [] takenSeats) {
        this.takenSeats = takenSeats;
    }

    public boolean[] getChoosedSeats() {
        return choosedSeats;
    }

    public void setChoosedSeats(int index, boolean value) {
        this.choosedSeats[index] = value;
    }

    public boolean[] getChoosedSeatsPrev() {
        return choosedSeatsPrev;
    }

    public void setChoosedSeatsPrev(int index, boolean value) {
        this.choosedSeatsPrev = choosedSeatsPrev;
    }

    public boolean[] getTakenYourSeats() {
        return takenYourSeats;
    }

    public void setTakenYourSeats(int index, boolean value) {
        this.takenYourSeats[index] = value;
    }
}
