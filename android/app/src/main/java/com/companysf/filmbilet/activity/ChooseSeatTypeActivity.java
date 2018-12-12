package com.companysf.filmbilet.activity;

import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.companysf.filmbilet.asyncTasks.FreeSeatsTask;
import com.companysf.filmbilet.asyncTasks.FreeSectorsTask;
import com.companysf.filmbilet.R;
import com.companysf.filmbilet.addition.SessionManager;


import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.Map;


public class ChooseSeatTypeActivity extends AppCompatActivity {

    private static final String logTag = MainActivity.class.getSimpleName();
    private SessionManager sManager;
    private Map<Button, Boolean> buttons = new HashMap<>();

    ConstraintLayout constraintLayout;
    Button button1, button2, button3, button4, button5, button6, button7, button8, btn_back, btn_next;
    ProgressBar progressBar;


    //seats
    private Button buttonIR_1, buttonIR_2, buttonIR_3, buttonIR_4, buttonIR_5,buttonIR_6, buttonIR_7,
            buttonIIR_1, buttonIIR_2, buttonIIR_3, buttonIIR_4, buttonIIR_5,buttonIIR_6, buttonIIR_7,
            buttonIIIR_1, buttonIIIR_2, buttonIIIR_3, buttonIIIR_4, buttonIIIR_5,buttonIIIR_6, buttonIIIR_7,
            buttonIVR_1, buttonIVR_2, buttonIVR_3, buttonIVR_4, buttonIVR_5,buttonIVR_6, buttonIVR_7,
            buttonVR_1, buttonVR_2, buttonVR_3, buttonVR_4, buttonVR_5,buttonVR_6, buttonVR_7,
            btnReserve;


    ArrayList<Button> seatsButtons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_choose_seat_type2);

        // btnNext = (Button) findViewById(R.id.btnNext);

        //adjusting size of  the buttons

        constraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayout);

        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        button5 = (Button) findViewById(R.id.button5);
        button6 = (Button) findViewById(R.id.button6);
        button7 = (Button) findViewById(R.id.button7);
        button8 = (Button) findViewById(R.id.button8);

        btn_back = (Button) findViewById(R.id.btn_back);
        btn_next = (Button) findViewById(R.id.btn_next);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        buttons.put(button1, false);
        buttons.put(button2, false);
        buttons.put(button3, false);
        buttons.put(button4, false);
        buttons.put(button5, false);
        buttons.put(button6, false);
        buttons.put(button7, false);
        buttons.put(button8, false);


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

            ConstraintLayout.LayoutParams params9 = (ConstraintLayout.LayoutParams) btn_back.getLayoutParams();
            params9.width = (getResources().getDisplayMetrics().widthPixels) / 6;
            params9.height = ((getResources().getDisplayMetrics().heightPixels / 3)) / 6;
            btn_back.setLayoutParams(params9);


            ConstraintLayout.LayoutParams params10 = (ConstraintLayout.LayoutParams) btn_next.getLayoutParams();
            params10.width = (getResources().getDisplayMetrics().widthPixels) / 6;
            params10.height = ((getResources().getDisplayMetrics().heightPixels / 3)) / 6;
            btn_next.setLayoutParams(params10);

            //execute(nr_repertuaru z poprzedniuego intentu)
            new FreeSectorsTask(getApplicationContext(), constraintLayout, true).execute(1);

            //odświeżanie wolnych sektorów co 2 sekundy

