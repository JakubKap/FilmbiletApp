package com.companysf.filmbilet.Connection;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.companysf.filmbilet.Activity.CustomerReservationsActivity;
import com.companysf.filmbilet.Adapter.CustomerReservationsListAdapter;
import com.companysf.filmbilet.App.AppConfig;
import com.companysf.filmbilet.App.AppController;
import com.companysf.filmbilet.Connection.Listener.ServerConnectionListener;
import com.companysf.filmbilet.Entities.Customer;
import com.companysf.filmbilet.Entities.CustomerReservation;
import com.companysf.filmbilet.Entities.Movie;
import com.companysf.filmbilet.Entities.Repertoire;
import com.companysf.filmbilet.Entities.ReservationsList;
import com.companysf.filmbilet.R;
import com.companysf.filmbilet.Utils.ConnectionDetector;
import com.companysf.filmbilet.Utils.SQLiteHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CustomerReservationsConnection {
    private static final String logTag = CustomerReservationsActivity.class.getSimpleName();
    private Context context;
    private ConnectionDetector cd;
    private ServerConnectionListener serverConnectionListener;
    private ReservationsList reservationsList;
    private CustomerReservationsListAdapter adapter;

    public CustomerReservationsConnection(Context context, ServerConnectionListener serverConnectionListener, ReservationsList reservationsList, CustomerReservationsListAdapter adapter) {
        this.context = context;
        this.cd = new ConnectionDetector(context);
        this.serverConnectionListener = serverConnectionListener;
        this.reservationsList = reservationsList;
        this.adapter = adapter;
    }

    public void updateDataFromServer(final boolean manualSwipeRefresh) {
        if (cd.connected()) {
            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    AppConfig.GET_CUSTOMER_RESERVATIONS,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(logTag, "get customer reservations");
                            try {
                                JSONObject json = new JSONObject(response);
                                boolean error = json.getBoolean(context.getString(R.string.error));
                                if (error) {
                                    Log.d(logTag, "Error on getting data from API Service");
                                    serverConnectionListener.callBackOnError();
                                } else {
                                    serverConnectionListener.callBackOnSuccess();

                                    JSONArray CustomerReservationJson =
                                            json.getJSONArray(
                                                    context.getString
                                                            (R.string.customerReservationsJsonName)
                                            );
                                    reservationsList.getList().clear();

                                    for (int i = 0; i < CustomerReservationJson.length(); i++) {
                                        Log.d(logTag, "CustomerReservationJson LOG " + CustomerReservationJson.length());
                                        JSONObject CustomerReservationJSON = CustomerReservationJson.getJSONObject(i);
                                        CustomerReservation customerReservation =
                                                new CustomerReservation(
                                                        CustomerReservationJSON.getString(context.getString(R.string.jsonSeatNumbers)),
                                                        CustomerReservationJSON.getString(context.getString(R.string.jsonReservDate)),
                                                        new Repertoire(
                                                                new Movie(
                                                                        CustomerReservationJSON.getString(context.getString(R.string.jsonMovieTitle))
                                                                ),
                                                                CustomerReservationJSON.getString(context.getString(R.string.jsonRepertDate)),
                                                                Integer.parseInt(CustomerReservationJSON.getString(context.getString(R.string.jsonRepertId)))
                                                        ),
                                                        Float.parseFloat(CustomerReservationJSON.getString(context.getString(R.string.jsonPrice)))
                                                );
                                        reservationsList.getList().add(customerReservation);
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                serverConnectionListener.callBackOnError();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(logTag, "Error on response");
                            serverConnectionListener.callBackOnError();
                        }
                    }
            ){
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put(context.getString(R.string.reservationPutReqParam), getCustomerId());
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(stringRequest, context.getString(R.string.loginRequestAdd));
        } else {
            serverConnectionListener.callBackOnNoNetwork();
        }
        serverConnectionListener.callBackOnEndOfFetchingData(manualSwipeRefresh);
    }

    public void updateDataFromServer() {
        updateDataFromServer(false);
    }

    private String getCustomerId() {
        SQLiteHandler db = new SQLiteHandler(context);
        Customer customer = db.getCustomer();
        return customer.getId();
    }
}
