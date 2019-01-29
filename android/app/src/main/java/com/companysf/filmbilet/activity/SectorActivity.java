package com.companysf.filmbilet.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.companysf.filmbilet.connection.Listener.ErrorListener;
import com.companysf.filmbilet.connection.ReservationConnection;
import com.companysf.filmbilet.connection.Listener.ReservationConnListener;
import com.companysf.filmbilet.connection.Listener.SectorListener;
import com.companysf.filmbilet.services.Login;
import com.companysf.filmbilet.services.SectorService;
import com.companysf.filmbilet.R;


import java.util.Locale;

import static com.companysf.filmbilet.utils.ToastUtils.showLongToast;


public class SectorActivity extends AppCompatActivity implements ErrorListener, ReservationConnListener, SectorListener {

    private static final String logTag = SectorActivity.class.getSimpleName();

    AlertDialog.Builder builder;

    private SectorService sectorService;

    private Button[] seatButtons;
    private Button[] sectorButtons;
    private Button[] columnButtons;
    private Button[] rowButtons;

    private Button seatsApproveButton;
    private Button seatsCloseButton;

    private TextView secChoosedPlaces, secSummaryPrice;

    private TextView[] freeSeats;

    Typeface opensansRegular;
    Typeface opensansBold;

    private ProgressBar secProgressBar;
    private ProgressBar seatsProgressBar;
    private TextView title, subtitle;

