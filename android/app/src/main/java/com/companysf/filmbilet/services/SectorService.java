package com.companysf.filmbilet.services;

import android.content.Context;
import android.util.Log;

import com.companysf.filmbilet.R;
import com.companysf.filmbilet.connection.Listener.ErrorListener;
import com.companysf.filmbilet.connection.ReservationConnection;
import com.companysf.filmbilet.connection.Listener.ReservationConnListener;
import com.companysf.filmbilet.connection.Listener.SectorListener;
import com.companysf.filmbilet.connection.Listener.SocketListener;
import com.companysf.filmbilet.entities.Customer;
import com.companysf.filmbilet.entities.Hall;
import com.companysf.filmbilet.entities.Sector;
import com.companysf.filmbilet.utils.SQLiteHandler;

import java.util.Locale;

import okhttp3.WebSocket;

public class SectorService implements ReservationConnListener, SocketListener {
    private static final String logTag = SectorService.class.getSimpleName();

    private Context context;
    private WebSocket webSocket;
    private ReservationConnection reservationConnection;
    private SectorListener sectorListener;
    private MyWebSocketListener myWebSocketListener;

    private int repertoireId;
    private int numOfSectors;
    private int numOfSeats;

    private Hall hall;
    private Sector [] sectors;


    public SectorService(int repertoireId,Context context, ErrorListener errorListener,
                         SectorListener sectorListener, int numOfSectors, int numOfSeats){
        this.repertoireId = repertoireId;
        this.context = context;
        this.sectorListener = sectorListener;
        this.myWebSocketListener = new MyWebSocketListener(this);
        this.reservationConnection = new ReservationConnection(
                context, errorListener, this
        );
        this.numOfSectors=numOfSectors;
        this.numOfSeats=numOfSeats;

        this.hall = new Hall(numOfSeats);
        this.sectors = new Sector[numOfSectors];

        for(int i=0; i<sectors.length;i++){
            if(i==0 || i==1)
                sectors[i] = new Sector(10);
            else if (i==2 || i==3)
                sectors[i]= new Sector(15);
            else if (i==4 || i==5)
                sectors[i]= new Sector(20);
            else
                sectors[i]= new Sector(30);
        }

        assignFirstSecSeat();
        reservationConnection.getReservations(repertoireId);
    }

