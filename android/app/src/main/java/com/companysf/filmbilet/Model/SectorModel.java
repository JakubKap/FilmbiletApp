package com.companysf.filmbilet.Model;

import android.util.Log;

public class SectorModel {
    private static final String logTag = SectorModel.class.getSimpleName();
    private int numOfSectors;
    private int numOfSeats;


    private boolean [] choosedSector;
    private boolean[] choosedSeats;

    private int[] seatSector;
    private int[] seatRow;

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
        
    }


}
