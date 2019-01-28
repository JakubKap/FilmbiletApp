package com.companysf.filmbilet.connection;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.companysf.filmbilet.app.AppConfig;
import com.companysf.filmbilet.app.AppController;
import com.companysf.filmbilet.connection.Listener.ErrorListener;
import com.companysf.filmbilet.entities.Reservation;
import com.companysf.filmbilet.connection.Listener.ReservationConnListener;
import com.companysf.filmbilet.R;
import com.companysf.filmbilet.utils.ConnectionDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ReservationConnection {

    private static final String logTag = ReservationConnection.class.getSimpleName();
    private Context mContext;
    private ErrorListener errorListener;
    private ReservationConnListener reservationConnListener;
    private ConnectionDetector cd;

    public ReservationConnection(Context c, ErrorListener errorListener, ReservationConnListener reservationConnListener){
        this.mContext = c;
        this.errorListener = errorListener;
        this.reservationConnListener = reservationConnListener;
        this.cd = new ConnectionDetector(c);
    }

    public void getReservations(int repertoireId) {

        if (cd.connected()) {
            Log.d(logTag, "Jest polaczenie inter");
            final String repertoireIdString = Integer.toString(repertoireId);
            final boolean[] choosedPlaces = new boolean[280];

            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    AppConfig.GET_RESERVATIONS,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(logTag, mContext.getString(R.string.getResLog) + response);
                            try {
                                JSONObject json = new JSONObject(response);
                                boolean error = json.getBoolean(mContext.getString(R.string.error));
                                if (error) {
                                    errorListener.callBackOnError();
                                    //TODO zmiana w php na error = false
                                } else {
                                    JSONArray reservationsJson = json.getJSONArray(mContext.getString(R.string.resArrayName));
                                    for (int i = 0; i < reservationsJson.length(); i++) {
                                        Log.d(logTag, mContext.getString(R.string.resJsonLog) + reservationsJson.length());
                                        JSONObject reservationJSON = reservationsJson.getJSONObject(i);
                                        Reservation reservation = new Reservation(
                                                reservationJSON.getInt(mContext.getString(R.string.id)),
                                                reservationJSON.getInt(mContext.getString(R.string.resSeatNumber)),
                                                reservationJSON.getInt(mContext.getString(R.string.resRow))
                                        );

                                        choosedPlaces[reservation.getSeatNumber() - 1] = true;
                                        int number = reservation.getSeatNumber() - 1;
                                        Log.d(logTag, "choosedPlaces[ " + number + " ] = " + choosedPlaces[number]);
                                    }


                                }
                            } catch (JSONException e) {
                                errorListener.callBackOnError();
                        }

                            for (int i = 0; i < choosedPlaces.length; i++)
                                if (choosedPlaces[i])
                                    Log.d(logTag, "After choosedPlaces[ " + i + " ] = " + choosedPlaces[i]);

                            reservationConnListener.onDbResponseCallback(choosedPlaces);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(logTag, mContext.getString(R.string.resErrorLog) + error.getMessage());
                    errorListener.callBackOnError();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put(mContext.getString(R.string.repertoireId), repertoireIdString);
                    return params;
                }
            };

            AppController.getInstance().addToRequestQueue(stringRequest, mContext.getString(R.string.registerRequestAdd));
        }
        else{
            errorListener.callBackOnNoNetwork();
            Log.d(logTag, "Brak polaczenia inter");
        }
    }

    public void saveReservation(String customerIdPar, int seatNumberPar, int seatTypeIdPar, int rowPar,  int repertoireIdPar){

        if (cd.connected()) {
            Log.d(logTag, "Jest polaczenie inter");
            //final String customerId = customerIdPar;
            final String customerId = customerIdPar;
            final String seatNumber = Integer.toString(seatNumberPar);
            final String seatTypeId = Integer.toString(seatTypeIdPar);
            final String row = Integer.toString(rowPar);
            final String repertoireId = Integer.toString(repertoireIdPar);

            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    AppConfig.STORE_RESERVATION,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(logTag, mContext.getString(R.string.saveResRequest) + response);
                            try {
                                JSONObject json = new JSONObject(response);
                                boolean error = json.getBoolean(mContext.getString(R.string.error));
                                if (error) {
                                    errorListener.callBackOnError();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                errorListener.callBackOnError();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(logTag, mContext.getString(R.string.saveResErrorLog) + error.getMessage());
                    errorListener.callBackOnError();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put(mContext.getString(R.string.customerId), customerId);
                    params.put(mContext.getString(R.string.resSeatNumber), seatNumber);
                    params.put(mContext.getString(R.string.resRow), row);
                    params.put(mContext.getString(R.string.seatTypeId), seatTypeId);
                    params.put(mContext.getString(R.string.repertoireId), repertoireId);
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(stringRequest, mContext.getString(R.string.registerRequestAdd));
        }
        else{
            errorListener.callBackOnNoNetwork();
            Log.d(logTag, "Brak polaczenia inter");
        }
    }

}