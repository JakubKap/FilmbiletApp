package com.companysf.filmbilet.activity;

import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.companysf.filmbilet.AsyncTasks.FreeSeatsTask;
import com.companysf.filmbilet.AsyncTasks.FreeSectorsTask;
import com.companysf.filmbilet.R;
import com.companysf.filmbilet.addition.SessionManager;


import java.util.HashMap;

import java.util.Map;


public class ChooseSeatTypeActivity extends AppCompatActivity {

    private static final String logTag = MainActivity.class.getSimpleName();
    private SessionManager sManager;
    private Map<Button, Boolean> sectorButtons = new HashMap<>();

    ConstraintLayout constraintLayout;
    Button button1, button2, button3, button4, button5, button6, button7, button8, btn_next;
    ProgressBar progressBar;

    TextView textView3, textView4;

    //seats
    private Button buttonIR_1, buttonIR_2, buttonIR_3, buttonIR_4, buttonIR_5,buttonIR_6, buttonIR_7,
            buttonIIR_1, buttonIIR_2, buttonIIR_3, buttonIIR_4, buttonIIR_5,buttonIIR_6, buttonIIR_7,
            buttonIIIR_1, buttonIIIR_2, buttonIIIR_3, buttonIIIR_4, buttonIIIR_5,buttonIIIR_6, buttonIIIR_7,
            buttonIVR_1, buttonIVR_2, buttonIVR_3, buttonIVR_4, buttonIVR_5,buttonIVR_6, buttonIVR_7,
            buttonVR_1, buttonVR_2, buttonVR_3, buttonVR_4, buttonVR_5,buttonVR_6, buttonVR_7,
            btnReserve, btnAccept, buttonClose;

    private TextView textView3Seats;

    private Map<Button, Boolean> seatButtons = new HashMap<>();


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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        btnAccept = (Button) findViewById(R.id.btnAccept);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        textView3=(TextView) findViewById(R.id.textView3);
        textView4=(TextView) findViewById(R.id.textView4);

        sectorButtons.put(button1, false);
        sectorButtons.put(button2, false);
        sectorButtons.put(button3, false);
        sectorButtons.put(button4, false);
        sectorButtons.put(button5, false);
        sectorButtons.put(button6, false);
        sectorButtons.put(button7, false);
        sectorButtons.put(button8, false);


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

/*

        sManager = new SessionManager(getApplicationContext());

        if (!sManager.isLoggedIn()) {
            logOutCustomer();
        }
*/




        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // Intent i= new Intent(getApplicationContext(),ChooseSeatActivity.class);
              //  startActivity(i);

                // inflate the layout of the popup window


                //dodanie warunku na to, że tylko jeden z sektorów może być zaznaczony

                int selected = 0;
                boolean isButtonChanged = false;
                for (Map.Entry<Button, Boolean> entry : sectorButtons.entrySet()) {
                    Button key = entry.getKey();
                    Boolean value = entry.getValue();

                    if (value && key.isEnabled())
                        selected++;

                    else if(value && !key.isEnabled()){
                        sectorButtons.put(key, false);
                        Toast.makeText(getApplicationContext(),
                                "Ostatnie miejsce z wybranego sektora zostało zarezerwowane. Wybierz inny sektor.", Toast.LENGTH_SHORT).show();
                    Log.d("Przegląd buttonów", "" + key +" " + value);
                    isButtonChanged=true;
                    }
                }

                if ((selected == 0 || selected > 1) && !isButtonChanged)
                    Toast.makeText(getApplicationContext(), "Wybierz jeden sektor, aby przejść dalej.", Toast.LENGTH_SHORT).show();

                else if(selected == 1 && !isButtonChanged){
                    LayoutInflater inflater = (LayoutInflater)
                            getSystemService(LAYOUT_INFLATER_SERVICE);



                    int seatTypeId=selectedSector();

                    final View popupView = inflater.inflate(R.layout.activity_choose_seat_left, null);

                    //LinearLayout linearLayoutSeats=(LinearLayout) findViewById(R.id.linearLayoutSeats);


                    boolean flag1 = sectorButtons.get(button1);
                    boolean flag2 = sectorButtons.get(button2);
                    boolean flag3 = sectorButtons.get(button3);
                    boolean flag4 = sectorButtons.get(button4);
                    boolean flag5 = sectorButtons.get(button5);
                    boolean flag6 = sectorButtons.get(button6);
                    boolean flag7 = sectorButtons.get(button7);
                    boolean flag8 = sectorButtons.get(button8);

/*
                    if(flag1 || flag3 || flag5 || flag7)
                    {
                        new FreeSeatsTask(getApplicationContext(), popupView, true, true, seatTypeId).execute(1);
                    }

                    else {
                        new FreeSeatsTask(getApplicationContext(), popupView, true, false, seatTypeId).execute(1);
                    }*/
                    // create the popup window
                    int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    boolean focusable = true; // lets taps outside the popup also dismiss it
                    final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                    // show the popup window
                    // which view you pass in doesn't matter, it is only used for the window tolken
                    popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);


