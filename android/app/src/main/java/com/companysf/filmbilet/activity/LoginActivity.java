package com.companysf.filmbilet.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.companysf.filmbilet.R;
import com.companysf.filmbilet.addition.SessionManager;

public class LoginActivity extends AppCompatActivity {
    private Button loginBtn;
    private Button registerButton;
    private EditText inputEmail;
    private EditText inputName;
    private EditText inputSurname;
    private SessionManager sManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getActionBar().hide();
    }
}
