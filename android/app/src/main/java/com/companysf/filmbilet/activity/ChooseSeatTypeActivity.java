package com.companysf.filmbilet.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.companysf.filmbilet.R;
import com.companysf.filmbilet.addition.ErrorDetector;
import com.companysf.filmbilet.addition.SQLiteHandler;
import com.companysf.filmbilet.addition.SessionManager;
import com.companysf.filmbilet.app.AppConfig;
import com.companysf.filmbilet.app.AppController;
import com.companysf.filmbilet.appLogic.Reservation;
import com.companysf.filmbilet.webSocket.Message;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import java.util.LinkedHashMap;

import java.util.Map;
import java.util.TreeMap;

import okhttp3.OkHttpClient;

import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;


public class ChooseSeatTypeActivity extends AppCompatActivity {

    private View popupView;
    private static final String logTag = MainActivity.class.getSimpleName();
    private SessionManager sManager;
    private SQLiteHandler db;
    private ErrorDetector ed;

    //TODO zmiana map na tablicę
    private Map<Button, Boolean> sectorButtons = new LinkedHashMap<>(); //zmiana na LinkedHashMap w celu pamiętania kolejności wstawianych elementów
    private Map<Integer, Integer> sectorAndSeat = new TreeMap<>();

    private PopupWindow popupWindow=null;

    private ConstraintLayout constraintLayout;
    private FrameLayout frameLayout; //używany do przyciemnienia backgroundu w popup
    private Button button1, button2, button3, button4, button5, button6, button7, button8, btn_next;
    private ProgressBar progressBar;

    private  TextView textView3, textView4;

    //popup

    private LinearLayout linearLayoutRows;
    private TextView textView1Seats, textView2Seats, textView3Seats;
    private GridLayout gridLayoutSeats;


    //seats columns and rows
    private Button button1C, button2C, button3C, button4C, button5C, button6C, button7C,
    buttonIR, buttonIIR, buttonIIIR, buttonIVR, buttonVR,  btnApprove;

    private ArrayList<Button> columnButtons = new ArrayList<>();

    //seats
    private Button buttonIR_1, buttonIR_2, buttonIR_3, buttonIR_4, buttonIR_5,buttonIR_6, buttonIR_7,
            buttonIIR_1, buttonIIR_2, buttonIIR_3, buttonIIR_4, buttonIIR_5,buttonIIR_6, buttonIIR_7,
            buttonIIIR_1, buttonIIIR_2, buttonIIIR_3, buttonIIIR_4, buttonIIIR_5,buttonIIIR_6, buttonIIIR_7,
            buttonIVR_1, buttonIVR_2, buttonIVR_3, buttonIVR_4, buttonIVR_5,buttonIVR_6, buttonIVR_7,
            buttonVR_1, buttonVR_2, buttonVR_3, buttonVR_4, buttonVR_5,buttonVR_6, buttonVR_7, btnReserve, buttonClose, btn;




    private Map<Button, Boolean> seatButtons = new LinkedHashMap<>();


    //TODO zamiana HashMap na tablice
    private Map<Integer, Integer> selectedSeats = new HashMap<>();
    //TODO zamiana HashMap na tablice
    private Map<Integer, Integer> seatAndRowMap = new HashMap<>();


    //zmienne służace do komunikacji z socketem

    private boolean choosedPlaces [] = new boolean[280]; //zajęte miejsce (wiadomo o nich z socketu i z BD)
    private boolean myChoosedPlaces[] = new boolean[280]; //zajęte miejsca przez użytkownika



    public void preparePopUp(){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {


         Map<Button, Integer> seatNumber = new HashMap<>();//mapa zawierająca button oraz odpowiadający mu nr siedzenia
         ArrayList<Button> buttons = new ArrayList<>(seatButtons.keySet());

        int seatTypeId = selectedSector();


        boolean flags [] = new boolean[8];
        int inc=0;
        for (Map.Entry<Button, Boolean> entry : sectorButtons.entrySet()) {
            flags[inc] = entry.getValue();
            inc++;
        }

        int startSeat = 1;

        if(flags[1]) startSeat=8;
        else if (flags[2]) startSeat = 71;
        else if (flags[3]) startSeat = 78;
        else if (flags[4]) startSeat = 141;
        else if (flags[5]) startSeat = 148;
        else if (flags[6]) startSeat = 211;
        else if(flags[7]) startSeat = 218;

        int seatNr = startSeat;

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

        for(Button b : buttons)
            Log.d(logTag, "Zawartość b : " + b.getText());

        //Log.d(logTag, "seatNumber.get(0) =  " + seatNumber.get(0));

        String text;
        int logNr=0;
        for(int i=0; i<35; i++){
            text = Integer.toString(seatNumber.get(buttons.get(i)));
            Log.d(logTag, "Pobrana wartość : " +seatNumber.get(buttons.get(i)));

            /*if(text.length() == 3)
                buttons.get(i).setTextSize(TypedValue.COMPLEX_UNIT_SP, 7);*/

            text =  Integer.toString(seatNumber.get( buttons.get(i)));
            buttons.get(i).setText(text);
            Log.d(logTag, "Zmieniona wartość textu buttona: " + seatNumber.get( buttons.get(logNr)) );
            logNr++;
        }

        //ustawienie prawidłowej nazwy sektora

        text = "Sektor 1";
        switch(startSeat){
            case 8:
                text="Sektor 2";
                break;
            case 71:
                text="Sektor 3";
                break;
            case 78:
                text = "Sektor 4";
                break;
            case 141:
                text = "Sektor 5";
                break;
            case 148:
                text="Sektor 6";
                break;
            case 211:
                text="Sektor 7";
                break;
            case 218:
                text="Sektor 8";
                break;
        }

        textView1Seats.setText(text);

        linearLayoutRows.setVisibility(View.INVISIBLE);

        textView1Seats.setVisibility(View.INVISIBLE);
        textView2Seats.setVisibility(View.INVISIBLE);
        textView3Seats.setVisibility(View.INVISIBLE);

        gridLayoutSeats.setVisibility(View.INVISIBLE);

        Log.d(logTag, "OnPreExecute przed zmianą buttonów");
        for(Button b : buttons){
            b.setBackgroundResource(R.drawable.button_normal_seat);
            b.setVisibility(View.INVISIBLE);
        }

        Log.d(logTag, "OnPreExecute po zminie buttonów");
        btnApprove.setVisibility(View.INVISIBLE);

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
                String txt = Integer.toString(number);
                cb.setText(txt);
                Log.d(logTag, "Wartość cb.setText = " + cb.getText());
                number++;
            }
        }
        else{
            number = 8;
            for(Button cb : columnButtons){
                String txt = Integer.toString(number);
                cb.setText(txt);
                Log.d(logTag, "Wartość cb.setText = " + cb.getText());
                number++;
            }
        }
        columnButtons.clear();

