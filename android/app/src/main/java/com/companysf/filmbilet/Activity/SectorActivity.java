package com.companysf.filmbilet.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.companysf.filmbilet.Connection.ReservationConnection;
import com.companysf.filmbilet.Model.SectorModel;
import com.companysf.filmbilet.R;
import com.companysf.filmbilet.Utilies.ErrorDetector;
import com.companysf.filmbilet.Utilies.SQLiteHandler;
import com.companysf.filmbilet.Utilies.SessionManager;
import com.companysf.filmbilet.App.AppConfig;
import com.companysf.filmbilet.App.AppController;
import com.companysf.filmbilet.Entities.Reservation;
import com.companysf.filmbilet.WebSocket.Message;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import java.util.LinkedHashMap;

import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.OkHttpClient;

import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;


public class SectorActivity extends AppCompatActivity {

    private static final String logTag = MainActivity.class.getSimpleName();
    private SessionManager sManager;
    private SQLiteHandler db;
    private ErrorDetector ed;

    private ReservationConnection reservationConnection;
    private SectorModel sectorModel;

    private Button[] sectorButtons;
    private Button[] seatButtons;
    private Button[] columnButtons;
    private Button[] rowButtons;

    private Button seatsApproveButton, secBtnReserve, seatsCloseButton, btn;

    private TextView secChoosedPlaces, secSummaryPrice;

    private TextView[] freeSeats;

    Typeface opensansRegular;
    Typeface opensansBold;

    private ConstraintLayout constraintLayout;
    private ProgressBar SecProgressBar;
    private FrameLayout secFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sManager = new SessionManager(getApplicationContext());
        db = new SQLiteHandler(getApplicationContext());

        if (!sManager.isLoggedIn()) {
            logOutCustomer();
        }

        setContentView(R.layout.sector);

        reservationConnection = new ReservationConnection(getApplicationContext(), this);
        sectorModel = new SectorModel(8,280);

        ed = new ErrorDetector(this);

        sectorButtons = new Button[8];
        seatButtons = new Button[35];
        columnButtons = new Button[8];
        rowButtons = new Button[5];
        freeSeats = new TextView[8];

        constraintLayout = findViewById(R.id.constraintLayout);

        sectorButtons[0] = findViewById(R.id.secButton1);
        sectorButtons[1] = findViewById(R.id.secButton2);
        sectorButtons[2] = findViewById(R.id.secButton3);
        sectorButtons[3] = findViewById(R.id.secButton4);
        sectorButtons[4] = findViewById(R.id.secButton5);
        sectorButtons[5] = findViewById(R.id.secButton6);
        sectorButtons[6] = findViewById(R.id.secButton7);
        sectorButtons[7] = findViewById(R.id.secButton8);

        freeSeats[0] = findViewById(R.id.freeSeats1);
        freeSeats[1] = findViewById(R.id.freeSeats2);
        freeSeats[2] = findViewById(R.id.freeSeats3);
        freeSeats[3] = findViewById(R.id.freeSeats4);
        freeSeats[4] = findViewById(R.id.freeSeats5);
        freeSeats[5] = findViewById(R.id.freeSeats6);
        freeSeats[6] = findViewById(R.id.freeSeats7);
        freeSeats[7] = findViewById(R.id.freeSeats8);

        SecProgressBar = findViewById(R.id.secProgressBar);
        secBtnReserve = findViewById(R.id.secBtnReserve);

        secFrameLayout = findViewById(R.id.secFrameLayout);
        secFrameLayout.getForeground().setAlpha(0);

        secChoosedPlaces = findViewById(R.id.secChoosedPlaces);
        secSummaryPrice = findViewById(R.id.secSummaryPrice);

        opensansRegular = Typeface.createFromAsset(getAssets(), getString(R.string.opensSansRegular));
        opensansBold = Typeface.createFromAsset(getAssets(), getString(R.string.opensSansBold));

        for(TextView freeSeat : freeSeats)
            freeSeat.setTypeface(opensansRegular);

        secBtnReserve.setTypeface(opensansBold);

        secChoosedPlaces.setTypeface(opensansBold);

        secSummaryPrice.setTypeface(opensansBold);

        TextView stepNumber2 = findViewById(R.id.stepNumber2);
        stepNumber2.setTypeface(opensansBold);

        TextView selectSectorText = findViewById(R.id.selectSectorText);
        selectSectorText.setTypeface(opensansBold);

        TextView[] secLabels = new TextView[8];
        secLabels[0] = findViewById(R.id.sec1Label);
        secLabels[1] = findViewById(R.id.sec2Label);
        secLabels[2] = findViewById(R.id.sec3Label);
        secLabels[3] = findViewById(R.id.sec4Label);
        secLabels[4] = findViewById(R.id.sec5Label);
        secLabels[5] = findViewById(R.id.sec6Label);
        secLabels[6] = findViewById(R.id.sec7Label);
        secLabels[7] = findViewById(R.id.sec8Label);

        for(TextView secLabel : secLabels)
            secLabel.setTypeface(opensansBold);

        TextView[] secPrices = new TextView[8];
        secPrices[0]=findViewById(R.id.sec1Price);
        secPrices[1]=findViewById(R.id.sec2Price);
        secPrices[2]=findViewById(R.id.sec3Price);
        secPrices[3]=findViewById(R.id.sec4Price);
        secPrices[4]=findViewById(R.id.sec5Price);
        secPrices[5]=findViewById(R.id.sec6Price);
        secPrices[6]=findViewById(R.id.sec7Price);
        secPrices[7]=findViewById(R.id.sec8Price);

        for(TextView secPrice : secPrices)
            secPrice.setTypeface(opensansRegular);

        TextView choosedPlacesText = findViewById(R.id.choosedPlacesText);
        choosedPlacesText.setTypeface(opensansRegular);

        TextView priceLabel = findViewById(R.id.priceLabel);
        priceLabel.setTypeface(opensansRegular);

        sectorModel.assignRowToSeat();


    }


    private void logOutCustomer() {
        sManager.setLogin(false);
        db.deleteCustomers();
    }
}



