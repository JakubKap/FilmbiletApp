package com.companysf.filmbilet.services;

import android.util.Log;

public class SectorModel {
    private static final String logTag = SectorModel.class.getSimpleName();
    private int numOfSectors;
    private int numOfSeats;


    private boolean [] choosedSector;
    private boolean[] choosedSeats;

    private int[] seatSector;
    private int[] seatRow;

    private int[] freeSeatsInSector;
    public SectorModel(int numOfSectors, int numOfSeats){
        this.numOfSectors=numOfSectors;
        this.numOfSeats=numOfSeats;

        this.choosedSector = new boolean[numOfSectors];
        for(int i = 0; i< choosedSector.length; i++)
            choosedSector[i]=false;

        this.seatSector = new int[numOfSeats];

        this.choosedSeats = new boolean[numOfSeats];
        for(int i = 0; i< choosedSeats.length; i++)
            choosedSeats[i]=false;

        this.seatSector = new int[numOfSeats];
        this.seatRow = new int [numOfSeats];
        this.freeSeatsInSector = new int [numOfSectors];

    }

    public void assignRowToSeat(){
        int value = 1;

        for (int i = 1; i <= numOfSeats; i++) {
            if ((i - 1) % ((numOfSectors*2)-2) == 0 && i != 1)
                value++;
            seatRow[i-1] = value;
            Log.d(logTag, "Wstawiona wartość <Nr_miejsca, Rząd>= " + "<" + (i-1) + ", " + value + ">");
        }
    }

    public void assignSectorToSeat(){

        int sectorNumber = 1;
        for (int i = 1; i <= 211; i += 70) {
            int firstSeat = i;

            for (int j = 1; j <= 35; j++) {
                if (j == 8 || j == 15 || j == 22 || j == 29) {
                    firstSeat += 7;
                    seatSector[firstSeat-1] = sectorNumber;
                    firstSeat++;
                } else {
                    seatSector[firstSeat-1] = sectorNumber;
                    firstSeat++;
                }

            }
            sectorNumber += 2;
        }

        sectorNumber = 2;
        for (int i = 8; i <= 218; i += 70) {
            int firstSeat = i;

            for (int j = 1; j <= 35; j++) {
                if (j == 8 || j == 15 || j == 22 || j == 29) {
                    firstSeat += 7;
                    seatSector[firstSeat-1] = sectorNumber;
                    firstSeat++;
                } else {
                    seatSector[firstSeat-1] = sectorNumber;
                    firstSeat++;
                }
            }
            sectorNumber += 2;
        }
        for(int i =0; i<seatSector.length; i++)
            Log.d(logTag, "seatSetor[" + i + "] = " + seatSector[i]);

    }
    public void updateSectorSeats(){
        for(int i=0; i<freeSeatsInSector.length; i++)
            freeSeatsInSector[i] = freeSeatsOfSector(i);
    }


    public int freeSeatsOfSector(int sectorNum){
        int freeSeats = 35;
        for(int i =0; i<choosedSeats.length; i++){
            if(choosedSeats[i] && seatSector[i] == (sectorNum+1))
                freeSeats--;
        }
        return freeSeats;
    }

    public void setChoosedSeats(boolean[] choosedSeats) {
        this.choosedSeats = choosedSeats;
    }

    public boolean[] getChoosedSeats() {
        return choosedSeats;
    }

    public int[] getFreeSeatsInSector() {
        return freeSeatsInSector;
    }
}