        //zmiana numerów rzędów

        //tablica rzymskich numerów
        String[] numberOfRow  = new String[20];
        numberOfRow[0]= "I";
        numberOfRow[1]= "II";
        numberOfRow[2]= "III";
        numberOfRow[3]= "IV";
        numberOfRow[4]= "V";
        numberOfRow[5]= "VI";
        numberOfRow[6]= "VII";
        numberOfRow[7]= "VIII";
        numberOfRow[8]= "IX";
        numberOfRow[9]= "X";
        numberOfRow[10]= "XI";
        numberOfRow[11]= "XII";
        numberOfRow[12]= "XIII";
        numberOfRow[13]= "XIV";
        numberOfRow[14]= "XV";
        numberOfRow[15]= "XVI";
        numberOfRow[16]= "XVII";
        numberOfRow[17]= "XVIII";
        numberOfRow[18]= "XIX";
        numberOfRow[19]= "XX";

        if(buttonIR_1.getText().equals("1") || buttonIR_1.getText().equals("8")) {
            buttonIR.setText(numberOfRow[0]);
            buttonIIR.setText(numberOfRow[1]);
            buttonIIIR.setText(numberOfRow[2]);
            buttonIVR.setText(numberOfRow[3]);
            buttonVR.setText(numberOfRow[4]);
        }
        else if(buttonIR_1.getText().equals("71") || buttonIR_1.getText().equals("78")){
            buttonIR.setText(numberOfRow[5]);
            buttonIIR.setText(numberOfRow[6]);
            buttonIIIR.setText(numberOfRow[7]);
            buttonIVR.setText(numberOfRow[8]);
            buttonVR.setText(numberOfRow[9]);
        }
        else if(buttonIR_1.getText().equals("141") || buttonIR_1.getText().equals("148")){
            buttonIR.setText(numberOfRow[10]);
            buttonIIR.setText(numberOfRow[11]);
            buttonIIIR.setText(numberOfRow[12]);
            buttonIVR.setText(numberOfRow[13]);
            buttonVR.setText(numberOfRow[14]);
        }
        else if(buttonIR_1.getText().equals("211") || buttonIR_1.getText().equals("218")){
            buttonIR.setText(numberOfRow[15]);
            buttonIIR.setText(numberOfRow[16]);
            buttonIIIR.setText(numberOfRow[17]);
            buttonIVR.setText(numberOfRow[18]);
            buttonVR.setText(numberOfRow[19]);
        }

        markChoosedPlaces();

        // pętla służąca do pokazania użytkownikowi miejsc,które wcześniej wybrał (miejsca są
        // zaznaczane na nowo w momencie ponownego kliknięcia w sektor)
        for (Button b : buttons) {
            int SeatNumber =  Integer.parseInt(b.getText().toString()); //parsowanie nr miejsca do int
            if(selectedSeats.containsKey(SeatNumber)){
                b.setBackgroundResource(R.drawable.button_light);
                Log.d(logTag, "Znaleziona ponowna wartość seatNumber: " + number);

                textView3Seats.setVisibility(View.VISIBLE);
                btnApprove.setVisibility(View.VISIBLE);
            }

        }

        for(Button b : buttons)
            b.setVisibility(View.VISIBLE);


        linearLayoutRows.setVisibility(View.VISIBLE);

        textView1Seats.setVisibility(View.VISIBLE);
        textView2Seats.setVisibility(View.VISIBLE);


        gridLayoutSeats.setVisibility(View.VISIBLE);

        textView3Seats.setVisibility(View.INVISIBLE);
        btnApprove.setVisibility(View.INVISIBLE);


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

