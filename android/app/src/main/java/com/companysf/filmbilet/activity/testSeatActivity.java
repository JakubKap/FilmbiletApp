package com.companysf.filmbilet.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.companysf.filmbilet.R;

public class testSeatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_seat);

        Button buttonIR_1 = findViewById(R.id.buttonIR_1);

        buttonIR_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(testSeatActivity.this, "Button1", Toast.LENGTH_LONG).show();
            }
        });
    }

}
