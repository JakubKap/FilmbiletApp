package com.companysf.filmbilet.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.companysf.filmbilet.connection.CustomerReservationsConnection;
import com.companysf.filmbilet.connection.Listener.ServerConnectionListener;
import com.companysf.filmbilet.entities.ReservationsList;
import com.companysf.filmbilet.services.Login;
import com.companysf.filmbilet.R;
import com.companysf.filmbilet.adapter.CustomerReservationsListAdapter;


public class CustomerReservationsActivity extends AppCompatActivity implements ServerConnectionListener {
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout emptyListRefreshLayout;

    private CustomerReservationsListAdapter adapter;
    private ReservationsList reservationsList;
    private static final String TAG = CustomerReservationsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_reservations);

        Login login = new Login(this);

        if (!login.userIsLoggedIn())
            switchToLoginActivity();

        swipeRefreshLayout = findViewById(R.id.swiper);
        emptyListRefreshLayout = findViewById(R.id.emptyListRefreshLayout);
        TextView title = findViewById(R.id.title);

        Typeface opensansRegular = Typeface.createFromAsset(getAssets(), getString(R.string.opensSansRegular));
        Typeface opensansBold = Typeface.createFromAsset(getAssets(), getString(R.string.opensSansBold));
        Typeface opensansItalic = Typeface.createFromAsset(getAssets(), getString(R.string.opensSansItalic));
        title.setTypeface(opensansBold);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        reservationsList = new ReservationsList();
        adapter = new CustomerReservationsListAdapter(
                this, reservationsList.getList(), opensansItalic, opensansRegular
        );

        final CustomerReservationsConnection customerReservationsConnection =
                new CustomerReservationsConnection(
                        this,
                        this,
                        reservationsList,
                        adapter
                );

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        customerReservationsConnection.updateDataFromServer();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                customerReservationsConnection.updateDataFromServer(true);
            }
        });
    }

    private void switchToLoginActivity() {
        Intent intent = new Intent(CustomerReservationsActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void callBackOnEndOfFetchingData(boolean manualSwipeRefresh) {
        if (manualSwipeRefresh)
            swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void callBackOnSuccess() {
        Log.d(TAG, "callBack on success w klasie CustomerReservations");
        emptyListRefreshLayout.setVisibility(View.GONE);
    }

    @Override
    public void callBackOnError() {
        makeRefreshLayoutVisible();
        showDialog(
                getString(R.string.serverErrorTitle),
                getString(R.string.serverErrorCheckLater)
        );
    }

    @Override
    public void callBackOnNoNetwork() {
        makeRefreshLayoutVisible();
        showDialog(
                getString(R.string.networkConnectionErrorTitle),
                getString(R.string.checkConnectionErrorStatement)
        );
    }

    private void makeRefreshLayoutVisible() {
        if (reservationsList.getList().isEmpty()) {
            emptyListRefreshLayout.setVisibility(View.VISIBLE);
        }
    }

    public void showDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(getString(R.string.dialogPositiveBtnText),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builder.show();
    }
}
