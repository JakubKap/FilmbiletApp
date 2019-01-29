package com.companysf.filmbilet.services;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import com.companysf.filmbilet.R;
import com.companysf.filmbilet.entities.CustomerReservation;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

public class PdfFile {
    private Context context;

    private Document document;
    private File rootFolder;
    private String folder;
    private String fontPath;
    private Font fieldHintFont;
    private Font fieldValueFont;
    private LineSeparator lineSeparator;

    private String name;

    private CustomerReservation reservation;

    public PdfFile(Context context, File rootFolder, String folder, String name, String fontPath, CustomerReservation reservation) {
        this.context = context;
        this.name = name;
        this.fontPath = fontPath;
        this.rootFolder = rootFolder;
        this.folder = folder;
        this.reservation = reservation;
    }

    /**
     *
     * @return  true if file was created with success,
     *          false if file couldn't be created Due to the lack of permissions
     */
    public boolean createPdfFile() {
        if (arePermissions())
            return createFile();
        else
            return false;
    }

    private boolean createFile() {
        try {
            File fileDirecory = new File(this.rootFolder
                    + File.separator
                    + this.folder
                    + File.separator);
            if (!fileDirecory.exists()) {
                fileDirecory.mkdir();
            }

            String destination = fileDirecory +File.separator+this.name;

            if (new File(destination).exists()) {
                new File(destination).delete();
            }

            this.document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(destination));
            this.document.open();
            this.document.setPageSize(PageSize.A4);
            this.document.addCreationDate();

            this.document.addAuthor(context.getString(R.string.pdfAuthor));

            float smallSize = 20.0f;
            float normalSize = 26.0f;
            float bigSize = 36.0f;
            BaseFont myFont = BaseFont.createFont(this.fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            this.fieldHintFont = new Font(myFont, smallSize, Font.NORMAL,
                    new BaseColor(99, 100, 102, 255)    //#636466
            );
            this.fieldValueFont = new Font(myFont, normalSize, Font.NORMAL, BaseColor.BLACK);

            this.lineSeparator = new LineSeparator();
            this.lineSeparator.setLineColor(
                    new BaseColor(231, 232, 237, 255)   //#e7e8ed
            );

            Font titleFont = new Font(myFont, bigSize, Font.NORMAL, BaseColor.BLACK);
            Paragraph titleContent = new Paragraph(
                    new Chunk(context.getString(R.string.reservingTicketsText), titleFont)
            );
            titleContent.setAlignment(Element.ALIGN_CENTER);
            this.document.add(titleContent);

            createFieldWithOneInfo(context.getString(R.string.myReservationMovieTitle)
                    , reservation.getRepertoire().getMovie().getTitle()
            );
            createFieldWithOneInfo(
                    context.getString(R.string.reservationDate), reservation.getReservationDateFormat().getStringDateTime()
            );
            createFieldWithOneInfo(context.getString(R.string.repertoireDate),
                    reservation.getRepertoire().getDateFormat().getStringDateTime()
            );
            createFieldWithOneInfo(context.getString(R.string.reservedSeats),
                    String.format(
                            new Locale(context.getString(R.string.polish), context.getString(R.string.poland)),
                            "%s",
                            reservation.getSeatNumbers()
                    )
            );
            createFieldWithOneInfo(context.getString(R.string.allTicketsPrice),
                    String.format(
                            new Locale(context.getString(R.string.polish), context.getString(R.string.poland)),
                            "%.2f",
                            reservation.getPrice()
                    ) + context.getString(R.string.currency)
            );

            this.document.close();

        } catch (IOException | DocumentException ie) {
            return false;
        }
        return true;
    }

    private void createFieldWithOneInfo (String fieldHintText, String fieldValueText) {
        try {
            Paragraph fieldHintParagraph = new Paragraph(
                    new Chunk(fieldHintText, this.fieldHintFont)
            );
            Paragraph fieldValueParagraph = new Paragraph(
                    new Chunk(fieldValueText, this.fieldValueFont)
            );
            this.document.add(fieldHintParagraph);
            this.document.add(fieldValueParagraph);

            this.document.add(new Paragraph(context.getString(R.string.emptyString)));
            this.document.add(new Chunk(this.lineSeparator));
            this.document.add(new Paragraph(context.getString(R.string.emptyString)));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    private boolean arePermissions() {
        return !(ContextCompat.checkSelfPermission(
                        context, Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_DENIED);
    }
}
