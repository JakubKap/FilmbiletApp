package com.companysf.filmbilet.AsyncTasks;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.content.Context;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.companysf.filmbilet.R;
import com.companysf.filmbilet.app.AppConfig;
import com.companysf.filmbilet.app.AppController;
import com.companysf.filmbilet.appLogic.Reservation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FreeSeatsTask extends AsyncTask<Void, Integer, Void> {

    private static final String logTag = FreeSeatsTask.class.getSimpleName();
    private WeakReference<Context> contextref;
    private ArrayList<Button> buttons = new ArrayList<>();

    private ArrayList<Button> columnButtons = new ArrayList<>();
    private int startSeat;
    private int seatTypeId;
    private Map<Button, Integer> seatNumber = new HashMap<>();//mapa zawierająca button oraz odpowiadający mu nr siedzenia
    private LinearLayout linearLayoutRows;
    private ArrayList<Reservation> reservationList = new ArrayList<>();
    private ArrayList<Integer> takenSeats = new ArrayList<>();
    private TextView textView1Seats, textView2Seats, textView3Seats;
    private GridLayout gridLayoutSeats;

    private Button buttonIR_1, buttonIR_2, buttonIR_3, buttonIR_4, buttonIR_5,buttonIR_6, buttonIR_7,
            buttonIIR_1, buttonIIR_2, buttonIIR_3, buttonIIR_4, buttonIIR_5,buttonIIR_6, buttonIIR_7,
            buttonIIIR_1, buttonIIIR_2, buttonIIIR_3, buttonIIIR_4, buttonIIIR_5,buttonIIIR_6, buttonIIIR_7,
            buttonIVR_1, buttonIVR_2, buttonIVR_3, buttonIVR_4, buttonIVR_5,buttonIVR_6, buttonIVR_7,
            buttonVR_1, buttonVR_2, buttonVR_3, buttonVR_4, buttonVR_5,buttonVR_6, buttonVR_7,
            button1C, button2C, button3C, button4C, button5C, button6C, button7C,
            buttonIR, buttonIIR, buttonIIIR, buttonIVR, buttonVR,
            btnApprove;

    ProgressBar progressBarSeats;

    View linearLayout; //dodane
    private boolean start;
    private Map<Integer, Integer> selectedSeats = new HashMap<>();
    private boolean[] choosedPlaces = new boolean[280];



    public FreeSeatsTask(Context context, View linearLayout, Map<Integer, Integer> selectedSeats, boolean [] choosedPlaces, int startSeat, int seatTypeId) {

        contextref = new WeakReference<>(context);


        this.startSeat = startSeat;

        this.linearLayout = linearLayout;

        this.selectedSeats=selectedSeats;

        this.choosedPlaces=choosedPlaces;

        this.linearLayoutRows = (LinearLayout) linearLayout.findViewById(R.id.linearLayoutRows);

        this.progressBarSeats =(ProgressBar) linearLayout.findViewById(R.id.progressBarSeats);

        this.textView1Seats =(TextView)linearLayout.findViewById(R.id.textView1Seats);
        this.textView2Seats =(TextView)linearLayout.findViewById(R.id.textView2Seats);
        this.textView3Seats =(TextView)linearLayout.findViewById(R.id.textView3Seats);

        this.gridLayoutSeats =(GridLayout)linearLayout.findViewById(R.id.gridLayoutSeats);

        this.seatTypeId=seatTypeId;

        this.btnApprove =linearLayout.findViewById(R.id.btnApprove);

            this.buttonIR_1 = (Button) linearLayout.findViewById(R.id.buttonIR_1);
            buttons.add(buttonIR_1);
            this.buttonIR_2 = (Button) linearLayout.findViewById(R.id.buttonIR_2);
            buttons.add(buttonIR_2);
            this.buttonIR_3 = (Button) linearLayout.findViewById(R.id.buttonIR_3);
            buttons.add(buttonIR_3);
            this.buttonIR_4 = (Button) linearLayout.findViewById(R.id.buttonIR_4);
            buttons.add(buttonIR_4);
            this.buttonIR_5 = (Button) linearLayout.findViewById(R.id.buttonIR_5);
            buttons.add(buttonIR_5);
            this.buttonIR_6 = (Button) linearLayout.findViewById(R.id.buttonIR_6);
            buttons.add(buttonIR_6);
            this.buttonIR_7 = (Button) linearLayout.findViewById(R.id.buttonIR_7);
            buttons.add(buttonIR_7);

            this.buttonIIR_1 = (Button) linearLayout.findViewById(R.id.buttonIIR_1);
            buttons.add(buttonIIR_1);
            this.buttonIIR_2 = (Button) linearLayout.findViewById(R.id.buttonIIR_2);
            buttons.add(buttonIIR_2);
            this.buttonIIR_3 = (Button) linearLayout.findViewById(R.id.buttonIIR_3);
            buttons.add(buttonIIR_3);
            this.buttonIIR_4 = (Button) linearLayout.findViewById(R.id.buttonIIR_4);
            buttons.add(buttonIIR_4);
            this.buttonIIR_5 = (Button) linearLayout.findViewById(R.id.buttonIIR_5);
            buttons.add(buttonIIR_5);
            this.buttonIIR_6 = (Button) linearLayout.findViewById(R.id.buttonIIR_6);
            buttons.add(buttonIIR_6);
            this.buttonIIR_7 = (Button) linearLayout.findViewById(R.id.buttonIIR_7);
            buttons.add(buttonIIR_7);

            this.buttonIIIR_1 = (Button) linearLayout.findViewById(R.id.buttonIIIR_1);
            buttons.add(buttonIIIR_1);
            this.buttonIIIR_2 = (Button) linearLayout.findViewById(R.id.buttonIIIR_2);
            buttons.add(buttonIIIR_2);
            this.buttonIIIR_3 = (Button) linearLayout.findViewById(R.id.buttonIIIR_3);
            buttons.add(buttonIIIR_3);
            this.buttonIIIR_4 = (Button) linearLayout.findViewById(R.id.buttonIIIR_4);
            buttons.add(buttonIIIR_4);
            this.buttonIIIR_5 = (Button) linearLayout.findViewById(R.id.buttonIIIR_5);
            buttons.add(buttonIIIR_5);
            this.buttonIIIR_6 = (Button) linearLayout.findViewById(R.id.buttonIIIR_6);
            buttons.add(buttonIIIR_6);
            this.buttonIIIR_7 = (Button) linearLayout.findViewById(R.id.buttonIIIR_7);
            buttons.add(buttonIIIR_7);

            this.buttonIVR_1 = (Button) linearLayout.findViewById(R.id.buttonIVR_1);
            buttons.add(buttonIVR_1);
            this.buttonIVR_2 = (Button) linearLayout.findViewById(R.id.buttonIVR_2);
            buttons.add(buttonIVR_2);
            this.buttonIVR_3 = (Button) linearLayout.findViewById(R.id.buttonIVR_3);
            buttons.add(buttonIVR_3);
            this.buttonIVR_4 = (Button) linearLayout.findViewById(R.id.buttonIVR_4);
            buttons.add(buttonIVR_4);
            this.buttonIVR_5 = (Button) linearLayout.findViewById(R.id.buttonIVR_5);
            buttons.add(buttonIVR_5);
            this.buttonIVR_6 = (Button) linearLayout.findViewById(R.id.buttonIVR_6);
            buttons.add(buttonIVR_6);
            this.buttonIVR_7 = (Button) linearLayout.findViewById(R.id.buttonIVR_7);
            buttons.add(buttonIVR_7);

            this.buttonVR_1 = (Button) linearLayout.findViewById(R.id.buttonVR_1);
            buttons.add(buttonVR_1);
            this.buttonVR_2 = (Button) linearLayout.findViewById(R.id.buttonVR_2);
            buttons.add(buttonVR_2);
            this.buttonVR_3 = (Button) linearLayout.findViewById(R.id.buttonVR_3);
            buttons.add(buttonVR_3);
            this.buttonVR_4 = (Button) linearLayout.findViewById(R.id.buttonVR_4);
            buttons.add(buttonVR_4);
            this.buttonVR_5 = (Button) linearLayout.findViewById(R.id.buttonVR_5);
            buttons.add(buttonVR_5);
            this.buttonVR_6 = (Button) linearLayout.findViewById(R.id.buttonVR_6);
            buttons.add(buttonVR_6);
            this.buttonVR_7 = (Button) linearLayout.findViewById(R.id.buttonVR_7);
            buttons.add(buttonVR_7);

            this.button1C = (Button) linearLayout.findViewById(R.id.button1C);
            columnButtons.add(button1C);
            this.button2C = (Button) linearLayout.findViewById(R.id.button2C);
            columnButtons.add(button2C);
            this.button3C = (Button) linearLayout.findViewById(R.id.button3C);
            columnButtons.add(button3C);
            this.button4C = (Button) linearLayout.findViewById(R.id.button4C);
            columnButtons.add(button4C);
            this.button5C = (Button) linearLayout.findViewById(R.id.button5C);
            columnButtons.add(button5C);
            this.button6C = (Button) linearLayout.findViewById(R.id.button6C);
            columnButtons.add(button6C);
            this.button7C = (Button) linearLayout.findViewById(R.id.button7C);
            columnButtons.add(button7C);

            this.buttonIR = (Button) linearLayout.findViewById(R.id.buttonIR);
            this.buttonIIR = (Button) linearLayout.findViewById(R.id.buttonIIR);
            this.buttonIIIR = (Button) linearLayout.findViewById(R.id.buttonIIIR);
            this.buttonIVR = (Button) linearLayout.findViewById(R.id.buttonIVR);
            this.buttonVR = (Button) linearLayout.findViewById(R.id.buttonVR);


            Log.d(logTag, "Czy buttons empty: " + buttons.isEmpty());

        }


    public void changeColorOfButton(Button button){

        if(choosedPlaces[seatNumber.get(button)-1]){
            button.setEnabled(false);
            button.setTextColor(Color.WHITE);
            button.setBackgroundResource(R.drawable.button_taken);

        }
        else { //miejsce, które w momencie załadowanie popupu jest wolne
            button.setBackgroundResource(R.drawable.button_normal_seat);
        }

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        Log.d(logTag, "Rozpoczęcie onPreExecute");

        Log.d(logTag, "Czy buttons empty: " + buttons.isEmpty());
        Log.d(logTag, "buttons size: " + buttons.size());


        //wypełnienie buttonów opodwiednimi nr siedzień
        int seatNr = this.startSeat;


        for(int i=1; i<=35; i++){

            if(i==8 || i==15 || i==22 || i==29) {
                seatNr += 7;
                seatNumber.put(buttons.get(i-1),seatNr);
                Log.d(logTag, "Dodana wartość do siatki: " + seatNr + " dla i = " + i);
                seatNr++;
            }
            else{
                seatNumber.put(buttons.get(i-1), seatNr);
                Log.d(logTag, "Dodana wartość do siatki: " + seatNr+ " dla i = " + i);
                seatNr++;
            }


        }

        String text = "";
        int logNr=0;
        for(int i=0; i<35; i++){
            text = "" + seatNumber.get(i);
            if(text.length() == 3)
                buttons.get(i).setTextSize(TypedValue.COMPLEX_UNIT_SP, 7);

            buttons.get(i).setText(""+seatNumber.get( buttons.get(i)));
            Log.d(logTag, "Zmieniona wartość textu buttona: " + seatNumber.get( buttons.get(logNr)) );
            logNr++;
        }

        //ustawienie prawidłowej nazwy sektora

        switch(startSeat){
            case 1:
                textView1Seats.setText("Sektor 1");
                break;
            case 8:
                textView1Seats.setText("Sektor 2");
                break;
            case 71:
                textView1Seats.setText("Sektor 3");
                break;
            case 78:
                textView1Seats.setText("Sektor 4");
                break;
            case 141:
                textView1Seats.setText("Sektor 5");
                break;
            case 148:
                textView1Seats.setText("Sektor 6");
                break;
            case 211:
                textView1Seats.setText("Sektor 7");
                break;
            case 218:
                textView1Seats.setText("Sektor 8");
                break;
        }


            this.linearLayoutRows.setVisibility(View.INVISIBLE);

            this.textView1Seats.setVisibility(View.INVISIBLE);
            this.textView2Seats.setVisibility(View.INVISIBLE);
            this.textView3Seats.setVisibility(View.INVISIBLE);

            this.gridLayoutSeats.setVisibility(View.INVISIBLE);

            Log.d(logTag, "OnPreExecute przed zmianą buttonów");
            for(Button b : buttons)
                b.setVisibility(View.INVISIBLE);

            Log.d(logTag, "OnPreExecute po zminie buttonów");
            this.btnApprove.setVisibility(View.INVISIBLE);

            //w celach testowych
        for (Map.Entry<Integer, Integer> entry : selectedSeats.entrySet()) {
            int number = entry.getKey();
            int seatType = entry.getValue();

            Log.d(logTag, "Przekazana wartość (nr, seatTypeId) do popup = (" + number + ", " +  seatType + ")");

        }

        //zmiana numerów kolumn
        int number = 1;
        if(buttonIR_1.getText().equals("1") || buttonIR_1.getText().equals("71") || buttonIR_1.getText().equals("141") || buttonIR_1.getText().equals("211")){
            for(Button cb : columnButtons){
                cb.setText(number+"");
                number++;
            }
        }
        else{
            number = 8;
            for(Button cb : columnButtons){
                cb.setText(number+"");
                number++;
            }
        }

        //zmiana numerów rzędów
        if(buttonIR_1.getText().equals("1") || buttonIR_1.getText().equals("8")) {
            buttonIR.setText("I");
            buttonIIR.setText("II");
            buttonIIIR.setText("III");
            buttonIVR.setText("IV");
            buttonVR.setText("V");
        }
        else if(buttonIR_1.getText().equals("71") || buttonIR_1.getText().equals("78")){
            buttonIR.setText("VI");
            buttonIIR.setText("VII");
            buttonIIIR.setText("VIII");
            buttonIVR.setText("IX");
            buttonVR.setText("X");
        }
        else if(buttonIR_1.getText().equals("141") || buttonIR_1.getText().equals("148")){
            buttonIR.setText("XI");
            buttonIIR.setText("XII");
            buttonIIIR.setText("XIII");
            buttonIVR.setText("XIV");
            buttonVR.setText("XV");
        }
        else if(buttonIR_1.getText().equals("211") || buttonIR_1.getText().equals("218")){
            buttonIR.setText("XVI");
            buttonIIR.setText("XVII");
            buttonIIIR.setText("XVIII");
            buttonIVR.setText("XIX");
            buttonVR.setText("XX");
        }
    }

    @Override
    protected Void doInBackground(Void ... voids) {


    return null;

    }

    protected void onProgressUpdate(Integer... values) {
        /*progressBarSeats.setVisibility(View.VISIBLE);
        super.onProgressUpdate(values);
        progressBarSeats.setProgress(values[0]);*/

    }

    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

        int index = 0;
        for(Button b  : buttons) {
            changeColorOfButton(b);
            index++;
        }

        // pętla służąca do pokazania użytkownikowi miejsc,które wcześniej wybrał (miejsca są
        // zaznaczane na nowo w momencie ponownego kliknięcia w sektor)
        for (Button b : buttons) {
            int number =  Integer.parseInt(b.getText().toString()); //parsowanie nr miejsca do int
            if(selectedSeats.containsKey(number)){
                b.setBackgroundResource(R.drawable.button_light);
                Log.d(logTag, "Znaleziona ponowna wartość seatNumber: " + number);

                textView3Seats.setVisibility(View.VISIBLE);
                btnApprove.setVisibility(View.VISIBLE);
            }

        }

        for(Button b : buttons)
            b.setVisibility(View.VISIBLE);


        this.linearLayoutRows.setVisibility(View.VISIBLE);

        this.textView1Seats.setVisibility(View.VISIBLE);
        this.textView2Seats.setVisibility(View.VISIBLE);


        this.gridLayoutSeats.setVisibility(View.VISIBLE);

        this.progressBarSeats.setVisibility(View.INVISIBLE);
        this.textView3Seats.setVisibility(View.INVISIBLE);
        this.btnApprove.setVisibility(View.INVISIBLE);

       //textView3Seats.append(""+0);

        int cena=10;

        switch(seatTypeId){
            case 1:
                cena=10;
                break;

            case 2:
                cena=15;
                break;

            case 3:
                cena=20;
                break;

            case 4:
                cena=30;
                break;
        }

        textView2Seats.setText("Cena za miejsce: " + cena + " zł");


    }


}