                    //popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.clear));
               /* popupWindow.setBackgroundDrawable(new ColorDrawable(
                        android.graphics.Color.TRANSPARENT));*/

                    Drawable d = new ColorDrawable(Color.WHITE);

                    d.setAlpha(130);

                    popupWindow.setBackgroundDrawable(new BitmapDrawable());

                    // popupWindow.showAsDropDown(mBtnPopUpWindow, 0, 0,Gravity.LEFT);

                    getWindow().setBackgroundDrawable(d);

                    //dodanie słuchacza do przycisków oraz textView popupview

                    buttonIR_1 = (Button) popupView.findViewById(R.id.buttonIR_1);
                    seatButtons.put(buttonIR_1,false);
                    buttonIR_2 = (Button) popupView.findViewById(R.id.buttonIR_2);
                    seatButtons.put(buttonIR_2,false);
                    buttonIR_3 = (Button) popupView.findViewById(R.id.buttonIR_3);
                    seatButtons.put(buttonIR_3,false);
                    buttonIR_4 = (Button) popupView.findViewById(R.id.buttonIR_4);
                    seatButtons.put(buttonIR_4,false);
                    buttonIR_5 = (Button) popupView.findViewById(R.id.buttonIR_5);
                    seatButtons.put(buttonIR_5,false);
                    buttonIR_6 = (Button) popupView.findViewById(R.id.buttonIR_6);
                    seatButtons.put(buttonIR_6,false);
                    buttonIR_7 = (Button) popupView.findViewById(R.id.buttonIR_7);
                    seatButtons.put(buttonIR_7,false);

                    buttonIIR_1 = (Button) popupView.findViewById(R.id.buttonIIR_1);
                    seatButtons.put(buttonIIR_1,false);
                    buttonIIR_2 = (Button) popupView.findViewById(R.id.buttonIIR_2);
                    seatButtons.put(buttonIIR_2,false);
                    buttonIIR_3 = (Button) popupView.findViewById(R.id.buttonIIR_3);
                    seatButtons.put(buttonIIR_3,false);
                    buttonIIR_4 = (Button) popupView.findViewById(R.id.buttonIIR_4);
                    seatButtons.put(buttonIIR_4,false);
                    buttonIIR_5 = (Button) popupView.findViewById(R.id.buttonIIR_5);
                    seatButtons.put(buttonIIR_5,false);
                    buttonIIR_6 = (Button) popupView.findViewById(R.id.buttonIIR_6);
                    seatButtons.put(buttonIIR_6,false);
                    buttonIIR_7 = (Button) popupView.findViewById(R.id.buttonIIR_7);
                    seatButtons.put(buttonIIR_7,false);

                    buttonIIIR_1 = (Button) popupView.findViewById(R.id.buttonIIIR_1);
                    seatButtons.put(buttonIIIR_1,false);
                    buttonIIIR_2 = (Button) popupView.findViewById(R.id.buttonIIIR_2);
                    seatButtons.put(buttonIIIR_2,false);
                    buttonIIIR_3 = (Button) popupView.findViewById(R.id.buttonIIIR_3);
                    seatButtons.put(buttonIIIR_3,false);
                    buttonIIIR_4 = (Button) popupView.findViewById(R.id.buttonIIIR_4);
                    seatButtons.put(buttonIIIR_4,false);
                    buttonIIIR_5 = (Button) popupView.findViewById(R.id.buttonIIIR_5);
                    seatButtons.put(buttonIIIR_5,false);
                    buttonIIIR_6 = (Button) popupView.findViewById(R.id.buttonIIIR_6);
                    seatButtons.put(buttonIIIR_6,false);
                    buttonIIIR_7 = (Button) popupView.findViewById(R.id.buttonIIIR_7);
                    seatButtons.put(buttonIIIR_7,false);

                    buttonIVR_1 = (Button) popupView.findViewById(R.id.buttonIVR_1);
                    seatButtons.put(buttonIVR_1,false);
                    buttonIVR_2 = (Button) popupView.findViewById(R.id.buttonIVR_2);
                    seatButtons.put(buttonIVR_2,false);
                    buttonIVR_3 = (Button) popupView.findViewById(R.id.buttonIVR_3);
                    seatButtons.put(buttonIVR_3,false);
                    buttonIVR_4 = (Button) popupView.findViewById(R.id.buttonIVR_4);
                    seatButtons.put(buttonIVR_4,false);
                    buttonIVR_5 = (Button) popupView.findViewById(R.id.buttonIVR_5);
                    seatButtons.put(buttonIVR_5,false);
                    buttonIVR_6 = (Button) popupView.findViewById(R.id.buttonIVR_6);
                    seatButtons.put(buttonIVR_6,false);
                    buttonIVR_7 = (Button) popupView.findViewById(R.id.buttonIVR_7);
                    seatButtons.put(buttonIVR_7,false);

