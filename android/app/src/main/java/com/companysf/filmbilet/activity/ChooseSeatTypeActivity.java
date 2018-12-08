package com.companysf.filmbilet.activity;

import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.Toast;

import com.companysf.filmbilet.AsyncTasks.FreeSectorsTask;
import com.companysf.filmbilet.R;
import com.companysf.filmbilet.addition.SessionManager;


import java.util.HashMap;

import java.util.Map;


public class ChooseSeatTypeActivity extends AppCompatActivity {

    private static final String logTag = MainActivity.class.getSimpleName();
    private SessionManager sManager;
    private Map<Button, Boolean> buttons = new HashMap<>();

    ConstraintLayout constraintLayout;
    Button button1, button2, button3, button4, button5, button6, button7, button8, btn_back, btn_next;
    ProgressBar progressBar;

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
            new FreeSectorsTask(getApplicationContext(), constraintLayout).execute(1);


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
                for (Map.Entry<Button, Boolean> entry : buttons.entrySet()) {
                    Button key = entry.getKey();
                    Boolean value = entry.getValue();

                    if (value) selected++;

                }

                if (selected == 0 || selected > 1)
                    Toast.makeText(getApplicationContext(), "Wybierz jeden sektor, aby przejść dalej.", Toast.LENGTH_SHORT).show();

                else {
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
                    View popupView;

                    if(flag1 || flag3 || flag5 || flag7)
                     popupView = inflater.inflate(R.layout.activity_choose_seat_left, null);
                    else
                        popupView = inflater.inflate(R.layout.activity_choose_seat_right, null);



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

}



