package com.companysf.filmbilet.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
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
import org.w3c.dom.Text;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import java.util.LinkedHashMap;

import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.OkHttpClient;

import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;


public class SectorActivity extends AppCompatActivity {

    private View popupView;
    private static final String logTag = MainActivity.class.getSimpleName();
    private SessionManager sManager;
    private SQLiteHandler db;
    private ErrorDetector ed;
    private int repertoireId;

    private Map<Button, Boolean> sectorButtons = new LinkedHashMap<>(); //zmiana na LinkedHashMap w celu pamiętania kolejności wstawianych elementów
    private Map<Integer, Integer> sectorAndSeat = new TreeMap<>();

    private PopupWindow popupWindow = null;

    private ConstraintLayout constraintLayout;
    private FrameLayout secFrameLayout; //używany do przyciemnienia backgroundu w popup
    private Button secButton1, secButton2, secButton3, secButton4, secButton5, secButton6, secButton7, secButton8;
    private ProgressBar SecProgressBar;

    private TextView secChoosedPlaces, secSummaryPrice;

    //tablica służaca do przechowywania obiektów TextView dla każdego z sektorów
    private TextView[] freeSeats = new TextView[8];

    //popup
    private ConstraintLayout seatsLinearLayout;
    private TextView title, subtitle;
    private GridLayout gridLayoutSeats;


    //seats columns and rows
    private Button seatsButton1C, seatsButton2C, seatsButton3C, seatsButton4C, seatsButton5C, seatsButton6C, seatsButton7C,
            seatsButtonIR, seatsButtonIIR, seatsButtonIIIR, seatsButtonIVR, seatsButtonVR, seatsApproveButton, secBtnReserve, seatsCloseButton, btn;

    private ArrayList<Button> columnButtons = new ArrayList<>();

    //seats
    private Button seatsButtonIR_1, seatsButtonIR_2, seatsButtonIR_3, seatsButtonIR_4, seatsButtonIR_5, seatsButtonIR_6, seatsButtonIR_7,
            seatsButtonIIR_1, seatsButtonIIR_2, seatsButtonIIR_3, seatsButtonIIR_4, seatsButtonIIR_5, seatsButtonIIR_6, seatsButtonIIR_7,
            seatsButtonIIIR_1, seatsButtonIIIR_2, seatsButtonIIIR_3, seatsButtonIIIR_4, seatsButtonIIIR_5, seatsButtonIIIR_6, seatsButtonIIIR_7,
            seatsButtonIVR_1, seatsButtonIVR_2, seatsButtonIVR_3, seatsButtonIVR_4, seatsButtonIVR_5, seatsButtonIVR_6, seatsButtonIVR_7,
            seatsButtonVR_1, seatsButtonVR_2, seatsButtonVR_3, seatsButtonVR_4, seatsButtonVR_5, seatsButtonVR_6, seatsButtonVR_7;

    private ProgressBar seatsProgressBar;
    private Map<Button, Boolean> seatButtons = new LinkedHashMap<>();
    private Map<Integer, Integer> selectedSeats = new HashMap<>();
    private Map<Integer, Integer> seatAndRowMap = new HashMap<>();

    Typeface opensansRegular;
    Typeface opensansBold;

    //zmienne służace do komunikacji z socketem

    private boolean choosedPlaces[] = new boolean[280]; //zajęte miejsce (wiadomo o nich z socketu i z BD)
    private boolean myChoosedPlaces[] = new boolean[280]; //zajęte miejsca przez użytkownika


