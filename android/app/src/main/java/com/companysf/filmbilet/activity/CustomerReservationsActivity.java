package com.companysf.filmbilet.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.companysf.filmbilet.connection.CustomerReservationsConnection;
import com.companysf.filmbilet.connection.Listener.ServerConnectionListener;
import com.companysf.filmbilet.entities.ReservationsList;
import com.companysf.filmbilet.services.Login;
import com.companysf.filmbilet.R;
import com.companysf.filmbilet.adapter.CustomerReservationsListAdapter;
import com.companysf.filmbilet.utils.ErrorDialog;


public class CustomerReservationsActivity extends AppCompatActivity implements ServerConnectionListener {
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout emptyListRefreshLayout;

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
        Button homeBtn = findViewById(R.id.homeBtn);

        Typeface opensansRegular = Typeface.createFromAsset(getAssets(), getString(R.string.opensSansRegular));
        Typeface opensansBold = Typeface.createFromAsset(getAssets(), getString(R.string.opensSansBold));
        Typeface opensansItalic = Typeface.createFromAsset(getAssets(), getString(R.string.opensSansItalic));
        title.setTypeface(opensansBold);
        homeBtn.setTypeface(opensansRegular);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        reservationsList = new ReservationsList();
        CustomerReservationsListAdapter adapter = new CustomerReservationsListAdapter(
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

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToMainActivity();
            }
        });
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
        makeEmptyListLayoutVisible();
    }

    @Override
    public void callBackOnError() {
        makeEmptyListLayoutVisible();
        ErrorDialog.showErrorDialog(
                this,
                getString(R.string.serverErrorTitle),
                getString(R.string.serverErrorCheckLater)
        );
    }

    @Override
    public void callBackOnNoNetwork() {
        makeEmptyListLayoutVisible();
        ErrorDialog.showErrorDialog(
                this,
                getString(R.string.networkConnectionErrorTitle),
                getString(R.string.checkConnectionErrorStatement)
        );
    }

    private void makeEmptyListLayoutVisible() {
        if (reservationsList.getList().isEmpty()) {
            emptyListRefreshLayout.setVisibility(View.VISIBLE);
        } else {
            emptyListRefreshLayout.setVisibility(View.GONE);
        }
    }

    private void switchToMainActivity() {
        Intent intent = new Intent(CustomerReservationsActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
