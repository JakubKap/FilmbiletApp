package com.companysf.filmbilet.Activity;

import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.companysf.filmbilet.Connection.ReservationConnection;
import com.companysf.filmbilet.Interfaces.ConnectionListener;
import com.companysf.filmbilet.Interfaces.SocketListener;
import com.companysf.filmbilet.Model.SectorModel;
import com.companysf.filmbilet.R;
import com.companysf.filmbilet.Utilies.ErrorDetector;
import com.companysf.filmbilet.Utilies.SQLiteHandler;
import com.companysf.filmbilet.Utilies.SessionManager;
import com.companysf.filmbilet.App.AppConfig;
import com.companysf.filmbilet.WebSocket.MyWebSocketListener;


import org.w3c.dom.Text;

import java.util.Locale;

import okhttp3.OkHttpClient;


public class SectorActivity extends AppCompatActivity implements SocketListener, ConnectionListener {

    private static final String logTag = MainActivity.class.getSimpleName();

    private SessionManager sManager;
    MyWebSocketListener myWebSocketListener;
    private SQLiteHandler db;
    private ErrorDetector ed;

    private int repertoireId;

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
    private ProgressBar secProgressBar;
    private FrameLayout secFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sector);

        sManager = new SessionManager(getApplicationContext());
        db = new SQLiteHandler(getApplicationContext());

        if (!sManager.isLoggedIn()) {
            logOutCustomer();
        }

        reservationConnection = new ReservationConnection(this, this);
        sectorModel = new SectorModel(8, 280);

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

        secProgressBar = findViewById(R.id.secProgressBar);
        secBtnReserve = findViewById(R.id.secBtnReserve);

       /* secFrameLayout = findViewById(R.id.secFrameLayout);
        secFrameLayout.getForeground().setAlpha(0);*/

        secChoosedPlaces = findViewById(R.id.secChoosedPlaces);
        secSummaryPrice = findViewById(R.id.secSummaryPrice);

        opensansRegular = Typeface.createFromAsset(getAssets(), getString(R.string.opensSansRegular));
        opensansBold = Typeface.createFromAsset(getAssets(), getString(R.string.opensSansBold));

        for (TextView freeSeat : freeSeats)
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

        for (TextView secLabel : secLabels)
            secLabel.setTypeface(opensansBold);

        TextView[] secPrices = new TextView[8];
        secPrices[0] = findViewById(R.id.sec1Price);
        secPrices[1] = findViewById(R.id.sec2Price);
        secPrices[2] = findViewById(R.id.sec3Price);
        secPrices[3] = findViewById(R.id.sec4Price);
        secPrices[4] = findViewById(R.id.sec5Price);
        secPrices[5] = findViewById(R.id.sec6Price);
        secPrices[6] = findViewById(R.id.sec7Price);
        secPrices[7] = findViewById(R.id.sec8Price);

        for (TextView secPrice : secPrices)
            secPrice.setTypeface(opensansRegular);

        TextView choosedPlacesText = findViewById(R.id.choosedPlacesText);
        choosedPlacesText.setTypeface(opensansRegular);

        TextView priceLabel = findViewById(R.id.priceLabel);
        priceLabel.setTypeface(opensansRegular);

        sectorModel.assignRowToSeat();
        sectorModel.assignSectorToSeat();

        Bundle b = getIntent().getExtras();
        repertoireId = b.getInt(getString(R.string.scheduleId));
        reservationConnection.getReservations(repertoireId);

        myWebSocketListener = new MyWebSocketListener(this);
    }

    private void logOutCustomer() {
        sManager.setLogin(false);
        db.deleteCustomers();
    }

    @Override
    public void onOpenCallback(String result) {
        Log.d(logTag, "onOpen");

        for (int i = 0; i < sectorButtons.length; i++) {
            final int index = i;
            sectorButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Button btn = findViewById(view.getId());
                    Log.d(logTag, "index buttona = " + index);

                    if(sectorModel.getFreeSeatsInSector()[index] == 0) {
                        ed.buildDialog(SectorActivity.this, getString(R.string.emptySectorTitle),
                                getString(R.string.emptySectorMsg)).show();
                        return;
                    }

                    final Dialog dialog = new Dialog(SectorActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.seat);

                    seatButtons[0] = findViewById(R.id.seatsButtonIR_1);
                    seatButtons[1] = findViewById(R.id.seatsButtonIR_2);
                    seatButtons[2] = findViewById(R.id.seatsButtonIR_3);
                    seatButtons[3] = findViewById(R.id.seatsButtonIR_4);
                    seatButtons[4] = findViewById(R.id.seatsButtonIR_5);
                    seatButtons[5] = findViewById(R.id.seatsButtonIR_6);
                    seatButtons[6] = findViewById(R.id.seatsButtonIR_7);

                    seatButtons[7] = findViewById(R.id.seatsButtonIIR_1);
                    seatButtons[8] = findViewById(R.id.seatsButtonIIR_2);
                    seatButtons[9] = findViewById(R.id.seatsButtonIIR_3);
                    seatButtons[10] = findViewById(R.id.seatsButtonIIR_4);
                    seatButtons[11] = findViewById(R.id.seatsButtonIIR_5);
                    seatButtons[12] = findViewById(R.id.seatsButtonIIR_6);
                    seatButtons[13] = findViewById(R.id.seatsButtonIIR_7);

                    seatButtons[14] = findViewById(R.id.seatsButtonIIIR_1);
                    seatButtons[15] = findViewById(R.id.seatsButtonIIIR_2);
                    seatButtons[16] = findViewById(R.id.seatsButtonIIIR_3);
                    seatButtons[17] = findViewById(R.id.seatsButtonIIIR_4);
                    seatButtons[18] = findViewById(R.id.seatsButtonIIIR_5);
                    seatButtons[19] = findViewById(R.id.seatsButtonIIIR_6);
                    seatButtons[20] = findViewById(R.id.seatsButtonIIIR_7);

                    seatButtons[21] = findViewById(R.id.seatsButtonIVR_1);
                    seatButtons[22] = findViewById(R.id.seatsButtonIVR_2);
                    seatButtons[23] = findViewById(R.id.seatsButtonIVR_3);
                    seatButtons[24] = findViewById(R.id.seatsButtonIVR_4);
                    seatButtons[25] = findViewById(R.id.seatsButtonIVR_5);
                    seatButtons[26] = findViewById(R.id.seatsButtonIVR_6);
                    seatButtons[27] = findViewById(R.id.seatsButtonIVR_7);

                    seatButtons[28] = findViewById(R.id.seatsButtonVR_1);
                    seatButtons[29] = findViewById(R.id.seatsButtonVR_2);
                    seatButtons[30] = findViewById(R.id.seatsButtonVR_3);
                    seatButtons[31] = findViewById(R.id.seatsButtonVR_4);
                    seatButtons[32] = findViewById(R.id.seatsButtonVR_5);
                    seatButtons[33] = findViewById(R.id.seatsButtonVR_6);
                    seatButtons[34] = findViewById(R.id.seatsButtonVR_7);

                    dialog.show();
                }
            });
        }
    }

    @Override
    public void onDbResponseCallback(boolean[] takenSeats) {
        sectorModel.setChoosedSeats(takenSeats);
        sectorModel.updateSectorSeats();
        for (int i = 0; i < sectorModel.getChoosedSeats().length; i++)
            Log.d(logTag, "Model choosedSeats = " + sectorModel.getChoosedSeats()[i]);

        updateSectors(true);
    }

    public void updateSectors(boolean ifStart) {
        final boolean start = ifStart;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (start) {
                    secProgressBar.setVisibility(View.INVISIBLE);
                    secChoosedPlaces.setVisibility(View.VISIBLE);
                    secSummaryPrice.setVisibility(View.VISIBLE);
                    String pom = getString(R.string.summaryStartText);
                    secSummaryPrice.setText(pom);
                }
                for(int i=0; i<freeSeats.length; i++)
                    freeSeats[i].setText(String.format(new Locale("pl", "PL"), "%d",
                            sectorModel.getFreeSeatsInSector()[i]));

            }
        });

    }

}



