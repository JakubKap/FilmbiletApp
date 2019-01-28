package com.companysf.filmbilet.services;

import android.content.Context;
import android.util.Log;

import com.companysf.filmbilet.R;
import com.companysf.filmbilet.connection.Listener.ErrorListener;
import com.companysf.filmbilet.connection.ReservationConnection;
import com.companysf.filmbilet.connection.Listener.ReservationConnListener;
import com.companysf.filmbilet.connection.Listener.SectorListener;
import com.companysf.filmbilet.connection.Listener.SocketListener;

import java.util.Locale;

import okhttp3.WebSocket;

public class Sector implements SocketListener {
    private static final String logTag = Sector.class.getSimpleName();

    private Context context;
    private WebSocket webSocket;
    private ErrorListener errorListener;
    private ReservationConnListener reservationConnListener;
    private ReservationConnection reservationConnection;
    private SectorListener sectorListener;
    private MyWebSocketListener myWebSocketListener;

    private int repertoireId;
    private int numOfSectors;
    private int numOfSeats;


    private boolean[] takenSeats;

    private boolean[] choosedSeats;
    private boolean[] choosedSeatsPrev;
    boolean [] takenYourSeats;

    private int[] seatSector;
    private int[] seatRow;

    private int [] seatNumbers;
    private int [] sectorPrices;

    private int[] freeSeatsInSector;
    private int[] startSecSeats;
    private String[] sectorTitles;
    private String[] sectorSubitles;

    public Sector(Context context, ErrorListener errorListener, ReservationConnListener reservationConnListener, SectorListener sectorListener, int numOfSectors, int numOfSeats){
        this.context = context;
        this.errorListener = errorListener;
        this.reservationConnListener = reservationConnListener;
        this.sectorListener = sectorListener;
        this.myWebSocketListener = new MyWebSocketListener(this);
        this.reservationConnection = new ReservationConnection(context, errorListener, reservationConnListener);
        this.numOfSectors=numOfSectors;
        this.numOfSeats=numOfSeats;

        this.seatSector = new int[numOfSeats];

        this.takenSeats = new boolean[numOfSeats];
        for(int i = 0; i< takenSeats.length; i++)
            takenSeats[i]=false;

        this.choosedSeats = new boolean[numOfSeats];
        this.choosedSeatsPrev = new boolean[numOfSeats];
        this.takenYourSeats = new boolean[numOfSeats];
        for(int i = 0; i< choosedSeats.length; i++) {
            choosedSeats[i] = false;
            choosedSeatsPrev[i]=false;
            takenYourSeats[i] = false;
        }

        this.seatSector = new int[numOfSeats];
        this.seatRow = new int [numOfSeats];
        this.seatNumbers = new int[35];

        this.sectorPrices = new int[numOfSectors/2];
        sectorPrices[0]=10;
        sectorPrices[1]=15;
        sectorPrices[2]=20;
        sectorPrices[3]=30;

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
        for(int i = 0; i< takenSeats.length; i++){
            if(takenSeats[i] && seatSector[i] == (sectorNum+1))
                freeSeats--;
        }
        return freeSeats;
    }

