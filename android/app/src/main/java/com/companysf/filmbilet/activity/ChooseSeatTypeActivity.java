package com.companysf.filmbilet.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.companysf.filmbilet.R;

public class ChooseSeatTypeActivity extends AppCompatActivity {

    //Button btnNext;

    Button button1, button2, button3, button4, button5, button6, button7, button8, btn_back, btn_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_seat_type2);

       // btnNext = (Button) findViewById(R.id.btnNext);

        //adjusting size of  the buttons

        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        button5 = (Button) findViewById(R.id.button5);
        button6 = (Button) findViewById(R.id.button6);
        button7 = (Button) findViewById(R.id.button7);
        button8 = (Button) findViewById(R.id.button8);

        btn_back = (Button) findViewById(R.id.btn_back);
        btn_next=(Button) findViewById(R.id.btn_next);

        //ConstraintLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)button1.getLayoutParams();


        int orientation = getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_PORTRAIT)
        {

            ConstraintLayout.LayoutParams params1 = (ConstraintLayout.LayoutParams)button1.getLayoutParams();
            params1.width=(getResources().getDisplayMetrics().widthPixels)/4;
            params1.height=((getResources().getDisplayMetrics().heightPixels/3))/4;
            button1.setLayoutParams(params1);

            ConstraintLayout.LayoutParams params2 = (ConstraintLayout.LayoutParams)button2.getLayoutParams();
            params2.width=(getResources().getDisplayMetrics().widthPixels)/4;
            params2.height=((getResources().getDisplayMetrics().heightPixels/3))/4;
            button2.setLayoutParams(params2);

            ConstraintLayout.LayoutParams params3 = (ConstraintLayout.LayoutParams)button3.getLayoutParams();
            params3.width=(getResources().getDisplayMetrics().widthPixels)/4;
            params3.height=((getResources().getDisplayMetrics().heightPixels/3))/4;
            button3.setLayoutParams(params3);

            ConstraintLayout.LayoutParams params4 = (ConstraintLayout.LayoutParams)button4.getLayoutParams();
            params4.width=(getResources().getDisplayMetrics().widthPixels)/4;
            params4.height=((getResources().getDisplayMetrics().heightPixels/3))/4;
            button4.setLayoutParams(params4);


            ConstraintLayout.LayoutParams params5 = (ConstraintLayout.LayoutParams)button5.getLayoutParams();
            params5.width=(getResources().getDisplayMetrics().widthPixels)/4;
            params5.height=((getResources().getDisplayMetrics().heightPixels/3))/4;
            button5.setLayoutParams(params5);

            ConstraintLayout.LayoutParams params6 = (ConstraintLayout.LayoutParams)button6.getLayoutParams();
            params6.width=(getResources().getDisplayMetrics().widthPixels)/4;
            params6.height=((getResources().getDisplayMetrics().heightPixels/3))/4;
            button6.setLayoutParams(params6);

            ConstraintLayout.LayoutParams params7 = (ConstraintLayout.LayoutParams)button7.getLayoutParams();
            params7.width=(getResources().getDisplayMetrics().widthPixels)/4;
            params7.height=((getResources().getDisplayMetrics().heightPixels/3))/4;
            button7.setLayoutParams(params7);

            ConstraintLayout.LayoutParams params8 = (ConstraintLayout.LayoutParams)button8.getLayoutParams();
            params8.width=(getResources().getDisplayMetrics().widthPixels)/4;
            params8.height=((getResources().getDisplayMetrics().heightPixels/3))/4;
            button8.setLayoutParams(params8);

            ConstraintLayout.LayoutParams params9 = (ConstraintLayout.LayoutParams)btn_back.getLayoutParams();
            params9.width=(getResources().getDisplayMetrics().widthPixels)/6;
            params9.height=((getResources().getDisplayMetrics().heightPixels/3))/6;
            btn_back.setLayoutParams(params9);


            ConstraintLayout.LayoutParams params10 = (ConstraintLayout.LayoutParams)btn_next.getLayoutParams();
            params10.width=(getResources().getDisplayMetrics().widthPixels)/6;
            params10.height=((getResources().getDisplayMetrics().heightPixels/3))/6;
            btn_next.setLayoutParams(params10);

        }
        else
        {

            button1.setText("S1 dost.: 10 zł");
            button2.setText("S1 dost.: 10 zł");


            button3.setText("S2 dost.: 15 zł");
            button4.setText("S2 dost.: 15 zł");

            button5.setText("S3 dost.: 20 zł");
            button6.setText("S3 dost.: 20 zł");

            button7.setText("S4 dost.: 30 zł");
            button8.setText("S4 dost.: 30 zł");


        }

       btn_next.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent i= new Intent(getApplicationContext(),ChooseSeatActivity.class);
               startActivity(i);
           }
       });


    }
}