    public void assignFirstSecSeat(){

        int firstSeat = 1;
        for(int i=0; i<sectors.length; i++){
            sectors[i].setStartSecSeat(firstSeat);
            if(i%2 == 0)
                firstSeat+=7;
            else firstSeat+=63;
            Log.d(logTag,"First seat dla i = " + i + " = " +  sectors[i].getStartSecSeat());
        }
    }
    public void assignRowToSeat(){
        int value = 1;

        for (int i = 1; i <= numOfSeats; i++) {
            if ((i - 1) % ((numOfSectors*2)-2) == 0 && i != 1)
                value++;
            hall.setSeatRow(i-1, value);
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
                    hall.setSeatSector(firstSeat-1, sectorNumber);
                    firstSeat++;
                } else {
                    hall.setSeatSector(firstSeat-1, sectorNumber);
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
                    hall.setSeatSector(firstSeat-1, sectorNumber);
                    firstSeat++;
                } else {
                    hall.setSeatSector(firstSeat-1, sectorNumber);
                    firstSeat++;
                }
            }
            sectorNumber += 2;
        }
        for(int i =0; i<hall.getSeatSector().length; i++)
            Log.d(logTag, "seatSetor[" + i + "] = " + hall.getSeatSector()[i]);

    }
    public void updateSectorSeats(){
        for(int i=0; i<sectors.length; i++)
            sectors[i].setFreeSeats(freeSeatsOfSector(i));
    }


    public int freeSeatsOfSector(int sectorNum){
        int freeSeats = 35;
        for(int i = 0; i< hall.getTakenSeats().length; i++){
            if(hall.getTakenSeats()[i] && hall.getSeatSector()[i] == (sectorNum+1))
                freeSeats--;
        }
        return freeSeats;
    }

    public int[] seatNumbers(int sectorNumber){

        int startSeat = sectors[sectorNumber].getStartSecSeat();

        for (int i = 0; i < 35; i++) {

            if (i == 7 || i == 14 || i == 21 || i == 28) {
                startSeat += 7;
                sectors[sectorNumber].setSeatNumbers(i,startSeat);
                Log.d(logTag, "Dodana wartość do siatki: " + startSeat + " dla i = " + i);
                startSeat++;
            } else {
                sectors[sectorNumber].setSeatNumbers(i,startSeat);
                Log.d(logTag, "Dodana wartość do siatki: " + startSeat + " dla i = " + i);
                startSeat++;
            }
        }

        return sectors[sectorNumber].getSeatNumbers();
    }

    public void markSeat(int sectorIndex, int seatIndex){
        int seatNumber = sectors[sectorIndex].getSeatNumbers()[seatIndex];
        hall.setChoosedSeats((seatNumber-1), !hall.getChoosedSeats()[seatNumber-1]);
        hall.setChoosedSeatsPrev((seatNumber-1), !hall.getChoosedSeats()[seatNumber-1]);
        Log.d(logTag, "choosedSeats[" + (seatNumber-1) +"] = " + hall.getChoosedSeats()[seatNumber-1]);
    }

    public int numOfSeats(){
        int num=0;
        for(int i=0; i<hall.getChoosedSeats().length; i++)
            if(hall.getChoosedSeats()[i])
                num++;
            return num;
    }
    public int seatsPrice() {
        int price = 0;

        for (int i = 0; i < hall.getChoosedSeats().length; i++) {
            if (hall.getChoosedSeats()[i]) {
                price += sectors[hall.getSeatSector()[i]-1].getSectorPrice();
                Log.d(logTag, "Calculated price = " + price);
            }
        }
        return price;
    }

    public void clearMarkedSeats(){
        for(int i = 0; i< hall.getChoosedSeatsPrev().length; i++)
            hall.setChoosedSeatsPrev(i,false);
    }

    public void assignSeatsPrev() {
        System.arraycopy(hall.getChoosedSeats(), 0, hall.getChoosedSeatsPrev(), 0, hall.getChoosedSeats().length);
    }
    public void restoreChoosedSeats(){
        System.arraycopy(hall.getChoosedSeatsPrev(), 0, hall.getChoosedSeats(), 0, hall.getChoosedSeatsPrev().length);
    }


    @Override
    public void onDbResponseCallback(boolean[] takenSeats) {
        setTakenSeats(takenSeats);
        updateSectorSeats();

        for (int i = 0; i < getTakenSeats().length; i++)
            Log.d(logTag, "Model takenSeats = " + getTakenSeats()[i]);

        sectorListener.updateUiCallback(false);
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
        sectorListener.updateUiCallback(false);
        prepareDialog();
    }

    public void reactOnMessage(boolean[] reservedSeats){
        for(int i=0; i<hall.getTakenYourSeats().length; i++)
            hall.setTakenYourSeats(i,false);

        for (int i = 0; i < hall.getChoosedSeats().length; i++) {
            boolean choosedBefore = hall.getChoosedSeats()[i];
            hall.setChoosedSeats(i, ( hall.getChoosedSeats()[i] ^ reservedSeats[i] ) & hall.getChoosedSeats()[i]);
            if(choosedBefore && !hall.getChoosedSeats()[i]){
                hall.setTakenSeats(i,true);
                hall.setTakenYourSeats(i,true);
                Log.d(logTag,"Znaleziona wartość zajętego miejsca = " + i);

            }
            else if(reservedSeats[i]) {
                hall.setTakenSeats(i,true);
                Log.d(logTag,"Znaleziona wartość niezajętego miejsca = " + i);
            }

            if(hall.getChoosedSeats()[i])
                Log.d(logTag, "OnMessage choosedSeats[" + i +"] = " );
        }
            updateSectorSeats();
        }

        public void prepareDialog(){

            int sizeOfNumbersArray=0;
            for(int i=0; i<hall.getTakenYourSeats().length;i++)
                if(hall.getTakenYourSeats()[i])
                    sizeOfNumbersArray++;

            Log.d(logTag, "sizeofArrayNumber before= " + sizeOfNumbersArray);

            if(sizeOfNumbersArray>0){
                int [] takenSeatsNumbers = new int[sizeOfNumbersArray];
                int firstIndex=0;
                for(int i=0; i<hall.getTakenYourSeats().length; i++){
                    if(hall.getTakenYourSeats()[i]) {
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
        for (boolean choosedSeat : hall.getChoosedSeats())
            if (choosedSeat) num++;
        return num;
    }
    private int seatTypeId(int index){
        int sectorNum = hall.getSeatSector()[index];
        int seatTypeId;

        if(sectorNum == 1 || sectorNum == 2)
            seatTypeId = 1;
        else if(sectorNum == 3 || sectorNum == 4)
            seatTypeId = 2;
        else if(sectorNum == 5 || sectorNum == 6)
            seatTypeId = 3;
        else
            seatTypeId = 4;

        return seatTypeId;
    }

    public void saveToDb(){
        for(int i=0; i<hall.getChoosedSeats().length;i++)
            Log.d(logTag, "takenSeats before = " + hall.getTakenSeats()[i]);

        SQLiteHandler db = new SQLiteHandler(context);
        Customer customer = db.getCustomer();
        int customerId = customer.getId();

        for(int i=0; i<hall.getChoosedSeats().length; i++){
            if(hall.getChoosedSeats()[i]){
                int seatNumber = i+1;
                int seatTypeId = seatTypeId(i);
                int row = hall.getSeatRow()[i];
                hall.setTakenSeats(i, true);
                reservationConnection.saveReservation(customerId,seatNumber, seatTypeId, row, repertoireId);
            }
        }
        for(int i=0; i<hall.getChoosedSeats().length;i++)
            Log.d(logTag, "takenSeats after = " + hall.getTakenSeats()[i]);

        if(myWebSocketListener.getHttpClient() != null){
            myWebSocketListener.prepareMessage(context,this.webSocket, hall.getTakenSeats());
        }
        else
            sectorListener.socketCloseError();

    }

    public int sectorSubtitleIndex(int sectorIndex){
        if (sectorIndex == 0 || sectorIndex == 1)
            return 0;
        else if(sectorIndex == 2 || sectorIndex == 3)
            return 1;
        else if(sectorIndex == 4 || sectorIndex == 5)
            return 2;
        else return 3;
    }
    public int rowLabelsType(int sectorIndex){
        if (sectorIndex == 0 || sectorIndex == 1)
            return 0;

         else if (sectorIndex == 2 || sectorIndex == 3)
             return 1;


        else if (sectorIndex == 4 || sectorIndex == 5)
            return 2;

        else
            return 3;
    }
    public boolean columnLabelsType(int sectorIndex){
        return (sectorIndex % 2 == 0);
    }
    public boolean[] getChoosedSeatsPrev() {
        return hall.getChoosedSeatsPrev();
    }
    public void setTakenSeats(boolean[] takenSeats) {
        this.hall.setTakenSeats(takenSeats);
    }

    public boolean[] getTakenSeats() {
        return hall.getTakenSeats();
    }

    public int getFreeSeatsInSector(int index) {
        return sectors[index].getFreeSeats();
    }

    public void setRepertoireId(int repertoireId) {
        this.repertoireId = repertoireId;
    }


    public int[] getSeatNumbers(int index) {
        return sectors[index].getSeatNumbers();
    }

    public boolean[] getChoosedSeats() {
        return hall.getChoosedSeats();
    }

    public void setSeatNumbers(int index, int[] seatNumbers) {
        this.sectors[index].setSeatNumbers(seatNumbers);
    }

}
