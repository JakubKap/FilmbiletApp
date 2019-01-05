package com.companysf.filmbilet.activity;

import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
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
import com.companysf.filmbilet.AsyncTasks.FreeSeatsTask;
import com.companysf.filmbilet.AsyncTasks.FreeSectorsTask;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.OkHttpClient;
import okhttp3.Request.Builder;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;


public class ChooseSeatTypeActivity extends AppCompatActivity {

    private View popupView;
    private boolean isPopupActive;
    private static final String logTag = MainActivity.class.getSimpleName();
    private SessionManager sManager;
    private SQLiteHandler db;
    private ErrorDetector ed;
    private Map<Button, Boolean> sectorButtons = new LinkedHashMap<>(); //zmiana na LinkedHashMap w celu pamiętania kolejności wstawianych elementów
    private Map<Integer, Integer> sectorAndSeat = new TreeMap<>();

    private PopupWindow popupWindow=null;

    ConstraintLayout constraintLayout;
    FrameLayout frameLayout; //używany do przyciemnienia backgroundu w popup
    Button button1, button2, button3, button4, button5, button6, button7, button8, btn_next;
    ProgressBar progressBar;

    TextView textView3, textView4;

    //seats
    private Button buttonIR_1, buttonIR_2, buttonIR_3, buttonIR_4, buttonIR_5,buttonIR_6, buttonIR_7,
            buttonIIR_1, buttonIIR_2, buttonIIR_3, buttonIIR_4, buttonIIR_5,buttonIIR_6, buttonIIR_7,
            buttonIIIR_1, buttonIIIR_2, buttonIIIR_3, buttonIIIR_4, buttonIIIR_5,buttonIIIR_6, buttonIIIR_7,
            buttonIVR_1, buttonIVR_2, buttonIVR_3, buttonIVR_4, buttonIVR_5,buttonIVR_6, buttonIVR_7,
            buttonVR_1, buttonVR_2, buttonVR_3, buttonVR_4, buttonVR_5,buttonVR_6, buttonVR_7,
            btnApprove, btnReserve, buttonClose, btn;

    private TextView textView3Seats;

    private Map<Button, Boolean> seatButtons = new HashMap<>();

    private int overallPrice=0;
    private int overallSeats=0;
    private Map<Integer, Integer> selectedSeats = new HashMap<>();
    private Map<Integer, Integer> seatAndRowMap = new HashMap<>();


    //zmienne służace do komunikacji z socketem

    private boolean choosedPlaces [] = new boolean[280]; //zajęte miejsce (wiadomo o nich z socketu i z BD)
    private boolean myChoosedPlaces[] = new boolean[280]; //zajęte miejsca przez użytkownika

    private final String websocketURL = "ws://35.204.119.131:8080/";

    private void markChoosedPlaces(final boolean placesToMark []) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                boolean displayDialog = false;
                ArrayList<Integer> takenCheckedSeats = new ArrayList<>();

                for (Map.Entry<Button, Boolean> entry : seatButtons.entrySet()) {
                    Button button = entry.getKey();
                    Boolean isChecked = entry.getValue();

                    int index =  Integer.parseInt(button.getText().toString()); //parsowanie nr miejsca do int
                    if(placesToMark[index-1]){

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
                                    textView3Seats.setText("Wybrane miejsca: " + selected);
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

                                for(Integer in : takenCheckedSeats) {

                                    if (i > 0)
                                        text += ", " + in;
                                    else text = "" + in;

                                    i++;
                                }

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
            Button key = entry.getKey();
            Boolean value = entry.getValue();

            if(value) selected++;

        }
        return selected;
    }

