package com.companysf.filmbilet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.companysf.filmbilet.R;
import com.companysf.filmbilet.addition.SessionManager;
import com.companysf.filmbilet.app.AppConfig;
import com.companysf.filmbilet.app.AppController;
import com.companysf.filmbilet.appLogic.Customer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private static final String logTag = RegisterActivity.class.getSimpleName();
    private Button loginBtn;
    private Button registerBtn;
    private EditText inputName;
    private EditText inputSurname;
    private EditText inputPassword;
    private EditText inputEmail;
    private SessionManager sManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getActionBar().hide();

        loginBtn = findViewById(R.id.btn_login);
        registerBtn = findViewById(R.id.btn_register);
        inputEmail = findViewById(R.id.email);
        inputName = findViewById(R.id.name);
        inputPassword = findViewById(R.id.password);
        inputSurname = findViewById(R.id.surname);
        sManager = new SessionManager(getApplicationContext());

        if(sManager.isLoggedIn()){
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = inputName.getText().toString().trim();
                String surname = inputSurname.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (!name.isEmpty() && !surname.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    registerUser(name, surname, email, password);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Proszę uzupełnić wszystkie pola!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void registerUser(final String name, final String surname, final String email, final String password) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                AppConfig.REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(logTag, "Register request: " + response);
                        try {
                            JSONObject json = new JSONObject(response);
                            boolean error = json.getBoolean("error");
                            if (error){
                                Toast.makeText(getApplicationContext(), json.getString("message"),
                                        Toast.LENGTH_SHORT).show();
                            } else{

                                //TODO dodanie uniqueID klienta do bazy sqLite
//                                add fields from DB to class fields
//                                JSONObject customer = json.getJSONObject("customer");
//                                new Customer(customer.getString("name"),
//                                        customer.getString("surname"), json.getString("uid"));

                                Toast.makeText(getApplicationContext(), "Zostałeś pomyślnie zarejestrowany. Możesz się teraz zalogować", Toast.LENGTH_LONG).show();

                                //toggle to LoginActivity
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(logTag, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("surname", surname);
                params.put("email", email);
                params.put("password", password);

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, "req_register");
    }
}
