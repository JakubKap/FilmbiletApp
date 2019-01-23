package com.companysf.filmbilet.Connection;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.companysf.filmbilet.Activity.SectorActivity;
import com.companysf.filmbilet.App.AppConfig;
import com.companysf.filmbilet.App.AppController;
import com.companysf.filmbilet.Entities.Reservation;
import com.companysf.filmbilet.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ReservationConnection {

    private static final String logTag = ReservationConnection.class.getSimpleName();
    private Context mContext;
    private SectorActivity sectorActivity;

    public ReservationConnection(Context c, SectorActivity sectorActivity){
        this.mContext = c;
        this.sectorActivity = sectorActivity;
    }

    public void getReservations(int repertoireId) {

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
                                if (!json.getString(mContext.getString(R.string.message)).
                                        equals(mContext.getString(R.string.noResForRep)))
                                    Toast.makeText(
                                            mContext,
                                            json.getString(mContext.getString(R.string.message)),
                                            Toast.LENGTH_SHORT).show();
                            } else {
                                JSONArray reservationsJson = json.getJSONArray(mContext.getString(R.string.resArrayName));
                                for (int i = 0; i < reservationsJson.length(); i++) {
                                    Log.d(logTag, mContext.getString(R.string.resJsonLog) + reservationsJson.length());
                                    JSONObject reservationJSON = reservationsJson.getJSONObject(i);
                                    Reservation reservation = new Reservation(
                                            reservationJSON.getInt(mContext.getString(R.string.resId)),
                                            reservationJSON.getInt(mContext.getString(R.string.resSeatNumber)),
                                            reservationJSON.getInt(mContext.getString(R.string.resRow))
                                    );

                                    choosedPlaces[reservation.getSeatNumber() - 1] = true;
                                    int number = reservation.getSeatNumber() - 1;

                                    Log.d(logTag, "choosedPlaces[ " + number + " ] = " + choosedPlaces[number]);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(
                                    mContext,
                                    mContext.getString(R.string.jsonError) + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }

                        for (int i = 0; i < choosedPlaces.length; i++)
                            if (choosedPlaces[i])
                                Log.d(logTag, "After choosedPlaces[ " + i + " ] = " + choosedPlaces[i]);

                        sectorActivity.updateSectors(true,choosedPlaces);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(logTag, mContext.getString(R.string.resErrorLog) + error.getMessage());
                Toast.makeText(mContext,
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(mContext.getString(R.string.repId), repertoireIdString);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, mContext.getString(R.string.requestAdd));
    }

}