/*
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {

                public void run() {
                    //zapisanie poprzedniego stanu sektorów
                    Map<Button, Boolean> previousButtons = new HashMap<>(buttons);
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
                /*
                Intent i= new Intent(getApplicationContext(),ChooseSeatActivity.class);
                startActivity(i);*/

                // inflate the layout of the popup window


                //dodanie warunku na to, że tylko jeden z sektorów może być zaznaczony

                int selected = 0;
                boolean isButtonChanged = false;
                for (Map.Entry<Button, Boolean> entry : buttons.entrySet()) {
                    Button key = entry.getKey();
                    Boolean value = entry.getValue();

                    if (value && key.isEnabled())
                        selected++;

                    else if(value && !key.isEnabled()){
                        buttons.put(key, false);
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


                    boolean flag1 = buttons.get(button1);
                    boolean flag2 = buttons.get(button2);
                    boolean flag3 = buttons.get(button3);
                    boolean flag4 = buttons.get(button4);
                    boolean flag5 = buttons.get(button5);
                    boolean flag6 = buttons.get(button6);
                    boolean flag7 = buttons.get(button7);
                    boolean flag8 = buttons.get(button8);

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

                    final View popupView = inflater.inflate(R.layout.activity_choose_seat_left, null);

                    LinearLayout linearLayoutSeats=(LinearLayout) findViewById(R.id.linearLayoutSeats);




                    if(flag1 || flag3 || flag5 || flag7)
                    {
                        new FreeSeatsTask(getApplicationContext(), popupView, true, true, seatTypeId).execute(1);
                    }

                    else {
                        new FreeSeatsTask(getApplicationContext(), popupView, true, false, seatTypeId).execute(1);
                    }
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
                    seatsButtons.add(buttonIR_1);
                    buttonIR_2 = (Button) popupView.findViewById(R.id.buttonIR_2);
                    seatsButtons.add(buttonIR_2);
                    buttonIR_3 = (Button) popupView.findViewById(R.id.buttonIR_3);
                    seatsButtons.add(buttonIR_3);
                    buttonIR_4 = (Button) popupView.findViewById(R.id.buttonIR_4);
                    seatsButtons.add(buttonIR_4);
                    buttonIR_5 = (Button) popupView.findViewById(R.id.buttonIR_5);
                    seatsButtons.add(buttonIR_5);
                    buttonIR_6 = (Button) popupView.findViewById(R.id.buttonIR_6);
                    seatsButtons.add(buttonIR_6);
                    buttonIR_7 = (Button) popupView.findViewById(R.id.buttonIR_7);
                    seatsButtons.add(buttonIR_7);

                    buttonIIR_1 = (Button) popupView.findViewById(R.id.buttonIIR_1);
                    seatsButtons.add(buttonIIR_1);
                    buttonIIR_2 = (Button) popupView.findViewById(R.id.buttonIIR_2);
                    seatsButtons.add(buttonIIR_2);
                    buttonIIR_3 = (Button) popupView.findViewById(R.id.buttonIIR_3);
                    seatsButtons.add(buttonIIR_3);
                    buttonIIR_4 = (Button) popupView.findViewById(R.id.buttonIIR_4);
                    seatsButtons.add(buttonIIR_4);
                    buttonIIR_5 = (Button) popupView.findViewById(R.id.buttonIIR_5);
                    seatsButtons.add(buttonIIR_5);
                    buttonIIR_6 = (Button) popupView.findViewById(R.id.buttonIIR_6);
                    seatsButtons.add(buttonIIR_6);
                    buttonIIR_7 = (Button) popupView.findViewById(R.id.buttonIIR_7);
                    seatsButtons.add(buttonIIR_7);

                    buttonIIIR_1 = (Button) popupView.findViewById(R.id.buttonIIIR_1);
                    seatsButtons.add(buttonIIIR_1);
                    buttonIIIR_2 = (Button) popupView.findViewById(R.id.buttonIIIR_2);
                    seatsButtons.add(buttonIIIR_2);
                    buttonIIIR_3 = (Button) popupView.findViewById(R.id.buttonIIIR_3);
                    seatsButtons.add(buttonIIIR_3);
                    buttonIIIR_4 = (Button) popupView.findViewById(R.id.buttonIIIR_4);
                    seatsButtons.add(buttonIIIR_4);
                    buttonIIIR_5 = (Button) popupView.findViewById(R.id.buttonIIIR_5);
                    seatsButtons.add(buttonIIIR_5);
                    buttonIIIR_6 = (Button) popupView.findViewById(R.id.buttonIIIR_6);
                    seatsButtons.add(buttonIIIR_6);
                    buttonIIIR_7 = (Button) popupView.findViewById(R.id.buttonIIIR_7);
                    seatsButtons.add(buttonIIIR_7);

                    buttonIVR_1 = (Button) popupView.findViewById(R.id.buttonIVR_1);
                    seatsButtons.add(buttonIVR_1);
                    buttonIVR_2 = (Button) popupView.findViewById(R.id.buttonIVR_2);
                    seatsButtons.add(buttonIVR_2);
                    buttonIVR_3 = (Button) popupView.findViewById(R.id.buttonIVR_3);
                    seatsButtons.add(buttonIVR_3);
                    buttonIVR_4 = (Button) popupView.findViewById(R.id.buttonIVR_4);
                    seatsButtons.add(buttonIVR_4);
                    buttonIVR_5 = (Button) popupView.findViewById(R.id.buttonIVR_5);
                    seatsButtons.add(buttonIVR_5);
                    buttonIVR_6 = (Button) popupView.findViewById(R.id.buttonIVR_6);
                    seatsButtons.add(buttonIVR_6);
                    buttonIVR_7 = (Button) popupView.findViewById(R.id.buttonIVR_7);
                    seatsButtons.add(buttonIVR_7);

                    buttonVR_1 = (Button) popupView.findViewById(R.id.buttonVR_1);
                    seatsButtons.add(buttonVR_1);
                    buttonVR_2 = (Button) popupView.findViewById(R.id.buttonVR_2);
                    seatsButtons.add(buttonVR_2);
                    buttonVR_3 = (Button) popupView.findViewById(R.id.buttonVR_3);
                    seatsButtons.add(buttonVR_3);
                    buttonVR_4 = (Button) popupView.findViewById(R.id.buttonVR_4);
                    seatsButtons.add(buttonVR_4);
                    buttonVR_5 = (Button) popupView.findViewById(R.id.buttonVR_5);
                    seatsButtons.add(buttonVR_5);
                    buttonVR_6 = (Button) popupView.findViewById(R.id.buttonVR_6);
                    seatsButtons.add(buttonVR_6);
                    buttonVR_7 = (Button) popupView.findViewById(R.id.buttonVR_7);
                    seatsButtons.add(buttonVR_7);


                    View.OnClickListener seatBtnClick=new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                           Button btn = (Button) popupView.findViewById(v.getId());
                            btn.setBackgroundColor(Color.GREEN);
                        }
                    };

                    for(Button b : seatsButtons)
                        b.setOnClickListener(seatBtnClick);


                    // dismiss the popup window when touched
                    popupView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            popupWindow.dismiss();
                            Drawable d = new ColorDrawable(Color.WHITE);
                            popupWindow.setBackgroundDrawable(new BitmapDrawable());
                            getWindow().setBackgroundDrawable(d);


                            return true;
                        }
                    });
                }
            }

        });

    }







    public void buttonClicked(View view){

        String free ="#6bb9f0";

        Button btn = (Button) findViewById(view.getId());
        //btn.setEnabled(false);

        Animation animation = new AlphaAnimation(1.0f, 0.0f);
        animation.setDuration(200);

        btn.startAnimation(animation);

        if(!buttons.get(btn)){
            buttons.put(btn,true);
            btn.setBackgroundResource(R.drawable.button_light);
        }
        else{
            buttons.put(btn,false);
            btn.setBackgroundResource(R.drawable.button_normal);
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
}