    private int currentSector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sector);

        Login login = new Login(this);
        if (!login.userIsLoggedIn()) {
            switchToLoginActivity();
        }

        builder = new AlertDialog.Builder(this);

        ReservationConnection reservationConnection = new ReservationConnection(this, this, this);
        sectorService = new SectorService(this, this, this, this, 8, 280);

        sectorButtons = new Button[8];
        seatButtons = new Button[35];
        columnButtons = new Button[8];
        rowButtons = new Button[5];
        freeSeats = new TextView[8];

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
        secChoosedPlaces = findViewById(R.id.secChoosedPlaces);
        secSummaryPrice = findViewById(R.id.secSummaryPrice);
        Button secBtnReserve = findViewById(R.id.secBtnReserve);
        TextView stepNumber2 = findViewById(R.id.stepNumber2);
        TextView selectSectorText = findViewById(R.id.selectSectorText);
        TextView choosedPlacesText = findViewById(R.id.choosedPlacesText);
        TextView priceLabel = findViewById(R.id.priceLabel);

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

        opensansRegular = Typeface.createFromAsset(getAssets(), getString(R.string.opensSansRegular));
        opensansBold = Typeface.createFromAsset(getAssets(), getString(R.string.opensSansBold));

        stepNumber2.setTypeface(opensansBold);
        selectSectorText.setTypeface(opensansBold);
        priceLabel.setTypeface(opensansRegular);
        secBtnReserve.setTypeface(opensansBold);
        secChoosedPlaces.setTypeface(opensansBold);
        secSummaryPrice.setTypeface(opensansBold);
        choosedPlacesText.setTypeface(opensansRegular);
        for (TextView freeSeat : freeSeats)
            freeSeat.setTypeface(opensansRegular);
        for (TextView secLabel : secLabels)
            secLabel.setTypeface(opensansBold);
        for (TextView secPrice : secPrices)
            secPrice.setTypeface(opensansRegular);

        sectorService.assignRowToSeat();
        sectorService.assignSectorToSeat();

        Bundle bundle = getIntent().getExtras();
        int repertoireId = bundle.getInt(getString(R.string.repertoireId));
        reservationConnection.getReservations(repertoireId);
        sectorService.setRepertoireId(repertoireId);


        for (int i = 0; i < sectorButtons.length; i++) {
            setPopupOnSector(i);
            sectorService.setSeatNumbers(i, new int[35]);
            sectorService.clearMarkedSeats();
        }
        secBtnReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sectorService.numOfChoosedSeats() == 0) {
                    showDialog(getString(R.string.noChoosedPlacesTitle),
                            getString(R.string.noChoosedPlacesMsg));
                } else {
                    sectorService.saveToDb();
                    //Intent intent = new Intent(this, CustomerReservationsActivity.class);
                    Intent intent = new Intent(SectorActivity.this, CustomerReservationsActivity.class);
                    startActivity(intent);

                }
            }
        });
    }

    private void setPopupOnSector(final int sectorIndex) {
        final int index = sectorIndex;
        sectorButtons[sectorIndex].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(logTag, "index buttona = " + index);

                currentSector = sectorIndex;

                sectorService.assignSeatsPrev();
                for (int i = 0; i < sectorService.getChoosedSeatsPrev().length; i++)
                    if (sectorService.getChoosedSeatsPrev()[i])
                        Log.d(logTag, "Marked seat after opening sectorService " + i);

                if (sectorService.getFreeSeatsInSector(index) == 0) {
                    showDialog(
                            getString(R.string.emptySectorTitle),
                            getString(R.string.emptySectorMsg)
                    );
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

                setPopupMetadata(index);

                dialog.show();

                for (int i = 0; i < seatButtons.length; i++) {
                    final int index = i;
                    seatButtons[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            sectorService.markSeat(sectorIndex, index);
                            int seatNumber = sectorService.getSeatNumbers(sectorIndex)[index];
                            markSeat(seatButtons[index], sectorService.getChoosedSeats()[seatNumber - 1], false);
                        }
                    });
                }

                seatsApproveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        updateSummary();
                        currentSector = -1;
                    }
                });

                seatsCloseButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();

                        sectorService.restoreChoosedSeats();

                        for (int i = 0; i < sectorService.getChoosedSeatsPrev().length; i++)
                            if (sectorService.getChoosedSeatsPrev()[i])
                                Log.d(logTag, "Marked seat after closing sectorService " + i);

                        sectorService.restoreChoosedSeats();
                        for (int i = 0; i < sectorService.getChoosedSeats().length; i++)
                            if (sectorService.getChoosedSeats()[i])
                                Log.d(logTag, "Choosed Mseat after closing sectorService " + i);

                        currentSector = -1;
                        updateSummary();
                    }
                });
            }
        });
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
                for (int i = 0; i < freeSeats.length; i++) {
                    String text = String.format(
                            new Locale(getString(R.string.polish), getString(R.string.poland)),
                            "%d",
                            sectorService.getFreeSeatsInSector(i)
                    )
                            + getString(R.string.currency);
                    freeSeats[i].setText(text);
                }

            }
        });

    }

    public String sectorTitle(int index) {
        if (index == 0)
            return getString(R.string.upperSector1Text);
        else if (index == 1)
            return getString(R.string.upperSector2Text);
        else if (index == 2)
            return getString(R.string.upperSector3Text);
        else if (index == 3)
            return getString(R.string.upperSector4Text);
        else if (index == 4)
            return getString(R.string.upperSector5Text);
        else if (index == 5)
            return getString(R.string.upperSector6Text);
        else if (index == 6)
            return getString(R.string.upperSector7Text);
        else
            return getString(R.string.upperSector8Text);
    }

    public String sectorSubtitle(int index) {
        if (sectorService.sectorSubtitleIndex(index) == 0)
            return getString(R.string.sector1Subtitle);
        else if (sectorService.sectorSubtitleIndex(index) == 2)
            return getString(R.string.sector2Subtitle);
        else if (sectorService.sectorSubtitleIndex(index) == 3)
            return getString(R.string.sector3Subtitle);
        else
            return getString(R.string.sector4Subtitle);

    }

    public String[] rowLabels(int index) {
        String[] rowLabels = new String[5];
        if (sectorService.rowLabelsType(index) == 0) {
            rowLabels[0] = getString(R.string.row1);
            rowLabels[1] = getString(R.string.row2);
            rowLabels[2] = getString(R.string.row3);
            rowLabels[3] = getString(R.string.row4);
            rowLabels[4] = getString(R.string.row5);
        } else if (sectorService.rowLabelsType(index) == 1) {
            rowLabels[0] = getString(R.string.row6);
            rowLabels[1] = getString(R.string.row7);
            rowLabels[2] = getString(R.string.row8);
            rowLabels[3] = getString(R.string.row9);
            rowLabels[4] = getString(R.string.row10);
        }
        if (sectorService.rowLabelsType(index) == 2) {
            rowLabels[0] = getString(R.string.row11);
            rowLabels[1] = getString(R.string.row12);
            rowLabels[2] = getString(R.string.row13);
            rowLabels[3] = getString(R.string.row14);
            rowLabels[4] = getString(R.string.row15);
        } else  {
            rowLabels[0] = getString(R.string.row16);
            rowLabels[1] = getString(R.string.row17);
            rowLabels[2] = getString(R.string.row18);
            rowLabels[3] = getString(R.string.row19);
            rowLabels[4] = getString(R.string.row20);
        }

        return rowLabels;
    }

    public String[] columnLabels(int index) {
        String[] columnLabels = new String[7];
        if (sectorService.columnLabelsType(index)) {
            columnLabels[0] = getString(R.string.firstColumnTextLeft);
            columnLabels[1] = getString(R.string.secondColumnTextLeft);
            columnLabels[2] = getString(R.string.thirdColumnTextLeft);
            columnLabels[3] = getString(R.string.fourthColumnTextLeft);
            columnLabels[4] = getString(R.string.fifthColumnTextLeft);
            columnLabels[5] = getString(R.string.sixthColumnTextLeft);
            columnLabels[6] = getString(R.string.seventhColumnTextLeft);
        } else {
            columnLabels[0] = getString(R.string.firstColumnTextRight);
            columnLabels[1] = getString(R.string.secondColumnTextRight);
            columnLabels[2] = getString(R.string.thirdColumnTextRight);
            columnLabels[3] = getString(R.string.fourthColumnTextRight);
            columnLabels[4] = getString(R.string.fifthColumnTextRight);
            columnLabels[5] = getString(R.string.sixthColumnTextRight);
            columnLabels[6] = getString(R.string.seventhColumnTextRight);
        }

        return columnLabels;
    }

    public void setPopupMetadata(int index) {
        final int sectorIndex = index;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                seatsProgressBar.setVisibility(View.INVISIBLE);
                title.setText(sectorTitle(sectorIndex));
                subtitle.setText(sectorSubtitle(sectorIndex));

                String[] rowLabels = rowLabels(sectorIndex);
                for (int i = 0; i < rowLabels.length; i++)
                    rowButtons[i].setText(rowLabels[i]);

                String[] columnLabels = columnLabels(sectorIndex);
                for (int i = 0; i < columnLabels.length; i++)
                    columnButtons[i].setText(columnLabels[i]);

                int[] seatNumbers = sectorService.seatNumbers(sectorIndex);
                for (int i = 0; i < seatButtons.length; i++)
                    seatButtons[i].setText(
                            String.format(
                                    new Locale(getString(R.string.polish), getString(R.string.poland)),
                                    "%d",
                                    seatNumbers[i]
                            )
                    );

                markChoosedPlaces(sectorIndex);
            }
        });
    }

    public void markChoosedPlaces(int sectorIndex) {
        if (sectorIndex > 0) {
            final boolean[] takenSeats = sectorService.getTakenSeats();
            final int[] seatNumbers = sectorService.getSeatNumbers(sectorIndex);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < seatNumbers.length; i++) {
                        Log.d(logTag, "Mark seatNumber[i] = " + seatNumbers[i]);
                        if (takenSeats[seatNumbers[i] - 1]) {
                            seatButtons[i].setEnabled(false);
                            markTakenSeat(seatButtons[i]);
                        } else
                            markSeat(seatButtons[i], sectorService.getChoosedSeats()[seatNumbers[i] - 1], true);
                    }
                }
            });
        }
    }

    public void markSeat(Button button, boolean isTaken, boolean isInitial) {
        final boolean finalIsTaken = isTaken;
        final boolean finalIsInitial = isInitial;
        final Button finalButton = button;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!finalIsTaken) {
                    if (!finalIsInitial)
                        performAnimation(finalButton);
                    finalButton.setBackgroundResource(R.drawable.seat);
                    finalButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                } else {
                    if (!finalIsInitial)
                        performAnimation(finalButton);
                    finalButton.setBackgroundResource(R.drawable.seat_choosed);
                    finalButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                }
            }
        });
    }

    public void markTakenSeat(Button button) {
        final Button finalButton = button;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                finalButton.setBackgroundResource(R.drawable.seat_reserved);
                finalButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
            }
        });
    }

    public void performAnimation(Button button) {
        final Button finalButton = button;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Animation animation = new AlphaAnimation(1.0f, 0.0f);
                animation.setDuration(200);
                finalButton.startAnimation(animation);
            }
        });
    }

    public void updateSummary() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                secChoosedPlaces.setText(
                        String.format(
                                new Locale(getString(R.string.polish), getString(R.string.poland)),
                                "%d",
                                sectorService.numOfSeats()
                        )
                );
                secSummaryPrice.setText(
                        String.format(
                                new Locale(getString(R.string.polish), getString(R.string.poland)),
                                "%d",
                                sectorService.seatsPrice()
                        )
                );
            }
        });
    }

    private void switchToLoginActivity() {
        Intent intent = new Intent(SectorActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void updateUiCallback() {
        Log.d(logTag, "updateUiCallback");
        markChoosedPlaces(currentSector);
        updateSectors(false);
        updateSummary();
    }

    @Override
    public void onDbResponseCallback(boolean[] takenSeats) {
        sectorService.setTakenSeats(takenSeats);
        sectorService.updateSectorSeats();
        for (int i = 0; i < sectorService.getTakenSeats().length; i++)
            Log.d(logTag, "Model takenSeats = " + sectorService.getTakenSeats()[i]);

        updateSectors(true);
    }

    @Override
    public void callBackOnError() {
        showLongToast(this, getString(R.string.serverErrorTitle));
    }

    @Override
    public void callBackOnNoNetwork() {
        showDialog(
                getString(R.string.networkConnectionErrorTitle),
                getString(R.string.loginNetworkConnectionErrorMsg)
        );
    }

    @Override
    public void showDialogCallback(String takenSeatsNumbers, int seatCount) {
        if (seatCount > 1)
            showDialog(getString(R.string.choosedPlacesTitle),
                    getString(R.string.choosedPlacesMsg) + takenSeatsNumbers
            );
        else
            showDialog(getString(R.string.choosedPlaceTitle),
                    getString(R.string.choosedPlaceMsg) + takenSeatsNumbers
            );
    }

    @Override
    public void socketCloseError() {
        showDialog(
                getString(R.string.serverErrorTitle),
                getString(R.string.serverErrorMsg)
        );
    }

    public void showDialog(String title, String message) {
        final String finalTitle = title;
        final String finalMessage = message;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                builder.setTitle(finalTitle);
                builder.setMessage(finalMessage);
                builder.setPositiveButton(getString(R.string.dialogPositiveBtnText),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                builder.show();
            }
        });
    }
}