package com.companysf.filmbilet.activity;

import android.app.Dialog;
import android.content.Intent;
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

import com.companysf.filmbilet.connection.Listener.ErrorListener;
import com.companysf.filmbilet.connection.ReservationConnection;
import com.companysf.filmbilet.interfaces.ConnectionListener;
import com.companysf.filmbilet.interfaces.SocketListener;
import com.companysf.filmbilet.services.Login;
import com.companysf.filmbilet.services.SectorModel;
import com.companysf.filmbilet.R;
import com.companysf.filmbilet.utils.ErrorDetector;
import com.companysf.filmbilet.utils.ErrorDialog;
import com.companysf.filmbilet.utils.SQLiteHandler;
import com.companysf.filmbilet.utils.SessionManager;
import com.companysf.filmbilet.WebSocket.MyWebSocketListener;
import com.companysf.filmbilet.utils.ToastUtils;


import java.util.Locale;


public class SectorActivity extends AppCompatActivity implements ErrorListener, SocketListener, ConnectionListener {

    private static final String logTag = MainActivity.class.getSimpleName();

    MyWebSocketListener myWebSocketListener;
    private Login login;

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
    private ProgressBar seatsProgressBar;
    private TextView title, subtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sector);

        login = new Login(this);
        if (!login.userIsLoggedIn()) {
            switchToLoginActivity();
        }

        reservationConnection = new ReservationConnection(this, this, this);
        sectorModel = new SectorModel(this,8, 280);

        sectorButtons = new Button[8];
        seatButtons = new Button[35];
        columnButtons = new Button[8];
        rowButtons = new Button[5];
        freeSeats = new TextView[8];

        //TODO sprawdzić, czy potrzebne
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

        secChoosedPlaces = findViewById(R.id.secChoosedPlaces);
        secSummaryPrice = findViewById(R.id.secSummaryPrice);

        TextView stepNumber2 = findViewById(R.id.stepNumber2);

        TextView selectSectorText = findViewById(R.id.selectSectorText);

        TextView[] secLabels = new TextView[8];
        secLabels[0] = findViewById(R.id.sec1Label);
        secLabels[1] = findViewById(R.id.sec2Label);
        secLabels[2] = findViewById(R.id.sec3Label);
        secLabels[3] = findViewById(R.id.sec4Label);
        secLabels[4] = findViewById(R.id.sec5Label);
        secLabels[5] = findViewById(R.id.sec6Label);
        secLabels[6] = findViewById(R.id.sec7Label);
        secLabels[7] = findViewById(R.id.sec8Label);

        TextView[] secPrices = new TextView[8];
        secPrices[0] = findViewById(R.id.sec1Price);
        secPrices[1] = findViewById(R.id.sec2Price);
        secPrices[2] = findViewById(R.id.sec3Price);
        secPrices[3] = findViewById(R.id.sec4Price);
        secPrices[4] = findViewById(R.id.sec5Price);
        secPrices[5] = findViewById(R.id.sec6Price);
        secPrices[6] = findViewById(R.id.sec7Price);
        secPrices[7] = findViewById(R.id.sec8Price);

        TextView choosedPlacesText = findViewById(R.id.choosedPlacesText);

        TextView priceLabel = findViewById(R.id.priceLabel);
        priceLabel.setTypeface(opensansRegular);

        opensansRegular = Typeface.createFromAsset(getAssets(), getString(R.string.opensSansRegular));
        opensansBold = Typeface.createFromAsset(getAssets(), getString(R.string.opensSansBold));

        for (TextView freeSeat : freeSeats)
            freeSeat.setTypeface(opensansRegular);

        secBtnReserve.setTypeface(opensansBold);
        secChoosedPlaces.setTypeface(opensansBold);
        secSummaryPrice.setTypeface(opensansBold);

        for (TextView secLabel : secLabels)
            secLabel.setTypeface(opensansBold);

        stepNumber2.setTypeface(opensansBold);
        selectSectorText.setTypeface(opensansBold);

        for (TextView secPrice : secPrices)
            secPrice.setTypeface(opensansRegular);

        choosedPlacesText.setTypeface(opensansRegular);

        sectorModel.assignRowToSeat();
        sectorModel.assignSectorToSeat();

        Bundle b = getIntent().getExtras();
        int repertoireId = b.getInt(getString(R.string.scheduleId));
        reservationConnection.getReservations(repertoireId);
        sectorModel.setRepertoireId(repertoireId);
        myWebSocketListener = new MyWebSocketListener(this);
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
                        ErrorDialog.showErrorDialog(getApplicationContext(), getString(R.string.emptySectorTitle), getString(R.string.emptySectorMsg));
                        return;
                    }

                    final Dialog dialog = new Dialog(SectorActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.seat);

                    seatButtons[0] = dialog.findViewById(R.id.seatsButtonIR1);
                    seatButtons[1] = dialog.findViewById(R.id.seatsButtonIR2);
                    seatButtons[2] = dialog.findViewById(R.id.seatsButtonIR3);
                    seatButtons[3] = dialog.findViewById(R.id.seatsButtonIR4);
                    seatButtons[4] = dialog.findViewById(R.id.seatsButtonIR5);
                    seatButtons[5] = dialog.findViewById(R.id.seatsButtonIR6);
                    seatButtons[6] = dialog.findViewById(R.id.seatsButtonIR7);

                    seatButtons[7] = dialog.findViewById(R.id.seatsButtonIIR1);
                    seatButtons[8] = dialog.findViewById(R.id.seatsButtonIIR2);
                    seatButtons[9] = dialog.findViewById(R.id.seatsButtonIIR3);
                    seatButtons[10] = dialog.findViewById(R.id.seatsButtonIIR4);
                    seatButtons[11] = dialog.findViewById(R.id.seatsButtonIIR5);
                    seatButtons[12] = dialog.findViewById(R.id.seatsButtonIIR6);
                    seatButtons[13] = dialog.findViewById(R.id.seatsButtonIIR7);

                    seatButtons[14] = dialog.findViewById(R.id.seatsButtonIIIR1);
                    seatButtons[15] = dialog.findViewById(R.id.seatsButtonIIIR2);
                    seatButtons[16] = dialog.findViewById(R.id.seatsButtonIIIR3);
                    seatButtons[17] = dialog.findViewById(R.id.seatsButtonIIIR4);
                    seatButtons[18] = dialog.findViewById(R.id.seatsButtonIIIR5);
                    seatButtons[19] = dialog.findViewById(R.id.seatsButtonIIIR6);
                    seatButtons[20] = dialog.findViewById(R.id.seatsButtonIIIR7);

                    seatButtons[21] = dialog.findViewById(R.id.seatsButtonIVR1);
                    seatButtons[22] = dialog.findViewById(R.id.seatsButtonIVR2);
                    seatButtons[23] = dialog.findViewById(R.id.seatsButtonIVR3);
                    seatButtons[24] = dialog.findViewById(R.id.seatsButtonIVR4);
                    seatButtons[25] = dialog.findViewById(R.id.seatsButtonIVR5);
                    seatButtons[26] = dialog.findViewById(R.id.seatsButtonIVR6);
                    seatButtons[27] = dialog.findViewById(R.id.seatsButtonIVR7);

                    seatButtons[28] = dialog.findViewById(R.id.seatsButtonVR1);
                    seatButtons[29] = dialog.findViewById(R.id.seatsButtonVR2);
                    seatButtons[30] = dialog.findViewById(R.id.seatsButtonVR3);
                    seatButtons[31] = dialog.findViewById(R.id.seatsButtonVR4);
                    seatButtons[32] = dialog.findViewById(R.id.seatsButtonVR5);
                    seatButtons[33] = dialog.findViewById(R.id.seatsButtonVR6);
                    seatButtons[34] = dialog.findViewById(R.id.seatsButtonVR7);

                    seatsApproveButton = dialog.findViewById(R.id.seatsApproveButton);
                    seatsCloseButton = dialog.findViewById(R.id.seatsCloseButton);

                    title = dialog.findViewById(R.id.title);
                    subtitle = dialog.findViewById(R.id.subtitle);

                    columnButtons[0] = dialog.findViewById(R.id.seatsButton1C);
                    columnButtons[1] = dialog.findViewById(R.id.seatsButton2C);
                    columnButtons[2] = dialog.findViewById(R.id.seatsButton3C);
                    columnButtons[3] = dialog.findViewById(R.id.seatsButton4C);
                    columnButtons[4] = dialog.findViewById(R.id.seatsButton5C);
                    columnButtons[5] = dialog.findViewById(R.id.seatsButton6C);
                    columnButtons[6] = dialog.findViewById(R.id.seatsButton7C);

                    rowButtons[0] = dialog.findViewById(R.id.seatsButtonIR);
                    rowButtons[1] = dialog.findViewById(R.id.seatsButtonIIR);
                    rowButtons[2] = dialog.findViewById(R.id.seatsButtonIIIR);
                    rowButtons[3] = dialog.findViewById(R.id.seatsButtonIVR);
                    rowButtons[4] = dialog.findViewById(R.id.seatsButtonVR);

                    seatsProgressBar = dialog.findViewById(R.id.seatsProgressBar);

                            title.setTypeface(opensansBold);
                    subtitle.setTypeface(opensansBold);
                    seatsCloseButton.setTypeface(opensansRegular);
                    seatsApproveButton.setTypeface(opensansRegular);

                    preparePopUp(index);

                    dialog.show();
                }
            });
        }
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

    public void preparePopUp(int index){
        seatsProgressBar.setVisibility(View.INVISIBLE);
        title.setText(sectorModel.getSectorTitles()[index]);
        subtitle.setText(sectorModel.sectorSubtitle(index));
        int [] seatNumbers = sectorModel.seatNumbers(index);
        for(int i=0; i<seatButtons.length; i++)
            seatButtons[i].setText(String.format(new Locale("pl", "PL"), "%d",
                    seatNumbers[i]));
    }

    private void switchToLoginActivity() {
        Intent intent = new Intent(SectorActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onDbResponseCallback(boolean[] takenSeats) {
        sectorModel.setChoosedSeats(takenSeats);
        sectorModel.updateSectorSeats();
        for (int i = 0; i < sectorModel.getChoosedSeats().length; i++)
            Log.d(logTag, "Model choosedSeats = " + sectorModel.getChoosedSeats()[i]);

        updateSectors(true);
    }

    @Override
    public void callBackOnError() {
        ToastUtils.showLongToast(this, getString(R.string.serverErrorTitle));

    }

    @Override
    public void callBackOnNoNetwork() {
        ErrorDialog.showErrorDialog(
                this,
                getString(R.string.networkConnectionErrorTitle),
                getString(R.string.loginNetworkConnectionErrorMsg)
        );
    }
}



