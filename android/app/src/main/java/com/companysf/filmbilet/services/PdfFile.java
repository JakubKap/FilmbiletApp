package com.companysf.filmbilet.services;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import com.companysf.filmbilet.entities.CustomerReservation;
import com.companysf.filmbilet.R;
import com.companysf.filmbilet.utils.ToastUtils;
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

    private String name;        //"reservations.pdf"

    private CustomerReservation reservation;
    private ToastUtils toastUtils;


    //fontPath = "assets/opensans_regular.ttf"
    //rootFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    //folder = "moje_rezerwacje_filmbilet"
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
            ToastUtils toastUtils = new ToastUtils(context);

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

            this.document.addAuthor("Kino-filmbilet");

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
                    new Chunk("Rezerwacja biletów w kinie Filmbilet", titleFont)
            );
            titleContent.setAlignment(Element.ALIGN_CENTER);
            this.document.add(titleContent);

            createFieldWithOneInfo(
                    "Tytuł filmu:", reservation.getRepertoire().getMovie().getTitle()
            );
            createFieldWithOneInfo(
                    "Data rezerwacji:", reservation.getReservationDate().getStringDateTime()
            );
            createFieldWithOneInfo("Data seansu:",
                    reservation.getRepertoire().getDate().getStringDateTime()
            );
            createFieldWithOneInfo("Zarezerwowane miejsca:",
                    String.format(
                            new Locale("pl", "PL"),
                            "%s",
                            reservation.getSeatNumbers()
                    )
            );
            createFieldWithOneInfo("Cena wszystkich biletów",
                    String.format(
                            new Locale("pl", "PL"),
                            "%.2f",
                            reservation.getPrice()
                    ) + " zł"
            );

            this.document.close();

            toastUtils.showShortToast(context.getString(R.string.generatePdfInfo));

        } catch (IOException | DocumentException ie) {
            toastUtils.showShortToast(context.getString(R.string.generatePdfError));
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

            this.document.add(new Paragraph(""));
            this.document.add(new Chunk(this.lineSeparator));
            this.document.add(new Paragraph(""));
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