                    buttonVR_1 = (Button) popupView.findViewById(R.id.buttonVR_1);
                    seatButtons.put(buttonVR_1,false);
                    buttonVR_2 = (Button) popupView.findViewById(R.id.buttonVR_2);
                    seatButtons.put(buttonVR_2,false);
                    buttonVR_3 = (Button) popupView.findViewById(R.id.buttonVR_3);
                    seatButtons.put(buttonVR_3,false);
                    buttonVR_4 = (Button) popupView.findViewById(R.id.buttonVR_4);
                    seatButtons.put(buttonVR_4,false);
                    buttonVR_5 = (Button) popupView.findViewById(R.id.buttonVR_5);
                    seatButtons.put(buttonVR_5,false);
                    buttonVR_6 = (Button) popupView.findViewById(R.id.buttonVR_6);
                    seatButtons.put(buttonVR_6,false);
                    buttonVR_7 = (Button) popupView.findViewById(R.id.buttonVR_7);
                    seatButtons.put(buttonVR_7,false);

                    textView3Seats = (TextView) popupView.findViewById(R.id.textView3Seats);


                    View.OnClickListener seatBtnClick=new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                           Button btn = (Button) popupView.findViewById(v.getId());


                            Animation animation = new AlphaAnimation(1.0f, 0.0f);
                            animation.setDuration(200);

                            btn.startAnimation(animation);


                            if(!seatButtons.get(btn)){
                                seatButtons.put(btn,true);
                                btn.setBackgroundResource(R.drawable.button_light);
                            }
                            else{
                                seatButtons.put(btn,false);
                                btn.setBackgroundResource(R.drawable.button_normal_first);
                            }


                            int selected = selectedSeats();