        String txt = "Cena za miejsce: " + cena + " zł";
        textView2Seats.setText(txt);

            }

        });

    }

    public void markChoosedPlaces() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                boolean displayDialog = false;
                ArrayList<Integer> takenCheckedSeats = new ArrayList<>();

                for (Map.Entry<Button, Boolean> entry : seatButtons.entrySet()) {
                    Button button = entry.getKey();
                    Boolean isChecked = entry.getValue();

                    int index =  Integer.parseInt(button.getText().toString()); //parsowanie nr miejsca do int
                    if(choosedPlaces[index-1]){

                            button.setEnabled(false);
                            button.setBackgroundResource(R.drawable.button_taken);
                            button.setTextColor(Color.WHITE);

                            if(isChecked){
                                //sytuacja, gdy ktoś zajmie miejsce/miejsca w sektorze, które przed chwilą wybraliśmy
                                seatButtons.put(button, false);
                                displayDialog=true;
                                takenCheckedSeats.add(Integer.parseInt(button.getText().toString())); //parsowanie nr miejsca do int i zapisanie do kolekcji;

                                int selected = selectedSeats();

                                if(selected > 0) {
                                    btnApprove.setVisibility(View.VISIBLE);
                                    textView3Seats.setVisibility(View.VISIBLE);
                                    //TODO nie robić konkatenacji na setText(), poniżej przykład

                                    String text = "Wybrane miejsca: " + selected;
                                    textView3Seats.setText(text);
                                }
                                else{
                                    btnApprove.setVisibility(View.INVISIBLE);
                                    textView3Seats.setVisibility(View.INVISIBLE);
                                }
                            }
                    }

                }

                if(displayDialog) {
                    //wyświetlenie komunikatu

                            if(takenCheckedSeats.size()==1)
                            ed.buildDialog(ChooseSeatTypeActivity.this, "Zajęcie miejsca",
                            "Inny użytkownik przed chwilą zajął wybrane przez ciebie miejsce o numerze " +
                                    takenCheckedSeats.get(0) +".").show();

                            else{
                                //jeśli więcej miejsc
                                //zbudowanie Stringa

                                int i=0;
                                String text = "";

                                //posortowanie kolekcji
                                Collections.sort(takenCheckedSeats);


                                StringBuilder sB = new StringBuilder(text);

                                for(Integer in : takenCheckedSeats) {

                                    if(i>0)
                                    sB.append(", ");
                                    sB.append(in);

                                    i++;
                                }

                                text = sB.toString();
                                ed.buildDialog(ChooseSeatTypeActivity.this, "Zajęcie miejsc",
                                        "Inny użytkownik przed chwilą zajął wybrane przez ciebie miejsca o numerach " +
                                                text + ".").show();
                            }

                }
            }
        });
    }

    private void logOutCustomer(){
        sManager.setLogin(false);

        db.deleteCustomers();

    }

    public int selectedSeats() {

        int selected = 0;
        for (Map.Entry<Button, Boolean> entry : seatButtons.entrySet()) {
            Boolean value = entry.getValue();

            if(value) selected++;

        }
        return selected;
    }

    public int selectedSector(){
        boolean flags [] = new boolean[8];
        int inc=0;
        for (Map.Entry<Button, Boolean> entry : sectorButtons.entrySet()) {
            flags[inc] = entry.getValue();
            inc++;
        }

        //wykrycie, który typ miejsca został wybrany
        int seatTypeId=1;

        if(flags[2] || flags[3])
            seatTypeId=2;
        else if(flags[4] || flags[5])
            seatTypeId=3;
        else if (flags[6] || flags[7])
            seatTypeId=4;

        return seatTypeId;

    }

    public void saveSeats(int seatType){

        for (Map.Entry<Button, Boolean> entry : seatButtons.entrySet()) {
            Button key = entry.getKey();
            Boolean value = entry.getValue();

            if(value){
                int number =  Integer.parseInt(key.getText().toString()); //parsowanie nr miejsca do int
                selectedSeats.put(number, seatType);

                Log.d(logTag, "Zapisane miejsce (nr, seatTypeId) = (" + number + ", " +  seatType + ")");
            }

        }
    }


    public void updateSummary(){


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int numberOfSeats = 0;
                int price = 0;

                //aktualizacja podsumowania

                for (Map.Entry<Integer, Integer> entry : selectedSeats.entrySet()) {
                    int seatTypeId = entry.getValue();

                    numberOfSeats++;

                    switch (seatTypeId) {
                        case 1:
                            price += 10;
                            break;
                        case 2:
                            price += 15;
                            break;

                        case 3:
                            price += 20;
                            break;

                        case 4:
                            price += 30;
                            break;
                    }

                }

                if(numberOfSeats>0){
                    String text = "Liczba miejsc: " + numberOfSeats
                            + "\nCena: " + price + " zł";
                    textView4.setText(text);
                }

                else{
                    textView3.setText("");
                    textView4.setText("");
                    btnReserve.setEnabled(false);
                    btnReserve.setTextColor(Color.BLACK);
                    btnReserve.setBackgroundResource(R.drawable.rounded_button_gray);
                }
            }
            });

    }

    public void buildDialog(ArrayList<Integer> list){
        final ArrayList<Integer> takenYourSeats = new ArrayList<>(list);
        //final ErrorDetector det = new ErrorDetector(this);

        for(Integer i :takenYourSeats)
            Log.d(logTag, "Zawartość takenYourSeats = " + i);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                    if(takenYourSeats.size() == 1) {
                        ed.buildDialog(ChooseSeatTypeActivity.this, "Zajęcie miejsca",
                                "Inny użytkownik przed chwilą zajął wybrane przez ciebie miejsce o numerze " + takenYourSeats.get(0) + ".").show();
                    }
                        else if(takenYourSeats.size() > 1){

                        String text="";

                        StringBuilder sB = new StringBuilder(text);
                        int i=0;
                        for(Integer in : takenYourSeats) {
                            if(i>0)
                                sB.append(", ");

                            sB.append(in);

                            i++;
                        }
                        text=sB.toString();

                        ed.buildDialog(ChooseSeatTypeActivity.this, "Zajęcie miejsc", "Inny użytkownik przed chwilą zajął wybrane przez ciebie miejsca o numerach: " + text + ".").show();

                    }
            }
        });

    }


    public void updateSectors(boolean ifStart){

        final boolean start = ifStart;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if(start){

                    button1.setBackgroundResource(R.drawable.button_normal_first);

                    button2.setBackgroundResource(R.drawable.button_normal_first);

                    button3.setBackgroundResource(R.drawable.button_normal_second);

                    button4.setBackgroundResource(R.drawable.button_normal_second);

                    button5.setBackgroundResource(R.drawable.button_normal_third);

                    button6.setBackgroundResource(R.drawable.button_normal_third);

                    button7.setBackgroundResource(R.drawable.button_normal_fourth);

                    button8.setBackgroundResource(R.drawable.button_normal_fourth);

                    button1.setBackgroundResource(R.drawable.button_normal_first);

                    btn_next.setVisibility(View.INVISIBLE);

                    progressBar.setVisibility(View.INVISIBLE);

                    textView3.setVisibility(View.INVISIBLE);

                    textView4.setVisibility(View.INVISIBLE);


                }


                int sectorNumber = 1;

                Log.d(logTag, "Zawartość choosedPlaces w metodzie updateSectors: ");
                for(int i=0; i<choosedPlaces.length; i++)
                    Log.d(logTag, "choosedPlaces[ " + i + " ] = " + choosedPlaces[i]);

                for (Map.Entry<Button, Boolean> entry1 : sectorButtons.entrySet()) {
                    Button btn = entry1.getKey();

                    int takenSeats=0;
                    for(Map.Entry<Integer, Integer> entry2 : sectorAndSeat.entrySet()){
                        int seatNumber = entry2.getKey();
                        int sector = entry2.getValue();


                        if(sector == sectorNumber){
                            if(choosedPlaces[seatNumber-1]){
                                takenSeats++;
                                Log.d(logTag, "choosedPlaces[(seatNumber-1) = " + (seatNumber-1) + " ] = true oraz takenSeats = "  + takenSeats);
                            }

                        }
                    }

                    int free = 35 - takenSeats;
                    Log.d(logTag, "free = " + free);
                    String prepText = free + "/35";
                    btn.setText(prepText);

                    //w przypadku zajęcia wszystkich miejsc, przycisk staje się nieaktywny
                    //if(free==0) btn.setEnabled(false);

                        sectorNumber++;
                }

            }

        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ed = new ErrorDetector(this);

        //sprawdzenie zalogowania

        sManager = new SessionManager(getApplicationContext());
        db = new SQLiteHandler(getApplicationContext());

        if (!sManager.isLoggedIn()) {
            logOutCustomer();
        }


        setContentView(R.layout.activity_choose_seat_type2);


        //adjusting size of  the sectorButtons

        constraintLayout = findViewById(R.id.constraintLayout);

        button1 = findViewById(R.id.button1);
        button2 =  findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button5 =  findViewById(R.id.button5);
        button6 =  findViewById(R.id.button6);
        button7 =  findViewById(R.id.button7);
        button8 =  findViewById(R.id.button8);

        btn_next =  findViewById(R.id.btn_next);
        btnReserve =  findViewById(R.id.btnReserve);

        progressBar =  findViewById(R.id.progressBar);

        textView3= findViewById(R.id.textView3);
        textView4= findViewById(R.id.textView4);

        frameLayout =  findViewById(R.id.frameLayout);
        frameLayout.getForeground().setAlpha( 0);

        sectorButtons.put(button1, false);
        sectorButtons.put(button2, false);
        sectorButtons.put(button3, false);
        sectorButtons.put(button4, false);
        sectorButtons.put(button5, false);
        sectorButtons.put(button6, false);
        sectorButtons.put(button7, false);
        sectorButtons.put(button8, false);



        //przyciski reprezentujące miejsca

        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);

        final ViewGroup nullParent = null;
        popupView = inflater.inflate(R.layout.activity_choose_seat_left, nullParent);

        //TODO forEach na każdy z przycisków

        //wypełnienie mapy <Nr_miejsca, Rząd> potrzebenej do znalezienia wiersza dla konkretnego miejsca
        int value=1;

        for(int i=1; i<=280; i++){
            if ((i-1) % 14 == 0 && i!=1)
                value++;

            seatAndRowMap.put(i, value);

            Log.d(logTag, "Wstawiona wartość <Nr_miejsca, Rząd>= " + "<" + i + ", " + value + ">");

        }

        //wypełnienie mapy <NrMiejsca, Sektor> potrzebnej do obliczenia liczby wolnych miejsc w sektorze po otrzymaniu wiadomości

        //rzędy parzyste
        int sectorNumber = 1;
        for(int i=1; i<=211; i+=70) {
                int firstSeat = i;

                for (int j = 1; j <= 35; j++) {
                     if(j==8 || j==15 || j==22 || j==29) {
                         firstSeat += 7;
                         sectorAndSeat.put(firstSeat,sectorNumber);
                         firstSeat++;
                     }
                     else{
                         sectorAndSeat.put(firstSeat,sectorNumber);
                         firstSeat++;
                     }

                }
                    sectorNumber+=2;
            }

            sectorNumber=2;
        for(int i=8; i<=218; i+=70){
            int firstSeat = i;

            for (int j = 1; j <= 35; j++) {
                if(j==8 || j==15 || j==22 || j==29) {
                    firstSeat += 7;
                    sectorAndSeat.put(firstSeat, sectorNumber);
                    firstSeat++;
                }
                else{
                    sectorAndSeat.put(firstSeat, sectorNumber);
                    firstSeat++;
                }
            }
            sectorNumber+=2;
        }

        //posortowanie mapy (wg wartości klucza)
       // List sortedKeys = new ArrayList(sectorAndSeat.keySet());
        //Collections.sort(sortedKeys);


        //test

        for (Map.Entry<Integer, Integer> entry : sectorAndSeat.entrySet()) {
            int seatNumber = entry.getKey();
            int sector = entry.getValue();

            Log.d(logTag, "sectorAndSeat<seatNumber, sector> = "+ "< " + seatNumber + ", "
                    + sector  + " >");
        }

        //ConstraintLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)button1.getLayoutParams();


        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            //wielkość elementów
            ConstraintLayout.LayoutParams params1 = (ConstraintLayout.LayoutParams) button1.getLayoutParams();
            params1.width = (getResources().getDisplayMetrics().widthPixels) / 4;
            params1.height = ((getResources().getDisplayMetrics().heightPixels / 3)) / 4;
            button1.setLayoutParams(params1);

            ConstraintLayout.LayoutParams params2 = (ConstraintLayout.LayoutParams) button2.getLayoutParams();
            params2.width = (getResources().getDisplayMetrics().widthPixels) / 4;
            params2.height = ((getResources().getDisplayMetrics().heightPixels / 3)) / 4;
            button2.setLayoutParams(params2);

            ConstraintLayout.LayoutParams params3 = (ConstraintLayout.LayoutParams) button3.getLayoutParams();
            params3.width = (getResources().getDisplayMetrics().widthPixels) / 4;
            params3.height = ((getResources().getDisplayMetrics().heightPixels / 3)) / 4;
            button3.setLayoutParams(params3);

            ConstraintLayout.LayoutParams params4 = (ConstraintLayout.LayoutParams) button4.getLayoutParams();
            params4.width = (getResources().getDisplayMetrics().widthPixels) / 4;
            params4.height = ((getResources().getDisplayMetrics().heightPixels / 3)) / 4;
            button4.setLayoutParams(params4);


            ConstraintLayout.LayoutParams params5 = (ConstraintLayout.LayoutParams) button5.getLayoutParams();
            params5.width = (getResources().getDisplayMetrics().widthPixels) / 4;
            params5.height = ((getResources().getDisplayMetrics().heightPixels / 3)) / 4;
            button5.setLayoutParams(params5);

            ConstraintLayout.LayoutParams params6 = (ConstraintLayout.LayoutParams) button6.getLayoutParams();
            params6.width = (getResources().getDisplayMetrics().widthPixels) / 4;
            params6.height = ((getResources().getDisplayMetrics().heightPixels / 3)) / 4;
            button6.setLayoutParams(params6);

            ConstraintLayout.LayoutParams params7 = (ConstraintLayout.LayoutParams) button7.getLayoutParams();
            params7.width = (getResources().getDisplayMetrics().widthPixels) / 4;
            params7.height = ((getResources().getDisplayMetrics().heightPixels / 3)) / 4;
            button7.setLayoutParams(params7);

            ConstraintLayout.LayoutParams params8 = (ConstraintLayout.LayoutParams) button8.getLayoutParams();
            params8.width = (getResources().getDisplayMetrics().widthPixels) / 4;
            params8.height = ((getResources().getDisplayMetrics().heightPixels / 3)) / 4;
            button8.setLayoutParams(params8);



            ConstraintLayout.LayoutParams params10 = (ConstraintLayout.LayoutParams) btn_next.getLayoutParams();
            params10.width = (getResources().getDisplayMetrics().widthPixels) / 4;
            params10.height = ((getResources().getDisplayMetrics().heightPixels / 3)) / 4;
            btn_next.setLayoutParams(params10);


            //execute(nr_repertuaru z poprzedniuego intentu)
            //TODO wstawienie do execute() nr repertuaru

            //button wyłączony do czasu wyboru miejsca
            this.btn_next.setEnabled(false);


            //odświeżanie wolnych sektorów co 2 sekundy

