package com.companysf.filmbilet.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.companysf.filmbilet.R;
import com.companysf.filmbilet.addition.SQLiteHandler;
import com.companysf.filmbilet.addition.SessionManager;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private SessionManager sManager;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sManager = new SessionManager(getApplicationContext());

        if (!sManager.isLoggedIn()) {
            logOutCustomer();
        }

        Button btn_logout = findViewById(R.id.btn_logout);
        TextView customerInfo = findViewById(R.id.customer_info);

        HashMap<String, String> customer = db.getCustomer();
        String name = customer.get("name");
        String surname = customer.get("surname");
        String email = customer.get("email");
        customerInfo.setText(name + surname + email);

        db = new SQLiteHandler(getApplicationContext());

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOutCustomer();
            }
        });
    }

    private void logOutCustomer(){
        sManager.setLogin(false);

        db.deleteCustomers();

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
