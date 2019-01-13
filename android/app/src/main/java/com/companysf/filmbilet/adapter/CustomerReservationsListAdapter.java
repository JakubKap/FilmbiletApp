package com.companysf.filmbilet.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.companysf.filmbilet.R;
import com.companysf.filmbilet.appLogic.CustomerReservation;

import java.util.List;
import java.util.Locale;

public class CustomerReservationsListAdapter extends RecyclerView.Adapter<CustomerReservationsListAdapter.MyViewHolder> {
    private Context context;
    private List<CustomerReservation> reservationsList;
    private Typeface opensansBold;
    private Typeface opensansRegular;
    private Typeface opensansItalic;

    public CustomerReservationsListAdapter(
                    Context context,
                    List<CustomerReservation> reservationsList,
                    Typeface opensansBold,
                    Typeface opensansItalic,
                    Typeface opensansRegular
            ) {
        this.context = context;
        this.reservationsList = reservationsList;
        this.opensansBold = opensansBold;
        this.opensansItalic = opensansItalic;
        this.opensansRegular = opensansRegular;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView price, priceText, seatNumbers, reservationDateText, reservationDate,
                repertoireDateText, repertoireDate, title, seatNumbersText;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title= itemView.findViewById(R.id.title);
            priceText = itemView.findViewById(R.id.priceText);
            price = itemView.findViewById(R.id.price);
            seatNumbers= itemView.findViewById(R.id.seatNumbers);
            reservationDate= itemView.findViewById(R.id.reservationDate);
            repertoireDate= itemView.findViewById(R.id.repertoireDate);
            seatNumbersText = itemView.findViewById(R.id.seatNumbersText);
            reservationDateText = itemView.findViewById(R.id.reservationDateText);
            repertoireDateText = itemView.findViewById(R.id.repertoireDateText);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.customer_reservations_list_row, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        CustomerReservation customerReservation = reservationsList.get(position);

        //set text to views
        myViewHolder.title.setText(customerReservation.getRepertoire().getMovie().getTitle());
        myViewHolder.price.setText(
                    String.format(new Locale("pl", "PL"), "%.2f",
                            customerReservation.getPrice())
                );
        myViewHolder.seatNumbers.setText(
                String.format(new Locale("pl", "PL"), "%s", customerReservation.getSeatNumbers())
        );
        myViewHolder.reservationDate.setText(
                customerReservation.getReservationDate().getStringDate()
        );
        myViewHolder.repertoireDate.setText(
                customerReservation.getRepertoire().getDate().getStringDate()
        );

        //font
        myViewHolder.title.setTypeface(opensansRegular);
        myViewHolder.priceText.setTypeface(opensansRegular);
        myViewHolder.price.setTypeface(opensansItalic);
        myViewHolder.seatNumbersText.setTypeface(opensansRegular);
        myViewHolder.seatNumbers.setTypeface(opensansItalic);
        myViewHolder.reservationDateText.setTypeface(opensansRegular);
        myViewHolder.reservationDate.setTypeface(opensansItalic);
        myViewHolder.repertoireDateText.setTypeface(opensansRegular);
        myViewHolder.repertoireDate.setTypeface(opensansItalic);
    }

    @Override
    public int getItemCount() {
        return reservationsList.size();
    }


}
