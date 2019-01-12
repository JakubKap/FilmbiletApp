package com.companysf.filmbilet.activity;

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
import com.companysf.filmbilet.R;
import com.companysf.filmbilet.addition.ConnectionDetector;
import com.companysf.filmbilet.addition.SQLiteHandler;
import com.companysf.filmbilet.addition.SessionManager;
import com.companysf.filmbilet.app.AppConfig;
import com.companysf.filmbilet.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private static final String logTag = RegisterActivity.class.getSimpleName();
    private EditText inputName;
    private EditText inputSurname;
    private EditText inputPassword;
    private EditText inputEmail;
    private ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Views
        Button loginBtn = findViewById(R.id.btn_login);
        Button registerBtn = findViewById(R.id.btn_register);
        inputEmail = findViewById(R.id.email);
        inputName = findViewById(R.id.name);
        inputPassword = findViewById(R.id.password);
        inputSurname = findViewById(R.id.surname);

        SessionManager sManager = new SessionManager(getApplicationContext());
        SQLiteHandler db = new SQLiteHandler(getApplicationContext());
        cd = new ConnectionDetector(this);

        //font
        Typeface opensansRegular = Typeface.createFromAsset(getAssets(), "opensans_regular.ttf");
        Typeface opensansBold = Typeface.createFromAsset(getAssets(), "opensans_bold.ttf");

        registerBtn.setTypeface(opensansBold);
        loginBtn.setTypeface(opensansBold);

        if(sManager.isLoggedIn()){
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateEmail();
            }
        });

        inputPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //on enter click
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
                    validateEmail();
                    return true;
                }
                return false;
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

    private void validateEmail() {
        if (cd.connected()){
            String name = inputName.getText().toString().trim();
            String surname = inputSurname.getText().toString().trim();
            String email = inputEmail.getText().toString().trim();
            String password = inputPassword.getText().toString().trim();

            if (!isEmail(email)){
                Toast.makeText(getApplicationContext(),
                        "Wprowadzony email nie jest poprawny!", Toast.LENGTH_LONG)
                        .show();
            } else if (name.isEmpty() || surname.isEmpty() || password.isEmpty()) {
                Toast.makeText(getApplicationContext(),
                        "Proszę uzupełnić wszystkie pola!", Toast.LENGTH_LONG)
                        .show();
            } else {
                registerUser(name, surname, email, password);
            }
        } else{
            cd.buildDialog(RegisterActivity.this, "Błąd połączenia internetowego", "Potrzebujesz dostępu do internetu, żeby móc się zalogować").show();
        }
    }

    boolean isEmail(String email){
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
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
                                Toast.makeText(
                                        getApplicationContext(),
                                        "Błąd serwera",
                                        Toast.LENGTH_SHORT).show();
                            } else{
                                Toast.makeText(
                                        getApplicationContext(),
                                        "Zostałeś pomyślnie zarejestrowany. Możesz się teraz zalogować",
                                        Toast.LENGTH_LONG).show();

                                //toggle to LoginActivity
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Błąd serwera: ",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(logTag, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Błąd serwera", Toast.LENGTH_LONG).show();
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
