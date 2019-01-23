package com.companysf.filmbilet.Activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.companysf.filmbilet.Connection.ServerConnectionListener;
import com.companysf.filmbilet.Connection.MovieConnection;
import com.companysf.filmbilet.Entities.Customer;
import com.companysf.filmbilet.Model.Login;
import com.companysf.filmbilet.Entities.MovieList;
import com.companysf.filmbilet.R;
import com.companysf.filmbilet.Adapter.MoviesListAdapter;
import com.companysf.filmbilet.Utilies.ConnectionDetector;
import com.companysf.filmbilet.Utilies.ErrorDialog;
import com.companysf.filmbilet.Utilies.SQLiteHandler;

public class MainActivity extends AppCompatActivity implements ServerConnectionListener {
    private ErrorDialog errorDialog;
    private MovieList movieList;
    private MovieConnection movieConnection;
    private LinearLayout emptyListRefreshLayout;
    SwipeRefreshLayout swipeRefreshLayout;

    private Login login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = new Login(getApplicationContext());
        if (!login.userIsLoggedIn()) {
            switchToLoginActivity();
        }

        Button btnLogout = findViewById(R.id.btn_logout);
        Button btnCustomerReservations = findViewById(R.id.btn_customer_reservations);
        TextView customerInfo = findViewById(R.id.customer_info);
        ListView moviesListView = findViewById(R.id.movies_list_view);
        ImageButton btn_refresh = findViewById(R.id.btn_refresh_assets);
        swipeRefreshLayout = findViewById(R.id.swiper);
        emptyListRefreshLayout = findViewById(R.id.empty_list_refresh_layout);
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
        errorDialog = new ErrorDialog(this);
        SQLiteHandler db = new SQLiteHandler(getApplicationContext());
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
        setRefreshLayoutVisible();
        errorDialog.showErrorDialog(
                getString(R.string.serverErrorTitle),
                getString(R.string.serverErrorCheckLater)
        );
    }

    @Override
    public void callBackOnNoNetwork() {
        setRefreshLayoutVisible();
        errorDialog.showErrorDialog(
                getString(R.string.networkConnectionErrorTitle),
                getString(R.string.checkConnectionErrorStatement)
        );
    }

    @Override
    public void callBackOnSuccess() {
        emptyListRefreshLayout.setVisibility(View.GONE);
    }

    @Override
    public void callBackOnEndOfFetchingData(boolean manualSwipeRefresh) {
        if (manualSwipeRefresh)
            swipeRefreshLayout.setRefreshing(false);
    }

    private void setRefreshLayoutVisible() {
        if (movieList.getList().isEmpty()) {
            emptyListRefreshLayout.setVisibility(View.VISIBLE);
        }
    }
}