    public void preparePopUp() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {


                Map<Button, Integer> seatNumber = new HashMap<>();  //mapa zawierająca button oraz odpowiadający mu nr siedzenia
                ArrayList<Button> buttons = new ArrayList<>(seatButtons.keySet());

                Log.d(logTag, "seatButtons.size() w preparePopUp == " + seatButtons.size());

                int seatTypeId = selectedSector();

                boolean flags[] = new boolean[8];
                int inc = 0;
                for (Map.Entry<Button, Boolean> entry : sectorButtons.entrySet()) {
                    flags[inc] = entry.getValue();
                    inc++;
                }

                int startSeat = 1;

                if (flags[1]) startSeat = 8;
                else if (flags[2]) startSeat = 71;
                else if (flags[3]) startSeat = 78;
                else if (flags[4]) startSeat = 141;
                else if (flags[5]) startSeat = 148;
                else if (flags[6]) startSeat = 211;
                else if (flags[7]) startSeat = 218;

                int seatNr = startSeat;

                for (int i = 1; i <= 35; i++) {

                    if (i == 8 || i == 15 || i == 22 || i == 29) {
                        seatNr += 7;
                        seatNumber.put(buttons.get(i - 1), seatNr);
                        Log.d(logTag, "Dodana wartość do siatki: " + seatNr + " dla i = " + i);
                        seatNr++;
                    } else {
                        seatNumber.put(buttons.get(i - 1), seatNr);
                        Log.d(logTag, "Dodana wartość do siatki: " + seatNr + " dla i = " + i);
                        seatNr++;
                    }


                }

                for (Button b : buttons)
                    Log.d(logTag, "Zawartość b : " + b.getText());

                //Log.d(logTag, "seatNumber.get(0) =  " + seatNumber.get(0));

                String text;
                int logNr = 0;
                for (int i = 0; i < 35; i++) {
                    text = Integer.toString(seatNumber.get(buttons.get(i)));
                    Log.d(logTag, "Pobrana wartość : " + seatNumber.get(buttons.get(i)));

                    buttons.get(i).setTextSize(TypedValue.COMPLEX_UNIT_SP, 7);

                    buttons.get(i).setText(text);
                    Log.d(logTag, "Zmieniona wartość textu buttona: " + seatNumber.get(buttons.get(logNr)));
                    logNr++;
                }

                //ustawienie prawidłowej nazwy sektora

                switch (startSeat) {
                    case 1:
                        title.setText(getString(R.string.upperSector1Text));
                        break;
                    case 8:
                        title.setText(getString(R.string.upperSector2Text));
                        break;
                    case 71:
                        title.setText(getString(R.string.upperSector3Text));
                        break;
                    case 78:
                        title.setText(getString(R.string.upperSector4Text));
                        break;
                    case 141:
                        title.setText(getString(R.string.upperSector5Text));
                        break;
                    case 148:
                        title.setText(getString(R.string.upperSector6Text));
                        break;
                    case 211:
                        title.setText(getString(R.string.upperSector7Text));
                        break;
                    case 218:
                        title.setText(getString(R.string.upperSector8Text));
                        break;
                }

                seatsLinearLayout.setVisibility(View.INVISIBLE);
                title.setVisibility(View.INVISIBLE);
                subtitle.setVisibility(View.INVISIBLE);
                //textView3Seats.setVisibility(View.INVISIBLE);

                gridLayoutSeats.setVisibility(View.INVISIBLE);

                Log.d(logTag, "OnPreExecute przed zmianą buttonów");
                for (Button b : buttons) {
                    b.setBackgroundResource(R.drawable.seat);
                    b.setVisibility(View.INVISIBLE);
                }

                Log.d(logTag, "OnPreExecute po zminie buttonów");
                seatsApproveButton.setVisibility(View.INVISIBLE);

                //w celach testowych
                for (Map.Entry<Integer, Integer> entry : selectedSeats.entrySet()) {
                    int number = entry.getKey();
                    int seatType = entry.getValue();
                    Log.d(logTag, "Przekazana wartość (nr, seatTypeId) do popup = (" + number + ", " + seatType + ")");

                }

                //zmiana numerów kolumn
                int number = 1;
                if (seatsButtonIR_1.getText().equals("1") || seatsButtonIR_1.getText().equals("71") || seatsButtonIR_1.getText().equals("141") || seatsButtonIR_1.getText().equals("211")) {
                    for (Button cb : columnButtons) {
                        String txt = Integer.toString(number);
                        cb.setText(txt);
                        Log.d(logTag, "Wartość cb.setText = " + cb.getText());
                        number++;
                    }
                } else {
                    number = 8;
                    for (Button cb : columnButtons) {
                        String txt = Integer.toString(number);
                        cb.setText(txt);
                        Log.d(logTag, "Wartość cb.setText = " + cb.getText());
                        number++;
                    }
                }
                columnButtons.clear();

                //zmiana numerów rzędów


                if (seatsButtonIR_1.getText().equals("1") || seatsButtonIR_1.getText().equals("8")) {
                    seatsButtonIR.setText(getString(R.string.row1));
                    seatsButtonIIR.setText(getString(R.string.row2));
                    seatsButtonIIIR.setText(getString(R.string.row3));
                    seatsButtonIVR.setText(getString(R.string.row4));
                    seatsButtonVR.setText(getString(R.string.row5));
                } else if (seatsButtonIR_1.getText().equals("71") || seatsButtonIR_1.getText().equals("78")) {
                    seatsButtonIR.setText(getString(R.string.row6));
                    seatsButtonIIR.setText(getString(R.string.row7));
                    seatsButtonIIIR.setText(getString(R.string.row8));
                    seatsButtonIVR.setText(getString(R.string.row9));
                    seatsButtonVR.setText(getString(R.string.row10));
                } else if (seatsButtonIR_1.getText().equals("141") || seatsButtonIR_1.getText().equals("148")) {
                    seatsButtonIR.setText(getString(R.string.row11));
                    seatsButtonIIR.setText(getString(R.string.row12));
                    seatsButtonIIIR.setText(getString(R.string.row13));
                    seatsButtonIVR.setText(getString(R.string.row14));
                    seatsButtonVR.setText(getString(R.string.row15));
                } else if (seatsButtonIR_1.getText().equals("211") || seatsButtonIR_1.getText().equals("218")) {
                    seatsButtonIR.setText(getString(R.string.row16));
                    seatsButtonIIR.setText(getString(R.string.row17));
                    seatsButtonIIIR.setText(getString(R.string.row18));
                    seatsButtonIVR.setText(getString(R.string.row19));
                    seatsButtonVR.setText(getString(R.string.row20));
                }

                markChoosedPlaces();

                // pętla służąca do pokazania użytkownikowi miejsc,które wcześniej wybrał (miejsca są
                // zaznaczane na nowo w momencie ponownego kliknięcia w sektor)
                for (Button b : buttons) {
                    int SeatNumber = Integer.parseInt(b.getText().toString()); //parsowanie nr miejsca do int
                    if (selectedSeats.containsKey(SeatNumber)) {
                        b.setBackgroundResource(R.drawable.seat_choosed);
                        Log.d(logTag, "Znaleziona ponowna wartość seatNumber: " + number);

                        seatsApproveButton.setVisibility(View.INVISIBLE);
                    }
                }

                for (Button b : buttons)
                    b.setVisibility(View.VISIBLE);

                seatsLinearLayout.setVisibility(View.VISIBLE);
                title.setVisibility(View.VISIBLE);
                subtitle.setVisibility(View.VISIBLE);
                gridLayoutSeats.setVisibility(View.VISIBLE);
                seatsApproveButton.setVisibility(View.INVISIBLE);

                switch (seatTypeId) {
                    case 1:
                        subtitle.setText(getString(R.string.sector1Price));
                        break;

                    case 2:
                        subtitle.setText(getString(R.string.sector2Price));
                        break;

                    case 3:
                        subtitle.setText(getString(R.string.sector3Price));
                        break;

                    case 4:
                        subtitle.setText(getString(R.string.sector4Price));
                        break;
                }


                seatsProgressBar.setVisibility(View.INVISIBLE);

                seatsCloseButton.setVisibility(View.VISIBLE);
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

                    int index = Integer.parseInt(button.getText().toString()); //parsowanie nr miejsca do int
                    if (choosedPlaces[index - 1]) {

                        button.setEnabled(false);
                        button.setBackgroundResource(R.drawable.seat_reserved);
                        button.setTextColor(Color.WHITE);

                        if (isChecked) {
                            //sytuacja, gdy ktoś zajmie miejsce/miejsca w sektorze, które przed chwilą wybraliśmy
                            seatButtons.put(button, false);
                            displayDialog = true;
                            takenCheckedSeats.add(Integer.parseInt(button.getText().toString())); //parsowanie nr miejsca do int i zapisanie do kolekcji;

                            int selected = selectedSeats();

                            if (selected > 0) {
                                seatsApproveButton.setVisibility(View.VISIBLE);
                            } else {
                                seatsApproveButton.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                }

                if (displayDialog) {
                    //wyświetlenie komunikatu

                    if (takenCheckedSeats.size() == 1)
                        ed.buildDialog(SectorActivity.this, getString(R.string.choosedPlaceTitle),
                                getString(R.string.choosedPlaceMsg)+
                                        takenCheckedSeats.get(0) + getString(R.string.dot)).show();
                    else {
                        //jeśli więcej miejsc
                        //zbudowanie Stringa

                        int i = 0;
                        String text = "";

                        //posortowanie kolekcji
                        Collections.sort(takenCheckedSeats);

                        StringBuilder sB = new StringBuilder(text);

                        for (Integer in : takenCheckedSeats) {

                            if (i > 0)
                                sB.append(", ");
                            sB.append(in);

                            i++;
                        }

                        text = sB.toString();
                        ed.buildDialog(SectorActivity.this, getString(R.string.choosedPlacesTitle),
                                getString(R.string.choosedPlacesMsg) +
                                        text + getString(R.string.dot)).show();
                    }
                }
            }
        });
    }

    private void logOutCustomer() {
        sManager.setLogin(false);
        db.deleteCustomers();
    }

    public int selectedSeats() {

        int selected = 0;
        for (Map.Entry<Button, Boolean> entry : seatButtons.entrySet()) {
            Boolean value = entry.getValue();
            if (value)
                selected++;
        }
        return selected;
    }

    public int selectedSector() {
        boolean flags[] = new boolean[8];
        int inc = 0;
        for (Map.Entry<Button, Boolean> entry : sectorButtons.entrySet()) {
            flags[inc] = entry.getValue();
            inc++;
        }

        //wykrycie, który typ miejsca został wybrany
        int seatTypeId = 1;

        if (flags[2] || flags[3])
            seatTypeId = 2;
        else if (flags[4] || flags[5])
            seatTypeId = 3;
        else if (flags[6] || flags[7])
            seatTypeId = 4;

        return seatTypeId;

    }

    public void saveSeats(int seatType) {

        for (Map.Entry<Button, Boolean> entry : seatButtons.entrySet()) {
            Button key = entry.getKey();
            Boolean value = entry.getValue();

            if (value) {
                int number = Integer.parseInt(key.getText().toString()); //parsowanie nr miejsca do int
                selectedSeats.put(number, seatType);

                Log.d(logTag, "Zapisane miejsce (nr, seatTypeId) = (" + number + ", " + seatType + ")");
            }
        }
    }


    public void updateSummary() {


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
                secChoosedPlaces.setText(String.format(new Locale("pl", "PL"), "%d", numberOfSeats));
                String text = Integer.toString(price) + getString(R.string.currency);
                secSummaryPrice.setText(text);
            }
        });

    }

    public void buildDialog(ArrayList<Integer> list) {
        final ArrayList<Integer> takenYourSeats = new ArrayList<>(list);

        for (Integer i : takenYourSeats)
            Log.d(logTag, "Zawartość takenYourSeats = " + i);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (takenYourSeats.size() == 1) {
                    ed.buildDialog(SectorActivity.this, getString(R.string.choosedPlaceTitle),
                            getString(R.string.choosedPlaceMsg) + takenYourSeats.get(0) + getString(R.string.dot)).show();
                } else if (takenYourSeats.size() > 1) {

                    String text = "";

                    StringBuilder sB = new StringBuilder(text);
                    int i = 0;
                    for (Integer in : takenYourSeats) {
                        if (i > 0)
                            sB.append(", ");

                        sB.append(in);

                        i++;
                    }
                    text = sB.toString();

                    ed.buildDialog(SectorActivity.this, getString(R.string.choosedPlacesTitle),

                             getString(R.string.choosedPlacesMsg) +  text + getString(R.string.dot)).show();
                }
            }
        });

    }


    public void updateSectors(boolean ifStart) {

        final boolean start = ifStart;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (start) {
                    SecProgressBar.setVisibility(View.INVISIBLE);

                    secChoosedPlaces.setVisibility(View.VISIBLE);

                    secSummaryPrice.setVisibility(View.VISIBLE);

                    String pom = getString(R.string.summaryStartText);
                    secSummaryPrice.setText(pom);
                }

                int sectorNumber = 1;

                Log.d(logTag, "Zawartość choosedPlaces w metodzie updateSectors: ");
                for (int i = 0; i < choosedPlaces.length; i++)
                    Log.d(logTag, "choosedPlaces[ " + i + " ] = " + choosedPlaces[i]);

                for (Map.Entry<Button, Boolean> entry1 : sectorButtons.entrySet()) {

                    int takenSeats = 0;
                    for (Map.Entry<Integer, Integer> entry2 : sectorAndSeat.entrySet()) {
                        int seatNumber = entry2.getKey();
                        int sector = entry2.getValue();


                        if (sector == sectorNumber) {
                            if (choosedPlaces[seatNumber - 1]) {
                                takenSeats++;
                                Log.d(logTag, "choosedPlaces[(seatNumber-1) = " + (seatNumber - 1) + " ] = true oraz takenSeats = " + takenSeats);
                            }

                        }
                    }

                    int free = 35 - takenSeats;
                    Log.d(logTag, "free = " + free);

                    freeSeats[sectorNumber - 1].setText(
                            String.format(new Locale("pl", "PL"), "%d", free)
                    );
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


        setContentView(R.layout.sector);


        //adjusting size of  the sectorButtons

        constraintLayout = findViewById(R.id.constraintLayout);

        secButton1 = findViewById(R.id.secButton1);
        secButton2 = findViewById(R.id.secButton2);
        secButton3 = findViewById(R.id.secButton3);
        secButton4 = findViewById(R.id.secButton4);
        secButton5 = findViewById(R.id.secButton5);
        secButton6 = findViewById(R.id.secButton6);
        secButton7 = findViewById(R.id.secButton7);
        secButton8 = findViewById(R.id.secButton8);

        secBtnReserve = findViewById(R.id.secBtnReserve);

        SecProgressBar = findViewById(R.id.secProgressBar);


        secFrameLayout = findViewById(R.id.secFrameLayout);
        secFrameLayout.getForeground().setAlpha(0);

        secChoosedPlaces = findViewById(R.id.secChoosedPlaces);
        secSummaryPrice = findViewById(R.id.secSummaryPrice);


        freeSeats[0] = findViewById(R.id.freeSeats1);
        freeSeats[1] = findViewById(R.id.freeSeats2);
        freeSeats[2] = findViewById(R.id.freeSeats3);
        freeSeats[3] = findViewById(R.id.freeSeats4);
        freeSeats[4] = findViewById(R.id.freeSeats5);
        freeSeats[5] = findViewById(R.id.freeSeats6);
        freeSeats[6] = findViewById(R.id.freeSeats7);
        freeSeats[7] = findViewById(R.id.freeSeats8);


        sectorButtons.put(secButton1, false);
        sectorButtons.put(secButton2, false);
        sectorButtons.put(secButton3, false);
        sectorButtons.put(secButton4, false);
        sectorButtons.put(secButton5, false);
        sectorButtons.put(secButton6, false);
        sectorButtons.put(secButton7, false);
        sectorButtons.put(secButton8, false);


        //fonts
         opensansRegular = Typeface.createFromAsset(getAssets(), getString(R.string.opensSansRegular));
         opensansBold = Typeface.createFromAsset(getAssets(), getString(R.string.opensSansBold));

        for(int i =0; i<freeSeats.length; i++)
            freeSeats[i].setTypeface(opensansRegular);

        secBtnReserve.setTypeface(opensansBold);

        secChoosedPlaces.setTypeface(opensansBold);

        secSummaryPrice.setTypeface(opensansBold);

        TextView stepNumber2 = findViewById(R.id.stepNumber2);
        stepNumber2.setTypeface(opensansBold);

        TextView selectSectorText = findViewById(R.id.selectSectorText);
        selectSectorText.setTypeface(opensansBold);

        TextView[] secLabels = new TextView[8];
        secLabels[0] = findViewById(R.id.sec1Label);
        secLabels[1] = findViewById(R.id.sec2Label);
        secLabels[2] = findViewById(R.id.sec3Label);
        secLabels[3] = findViewById(R.id.sec4Label);
        secLabels[4] = findViewById(R.id.sec5Label);
        secLabels[5] = findViewById(R.id.sec6Label);
        secLabels[6] = findViewById(R.id.sec7Label);
        secLabels[7] = findViewById(R.id.sec8Label);

        for(int i=0; i<secLabels.length; i++)
            secLabels[i].setTypeface(opensansBold);

        TextView[] secPrices = new TextView[8];
        secPrices[0]=findViewById(R.id.sec1Price);
        secPrices[1]=findViewById(R.id.sec2Price);
        secPrices[2]=findViewById(R.id.sec3Price);
        secPrices[3]=findViewById(R.id.sec4Price);
        secPrices[4]=findViewById(R.id.sec5Price);
        secPrices[5]=findViewById(R.id.sec6Price);
        secPrices[6]=findViewById(R.id.sec7Price);
        secPrices[7]=findViewById(R.id.sec8Price);

        for(int i=0; i<secPrices.length;i++)
            secPrices[i].setTypeface(opensansRegular);

        TextView choosedPlacesText = findViewById(R.id.choosedPlacesText);
        choosedPlacesText.setTypeface(opensansRegular);

        TextView priceLabel = findViewById(R.id.priceLabel);
        priceLabel.setTypeface(opensansRegular);

        //przyciski reprezentujące miejsca

        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);

        final ViewGroup nullParent = null;
        popupView = inflater.inflate(R.layout.seat, nullParent);


        //wypełnienie mapy <Nr_miejsca, Rząd> potrzebenej do znalezienia wiersza dla konkretnego miejsca
        int value = 1;

        for (int i = 1; i <= 280; i++) {
            if ((i - 1) % 14 == 0 && i != 1)
                value++;

            seatAndRowMap.put(i, value);

            Log.d(logTag, "Wstawiona wartość <Nr_miejsca, Rząd>= " + "<" + i + ", " + value + ">");

        }

        //wypełnienie mapy <NrMiejsca, Sektor> potrzebnej do obliczenia liczby wolnych miejsc w sektorze po otrzymaniu wiadomości

        //rzędy parzyste
        int sectorNumber = 1;
        for (int i = 1; i <= 211; i += 70) {
            int firstSeat = i;

            for (int j = 1; j <= 35; j++) {
                if (j == 8 || j == 15 || j == 22 || j == 29) {
                    firstSeat += 7;
                    sectorAndSeat.put(firstSeat, sectorNumber);
                    firstSeat++;
                } else {
                    sectorAndSeat.put(firstSeat, sectorNumber);
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
                    sectorAndSeat.put(firstSeat, sectorNumber);
                    firstSeat++;
                } else {
                    sectorAndSeat.put(firstSeat, sectorNumber);
                    firstSeat++;
                }
            }
            sectorNumber += 2;
        }

        //posortowanie mapy (wg wartości klucza)
        // List sortedKeys = new ArrayList(sectorAndSeat.keySet());
        //Collections.sort(sortedKeys);


        //test

        for (Map.Entry<Integer, Integer> entry : sectorAndSeat.entrySet()) {
            int seatNumber = entry.getKey();
            int sector = entry.getValue();

            Log.d(logTag, "sectorAndSeat<seatNumber, sector> = " + "< " + seatNumber + ", "
                    + sector + " >");
        }


        //obługa związana z socketem

        for (int i = 0; i < choosedPlaces.length; i++) {
            myChoosedPlaces[i] = false;
        }



        Bundle b = getIntent().getExtras();
        repertoireId = b.getInt(getString(R.string.scheduleId));

        final String repertoireIdString = "" + repertoireId;

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                AppConfig.GET_RESERVATIONS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(logTag, getString(R.string.getResLog) + response);
                        try {
                            JSONObject json = new JSONObject(response);
                            boolean error = json.getBoolean(getString(R.string.error));
                            if (error) {
                                if(!json.getString(getString(R.string.message)).
                                        equals(getString(R.string.noResForRep)))
                                Toast.makeText(
                                        getApplicationContext(),
                                        json.getString(getString(R.string.message)),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                JSONArray reservationsJson = json.getJSONArray(getString(R.string.resArrayName));
                                for (int i = 0; i < reservationsJson.length(); i++) {
                                    Log.d(logTag, getString(R.string.resJsonLog) + reservationsJson.length());
                                    JSONObject reservationJSON = reservationsJson.getJSONObject(i);
                                    Reservation reservation = new Reservation(
                                            reservationJSON.getInt(getString(R.string.resId)),
                                            reservationJSON.getInt(getString(R.string.resSeatNumber)),
                                            reservationJSON.getInt(getString(R.string.resRow))
                                    );

                                    choosedPlaces[reservation.getSeatNumber() - 1] = true;
                                    int number = reservation.getSeatNumber() - 1;

                                    Log.d(logTag, "choosedPlaces[ " + number + " ] = " + choosedPlaces[number]);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(
                                    getApplicationContext(),
                                    getString(R.string.jsonError) + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }

                        for (int i = 0; i < choosedPlaces.length; i++)
                            if (choosedPlaces[i])
                                Log.d(logTag, "After choosedPlaces[ " + i + " ] = " + choosedPlaces[i]);

                        updateSectors(true);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(logTag, getString(R.string.resErrorLog) + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(getString(R.string.repId), repertoireIdString);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, getString(R.string.requestAdd));


        final OkHttpClient httpClient = new OkHttpClient();
        okhttp3.Request request = new okhttp3.Request.Builder().url(AppConfig.websocketURL).build();

        WebSocketListener webSocketListener = new WebSocketListener() {
            @Override
            public void onOpen(final WebSocket webSocket, okhttp3.Response response) {


                Log.d(logTag, "Connection estabilished");


                View.OnClickListener buttonClicked = new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        btn = findViewById(v.getId());

                        //jeśli button jest wciśnięty, to go odznacz
                        //wyświetlenie komunikatu w sytuacji, gdy ktoś kliknie na sektor bez miejsc
                        if (btn.getText().equals("0/35")) {

                            ed.buildDialog(SectorActivity.this, getString(R.string.emptySectorTitle),
                                    getString(R.string.emptySectorMsg)).show();
                            return;
                        }

                        //jeśli button nie jest wciśnięty to wyświetl popup
                        sectorButtons.put(btn, true);

                        //po wciśnięciu button, następuje zmiana jego przezroczystości (bez zmiany koloru)
                        btn.getBackground().setAlpha(128);

                        LayoutInflater inflater = (LayoutInflater)
                                getSystemService(LAYOUT_INFLATER_SERVICE);

                        final ViewGroup nullParent = null;
                        popupView = inflater.inflate(R.layout.seat, nullParent);

                        //po kliknięciu dowolnego "aktywnego" sektora od razu pokazuje się popup oraz "chowają" elementy pod nim

                        // create the popup window
                        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

                        popupWindow = new PopupWindow(popupView, width, height, true);


                        // show the popup window
                        // which view you pass in doesn't matter, it is only used for the window tolken


                        //prawidłowe położenia popup
                        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 40);


                        //przyciemnienie backgroundu pod popupem
                        secFrameLayout.getForeground().setAlpha(165);


                        popupWindow.setBackgroundDrawable(new BitmapDrawable());
                        popupWindow.setFocusable(false);


                        seatsButtonIR_1 = popupView.findViewById(R.id.seatsButtonIR_1);
                        seatButtons.put(seatsButtonIR_1, false);
                        seatsButtonIR_2 = popupView.findViewById(R.id.seatsButtonIR_2);
                        seatButtons.put(seatsButtonIR_2, false);
                        seatsButtonIR_3 = popupView.findViewById(R.id.seatsButtonIR_3);
                        seatButtons.put(seatsButtonIR_3, false);
                        seatsButtonIR_4 = popupView.findViewById(R.id.seatsButtonIR_4);
                        seatButtons.put(seatsButtonIR_4, false);
                        seatsButtonIR_5 = popupView.findViewById(R.id.seatsButtonIR_5);
                        seatButtons.put(seatsButtonIR_5, false);
                        seatsButtonIR_6 = popupView.findViewById(R.id.seatsButtonIR_6);
                        seatButtons.put(seatsButtonIR_6, false);
                        seatsButtonIR_7 = popupView.findViewById(R.id.seatsButtonIR_7);
                        seatButtons.put(seatsButtonIR_7, false);

                        seatsButtonIIR_1 = popupView.findViewById(R.id.seatsButtonIIR_1);
                        seatButtons.put(seatsButtonIIR_1, false);
                        seatsButtonIIR_2 = popupView.findViewById(R.id.seatsButtonIIR_2);
                        seatButtons.put(seatsButtonIIR_2, false);
                        seatsButtonIIR_3 = popupView.findViewById(R.id.seatsButtonIIR_3);
                        seatButtons.put(seatsButtonIIR_3, false);
                        seatsButtonIIR_4 = popupView.findViewById(R.id.seatsButtonIIR_4);
                        seatButtons.put(seatsButtonIIR_4, false);
                        seatsButtonIIR_5 = popupView.findViewById(R.id.seatsButtonIIR_5);
                        seatButtons.put(seatsButtonIIR_5, false);
                        seatsButtonIIR_6 = popupView.findViewById(R.id.seatsButtonIIR_6);
                        seatButtons.put(seatsButtonIIR_6, false);
                        seatsButtonIIR_7 = popupView.findViewById(R.id.seatsButtonIIR_7);
                        seatButtons.put(seatsButtonIIR_7, false);

                        seatsButtonIIIR_1 = popupView.findViewById(R.id.seatsButtonIIIR_1);
                        seatButtons.put(seatsButtonIIIR_1, false);
                        seatsButtonIIIR_2 = popupView.findViewById(R.id.seatsButtonIIIR_2);
                        seatButtons.put(seatsButtonIIIR_2, false);
                        seatsButtonIIIR_3 = popupView.findViewById(R.id.seatsButtonIIIR_3);
                        seatButtons.put(seatsButtonIIIR_3, false);
                        seatsButtonIIIR_4 = popupView.findViewById(R.id.seatsButtonIIIR_4);
                        seatButtons.put(seatsButtonIIIR_4, false);
                        seatsButtonIIIR_5 = popupView.findViewById(R.id.seatsButtonIIIR_5);
                        seatButtons.put(seatsButtonIIIR_5, false);
                        seatsButtonIIIR_6 = popupView.findViewById(R.id.seatsButtonIIIR_6);
                        seatButtons.put(seatsButtonIIIR_6, false);
                        seatsButtonIIIR_7 = popupView.findViewById(R.id.seatsButtonIIIR_7);
                        seatButtons.put(seatsButtonIIIR_7, false);

                        seatsButtonIVR_1 = popupView.findViewById(R.id.seatsButtonIVR_1);
                        seatButtons.put(seatsButtonIVR_1, false);
                        seatsButtonIVR_2 = popupView.findViewById(R.id.seatsButtonIVR_2);
                        seatButtons.put(seatsButtonIVR_2, false);
                        seatsButtonIVR_3 = popupView.findViewById(R.id.seatsButtonIVR_3);
                        seatButtons.put(seatsButtonIVR_3, false);
                        seatsButtonIVR_4 = popupView.findViewById(R.id.seatsButtonIVR_4);
                        seatButtons.put(seatsButtonIVR_4, false);
                        seatsButtonIVR_5 = popupView.findViewById(R.id.seatsButtonIVR_5);
                        seatButtons.put(seatsButtonIVR_5, false);
                        seatsButtonIVR_6 = popupView.findViewById(R.id.seatsButtonIVR_6);
                        seatButtons.put(seatsButtonIVR_6, false);
                        seatsButtonIVR_7 = popupView.findViewById(R.id.seatsButtonIVR_7);
                        seatButtons.put(seatsButtonIVR_7, false);

                        seatsButtonVR_1 = popupView.findViewById(R.id.seatsButtonVR_1);
                        seatButtons.put(seatsButtonVR_1, false);
                        seatsButtonVR_2 = popupView.findViewById(R.id.seatsButtonVR_2);
                        seatButtons.put(seatsButtonVR_2, false);
                        seatsButtonVR_3 = popupView.findViewById(R.id.seatsButtonVR_3);
                        seatButtons.put(seatsButtonVR_3, false);
                        seatsButtonVR_4 = popupView.findViewById(R.id.seatsButtonVR_4);
                        seatButtons.put(seatsButtonVR_4, false);
                        seatsButtonVR_5 = popupView.findViewById(R.id.seatsButtonVR_5);
                        seatButtons.put(seatsButtonVR_5, false);
                        seatsButtonVR_6 = popupView.findViewById(R.id.seatsButtonVR_6);
                        seatButtons.put(seatsButtonVR_6, false);
                        seatsButtonVR_7 = popupView.findViewById(R.id.seatsButtonVR_7);
                        seatButtons.put(seatsButtonVR_7, false);

                        seatsProgressBar = popupView.findViewById(R.id.seatsProgressBar);

                        Log.d(logTag, "seatButtons.size() po znalezieniu == " + seatButtons.size());

                        seatsApproveButton = popupView.findViewById(R.id.seatsApproveButton);
                        seatsCloseButton = popupView.findViewById(R.id.seatsCloseButton);

                        seatsLinearLayout = popupView.findViewById(R.id.seatsLinearLayout);

                        title = popupView.findViewById(R.id.title);
                        subtitle = popupView.findViewById(R.id.subtitle);

                        gridLayoutSeats = popupView.findViewById(R.id.gridLayoutSeats);

                        seatsButton1C = popupView.findViewById(R.id.seatsButton1C);
                        columnButtons.add(seatsButton1C);
                        seatsButton2C = popupView.findViewById(R.id.seatsButton2C);
                        columnButtons.add(seatsButton2C);
                        seatsButton3C = popupView.findViewById(R.id.seatsButton3C);
                        columnButtons.add(seatsButton3C);
                        seatsButton4C = popupView.findViewById(R.id.seatsButton4C);
                        columnButtons.add(seatsButton4C);
                        seatsButton5C = popupView.findViewById(R.id.seatsButton5C);
                        columnButtons.add(seatsButton5C);
                        seatsButton6C = popupView.findViewById(R.id.seatsButton6C);
                        columnButtons.add(seatsButton6C);
                        seatsButton7C = popupView.findViewById(R.id.seatsButton7C);
                        columnButtons.add(seatsButton7C);

                        seatsButtonIR = popupView.findViewById(R.id.seatsButtonIR);
                        seatsButtonIIR = popupView.findViewById(R.id.seatsButtonIIR);
                        seatsButtonIIIR = popupView.findViewById(R.id.seatsButtonIIIR);
                        seatsButtonIVR = popupView.findViewById(R.id.seatsButtonIVR);
                        seatsButtonVR = popupView.findViewById(R.id.seatsButtonVR);


                        title.setTypeface(opensansBold);
                        subtitle.setTypeface(opensansBold);
                        seatsCloseButton.setTypeface(opensansRegular);
                        seatsApproveButton.setTypeface(opensansRegular);

                        preparePopUp();

                        //po ponownym otwarciu popupu zaliczenie ponownie wybranego buttona jako wciśnięty
                        for (Map.Entry<Button, Boolean> entry : seatButtons.entrySet()) {
                            Button button = entry.getKey();

                            int number = Integer.parseInt(button.getText().toString()); //parsowanie nr miejsca do int
                            if (selectedSeats.containsKey(number)) {
                                seatButtons.put(button, true);

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
                                    secFrameLayout.getForeground().setAlpha(0);

                                    //usunięcie historii wybranych miejsc po kliknięciu poza popup
                                    seatButtons.clear();

                                    sectorButtons.put(btn, false);
                                    constraintLayout.setVisibility(View.VISIBLE); //przywrócenie dolnej warstwy

                                    return true;
                                } else return false;
                            }
                        });

                        //wciśnięcie przycisku zamykającego popup, przywraca dolną warstwę
                        seatsCloseButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                popupWindow.dismiss();

                                //rozjaśnienie backgroundu pod popupem
                                secFrameLayout.getForeground().setAlpha(0);

                                //usunięcie historii wybranych miejsc po kliknięciu poza popup
                                seatButtons.clear();

                                sectorButtons.put(btn, false);
                                constraintLayout.setVisibility(View.VISIBLE); //przywrócenie dolnej warstwy

                                //zaktualizowanie wyglądu sektorów
                                updateSectors(false);
                            }
                        });

                        View.OnClickListener seatBtnClick = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Button btn = popupView.findViewById(v.getId());

                                Animation animation = new AlphaAnimation(1.0f, 0.0f);
                                animation.setDuration(200);

                                btn.startAnimation(animation);

                                if (!seatButtons.get(btn)) {
                                    seatButtons.put(btn, true);
                                    btn.setBackgroundResource(R.drawable.seat_choosed);

                                    int number = Integer.parseInt(btn.getText().toString()); //parsowanie nr miejsca do int
                                    myChoosedPlaces[number - 1] = true; //zmiana wartości w tablicy Socketu na true
                                    Log.d(logTag, "Zmieniona wartość myChoosedPlaces[ " + (number - 1) + " ] = " + myChoosedPlaces[number - 1]);
                                } else {
                                    seatButtons.put(btn, false);
                                    btn.setBackgroundResource(R.drawable.seat);

                                    int number = Integer.parseInt(btn.getText().toString()); //parsowanie nr miejsca do int
                                    selectedSeats.remove(number); //usunięcie z Mapy odznaczonego miejsca
                                    myChoosedPlaces[number - 1] = false; //zmiana wartości w tablicy Socketu na false
                                    Log.d(logTag, "Zmieniona wartość myChoosedPlaces[ " + (number - 1) + " ] = " + myChoosedPlaces[number - 1]);
                                }

                                seatsApproveButton.setVisibility(View.VISIBLE);
                            }
                        };

                        for (Map.Entry<Button, Boolean> entry : seatButtons.entrySet()) {
                            Button key = entry.getKey();
                            key.setOnClickListener(seatBtnClick);
                        }


                        //dodanie obsługi klawisza zarezerwuj
                        seatsApproveButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                //pobranie informacji o wybranych miejscach i zapisanie

                                //pobranie informacji o seatTypeId (przez pobranie informacji o sektorze)
                                int seatType = selectedSector();

                                //pobranie informacji o nr_miejsca
                                saveSeats(seatType);

                                popupWindow.dismiss();

                                //rozjaśnienie backgroundu pod popupem
                                secFrameLayout.getForeground().setAlpha(0);

                                //po wyborze miejsc wyświetlamy podsumowanie oraz nadajemy kolor przyciskowi akceptacji
                                secBtnReserve.setBackgroundResource(R.drawable.rounded_button_light);
                                secBtnReserve.setEnabled(true);
                                secBtnReserve.setTextColor(Color.WHITE);

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

                                secChoosedPlaces.setText(
                                        String.format(
                                                new Locale("pl", "PL"),
                                                "%d",
                                                numberOfSeats
                                        )
                                );
                                String text = Integer.toString(price) + getString(R.string.currency);
                                secSummaryPrice.setText(text);
                                //zaktualizowanie wyglądu sektorów
                                updateSectors(false);
                                sectorButtons.put(btn, false);
                                seatButtons.clear();
                            }


                        });


                    }

                };
                secButton1.setOnClickListener(buttonClicked);
                secButton2.setOnClickListener(buttonClicked);
                secButton3.setOnClickListener(buttonClicked);
                secButton4.setOnClickListener(buttonClicked);
                secButton5.setOnClickListener(buttonClicked);
                secButton6.setOnClickListener(buttonClicked);
                secButton7.setOnClickListener(buttonClicked);
                secButton8.setOnClickListener(buttonClicked);

                secBtnReserve.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (selectedSeats.size() == 0)
                            ed.buildDialog(SectorActivity.this, getString(R.string.noChoosedPlacesTitle),
                                    getString(R.string.noChoosedPlacesMsg)).show();
                        else {
                            //porównanie miejsc obecnie wybranych z tymi pobranymi z bazy danych

                            boolean isWrong = false;
                            for (int i = 0; i < choosedPlaces.length; i++)
                                if (choosedPlaces[i] & myChoosedPlaces[i])
                                    isWrong = true;

                            if (isWrong) {
                                ed.buildDialog(SectorActivity.this, getString(R.string.choosedTakenPlaceTitle),
                                        getString(R.string.choosedTakenPlaceMsg)).show();
                            } else {
                                //zapis do bazy danych

                                
                                HashMap<String, String> customer = db.getCustomer();

                                int currentRepertoireId = repertoireId;

                                for (Map.Entry<Integer, Integer> entry : selectedSeats.entrySet()) {

                                    final String customerId = customer.get(getString(R.string.resId));
                                    final String seatNumber = Integer.toString(entry.getKey());
                                    final String seatTypeId = Integer.toString(entry.getValue());
                                    final String row = Integer.toString(seatAndRowMap.get(entry.getKey()));
                                    final String repertoireId = Integer.toString(currentRepertoireId);

                                    Log.d(logTag, "Pobrana wartość row =  " + row);

                                    //zapisanie do bazy
                                    StringRequest stringRequest = new StringRequest(Request.Method.POST,
                                            AppConfig.STORE_RESERVATION,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    Log.d(logTag, getString(R.string.saveResRequest) + response);
                                                    try {
                                                        JSONObject json = new JSONObject(response);
                                                        boolean error = json.getBoolean(getString(R.string.error));
                                                        if (error) {
                                                            Toast.makeText(
                                                                    getApplicationContext(),
                                                                    json.getString(getString(R.string.message)),
                                                                    Toast.LENGTH_SHORT).show();
                                                        }
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                        Toast.makeText(
                                                                getApplicationContext(),
                                                                getString(R.string.jsonError) + e.getMessage(),
                                                                Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.e(logTag, getString(R.string.saveResErrorLog) + error.getMessage());
                                            Toast.makeText(getApplicationContext(),
                                                    error.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }) {
                                        @Override
                                        protected Map<String, String> getParams() {
                                            Map<String, String> params = new HashMap<>();
                                            params.put(getString(R.string.customerId), customerId);
                                            params.put(getString(R.string.resSeatNumber), seatNumber);
                                            params.put(getString(R.string.resRow), row);
                                            params.put(getString(R.string.seatTypeId), seatTypeId);
                                            params.put(getString(R.string.repId), repertoireId);
                                            return params;
                                        }
                                    };

                                    AppController.getInstance().addToRequestQueue(stringRequest, getString(R.string.requestAdd));
                                }

                                //wysłanie wiadomości do Socketu
                                sendMessageToServer(httpClient, webSocket);

                                //przejście do kolejnego ekranu - Moje rezerwacje

                                Intent intent = new Intent(SectorActivity.this, CustomerReservationsActivity.class);
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
                    choosedPlaces[i] = message.getChoosedPlaces()[i];
                    Log.d(logTag, "choosedPlaces [ " + i + " ] = " + choosedPlaces[i]);
                }
                markChoosedPlaces();
                //... oznaczenia wszystkich zajetych miejsc

                //myChoosedPlaces without places from websocket
                for (int i = 0; i < choosedPlaces.length; i++) {
                    myChoosedPlaces[i] = (myChoosedPlaces[i] ^ choosedPlaces[i]) & myChoosedPlaces[i];
                    Log.d(logTag, "myChoosedPlaces [ " + i + " ] = " + myChoosedPlaces[i]);
                }

                ArrayList<Integer> takenYourSeats = new ArrayList<>();
                //Sytuacja, gdy ktoś po wyborze miejsca, i zatwierdzeniu (popupu)
                // przejdzie do ekranu z sektorami. Następie ktoś zarezerwuje wybrane
                // przez niego miejsce.

                for (int i = 0; i < choosedPlaces.length; i++) {
                    if (selectedSeats.containsKey(i + 1) && choosedPlaces[i]) {
                        Log.d(logTag, "Wybrane przez ciebie miejsce " + (i + 1) + " zostało właśnie zajęte.");

                        selectedSeats.remove(i + 1);
                        takenYourSeats.add(i + 1);
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
                Log.d(logTag, getString(R.string.socketFailure) + t.getMessage());
            }
        };

        httpClient.newWebSocket(request, webSocketListener);
        httpClient.dispatcher().executorService().shutdown();

    }

    private void sendMessageToServer(OkHttpClient httpClient, WebSocket webSocket) {
        if (httpClient != null) {
            Message message = new Message(myChoosedPlaces);
            webSocket.send(message.getChoosedPlacesString());
            webSocket.close(1000, getString(R.string.socketCloseReason));
        } else {
            ed.buildDialog(SectorActivity.this, getString(R.string.serverErrorTitle),
                    getString(R.string.serverErrorMsg)).show();
            Log.d(logTag, getString(R.string.socketSendError));
        }
    }
}