    public int selectedSector(){
        boolean flag1 = sectorButtons.get(button1);
        boolean flag2 = sectorButtons.get(button2);
        boolean flag3 = sectorButtons.get(button3);
        boolean flag4 = sectorButtons.get(button4);
        boolean flag5 = sectorButtons.get(button5);
        boolean flag6 = sectorButtons.get(button6);
        boolean flag7 = sectorButtons.get(button7);
        boolean flag8 = sectorButtons.get(button8);

        //wykrycie, który typ miejsca został wybrany
        int seatTypeId=1;

        if(flag1 || flag2)
            seatTypeId=1;
        else if(flag3 || flag4)
            seatTypeId=2;
        else if(flag5 || flag6)
            seatTypeId=3;
        else if (flag7 || flag8)
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

                if(numberOfSeats>0)
                textView4.setText("Liczba miejsc: " + numberOfSeats
                        + "\nCena: " + price + " zł");
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
                    if(takenYourSeats.size()==0)
                        return;
                    else if(takenYourSeats.size() == 1) {
                        ed.buildDialog(ChooseSeatTypeActivity.this, "Zajęcie miejsca",
                                "Inny użytkownik przed chwilą zajął wybrane przez ciebie miejsce o numerze " + takenYourSeats.get(0) + ".").show();
                    }
                        else{

                        String text="";
                        int i=0;
                        for(Integer in : takenYourSeats) {
                            if(i>0)
                                text += ", " + in;
                                else text = "" + in;

                            i++;
                        }

                        ed.buildDialog(ChooseSeatTypeActivity.this, "Zajęcie miejsc", "Inny użytkownik przed chwilą zajął wybrane przez ciebie miejsca o numerach: " + text + ".").show();

                    }
            }
        });

    }


    public void updateSectors(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

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

        isPopupActive=false;
        ed = new ErrorDetector(this);

        //sprawdzenie zalogowania

        sManager = new SessionManager(getApplicationContext());
        db = new SQLiteHandler(getApplicationContext());

        if (!sManager.isLoggedIn()) {
            logOutCustomer();
        }


        setContentView(R.layout.activity_choose_seat_type2);

        // btnNext = (Button) findViewById(R.id.btnNext);

        //adjusting size of  the sectorButtons

        constraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayout);

        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        button5 = (Button) findViewById(R.id.button5);
        button6 = (Button) findViewById(R.id.button6);
        button7 = (Button) findViewById(R.id.button7);
        button8 = (Button) findViewById(R.id.button8);

        btn_next = (Button) findViewById(R.id.btn_next);
        btnReserve = (Button) findViewById(R.id.btnReserve);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        textView3=(TextView) findViewById(R.id.textView3);
        textView4=(TextView) findViewById(R.id.textView4);

        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
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

        popupView = inflater.inflate(R.layout.activity_choose_seat_left, null);

        buttonIR_1 = (Button) popupView.findViewById(R.id.buttonIR_1);
        seatButtons.put(buttonIR_1, false);
        buttonIR_2 = (Button) popupView.findViewById(R.id.buttonIR_2);
        seatButtons.put(buttonIR_2, false);
        buttonIR_3 = (Button) popupView.findViewById(R.id.buttonIR_3);
        seatButtons.put(buttonIR_3, false);
        buttonIR_4 = (Button) popupView.findViewById(R.id.buttonIR_4);
        seatButtons.put(buttonIR_4, false);
        buttonIR_5 = (Button) popupView.findViewById(R.id.buttonIR_5);
        seatButtons.put(buttonIR_5, false);
        buttonIR_6 = (Button) popupView.findViewById(R.id.buttonIR_6);
        seatButtons.put(buttonIR_6, false);
        buttonIR_7 = (Button) popupView.findViewById(R.id.buttonIR_7);
        seatButtons.put(buttonIR_7, false);

        buttonIIR_1 = (Button) popupView.findViewById(R.id.buttonIIR_1);
        seatButtons.put(buttonIIR_1, false);
        buttonIIR_2 = (Button) popupView.findViewById(R.id.buttonIIR_2);
        seatButtons.put(buttonIIR_2, false);
        buttonIIR_3 = (Button) popupView.findViewById(R.id.buttonIIR_3);
        seatButtons.put(buttonIIR_3, false);
        buttonIIR_4 = (Button) popupView.findViewById(R.id.buttonIIR_4);
        seatButtons.put(buttonIIR_4, false);
        buttonIIR_5 = (Button) popupView.findViewById(R.id.buttonIIR_5);
        seatButtons.put(buttonIIR_5, false);
        buttonIIR_6 = (Button) popupView.findViewById(R.id.buttonIIR_6);
        seatButtons.put(buttonIIR_6, false);
        buttonIIR_7 = (Button) popupView.findViewById(R.id.buttonIIR_7);
        seatButtons.put(buttonIIR_7, false);

        buttonIIIR_1 = (Button) popupView.findViewById(R.id.buttonIIIR_1);
        seatButtons.put(buttonIIIR_1, false);
        buttonIIIR_2 = (Button) popupView.findViewById(R.id.buttonIIIR_2);
        seatButtons.put(buttonIIIR_2, false);
        buttonIIIR_3 = (Button) popupView.findViewById(R.id.buttonIIIR_3);
        seatButtons.put(buttonIIIR_3, false);
        buttonIIIR_4 = (Button) popupView.findViewById(R.id.buttonIIIR_4);
        seatButtons.put(buttonIIIR_4, false);
        buttonIIIR_5 = (Button) popupView.findViewById(R.id.buttonIIIR_5);
        seatButtons.put(buttonIIIR_5, false);
        buttonIIIR_6 = (Button) popupView.findViewById(R.id.buttonIIIR_6);
        seatButtons.put(buttonIIIR_6, false);
        buttonIIIR_7 = (Button) popupView.findViewById(R.id.buttonIIIR_7);
        seatButtons.put(buttonIIIR_7, false);

        buttonIVR_1 = (Button) popupView.findViewById(R.id.buttonIVR_1);
        seatButtons.put(buttonIVR_1, false);
        buttonIVR_2 = (Button) popupView.findViewById(R.id.buttonIVR_2);
        seatButtons.put(buttonIVR_2, false);
        buttonIVR_3 = (Button) popupView.findViewById(R.id.buttonIVR_3);
        seatButtons.put(buttonIVR_3, false);
        buttonIVR_4 = (Button) popupView.findViewById(R.id.buttonIVR_4);
        seatButtons.put(buttonIVR_4, false);
        buttonIVR_5 = (Button) popupView.findViewById(R.id.buttonIVR_5);
        seatButtons.put(buttonIVR_5, false);
        buttonIVR_6 = (Button) popupView.findViewById(R.id.buttonIVR_6);
        seatButtons.put(buttonIVR_6, false);
        buttonIVR_7 = (Button) popupView.findViewById(R.id.buttonIVR_7);
        seatButtons.put(buttonIVR_7, false);

        buttonVR_1 = (Button) popupView.findViewById(R.id.buttonVR_1);
        seatButtons.put(buttonVR_1, false);
        buttonVR_2 = (Button) popupView.findViewById(R.id.buttonVR_2);
        seatButtons.put(buttonVR_2, false);
        buttonVR_3 = (Button) popupView.findViewById(R.id.buttonVR_3);
        seatButtons.put(buttonVR_3, false);
        buttonVR_4 = (Button) popupView.findViewById(R.id.buttonVR_4);
        seatButtons.put(buttonVR_4, false);
        buttonVR_5 = (Button) popupView.findViewById(R.id.buttonVR_5);
        seatButtons.put(buttonVR_5, false);
        buttonVR_6 = (Button) popupView.findViewById(R.id.buttonVR_6);
        seatButtons.put(buttonVR_6, false);
        buttonVR_7 = (Button) popupView.findViewById(R.id.buttonVR_7);
        seatButtons.put(buttonVR_7, false);

        btnApprove = (Button) popupView.findViewById(R.id.btnApprove);

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
            new FreeSectorsTask(getApplicationContext(), constraintLayout, true).execute(1);

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

            button1.setText("S1 dost.: 10 zł");
            button2.setText("S1 dost.: 10 zł");


            button3.setText("S2 dost.: 15 zł");
            button4.setText("S2 dost.: 15 zł");

            button5.setText("S3 dost.: 20 zł");
            button6.setText("S3 dost.: 20 zł");

            button7.setText("S4 dost.: 30 zł");
            button8.setText("S4 dost.: 30 zł");


        }


        //obługa związana z socketem

        for(int i=0; i<choosedPlaces.length; i++){
            myChoosedPlaces[i]=false;
        }

        //pobranie informacji z bazy danych na temat pobranych miejsc (DLA DANEGO REPERTUARU)


        int repertoireId = 1;

        final String repertoireIdString = "" + repertoireId;

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

                                    String text = "Sprawdź rezerwację " + reservation.getId() + " " + reservation.getCustomerId()
                                            + " " + reservation.getSeatNumber() + " " + reservation.getRow() + " " + reservation.getDatePom() + " "
                                            + reservation.getSeatTypeId();


                                    choosedPlaces[reservation.getSeatNumber()-1]=true;
                                    int number = reservation.getSeatNumber()-1;
                                    String index = "" + number;

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
        okhttp3.Request request = new okhttp3.Request.Builder().url(websocketURL).build();

        WebSocketListener webSocketListener = new WebSocketListener() {
            @Override
            public void onOpen(final WebSocket webSocket, okhttp3.Response response) {


                //foreach po mapie zawierającej nr_miejsca oraz informację, czy został wciśnięty
                /*for (Map.Entry<Button, Boolean> entry : seatButtons.entrySet()) {
                    final Button button = entry.getKey();

                    if(button.isEnabled())  Log.d(logTag, "isEnabled");
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //uzupełnienie tablicy potrzebnej do komunikacji przez Socket
                            int index = Integer.valueOf(button.getText().toString())-1;
                            boolean value = !myChoosedPlaces[index];
                            myChoosedPlaces[index] = value;

                            Log.d(logTag, "myChoosedPlaces[ " + index + " ] = "  +  myChoosedPlaces[index]);
                        }
                    });

                }*/

                Log.d(logTag, "Connection estabilished");



                /*
               btnReserve.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {

                       //w mapie znajdują się wybrane przez użytkownika nr miejsc
                       for (Map.Entry<Integer, Integer> entry : selectedSeats.entrySet()) {
                           int seatNumber = entry.getKey();
                           //int seatType = entry.getValue();

                           //zapisanie do tablicy miejsc, które zostały wybrane
                           myChoosedPlaces[seatNumber-1]=true;

                           System.out.println("Zapisana wartość myChoosedPlaces[ " + (seatNumber-1)  + " ] = " +  myChoosedPlaces[seatNumber-1]);

                       }

                   }

               });*/

                View.OnClickListener buttonClicked = new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        String free = "#6bb9f0";

                        btn = (Button) findViewById(v.getId());

                        //btn.setEnabled(false);

                        //Animation animation = new AlphaAnimation(1.0f, 0.0f);
                        //animation.setDuration(200);

                        //btn.startAnimation(animation);

                        //jeśli button jest wciśnięty, to go odznacz

                        //wyświetlenie komunikatu w sytuacji, gdy ktoś kliknie na sektor bez miejsc
                        if(btn.getText().equals("0/35")){

                            ed.buildDialog(ChooseSeatTypeActivity.this, "Brak miejsc w sektorze",
                                    "Niestety, wszystkie miejsca w tym sektorze są zajęte. Spróbuj znaleźć miejsce w innym sektorze.").show();

                            return;
                        }

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

                            popupView = inflater.inflate(R.layout.activity_choose_seat_left, null);

                            //po kliknięciu dowolnego "aktywnego" sektora od razu pokazuje się popup oraz "chowają" elementy pod nim

                            // constraintLayout.setVisibility(View.INVISIBLE); //schowanie dolnej warstwy


                            int seatTypeId = selectedSector();


                            // LinearLayout linearLayoutSeats=(LinearLayout) findViewById(R.id.linearLayoutSeats);


                            boolean flag1 = sectorButtons.get(button1);
                            boolean flag2 = sectorButtons.get(button2);
                            boolean flag3 = sectorButtons.get(button3);
                            boolean flag4 = sectorButtons.get(button4);
                            boolean flag5 = sectorButtons.get(button5);
                            boolean flag6 = sectorButtons.get(button6);
                            boolean flag7 = sectorButtons.get(button7);
                            boolean flag8 = sectorButtons.get(button8);

                            int startSeat = 1;

                            if (flag1) startSeat = 1;
                            else if (flag2) startSeat = 8;
                            else if (flag3) startSeat = 71;
                            else if (flag4) startSeat = 78;
                            else if (flag5) startSeat = 141;
                            else if (flag6) startSeat = 148;
                            else if (flag7) startSeat = 211;
                            else startSeat = 218;


                            new FreeSeatsTask(getApplicationContext(), popupView, selectedSeats,choosedPlaces, startSeat, seatTypeId).execute(1);


                            // create the popup window
                            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                            boolean focusable = true; // lets taps outside the popup also dismiss it
                            popupWindow = new PopupWindow(popupView, width, height, focusable);
                            //globalPopUpWindow = popupWindow;

                            isPopupActive = true;

                            // show the popup window
                            // which view you pass in doesn't matter, it is only used for the window tolken


                            //prawidłowe położenia popup
                            popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 40);


                            //przyciemnienie backgroundu pod popupem
                            frameLayout.getForeground().setAlpha(165);


                            popupWindow.setBackgroundDrawable(new BitmapDrawable());
                            popupWindow.setFocusable(false);

                            buttonIR_1 = (Button) popupView.findViewById(R.id.buttonIR_1);
                            seatButtons.put(buttonIR_1, false);
                            buttonIR_2 = (Button) popupView.findViewById(R.id.buttonIR_2);
                            seatButtons.put(buttonIR_2, false);
                            buttonIR_3 = (Button) popupView.findViewById(R.id.buttonIR_3);
                            seatButtons.put(buttonIR_3, false);
                            buttonIR_4 = (Button) popupView.findViewById(R.id.buttonIR_4);
                            seatButtons.put(buttonIR_4, false);
                            buttonIR_5 = (Button) popupView.findViewById(R.id.buttonIR_5);
                            seatButtons.put(buttonIR_5, false);
                            buttonIR_6 = (Button) popupView.findViewById(R.id.buttonIR_6);
                            seatButtons.put(buttonIR_6, false);
                            buttonIR_7 = (Button) popupView.findViewById(R.id.buttonIR_7);
                            seatButtons.put(buttonIR_7, false);

                            buttonIIR_1 = (Button) popupView.findViewById(R.id.buttonIIR_1);
                            seatButtons.put(buttonIIR_1, false);
                            buttonIIR_2 = (Button) popupView.findViewById(R.id.buttonIIR_2);
                            seatButtons.put(buttonIIR_2, false);
                            buttonIIR_3 = (Button) popupView.findViewById(R.id.buttonIIR_3);
                            seatButtons.put(buttonIIR_3, false);
                            buttonIIR_4 = (Button) popupView.findViewById(R.id.buttonIIR_4);
                            seatButtons.put(buttonIIR_4, false);
                            buttonIIR_5 = (Button) popupView.findViewById(R.id.buttonIIR_5);
                            seatButtons.put(buttonIIR_5, false);
                            buttonIIR_6 = (Button) popupView.findViewById(R.id.buttonIIR_6);
                            seatButtons.put(buttonIIR_6, false);
                            buttonIIR_7 = (Button) popupView.findViewById(R.id.buttonIIR_7);
                            seatButtons.put(buttonIIR_7, false);

                            buttonIIIR_1 = (Button) popupView.findViewById(R.id.buttonIIIR_1);
                            seatButtons.put(buttonIIIR_1, false);
                            buttonIIIR_2 = (Button) popupView.findViewById(R.id.buttonIIIR_2);
                            seatButtons.put(buttonIIIR_2, false);
                            buttonIIIR_3 = (Button) popupView.findViewById(R.id.buttonIIIR_3);
                            seatButtons.put(buttonIIIR_3, false);
                            buttonIIIR_4 = (Button) popupView.findViewById(R.id.buttonIIIR_4);
                            seatButtons.put(buttonIIIR_4, false);
                            buttonIIIR_5 = (Button) popupView.findViewById(R.id.buttonIIIR_5);
                            seatButtons.put(buttonIIIR_5, false);
                            buttonIIIR_6 = (Button) popupView.findViewById(R.id.buttonIIIR_6);
                            seatButtons.put(buttonIIIR_6, false);
                            buttonIIIR_7 = (Button) popupView.findViewById(R.id.buttonIIIR_7);
                            seatButtons.put(buttonIIIR_7, false);

                            buttonIVR_1 = (Button) popupView.findViewById(R.id.buttonIVR_1);
                            seatButtons.put(buttonIVR_1, false);
                            buttonIVR_2 = (Button) popupView.findViewById(R.id.buttonIVR_2);
                            seatButtons.put(buttonIVR_2, false);
                            buttonIVR_3 = (Button) popupView.findViewById(R.id.buttonIVR_3);
                            seatButtons.put(buttonIVR_3, false);
                            buttonIVR_4 = (Button) popupView.findViewById(R.id.buttonIVR_4);
                            seatButtons.put(buttonIVR_4, false);
                            buttonIVR_5 = (Button) popupView.findViewById(R.id.buttonIVR_5);
                            seatButtons.put(buttonIVR_5, false);
                            buttonIVR_6 = (Button) popupView.findViewById(R.id.buttonIVR_6);
                            seatButtons.put(buttonIVR_6, false);
                            buttonIVR_7 = (Button) popupView.findViewById(R.id.buttonIVR_7);
                            seatButtons.put(buttonIVR_7, false);

                            buttonVR_1 = (Button) popupView.findViewById(R.id.buttonVR_1);
                            seatButtons.put(buttonVR_1, false);
                            buttonVR_2 = (Button) popupView.findViewById(R.id.buttonVR_2);
                            seatButtons.put(buttonVR_2, false);
                            buttonVR_3 = (Button) popupView.findViewById(R.id.buttonVR_3);
                            seatButtons.put(buttonVR_3, false);
                            buttonVR_4 = (Button) popupView.findViewById(R.id.buttonVR_4);
                            seatButtons.put(buttonVR_4, false);
                            buttonVR_5 = (Button) popupView.findViewById(R.id.buttonVR_5);
                            seatButtons.put(buttonVR_5, false);
                            buttonVR_6 = (Button) popupView.findViewById(R.id.buttonVR_6);
                            seatButtons.put(buttonVR_6, false);
                            buttonVR_7 = (Button) popupView.findViewById(R.id.buttonVR_7);
                            seatButtons.put(buttonVR_7, false);


                            //dodanie słuchacza do przycisków oraz textView popupview


                            btnApprove = (Button) popupView.findViewById(R.id.btnApprove);

                            textView3Seats = (TextView) popupView.findViewById(R.id.textView3Seats);

                            buttonClose = (Button) popupView.findViewById(R.id.buttonClose);


                            textView3Seats.setVisibility(View.VISIBLE);
                            textView3Seats.setEnabled(true);
                            btnApprove.setVisibility(View.VISIBLE);
                            btnApprove.setEnabled(true);
                            //po ponownym otwarciu popupu zaliczenie ponownie wybranego buttona jako wciśnięty
                            for (Map.Entry<Button, Boolean> entry : seatButtons.entrySet()) {
                                Button button = entry.getKey();


                                int number = Integer.parseInt(button.getText().toString()); //parsowanie nr miejsca do int
                                if (selectedSeats.containsKey(number)) {
                                    seatButtons.put(button, true);
                                    int selected = selectedSeats();

                                    textView3Seats.setText("Wybrane miejsca: " + selected);
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

                                        isPopupActive = false;

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

                                    isPopupActive = false;

                                }
                            });


                            View.OnClickListener seatBtnClick = new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Button btn = (Button) popupView.findViewById(v.getId());


                                    Animation animation = new AlphaAnimation(1.0f, 0.0f);
                                    animation.setDuration(200);

                                    btn.startAnimation(animation);


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

                                    //if(selected > 0) {
                                        btnApprove.setVisibility(View.VISIBLE);
                                        textView3Seats.setVisibility(View.VISIBLE);
                                        textView3Seats.setText("Wybrane miejsca: " + selected);
                                    //}

                                   /* else{
                                        btnApprove.setVisibility(View.INVISIBLE);
                                        //textView3Seats.setVisibility(View.INVISIBLE);
                                    }*/

                                }
                            };

                            for (Map.Entry<Button, Boolean> entry : seatButtons.entrySet()) {
                                Button key = entry.getKey();
                                Boolean value = entry.getValue();
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
                                    textView3.setText("Podsumowanie:");





                        /*
                        for (Map.Entry<Button, Boolean> entry : sectorButtons.entrySet()) {
                            Button key = entry.getKey();
                            Boolean value = entry.getValue();
                            key.setEnabled(false);
                        }*/

                                    //po wyborze miejsc wyświetlamy podsumowanie oraz nadajemy kolor przyciskowi akceptacji
                                    btnReserve.setBackgroundResource(R.drawable.rounded_bordered_button_light);
                                    btnReserve.setEnabled(true);
                                    btnReserve.setTextColor(Color.WHITE);
                                    textView4.setVisibility(View.VISIBLE);

                                    //obliczenie ceny


                                    int numberOfSeats = 0;
                                    int price = 0;
                                    // int seatTypeId = selectedSector();


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

                                        textView4.setText("Liczba miejsc: " + numberOfSeats
                                                + "\nCena: " + price + " zł");


                                        btnReserve.setVisibility(View.VISIBLE);

                                        sectorButtons.put(btn, false);
                                        seatButtons.clear();

                                    }
                                    else{
                                        textView3.setVisibility(View.INVISIBLE);
                                        btnReserve.setVisibility(View.INVISIBLE);
                                        textView4.setVisibility(View.INVISIBLE);

                                    }
                                    //ustalenie które rzędy


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
                                return;

                            }
                            else{
                                //zapis do bazy danych

                                int currentCustomerId = 1; //TODO należy to pobrać z informacji o logowaniu

                                int currentRepertoireId = 1; //TODO pobrać z poprzedniego Intetentu

                                //selectedSeats <NrMiejsca, TypMiejsca> - wybrane przez usera
                                //seatAndRowMap <NrMiejsca, Rząd>


                                for (Map.Entry<Integer, Integer> entry : selectedSeats.entrySet()) {

                                    final String customerId = currentCustomerId + "";
                                    final String seatNumber = entry.getKey() + "";
                                    final String seatTypeId = entry.getValue() + "";
                                    final String row = seatAndRowMap.get(entry.getKey()) + "";
                                    final String repertoireId = currentRepertoireId + "";

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
                                                        } else {
                                                                JSONObject reservationJSON = json.getJSONObject("reservation");
                                                                Reservation reservation = new Reservation(
                                                                        reservationJSON.getInt("id"),
                                                                        reservationJSON.getInt("customerId"),
                                                                        reservationJSON.getInt("seatNumber"),
                                                                        reservationJSON.getInt("row"),
                                                                        reservationJSON.getString("date"),
                                                                        reservationJSON.getInt("seatTypeId"),
                                                                        reservationJSON.getInt("repertoireId")
                                                                );

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
                markChoosedPlaces(choosedPlaces);
                //... oznaczenia wszystkich zajetych miejsc

                //myChoosedPlaces without places from websocket
                for (int i = 0; i < choosedPlaces.length; i++) {
                    myChoosedPlaces [i] = ( myChoosedPlaces[i] ^ choosedPlaces[i] ) & myChoosedPlaces[i];
                    Log.d(logTag, "myChoosedPlaces [ " + i + " ] = " + myChoosedPlaces[i]);
                }
                //TODO sprawdzic czy ktores miejsce nie zostalo juz zajete i jesli tak to dac komunikat
                for (int i = 0; i < choosedPlaces.length; i++) {
                    if (myChoosedPlaces[i] & choosedPlaces[i]) {/*komunikat*/}
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
                updateSectors();

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

/*
        final OkHttpClient httpClient = new OkHttpClient();
        okhttp3.Request request = new okhttp3.Request.Builder().url(websocketURL).build();

        WebSocketListener webSocketListener = new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, okhttp3.Response response) {

              // if(!seatButtons.isEmpty()) {

                    //rozpatrywane tylko wtedy, gdy jest wyświetlony popup, czyli do seatButtons są dodane Buttony

                    for (Map.Entry<Button, Boolean> entry : seatButtons.entrySet()) {
                        final Button button = entry.getKey();

                        if(button.isEnabled())  Log.d(logTag, "isEnabled");
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                int index = Integer.valueOf(button.getText().toString())-1;
                                boolean value = !myChoosedPlaces[index];
                                myChoosedPlaces[index] = value;

                                Log.d(logTag, "myChoosedPlaces[ " + index + " ] = "  +  myChoosedPlaces[index]);
                            }
                        });

                    }

                //}

                Log.d(logTag, "Udało się");

            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                super.onMessage(webSocket, text);
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                super.onMessage(webSocket, bytes);
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                super.onClosing(webSocket, code, reason);
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket, code, reason);
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, okhttp3.Response response) {
                super.onFailure(webSocket, t, response);
            }
        };

        httpClient.newWebSocket(request, webSocketListener);
        httpClient.dispatcher().executorService().shutdown();*/
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
        //super.onBackPressed();
/*
            Log.d(logTag, "onBackPressed1");

            popupWindow.dismiss();

            //rozjaśnienie backgroundu pod popupem
            frameLayout.getForeground().setAlpha(0);

            //usunięcie historii wybranych miejsc po kliknięciu poza popup
            seatButtons.clear();

            sectorButtons.put(btn,false);
            constraintLayout.setVisibility(View.VISIBLE); //przywrócenie dolnej warstwy

            isPopupActive=false;*/

        if(popupWindow != null && popupWindow.isShowing()){
            //btn
        }
        else
            super.onBackPressed();

    }
}



