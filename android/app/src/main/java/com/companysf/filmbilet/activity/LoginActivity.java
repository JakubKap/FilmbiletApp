package com.companysf.filmbilet.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.companysf.filmbilet.R;
import com.companysf.filmbilet.addition.SQLiteHandler;
import com.companysf.filmbilet.addition.SessionManager;
import com.companysf.filmbilet.app.AppConfig;
import com.companysf.filmbilet.app.AppController;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getActionBar().hide();

        Button loginBtn = findViewById(R.id.btn_login);
        Button registerBtn = findViewById(R.id.btn_register);
        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
        sManager = new SessionManager(getApplicationContext());
        db = new SQLiteHandler(getApplicationContext());

        //if user is already logged in
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
                if (!email.isEmpty() && !password.isEmpty()) {
                    // login user
                    checkLogin(email, password);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Proszę wprowadzić E-mail i hasło ", Toast.LENGTH_LONG)
                            .show();
                }
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
        //volley string Request
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                AppConfig.LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(logTag, "Login response: " + response.toString());
                try {
                    JSONObject json = new JSONObject(response);
                    boolean error = json.getBoolean("error");
                    if (error){
                        Toast.makeText(getApplicationContext(), json.getString("message"),
                                Toast.LENGTH_SHORT).show();
                    } else{
                        sManager.setLogin(true);

                        //add fields from MySQL to SQLite
                        JSONObject customer = json.getJSONObject("customer");
                        db.addCustomer(
                                customer.getString("name"),
                                customer.getString("surname"),
                                customer.getString("email"),
                                json.getString("uid"));

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Błąd Json: " + e.getMessage(), Toast.LENGTH_LONG).show();
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
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, "req_login");
    }
}
