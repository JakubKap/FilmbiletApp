package com.companysf.filmbilet.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.companysf.filmbilet.R;
import com.companysf.filmbilet.Utilies.ConnectionDetector;
import com.companysf.filmbilet.Utilies.SQLiteHandler;
import com.companysf.filmbilet.Utilies.SessionManager;
import com.companysf.filmbilet.App.AppConfig;
import com.companysf.filmbilet.App.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private EditText inputEmail;
    private EditText inputPassword;
    private SessionManager sManager;
    private static final String logTag = LoginActivity.class.getSimpleName();
    private SQLiteHandler db;
    private ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginBtn = findViewById(R.id.btn_login);
        Button registerBtn = findViewById(R.id.btn_register);
        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
        sManager = new SessionManager(getApplicationContext());
        db = new SQLiteHandler(getApplicationContext());
        cd = new ConnectionDetector(this);

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(inputEmail.getWindowToken(), 0);

        Typeface opensansBold = Typeface.createFromAsset(getAssets(), getString(R.string.opensSansBold));
        loginBtn.setTypeface(opensansBold);
        registerBtn.setTypeface(opensansBold);

        if (sManager.isLoggedIn()) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                if (cd.connected()){
                    if (!email.isEmpty() && !password.isEmpty()) {
                        checkLogin(email, password);
                    } else {
                        Toast.makeText(getApplicationContext(),
                                getString(R.string.insertEmailAndPasswordPrompt), Toast.LENGTH_LONG)
                                .show();
                    }
                } else {
                    cd.buildDialog(LoginActivity.this,
                            getString(R.string.networkConnectionErrorTitle),
                            getString(R.string.loginNetworkConnectionErrorMsg)
                    ).show();
                }
            }
        });

        inputPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
                    if (cd.connected()){
                        if (!email.isEmpty() && !password.isEmpty()) {
                            checkLogin(email, password);
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    getString(R.string.insertEmailAndPasswordPrompt), Toast.LENGTH_LONG)
                                    .show();
                        }
                    } else {
                        cd.buildDialog(LoginActivity.this, getString(R.string.networkConnectionErrorTitle), getString(R.string.loginNetworkConnectionErrorMsg)).show();
                    }
                }
                return false;
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void checkLogin(final String email, final String password) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                AppConfig.LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(logTag, "Login response: " + response);
                try {
                    JSONObject json = new JSONObject(response);
                    boolean error = json.getBoolean(getString(R.string.error));
                    if (error){
                        Toast.makeText(getApplicationContext(), json.getString(getString(R.string.message)),
                                Toast.LENGTH_SHORT).show();
                    } else{
                        sManager.setLogin(true);

                        JSONObject customer = json.getJSONObject(getString(R.string.customerJsonName));
                        Log.d(logTag, "customerName: " + customer.getString("name") + "i jego id: " + Integer.toString(customer.getInt("id")));
                        db.addCustomer(
                                customer.getString(getString(R.string.jsonCustomerName)),
                                customer.getString(getString(R.string.jsonCustomerSurname)),
                                customer.getString(getString(R.string.jsonCustomerEmail)),
                                Integer.toString(customer.getInt(getString(R.string.jsonCustomerId))));

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), getString(R.string.serverErrorTitle), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(logTag, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(getString(R.string.loginPutReqEmailParam), email);
                params.put(getString(R.string.loginPutReqPasswordParam), password);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, getString(R.string.getReservationsRequestAdd));
    }


}
