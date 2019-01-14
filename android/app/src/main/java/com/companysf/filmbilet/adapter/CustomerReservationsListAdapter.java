package com.companysf.filmbilet.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.companysf.filmbilet.R;
import com.companysf.filmbilet.appLogic.CustomerReservation;
import com.companysf.filmbilet.appLogic.PdfFile;

import java.util.List;
import java.util.Locale;

public class CustomerReservationsListAdapter extends RecyclerView.Adapter<CustomerReservationsListAdapter.MyViewHolder> {
    private Context context;
    private List<CustomerReservation> reservationsList;
    private Typeface opensansRegular;
    private Typeface opensansItalic;

    public CustomerReservationsListAdapter(
            Context context,
            List<CustomerReservation> reservationsList,
            Typeface opensansItalic,
            Typeface opensansRegular
    ) {
        this.context = context;
        this.reservationsList = reservationsList;
        this.opensansItalic = opensansItalic;
        this.opensansRegular = opensansRegular;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView price, priceText, seatNumbers, reservationDateText, reservationDate,
                repertoireDateText, repertoireDate, title, seatNumbersText;
        Button generatePdfButton;
        MyViewHolder(@NonNull View itemView) {
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
            generatePdfButton = itemView.findViewById(R.id.generatePdfButton);
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
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int position) {
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
                customerReservation.getReservationDate().getStringDateTime()
        );
        myViewHolder.repertoireDate.setText(
                customerReservation.getRepertoire().getDate().getStringDateTime()
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
        myViewHolder.generatePdfButton.setTypeface(opensansRegular);

        myViewHolder.generatePdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerReservation customerReservation = reservationsList.get(myViewHolder.getAdapterPosition());
                String reservationDate = customerReservation.getReservationDate().getStringDate() +
                        "_" + customerReservation.getReservationDate().getStringTime(".");

                PdfFile pdfFile = new PdfFile(
                        context,
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                        "moje_rezerwacje_filmbilet",
                        "reservation_"+reservationDate+".pdf",
                        "assets/opensans_regular.ttf",
                        customerReservation);
                if (!pdfFile.createPdfFile())
                    Toast.makeText(
                            context,
                            "zabroniony dostep do modyfikacji plików",
                            Toast.LENGTH_LONG
                    ).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return reservationsList.size();
    }


}
