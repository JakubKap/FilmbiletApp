package com.companysf.filmbilet.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.companysf.filmbilet.connection.Listener.EmptyFieldsListener;
import com.companysf.filmbilet.connection.Listener.ErrorListener;
import com.companysf.filmbilet.services.Login;
import com.companysf.filmbilet.R;
import com.companysf.filmbilet.utils.ErrorDialog;
import com.companysf.filmbilet.utils.ToastUtils;

public class LoginActivity extends AppCompatActivity implements ErrorListener, EmptyFieldsListener {
    private EditText inputEmail;
    private EditText inputPassword;
    private ErrorDialog errorDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginBtn = findViewById(R.id.btnLogin);
        Button registerBtn = findViewById(R.id.btnRegister);
        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
        errorDialog = new ErrorDialog(this);
        final Login login = new Login(this, this, this);

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(inputEmail.getWindowToken(), 0);

        Typeface opensansBold = Typeface.createFromAsset(getAssets(), getString(R.string.opensSansBold));
        loginBtn.setTypeface(opensansBold);
        registerBtn.setTypeface(opensansBold);

        if (login.userIsLoggedIn()) {
            switchToMainActivity();
        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                login.loginCustomer(email, password);
            }
        });
        inputPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
                    String email = inputEmail.getText().toString().trim();
                    String password = inputPassword.getText().toString().trim();
                    login.loginCustomer(email, password);
                    return true;
                }
                return false;
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToRegisterActivity();
            }
        });
    }

    private void switchToRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    private void switchToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void callBackOnError() {
        ToastUtils.showLongToast(this, getString(R.string.serverErrorTitle));
    }

    @Override
    public void callBackOnNoNetwork() {
        errorDialog.showErrorDialog(
                getString(R.string.networkConnectionErrorTitle),
                getString(R.string.loginNetworkConnectionErrorMsg)
        );
    }

    @Override
    public void callBackOnEmptyField() {
        ToastUtils.showLongToast(this, getString(R.string.insertEmailAndPasswordPrompt));
    }
}
