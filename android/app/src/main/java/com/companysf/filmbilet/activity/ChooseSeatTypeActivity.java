package com.companysf.filmbilet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.companysf.filmbilet.R;

public class ChooseSeatTypeActivity extends AppCompatActivity {

    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_seat_type);

        btnNext = (Button) findViewById(R.id.btnNext);

       btnNext.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent i= new Intent(getApplicationContext(),ChooseSeatActivity.class);
               startActivity(i);
           }
       });


    }
}

