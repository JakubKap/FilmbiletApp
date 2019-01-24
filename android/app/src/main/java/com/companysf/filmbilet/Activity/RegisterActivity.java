package com.companysf.filmbilet.Activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.companysf.filmbilet.Connection.Listener.EmptyEmailListener;
import com.companysf.filmbilet.Connection.Listener.ErrorListener;
import com.companysf.filmbilet.Connection.Listener.Listener;
import com.companysf.filmbilet.Model.Login;
import com.companysf.filmbilet.Model.Register;
import com.companysf.filmbilet.R;
import com.companysf.filmbilet.Utilies.ConnectionDetector;
import com.companysf.filmbilet.Utilies.ErrorDialog;
import com.companysf.filmbilet.Utilies.SessionManager;
import com.companysf.filmbilet.App.AppConfig;
import com.companysf.filmbilet.App.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements Listener, EmptyEmailListener {
    private EditText inputName;
    private EditText inputSurname;
    private EditText inputPassword;
    private EditText inputEmail;
    private ErrorDialog errorDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button loginBtn = findViewById(R.id.btn_login);
        Button registerBtn = findViewById(R.id.btn_register);
        inputEmail = findViewById(R.id.email);
        inputName = findViewById(R.id.name);
        inputPassword = findViewById(R.id.password);
        inputSurname = findViewById(R.id.surname);

        Login login = new Login(getApplicationContext());
        final Register register = new Register(getApplicationContext(), this, this);
        errorDialog = new ErrorDialog(this);

        Typeface opensansBold = Typeface.createFromAsset(getAssets(), getString(R.string.opensSansBold));
        registerBtn.setTypeface(opensansBold);
        loginBtn.setTypeface(opensansBold);

        if (login.userIsLoggedIn()) {
            switchToMainActivity();
        }

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register.validateEmail(
                        inputName.getText().toString().trim(),
                        inputSurname.getText().toString().trim(),
                        inputEmail.getText().toString().trim(),
                        inputPassword.getText().toString().trim()
                );
            }
        });

        inputPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    register.validateEmail(
                            inputName.getText().toString().trim(),
                            inputSurname.getText().toString().trim(),
                            inputEmail.getText().toString().trim(),
                            inputPassword.getText().toString().trim()
                    );
                    return true;
                }
                return false;
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToLoginActivity();
            }
        });
    }

    private void switchToMainActivity() {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void switchToLoginActivity() {
        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void callBackOnError() {
        Toast.makeText(
                getApplicationContext(),
                getString(R.string.serverErrorTitle),
                Toast.LENGTH_SHORT
        ).show();
    }

    @Override
    public void callBackOnNoNetwork() {
        errorDialog.showErrorDialog(
                getString(R.string.networkConnectionErrorTitle),
                getString(R.string.RegisterNetworkConnectionErrorMsg)
        );
    }

    @Override
    public void callBackOnSuccess() {
        Toast.makeText(
                getApplicationContext(),
                getString(R.string.registerSuccessToast),
                Toast.LENGTH_LONG).show();
        switchToLoginActivity();
    }

    @Override
    public void callBackOnEmptyEmail() {
        Toast.makeText(getApplicationContext(),
                getString(R.string.notValidEmailMsg), Toast.LENGTH_LONG
        ).show();
    }

    @Override
    public void callBackOnEmptyField() {
        Toast.makeText(getApplicationContext(),
                getString(R.string.emptyRegisterFieldsMsg), Toast.LENGTH_LONG
        ).show();
    }
}
