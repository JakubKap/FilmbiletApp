package com.companysf.filmbilet.services;

import android.content.Context;
import android.util.Log;

import com.companysf.filmbilet.R;

public class SectorModel {
    private static final String logTag = SectorModel.class.getSimpleName();

    private Context context;

    private int repertoireId;
    private int numOfSectors;
    private int numOfSeats;


    private boolean [] choosedSector;
    private boolean[] choosedSeats;

    private int[] seatSector;
    private int[] seatRow;

    private int[] freeSeatsInSector;
    private int[] startSecSeats;
    private String[] sectorTitles;
    private String[] sectorSubitles;

    public SectorModel(Context context, int numOfSectors, int numOfSeats){
        this.context = context;
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

        assignFirstSecSeat();
        sectorTitles = new String[numOfSectors];
        sectorTitles[0] = context.getString(R.string.upperSector1Text);
        sectorTitles[1] = context.getString(R.string.upperSector2Text);
        sectorTitles[2] = context.getString(R.string.upperSector3Text);
        sectorTitles[3] = context.getString(R.string.upperSector4Text);
        sectorTitles[4] = context.getString(R.string.upperSector5Text);
        sectorTitles[5] = context.getString(R.string.upperSector6Text);
        sectorTitles[6] = context.getString(R.string.upperSector7Text);
        sectorTitles[7] = context.getString(R.string.upperSector8Text);

        sectorSubitles = new String[numOfSectors/2];
        sectorSubitles[0] = context.getString(R.string.sector1Subtitle);
        sectorSubitles[1] = context.getString(R.string.sector2Subtitle);
        sectorSubitles[2] = context.getString(R.string.sector3Subtitle);
        sectorSubitles[3] = context.getString(R.string.sector4Subtitle);
    }

    public void assignFirstSecSeat(){
        startSecSeats = new int[numOfSectors];

        int firstSeat = 1;
        for(int i=0; i<startSecSeats.length; i++){
            startSecSeats[i] = firstSeat;
            if(i%2 == 0)
                firstSeat+=7;
            else firstSeat+=63;
            Log.d(logTag,"First seat dla i = " + i + " = " +  startSecSeats[i]);
        }
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

    public int[] seatNumbers(int sectorNumber){

        int startSeat = startSecSeats[sectorNumber];

        int [] seatNumbers = new int[35];

        for (int i = 0; i < 35; i++) {

            if (i == 7 || i == 14 || i == 21 || i == 28) {
                startSeat += 7;
                seatNumbers[i] = startSeat;
                Log.d(logTag, "Dodana wartość do siatki: " + startSeat + " dla i = " + i);
                startSeat++;
            } else {
                seatNumbers[i] = startSeat;
                Log.d(logTag, "Dodana wartość do siatki: " + startSeat + " dla i = " + i);
                startSeat++;
            }
        }

        return seatNumbers;
    }
    public String sectorSubtitle(int index){
       if(index == 0 || index == 1)
           return sectorSubitles[0];
       else if(index == 2 || index == 3)
            return sectorSubitles[1];
        if(index == 4 || index == 5)
            return sectorSubitles[2];
        else
            return sectorSubitles[3];
    }

    public String [] rowLabels(int index){
        String[] rowLabels = new String[5];
        if(index == 0 || index == 1) {
            rowLabels[0] = context.getString(R.string.row1);
            rowLabels[1] = context.getString(R.string.row2);
            rowLabels[2] = context.getString(R.string.row3);
            rowLabels[3] = context.getString(R.string.row4);
            rowLabels[4] = context.getString(R.string.row5);
        }
        else if(index == 2 || index == 3) {
            rowLabels[0] = context.getString(R.string.row6);
            rowLabels[1] = context.getString(R.string.row7);
            rowLabels[2] = context.getString(R.string.row8);
            rowLabels[3] = context.getString(R.string.row9);
            rowLabels[4] = context.getString(R.string.row10);
        }
        if(index == 4 || index == 5) {
            rowLabels[0] = context.getString(R.string.row11);
            rowLabels[1] = context.getString(R.string.row12);
            rowLabels[2] = context.getString(R.string.row13);
            rowLabels[3] = context.getString(R.string.row14);
            rowLabels[4] = context.getString(R.string.row15);
        }
        else if(index == 6 || index == 7){
            rowLabels[0] = context.getString(R.string.row16);
            rowLabels[1] = context.getString(R.string.row17);
            rowLabels[2] = context.getString(R.string.row18);
            rowLabels[3] = context.getString(R.string.row19);
            rowLabels[4] = context.getString(R.string.row20);
        }

        return rowLabels;
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

    public void setRepertoireId(int repertoireId) {
        this.repertoireId = repertoireId;
    }

    public String[] getSectorTitles() {
        return sectorTitles;
    }

}
