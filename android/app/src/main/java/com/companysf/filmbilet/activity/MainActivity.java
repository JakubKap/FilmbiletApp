package com.companysf.filmbilet.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.companysf.filmbilet.connection.Listener.ServerConnectionListener;
import com.companysf.filmbilet.connection.MovieConnection;
import com.companysf.filmbilet.entities.Customer;
import com.companysf.filmbilet.services.Login;
import com.companysf.filmbilet.entities.MovieList;
import com.companysf.filmbilet.R;
import com.companysf.filmbilet.adapter.MoviesListAdapter;
import com.companysf.filmbilet.utils.ErrorDialog;
import com.companysf.filmbilet.utils.SQLiteHandler;

public class MainActivity extends AppCompatActivity implements ServerConnectionListener {
    private MovieList movieList;
    private MovieConnection movieConnection;
    private LinearLayout emptyListRefreshLayout;
    private ConstraintLayout notEmptyLayout;
    SwipeRefreshLayout swipeRefreshLayout;

    private Login login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = new Login(this);
        if (!login.userIsLoggedIn()) {
            switchToLoginActivity();
        }

        Button btnLogout = findViewById(R.id.btnLogout);
        Button btnCustomerReservations = findViewById(R.id.btnCustomerReservations);
        TextView customerInfo = findViewById(R.id.customerInfo);
        ListView moviesListView = findViewById(R.id.moviesListView);
        ImageButton btn_refresh = findViewById(R.id.btnRefreshAssets);
        swipeRefreshLayout = findViewById(R.id.swiper);
        emptyListRefreshLayout = findViewById(R.id.emptyListRefreshLayout);
        notEmptyLayout = findViewById(R.id.notEmptyLayout);
        TextView welcomeCustomer = findViewById(R.id.welcomeCustomer);

        Typeface opensansRegular = Typeface.createFromAsset(getAssets(), getString(R.string.opensSansRegular));
        Typeface opensansBold = Typeface.createFromAsset(getAssets(), getString(R.string.opensSansBold));
        Typeface opensansItalic = Typeface.createFromAsset(getAssets(), getString(R.string.opensSansItalic));

        welcomeCustomer.setTypeface(opensansRegular);
        customerInfo.setTypeface(opensansRegular);
        btnCustomerReservations.setTypeface(opensansBold);
        btnLogout.setTypeface(opensansBold);

        movieList = new MovieList();
        MoviesListAdapter adapter = new MoviesListAdapter(
                this,
                MainActivity.this,
                movieList.getList(),
                opensansRegular,
                opensansBold,
                opensansItalic
        );
        movieConnection = new MovieConnection(this, this, movieList, adapter);
        SQLiteHandler db = new SQLiteHandler(this);
        Customer customer = db.getCustomer();

        moviesListView.setAdapter(adapter);
        customerInfo.setText(customer.getEmail());

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOutCustomer();
            }
        });
        btnCustomerReservations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToCustomerReservations();
            }
        });
        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movieConnection.updateDataFromServer();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                movieConnection.updateDataFromServer(true);
            }
        });

        movieConnection.updateDataFromServer();
    }

    private void logOutCustomer() {
        login.logOutCustomer();
        switchToLoginActivity();
    }

    private void switchToLoginActivity() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void  switchToCustomerReservations() {
        Intent intent = new Intent(MainActivity.this, CustomerReservationsActivity.class);
        startActivity(intent);
    }

    @Override
    public void callBackOnError() {
        makeRefreshLayoutVisible();
        ErrorDialog.showErrorDialog(
                this,
                getString(R.string.serverErrorTitle),
                getString(R.string.serverErrorCheckLater)
        );
    }

    @Override
    public void callBackOnNoNetwork() {
        makeRefreshLayoutVisible();
        ErrorDialog.showErrorDialog(
                this,
                getString(R.string.networkConnectionErrorTitle),
                getString(R.string.checkConnectionErrorStatement)
        );
    }

    @Override
    public void callBackOnSuccess() {
        emptyListRefreshLayout.setVisibility(View.GONE);
        notEmptyLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void callBackOnEndOfFetchingData(boolean manualSwipeRefresh) {
        if (manualSwipeRefresh)
            swipeRefreshLayout.setRefreshing(false);
    }

    private void makeRefreshLayoutVisible() {
        if (movieList.getList().isEmpty()) {
            emptyListRefreshLayout.setVisibility(View.VISIBLE);
            notEmptyLayout.setVisibility(View.GONE);
        }
    }
}