                            textView3Seats.setText("Liczba wybranych miejsc: " + selected);

                        }
                    };

                    for (Map.Entry<Button, Boolean> entry : seatButtons.entrySet()) {
                        Button key = entry.getKey();
                        Boolean value = entry.getValue();
                        key.setOnClickListener(seatBtnClick);
                    }


                    btnReserve = (Button) popupView.findViewById(R.id.btnReserve);

                    //dodanie obsługi klawisza zarezerwuj
                    btnReserve.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(selectedSeats() > 0)
                            {
                                popupWindow.dismiss();
                                Drawable d = new ColorDrawable(Color.WHITE);
                                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                                getWindow().setBackgroundDrawable(d);

                                //Wyświetlenie podsumowania
                                btn_next.setVisibility(View.INVISIBLE);
                                textView3.setVisibility(View.VISIBLE);

                                for (Map.Entry<Button, Boolean> entry : sectorButtons.entrySet()) {
                                    Button key = entry.getKey();
                                    Boolean value = entry.getValue();
                                    key.setEnabled(false);
                                }

                                textView4.setVisibility(View.VISIBLE);

                                //obliczenie ceny

                                int price = 1;
                                int seatTypeId = selectedSector();

                                if(seatTypeId == 1)
                                    price = selectedSeats() * 10;
                                else if(seatTypeId == 2)
                                    price = selectedSeats() * 15;
                                else if(seatTypeId == 3)
                                    price = selectedSeats() * 20;
                                else if(seatTypeId == 4)
                                    price = selectedSeats() * 30;



                               textView4.setText("Liczba miejsc: " + selectedSeats()
                               + "\nCena: " + price + " zł");

                                btnAccept.setVisibility(View.VISIBLE);


                                //ustalenie które rzędy

                            }
                            else{
                                textView3Seats.setText("Musisz wybrać co najmniej 1 miejsce.");
                            }



                        }
                    });


                    // dismiss the popup window when touched
                    popupView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            popupWindow.dismiss();
                            Drawable d = new ColorDrawable(Color.WHITE);
                            popupWindow.setBackgroundDrawable(new BitmapDrawable());
                            getWindow().setBackgroundDrawable(d);
                            //usunięcie historii wybranych miejsc po kliknięciu poza popup
                            seatButtons.clear();

                            return true;
                        }
                    });
                }
            }

        });

    }







    public void buttonClicked(View view) {

        String free = "#6bb9f0";

        Button btn = (Button) findViewById(view.getId());
        //btn.setEnabled(false);

        //Animation animation = new AlphaAnimation(1.0f, 0.0f);
        //animation.setDuration(200);

        //btn.startAnimation(animation);

        //jeśli button jest wciśnięty, to go odznacz

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

            btn.setBackgroundResource(R.drawable.button_light);


            //po kliknięciu dowolnego "aktywnego" sektora od razu pokazuje się popup oraz "chowają" elementy pod nim

            constraintLayout.setVisibility(View.INVISIBLE); //schowanie dolnej warstwy

            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(LAYOUT_INFLATER_SERVICE);


            int seatTypeId = selectedSector();

            final View popupView = inflater.inflate(R.layout.activity_choose_seat_left, null);

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


            new FreeSeatsTask(getApplicationContext(), popupView, startSeat, seatTypeId).execute(1);

            // create the popup window
            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            boolean focusable = true; // lets taps outside the popup also dismiss it
            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);


            // show the popup window
            // which view you pass in doesn't matter, it is only used for the window tolken
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

            //popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.clear));
               /* popupWindow.setBackgroundDrawable(new ColorDrawable(
                        android.graphics.Color.TRANSPARENT));*/

            Drawable d = new ColorDrawable(Color.WHITE);

            d.setAlpha(180);

            popupWindow.setBackgroundDrawable(new BitmapDrawable());

            // popupWindow.showAsDropDown(mBtnPopUpWindow, 0, 0,Gravity.LEFT);


            getWindow().setBackgroundDrawable(d);

            //dodanie słuchacza do przycisków oraz textView popupview

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

            btnReserve = (Button) popupView.findViewById(R.id.btnReserve);

            textView3Seats = (TextView) popupView.findViewById(R.id.textView3Seats);

            buttonClose = (Button) popupView.findViewById(R.id.buttonClose);

            //wciśnięcie przycisku zamykającego popup, przywraca dolną warstwę
          buttonClose.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {


                  popupWindow.dismiss();
                  Drawable d = new ColorDrawable(Color.WHITE);
                  popupWindow.setBackgroundDrawable(new BitmapDrawable());
                  getWindow().setBackgroundDrawable(d);

                  //usunięcie historii wybranych miejsc po kliknięciu poza popup
                  seatButtons.clear();

                  constraintLayout.setVisibility(View.VISIBLE); //przywrócenie dolnej warstwy

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
                    } else {
                        seatButtons.put(btn, false);
                        btn.setBackgroundResource(R.drawable.button_normal_first);
                    }


                    int selected = selectedSeats();


                    btnReserve.setVisibility(View.VISIBLE);
                    textView3Seats.setVisibility(View.VISIBLE);
                    textView3Seats.setText("Wybrane miejsca: " + selected);

                }
            };

            for (Map.Entry<Button, Boolean> entry : seatButtons.entrySet()) {
                Button key = entry.getKey();
                Boolean value = entry.getValue();
                key.setOnClickListener(seatBtnClick);
            }


            //dodanie obsługi klawisza zarezerwuj
            btnReserve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (selectedSeats() > 0) {
                        popupWindow.dismiss();
                        Drawable d = new ColorDrawable(Color.WHITE);
                        popupWindow.setBackgroundDrawable(new BitmapDrawable());
                        getWindow().setBackgroundDrawable(d);

                        //Wyświetlenie podsumowania
                        btn_next.setVisibility(View.INVISIBLE);
                        textView3.setVisibility(View.VISIBLE);

                        for (Map.Entry<Button, Boolean> entry : sectorButtons.entrySet()) {
                            Button key = entry.getKey();
                            Boolean value = entry.getValue();
                            key.setEnabled(false);
                        }

                        textView4.setVisibility(View.VISIBLE);

                        //obliczenie ceny

                        int price = 1;
                        int seatTypeId = selectedSector();

                        if (seatTypeId == 1)
                            price = selectedSeats() * 10;
                        else if (seatTypeId == 2)
                            price = selectedSeats() * 15;
                        else if (seatTypeId == 3)
                            price = selectedSeats() * 20;
                        else if (seatTypeId == 4)
                            price = selectedSeats() * 30;


                        textView4.setText("Liczba miejsc: " + selectedSeats()
                                + "\nCena: " + price + " zł");

                        btnAccept.setVisibility(View.VISIBLE);


                        //ustalenie które rzędy

                    } else {
                        textView3Seats.setText("Musisz wybrać co najmniej 1 miejsce.");
                    }


                }
            });


/*
            // dismiss the popup window when touched
            popupView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {


                        popupWindow.dismiss();
                        Drawable d = new ColorDrawable(Color.WHITE);
                        popupWindow.setBackgroundDrawable(new BitmapDrawable());
                        getWindow().setBackgroundDrawable(d);

                        //usunięcie historii wybranych miejsc po kliknięciu poza popup
                        seatButtons.clear();

                        constraintLayout.setVisibility(View.VISIBLE); //przywrócenie dolnej warstwy
                        return true;

                }
            });
            */


        }

      }
    }

/*
    private View.OnClickListener seatButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button btn = (Button) findViewById(v.getId());
             btn.setBackgroundColor(Color.GREEN);

        }
    };*/