/*
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {

                public void run() {
                    //zapisanie poprzedniego stanu sektorów
                    Map<Button, Boolean> previousButtons = new HashMap<>(sectorButtons);
                    new FreeSectorsTask(getApplicationContext(), constraintLayout, false).execute(1);
                    //dodanie zabezpieczenia przed wybraniem miejsca które, akurat zostało zajęte
                    handler.postDelayed(this, 2000); //now is every 500 ms
                }
            }, 2000); //Every 500 ms
            */


        }//endif
        else {

            String text ="S1 dost.: 10 zł";
            button1.setText(text);
            button2.setText(text);

            text = "S2 dost.: 15 zł";
            button3.setText(text);
            button4.setText(text);

            text="S3 dost.: 20 zł";
            button5.setText(text);
            button6.setText(text);

            text="S4 dost.: 30 zł";
            button7.setText(text);
            button8.setText(text);


        }


        //obługa związana z socketem

        for(int i=0; i<choosedPlaces.length; i++){
            myChoosedPlaces[i]=false;
        }


        //TODO pobranie informacji z bazy danych na temat pobranych miejsc (DLA DANEGO REPERTUARU)

        Bundle b = getIntent().getExtras();

        int repertoireId = b.getInt("repertoireId");

        final String repertoireIdString = "" + repertoireId;

        RequestFuture<StringRequest> future = RequestFuture.newFuture();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                AppConfig.GET_RESERVATIONS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(logTag, "Reservation request: " + response);
                        try {
                            JSONObject json = new JSONObject(response);
                            boolean error = json.getBoolean("error");
                            if (error) {
                                Toast.makeText(
                                        getApplicationContext(),
                                        json.getString("message"),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                JSONArray reservationsJson = json.getJSONArray("reservation");
                                for (int i = 0; i < reservationsJson.length(); i++) {
                                    Log.d(logTag, "reservationJsonLOG " + reservationsJson.length());
                                    JSONObject reservationJSON = reservationsJson.getJSONObject(i);
                                    Reservation reservation = new Reservation(
                                            reservationJSON.getInt("id"),
                                            reservationJSON.getInt("customerId"),
                                            reservationJSON.getInt("seatNumber"),
                                            reservationJSON.getInt("row"),
                                            reservationJSON.getString("date"),
                                            reservationJSON.getInt("seatTypeId")
                                    );


                                    choosedPlaces[reservation.getSeatNumber()-1]=true;
                                    int number = reservation.getSeatNumber()-1;

                                    Log.d(logTag, "choosedPlaces[ " + number + " ] = "  +  choosedPlaces[number]);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Json error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }

                        for(int i=0; i<choosedPlaces.length; i++)
                            if(choosedPlaces[i])
                                Log.d(logTag, "After choosedPlaces[ " + i + " ] = "  +  choosedPlaces[i]);

                        updateSectors(true);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(logTag, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("repertoireId", repertoireIdString);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, "req_register");


        final OkHttpClient httpClient = new OkHttpClient();
        okhttp3.Request request = new okhttp3.Request.Builder().url(AppConfig.websocketURL).build();

        WebSocketListener webSocketListener = new WebSocketListener() {
            @Override
            public void onOpen(final WebSocket webSocket, okhttp3.Response response) {



                Log.d(logTag, "Connection estabilished");



                View.OnClickListener buttonClicked = new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {


                        btn =  findViewById(v.getId());


                        //jeśli button jest wciśnięty, to go odznacz

                        //wyświetlenie komunikatu w sytuacji, gdy ktoś kliknie na sektor bez miejsc
                        if(btn.getText().equals("0/35")){

                            ed.buildDialog(ChooseSeatTypeActivity.this, "Brak miejsc w sektorze",
                                    "Niestety, wszystkie miejsca w tym sektorze są zajęte. Spróbuj znaleźć miejsce w innym sektorze.").show();

                            return;
                        }

                        //TODO może powodować NullPointerExceptin

                        if (sectorButtons.get(btn)) {
                            sectorButtons.put(btn, false);

                            Animation animation = new AlphaAnimation(1.0f, 0.0f);
                            animation.setDuration(200);

                            btn.startAnimation(animation);

                            if (btn == button1 || btn == button2)
                                btn.setBackgroundResource(R.drawable.button_normal_first);
                            else if (btn == button3 || btn == button4)
                                btn.setBackgroundResource(R.drawable.button_normal_second);
                            else if (btn == button5 || btn == button6)
                                btn.setBackgroundResource(R.drawable.button_normal_third);
                            else if (btn == button7 || btn == button8)
                                btn.setBackgroundResource(R.drawable.button_normal_fourth);
                        }

                        //jeśli button nie jest wciśnięty to wyświetl popup
                        else {

                            sectorButtons.put(btn, true);

                            //btn.setBackgroundResource(R.drawable.button_light);

                            //po wciśnięciu button, następuje zmiana jego przezroczystości (bez zmiany koloru)
                            btn.getBackground().setAlpha(128);


                            LayoutInflater inflater = (LayoutInflater)
                                    getSystemService(LAYOUT_INFLATER_SERVICE);


                            //TODO NullPointerException
                            final ViewGroup nullParent = null;
                            popupView = inflater.inflate(R.layout.activity_choose_seat_left, nullParent);

                            //po kliknięciu dowolnego "aktywnego" sektora od razu pokazuje się popup oraz "chowają" elementy pod nim


                            //new FreeSeatsTask(getApplicationContext(), popupView, selectedSeats,choosedPlaces, startSeat, seatTypeId).execute();

                            // create the popup window
                            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                            popupWindow = new PopupWindow(popupView, width, height, true);



                            // show the popup window
                            // which view you pass in doesn't matter, it is only used for the window tolken


                            //prawidłowe położenia popup
                            popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 40);


                            //przyciemnienie backgroundu pod popupem
                            frameLayout.getForeground().setAlpha(165);


                            popupWindow.setBackgroundDrawable(new BitmapDrawable());
                            popupWindow.setFocusable(false);


                            buttonIR_1 =  popupView.findViewById(R.id.buttonIR_1);
                            seatButtons.put(buttonIR_1, false);
                            buttonIR_2 =  popupView.findViewById(R.id.buttonIR_2);
                            seatButtons.put(buttonIR_2, false);
                            buttonIR_3 =  popupView.findViewById(R.id.buttonIR_3);
                            seatButtons.put(buttonIR_3, false);
                            buttonIR_4 =  popupView.findViewById(R.id.buttonIR_4);
                            seatButtons.put(buttonIR_4, false);
                            buttonIR_5 =  popupView.findViewById(R.id.buttonIR_5);
                            seatButtons.put(buttonIR_5, false);
                            buttonIR_6 =  popupView.findViewById(R.id.buttonIR_6);
                            seatButtons.put(buttonIR_6, false);
                            buttonIR_7 =  popupView.findViewById(R.id.buttonIR_7);
                            seatButtons.put(buttonIR_7, false);

                            buttonIIR_1 =  popupView.findViewById(R.id.buttonIIR_1);
                            seatButtons.put(buttonIIR_1, false);
                            buttonIIR_2 =  popupView.findViewById(R.id.buttonIIR_2);
                            seatButtons.put(buttonIIR_2, false);
                            buttonIIR_3 =  popupView.findViewById(R.id.buttonIIR_3);
                            seatButtons.put(buttonIIR_3, false);
                            buttonIIR_4 =  popupView.findViewById(R.id.buttonIIR_4);
                            seatButtons.put(buttonIIR_4, false);
                            buttonIIR_5 =  popupView.findViewById(R.id.buttonIIR_5);
                            seatButtons.put(buttonIIR_5, false);
                            buttonIIR_6 =  popupView.findViewById(R.id.buttonIIR_6);
                            seatButtons.put(buttonIIR_6, false);
                            buttonIIR_7 =  popupView.findViewById(R.id.buttonIIR_7);
                            seatButtons.put(buttonIIR_7, false);

                            buttonIIIR_1 =  popupView.findViewById(R.id.buttonIIIR_1);
                            seatButtons.put(buttonIIIR_1, false);
                            buttonIIIR_2 =  popupView.findViewById(R.id.buttonIIIR_2);
                            seatButtons.put(buttonIIIR_2, false);
                            buttonIIIR_3 =  popupView.findViewById(R.id.buttonIIIR_3);
                            seatButtons.put(buttonIIIR_3, false);
                            buttonIIIR_4 =  popupView.findViewById(R.id.buttonIIIR_4);
                            seatButtons.put(buttonIIIR_4, false);
                            buttonIIIR_5 =  popupView.findViewById(R.id.buttonIIIR_5);
                            seatButtons.put(buttonIIIR_5, false);
                            buttonIIIR_6 =  popupView.findViewById(R.id.buttonIIIR_6);
                            seatButtons.put(buttonIIIR_6, false);
                            buttonIIIR_7 =  popupView.findViewById(R.id.buttonIIIR_7);
                            seatButtons.put(buttonIIIR_7, false);

                            buttonIVR_1 =  popupView.findViewById(R.id.buttonIVR_1);
                            seatButtons.put(buttonIVR_1, false);
                            buttonIVR_2 =  popupView.findViewById(R.id.buttonIVR_2);
                            seatButtons.put(buttonIVR_2, false);
                            buttonIVR_3 =  popupView.findViewById(R.id.buttonIVR_3);
                            seatButtons.put(buttonIVR_3, false);
                            buttonIVR_4 =  popupView.findViewById(R.id.buttonIVR_4);
                            seatButtons.put(buttonIVR_4, false);
                            buttonIVR_5 =  popupView.findViewById(R.id.buttonIVR_5);
                            seatButtons.put(buttonIVR_5, false);
                            buttonIVR_6 =  popupView.findViewById(R.id.buttonIVR_6);
                            seatButtons.put(buttonIVR_6, false);
                            buttonIVR_7 =  popupView.findViewById(R.id.buttonIVR_7);
                            seatButtons.put(buttonIVR_7, false);

                            buttonVR_1 =  popupView.findViewById(R.id.buttonVR_1);
                            seatButtons.put(buttonVR_1, false);
                            buttonVR_2 =  popupView.findViewById(R.id.buttonVR_2);
                            seatButtons.put(buttonVR_2, false);
                            buttonVR_3 =  popupView.findViewById(R.id.buttonVR_3);
                            seatButtons.put(buttonVR_3, false);
                            buttonVR_4 =  popupView.findViewById(R.id.buttonVR_4);
                            seatButtons.put(buttonVR_4, false);
                            buttonVR_5 =  popupView.findViewById(R.id.buttonVR_5);
                            seatButtons.put(buttonVR_5, false);
                            buttonVR_6 =  popupView.findViewById(R.id.buttonVR_6);
                            seatButtons.put(buttonVR_6, false);
                            buttonVR_7 =  popupView.findViewById(R.id.buttonVR_7);
                            seatButtons.put(buttonVR_7, false);

                            btnApprove =  popupView.findViewById(R.id.btnApprove);
                            buttonClose = popupView.findViewById(R.id.buttonClose);



                            linearLayoutRows = popupView.findViewById(R.id.linearLayoutRows);

                            textView1Seats = popupView.findViewById(R.id.textView1Seats);
                            textView2Seats = popupView.findViewById(R.id.textView2Seats);
                            textView3Seats = popupView.findViewById(R.id.textView3Seats);

                            gridLayoutSeats =popupView.findViewById(R.id.gridLayoutSeats);

                            button1C =  popupView.findViewById(R.id.button1C);
                            columnButtons.add(button1C);
                            button2C =  popupView.findViewById(R.id.button2C);
                            columnButtons.add(button2C);
                            button3C =  popupView.findViewById(R.id.button3C);
                            columnButtons.add(button3C);
                            button4C =  popupView.findViewById(R.id.button4C);
                            columnButtons.add(button4C);
                            button5C =  popupView.findViewById(R.id.button5C);
                            columnButtons.add(button5C);
                            button6C =  popupView.findViewById(R.id.button6C);
                            columnButtons.add(button6C);
                            button7C =  popupView.findViewById(R.id.button7C);
                            columnButtons.add(button7C);

                            buttonIR =  popupView.findViewById(R.id.buttonIR);
                            buttonIIR =  popupView.findViewById(R.id.buttonIIR);
                            buttonIIIR =  popupView.findViewById(R.id.buttonIIIR);
                            buttonIVR =  popupView.findViewById(R.id.buttonIVR);
                            buttonVR =  popupView.findViewById(R.id.buttonVR);



                            textView3Seats.setVisibility(View.VISIBLE);
                            textView3Seats.setEnabled(true);
                            btnApprove.setVisibility(View.VISIBLE);
                            btnApprove.setEnabled(true);

                            preparePopUp();

                            //po ponownym otwarciu popupu zaliczenie ponownie wybranego buttona jako wciśnięty
                            for (Map.Entry<Button, Boolean> entry : seatButtons.entrySet()) {
                                Button button = entry.getKey();


                                int number = Integer.parseInt(button.getText().toString()); //parsowanie nr miejsca do int
                                if (selectedSeats.containsKey(number)) {
                                    seatButtons.put(button, true);
                                    int selected = selectedSeats();

                                    String text =  "Wybrane miejsca: " + selected;
                                    textView3Seats.setText(text);
                                    Log.d(logTag, "Znaleziona ponowna wartość seatNumber: " + number);

                                }

                            }

                            //popupWindow.set

                            popupView.setOnKeyListener(new View.OnKeyListener() {
                                @Override
                                public boolean onKey(View v, int keyCode, KeyEvent event) {

                                    if (keyCode == KeyEvent.KEYCODE_BACK) {

                                        Log.d(logTag, "onBackPressed1");

                                        popupWindow.dismiss();

                                        //rozjaśnienie backgroundu pod popupem
                                        frameLayout.getForeground().setAlpha(0);

                                        //usunięcie historii wybranych miejsc po kliknięciu poza popup
                                        seatButtons.clear();

                                        sectorButtons.put(btn, false);
                                        constraintLayout.setVisibility(View.VISIBLE); //przywrócenie dolnej warstwy


                                        return true;
                                    } else return false;
                                }
                            });

                            //wciśnięcie przycisku zamykającego popup, przywraca dolną warstwę
                            buttonClose.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                    popupWindow.dismiss();

                                    //rozjaśnienie backgroundu pod popupem
                                    frameLayout.getForeground().setAlpha(0);

                                    //usunięcie historii wybranych miejsc po kliknięciu poza popup
                                    seatButtons.clear();

                                    sectorButtons.put(btn, false);
                                    constraintLayout.setVisibility(View.VISIBLE); //przywrócenie dolnej warstwy


                                }
                            });


                            View.OnClickListener seatBtnClick = new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Button btn =  popupView.findViewById(v.getId());


                                    Animation animation = new AlphaAnimation(1.0f, 0.0f);
                                    animation.setDuration(200);

                                    btn.startAnimation(animation);

                                    //TODO NullPointerException
                                    if (!seatButtons.get(btn)) {
                                        seatButtons.put(btn, true);
                                        btn.setBackgroundResource(R.drawable.button_light);

                                        int number = Integer.parseInt(btn.getText().toString()); //parsowanie nr miejsca do int
                                        myChoosedPlaces[number - 1] = true; //zmiana wartości w tablicy Socketu na true
                                        Log.d(logTag, "Zmieniona wartość myChoosedPlaces[ " + (number-1) + " ] = " +  myChoosedPlaces[number - 1]);
                                    }
                                    else {
                                        seatButtons.put(btn, false);
                                        btn.setBackgroundResource(R.drawable.button_normal);

                                        int number = Integer.parseInt(btn.getText().toString()); //parsowanie nr miejsca do int
                                        selectedSeats.remove(number); //usunięcie z Mapy odznaczonego miejsca
                                        myChoosedPlaces[number - 1] = false; //zmiana wartości w tablicy Socketu na false
                                        Log.d(logTag, "Zmieniona wartość myChoosedPlaces[ " + (number-1) + " ] = " +  myChoosedPlaces[number - 1]);
                                    }


                                    int selected = selectedSeats();


                                        btnApprove.setVisibility(View.VISIBLE);
                                        textView3Seats.setVisibility(View.VISIBLE);
                                        String text = "Wybrane miejsca: " + selected;
                                        textView3Seats.setText(text);


                                }
                            };

                            for (Map.Entry<Button, Boolean> entry : seatButtons.entrySet()) {
                                Button key = entry.getKey();
                                key.setOnClickListener(seatBtnClick);
                            }


                            //dodanie obsługi klawisza zarezerwuj
                            btnApprove.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    //pobranie informacji o wybranych miejscach i zapisanie

                                    //pobranie informacji o seatTypeId (przez pobranie informacji o sektorze)
                                    int seatType = selectedSector();

                                    //pobranie informacji o nr_miejsca
                                    saveSeats(seatType);


                                    popupWindow.dismiss();

                                    //rozjaśnienie backgroundu pod popupem
                                    frameLayout.getForeground().setAlpha(0);


                                    //Wyświetlenie podsumowania
                                    btn_next.setVisibility(View.INVISIBLE);
                                    textView3.setVisibility(View.VISIBLE);
                                    String text = "Podsumowanie:";
                                    textView3.setText(text);




                                    //po wyborze miejsc wyświetlamy podsumowanie oraz nadajemy kolor przyciskowi akceptacji
                                    btnReserve.setBackgroundResource(R.drawable.rounded_bordered_button_light);
                                    btnReserve.setEnabled(true);
                                    btnReserve.setTextColor(Color.WHITE);
                                    textView4.setVisibility(View.VISIBLE);

                                    //obliczenie ceny


                                    int numberOfSeats = 0;
                                    int price = 0;


                                    for (Map.Entry<Integer, Integer> entry : selectedSeats.entrySet()) {
                                        int seatTypeId = entry.getValue();

                                        numberOfSeats++;

                                        switch (seatTypeId) {
                                            case 1:
                                                price += 10;
                                                break;
                                            case 2:
                                                price += 15;
                                                break;

                                            case 3:
                                                price += 20;
                                                break;

                                            case 4:
                                                price += 30;
                                                break;
                                        }

                                    }

                                    if(price > 0) {

                                        String txt = "Liczba miejsc: " + numberOfSeats
                                                + "\nCena: " + price + " zł";

                                        textView4.setText(txt);


                                        btnReserve.setVisibility(View.VISIBLE);

                                        sectorButtons.put(btn, false);
                                        seatButtons.clear();

                                    }
                                    else{
                                        textView3.setVisibility(View.INVISIBLE);
                                        btnReserve.setVisibility(View.INVISIBLE);
                                        textView4.setVisibility(View.INVISIBLE);

                                    }


                                }


                            });


                        }

                    }

                };
                button1.setOnClickListener(buttonClicked);
                button2.setOnClickListener(buttonClicked);
                button3.setOnClickListener(buttonClicked);
                button4.setOnClickListener(buttonClicked);
                button5.setOnClickListener(buttonClicked);
                button6.setOnClickListener(buttonClicked);
                button7.setOnClickListener(buttonClicked);
                button8.setOnClickListener(buttonClicked);


                btnReserve.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(selectedSeats.size()==0)
                            ed.buildDialog(ChooseSeatTypeActivity.this, "Brak wybranych miejsc",
                                    "Nie wybrałeś żadnego miejsca").show();

                        else{

                            //porównanie miejsc obecnie wybranych z tymi pobranymi z bazy danych

                            boolean isWrong=false;
                            for(int i=0; i<choosedPlaces.length; i++)
                                if(choosedPlaces[i] & myChoosedPlaces[i])
                                    isWrong=true;

                            if(isWrong){
                                ed.buildDialog(ChooseSeatTypeActivity.this, "Wybranie zajętego miejsca",
                                        "Wybrałeś zajęte miejsce").show();


                            }
                            else{
                                //zapis do bazy danych

                                int currentCustomerId = 1; //TODO należy to pobrać z informacji o logowaniu

                                int currentRepertoireId = 1; //TODO pobrać z poprzedniego Intetentu

                                //selectedSeats <NrMiejsca, TypMiejsca> - wybrane przez usera
                                //seatAndRowMap <NrMiejsca, Rząd>


                                for (Map.Entry<Integer, Integer> entry : selectedSeats.entrySet()) {

                                    final String customerId = Integer.toString(currentCustomerId);
                                    final String seatNumber = Integer.toString(entry.getKey());
                                    final String seatTypeId = Integer.toString(entry.getValue());
                                    //TODO NullPointerException
                                    final String row = Integer.toString(seatAndRowMap.get(entry.getKey()));
                                    final String repertoireId = Integer.toString(currentRepertoireId);

                                    Log.d(logTag, "Pobrana wartość row =  " + row);

                                     //zapisanie do bazy
                                    StringRequest stringRequest = new StringRequest(Request.Method.POST,
                                            AppConfig.STORE_RESERVATION,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    Log.d(logTag, "Reservation request: " + response);
                                                    try {
                                                        JSONObject json = new JSONObject(response);
                                                        boolean error = json.getBoolean("error");
                                                        if (error) {
                                                            Toast.makeText(
                                                                    getApplicationContext(),
                                                                    json.getString("message"),
                                                                    Toast.LENGTH_SHORT).show();
                                                        }
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                        Toast.makeText(
                                                                getApplicationContext(),
                                                                "Json error: " + e.getMessage(),
                                                                Toast.LENGTH_LONG).show();
                                                    }


                                                }
                                            }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.e(logTag, "Registration Error: " + error.getMessage());
                                            Toast.makeText(getApplicationContext(),
                                                    error.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }) {
                                        @Override
                                        protected Map<String, String> getParams() {
                                            Map<String, String> params = new HashMap<>();
                                            params.put("customerId", customerId);
                                            params.put("seatNumber", seatNumber);
                                            params.put("row", row);
                                            params.put("seatTypeId", seatTypeId);
                                            params.put("repertoireId", repertoireId);
                                            return params;
                                        }
                                    };

                                    AppController.getInstance().addToRequestQueue(stringRequest, "req_register");



                                }

                                //wysłanie wiadomości do Socketu
                                sendMessageToServer(httpClient, webSocket);

                                //przejście do kolejnego ekranu - Moje rezerwacje

                                Intent intent = new Intent(ChooseSeatTypeActivity.this, ReservationsActivity.class);
                                startActivity(intent);


                            }

                        }

                    }
                });



            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {

                Message message = new Message(text);

                //asyncTask i kolko do momentu ...
                for (int i = 0; i < choosedPlaces.length; i++) {
                    choosedPlaces [i] = message.getChoosedPlaces()[i];
                    Log.d(logTag, "choosedPlaces [ " + i + " ] = " + choosedPlaces [i]);
                }
                markChoosedPlaces();
                //... oznaczenia wszystkich zajetych miejsc

                //myChoosedPlaces without places from websocket
                for (int i = 0; i < choosedPlaces.length; i++) {
                    myChoosedPlaces [i] = ( myChoosedPlaces[i] ^ choosedPlaces[i] ) & myChoosedPlaces[i];
                    Log.d(logTag, "myChoosedPlaces [ " + i + " ] = " + myChoosedPlaces[i]);
                }



                ArrayList<Integer> takenYourSeats = new ArrayList<>();
                //moje
                //Sytuacja, gdy ktoś po wyborze miejsca, i zatwierdzeniu (popupu) przejdzie do ekranu z sektorami. Następie ktoś zarezerwuje wybrane przez niego miejsce.

                for (int i = 0; i < choosedPlaces.length; i++) {
                    if (selectedSeats.containsKey(i+1) && choosedPlaces[i]) {
                        Log.d(logTag, "Wybrane przez ciebie miejsce " + (i+1) + " zostało właśnie zajęte.");

                        selectedSeats.remove(i+1);
                        takenYourSeats.add(i+1);

                    }
                }

                //aktualizacja podsumowania
                updateSummary();

                //wyświetlenie komunikatu o zajętym miejscu (o ile to konieczne)
                buildDialog(takenYourSeats);

                //aktualizacja buttonów sektorów
                updateSectors(false);

            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                super.onMessage(webSocket, bytes);

            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                webSocket.close(1000, null);
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket, code, reason);
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, okhttp3.Response response) {
                Log.d(logTag, "onFailure: " + t.getMessage());
            }
        };

        httpClient.newWebSocket(request, webSocketListener);
        httpClient.dispatcher().executorService().shutdown();

    }

    private void sendMessageToServer (OkHttpClient httpClient, WebSocket webSocket){
        if (httpClient != null){
            Message message = new Message(myChoosedPlaces);
            webSocket.send(message.getChoosedPlacesString());
            webSocket.close(1000, "Zarezerwowowano miejsca");
        } else {
            ed.buildDialog(ChooseSeatTypeActivity.this, "Błąd serwera",
                    "Podczas wysyłania informacji o wybranych miejscach wykryto błąd").show();
            Log.d(logTag, "sendMessageToServer: client rowny null");
        }
    }


      //obsługa systemowego przycisku wstecz
    @Override
    public void onBackPressed() {



    }
}