    public int[] seatNumbers(int sectorNumber){

        int startSeat = startSecSeats[sectorNumber];

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
    public String[] columnLabels(int index){
        String [] columnLabels = new String[7];
        if(index % 2 == 0){
            columnLabels[0] = context.getString(R.string.firstColumnTextLeft);
            columnLabels[1] = context.getString(R.string.secondColumnTextLeft);
            columnLabels[2] = context.getString(R.string.thirdColumnTextLeft);
            columnLabels[3] = context.getString(R.string.fourthColumnTextLeft);
            columnLabels[4] = context.getString(R.string.fifthColumnTextLeft);
            columnLabels[5] = context.getString(R.string.sixthColumnTextLeft);
            columnLabels[6] = context.getString(R.string.seventhColumnTextLeft);
        }
        else{
            columnLabels[0] = context.getString(R.string.firstColumnTextRight);
            columnLabels[1] = context.getString(R.string.secondColumnTextRight);
            columnLabels[2] = context.getString(R.string.thirdColumnTextRight);
            columnLabels[3] = context.getString(R.string.fourthColumnTextRight);
            columnLabels[4] = context.getString(R.string.fifthColumnTextRight);
            columnLabels[5] = context.getString(R.string.sixthColumnTextRight);
            columnLabels[6] = context.getString(R.string.seventhColumnTextRight);
        }

        return columnLabels;
    }

    public void markSeat(int index){
        int seatNumber = seatNumbers[index];
        choosedSeats[seatNumber-1] = !choosedSeats[seatNumber-1];
        choosedSeatsPrev[seatNumber-1] = !choosedSeats[seatNumber-1];
        Log.d(logTag, "choosedSeats[" + (seatNumber-1) +"] = " + choosedSeats[seatNumber-1] );
    }

    public int numOfSeats(){
        int num=0;
        for(int i=0; i<choosedSeats.length; i++)
            if(choosedSeats[i])
                num++;
            return num;
    }
    public int seatsPrice() {
        int price = 0;

        for (int i = 0; i < choosedSeats.length; i++) {
            if (choosedSeats[i]) {
                if (seatSector[i] == 1 || seatSector[i] == 2)
                    price += sectorPrices[0];
                else if (seatSector[i] == 3 || seatSector[i] == 4)
                    price += sectorPrices[1];
                else if (seatSector[i] == 5 || seatSector[i] == 6)
                    price += sectorPrices[2];
                else if (seatSector[i] == 7 || seatSector[i] == 8)
                    price += sectorPrices[3];
                Log.d(logTag, "Calculated price = " + price);
            }
        }

        return price;

    }

    public void clearMarkedSeats(){
        for(int i = 0; i< choosedSeatsPrev.length; i++)
            choosedSeatsPrev[i]=false;
    }

    public void assignSeatsPrev() {
        System.arraycopy(choosedSeats, 0, choosedSeatsPrev, 0, choosedSeats.length);
    }
    public void restoreChoosedSeats(){
        System.arraycopy(choosedSeatsPrev, 0, choosedSeats, 0, choosedSeatsPrev.length);
    }

    @Override
    public void onOpenCallback(WebSocket webSocket) {
        Log.d(logTag, "onOpenCallback");
        this.webSocket = webSocket;
    }

    @Override
    public void onMessageCallback(boolean[] reservedSeats) {
        Log.d(logTag, "onMessage");
        reactOnMessage(reservedSeats);
        sectorListener.updateUiCallback();
        prepareDialog();

    }

    public void reactOnMessage(boolean[] reservedSeats){
        for(int i=0; i<takenYourSeats.length; i++)
            takenYourSeats[i]=false;

        for (int i = 0; i < choosedSeats.length; i++) {
            boolean choosedBefore = choosedSeats[i];
            choosedSeats [i] = ( choosedSeats[i] ^ reservedSeats[i] ) & choosedSeats[i];

            if(choosedBefore && !choosedSeats[i]){
                takenSeats[i] = true;
                takenYourSeats[i]=true;
                Log.d(logTag,"Znaleziona wartość zajętego miejsca = " + i);

            }
            else if(reservedSeats[i]) {
                takenSeats[i] = true;
                Log.d(logTag,"Znaleziona wartość niezajętego miejsca = " + i);
            }

            if(choosedSeats[i])
                Log.d(logTag, "OnMessage choosedSeats[" + i +"] = " );
        }

            updateSectorSeats();

        }

        public void prepareDialog(){

            int sizeOfNumbersArray=0;
            for(int i=0; i<takenYourSeats.length;i++)
                if(takenYourSeats[i])
                    sizeOfNumbersArray++;

            Log.d(logTag, "sizeofArrayNumber before= " + sizeOfNumbersArray);

            if(sizeOfNumbersArray>0){
                int [] takenSeatsNumbers = new int[sizeOfNumbersArray];
                int firstIndex=0;
                for(int i=0; i<takenYourSeats.length; i++){
                    if(takenYourSeats[i]) {
                        int seatNumber = i;
                        takenSeatsNumbers[firstIndex] = ++seatNumber;
                        firstIndex++;
                    }
                }
                String takenSeatsNum = String.format(new Locale("pl", "PL"), "%d",
                        takenSeatsNumbers[0]);

                if(sizeOfNumbersArray > 1) {
                    StringBuilder sB = new StringBuilder(takenSeatsNum);

                    for (int i = 1; i < takenSeatsNumbers.length; i++) {
                        sB.append(", ");
                        sB.append(takenSeatsNumbers[i]);
                    }
                    takenSeatsNum = sB.toString();
                }

                Log.d(logTag, "sizeofArrayNumber after= " + sizeOfNumbersArray);
                sectorListener.showDialogCallback(takenSeatsNum, sizeOfNumbersArray);
            }

        }

    public int numOfChoosedSeats(){
        int num=0;
        for(int i=0; i<choosedSeats.length; i++)
            if(choosedSeats[i])
                num++;
        return num;
    }
    public int seatTypeId(int index){
        int sectorNum = seatSector[index];
        int seatTypeId=1;

        if(sectorNum == 1 || sectorNum == 2)
            seatTypeId = 1;
        else if(sectorNum == 3 || sectorNum == 4)
            seatTypeId = 2;
        else if(sectorNum == 5 || sectorNum == 6)
            seatTypeId = 3;
        else if(sectorNum == 7 || sectorNum == 8)
            seatTypeId = 4;
        return seatTypeId;

    }

    public void saveToDb(){
        for(int i=0; i<choosedSeats.length;i++)
            Log.d(logTag, "takenSeats before = " + takenSeats[i]);
        for(int i=0; i<choosedSeats.length; i++){
            if(choosedSeats[i]){
                //TODO przesyłać String reprezentujący id usera
                int seatNumber = i+1;
                int seatTypeId = seatTypeId(i);
                int row = seatRow[i];
                takenSeats[i] = true;
                reservationConnection.saveReservation("1",seatNumber, seatTypeId, row, repertoireId);
            }
        }
        for(int i=0; i<choosedSeats.length;i++)
            Log.d(logTag, "takenSeats after = " + takenSeats[i]);

        if(myWebSocketListener.getHttpClient() != null){
            myWebSocketListener.prepareMessage(context,this.webSocket, takenSeats);
        }
        else
            sectorListener.socketCloseError();

    }

    public boolean[] getChoosedSeatsPrev() {
        return choosedSeatsPrev;
    }
    public void setTakenSeats(boolean[] takenSeats) {
        this.takenSeats = takenSeats;
    }

    public boolean[] getTakenSeats() {
        return takenSeats;
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

    public int[] getSeatNumbers() {
        return seatNumbers;
    }

    public boolean[] getChoosedSeats() {
        return choosedSeats;
    }

    public void setSeatNumbers(int[] seatNumbers) {
        this.seatNumbers = seatNumbers;
    }

    public void restoreChoosedSeats(boolean[] choosedSeats) {
        this.choosedSeats = choosedSeats;
    }

}
