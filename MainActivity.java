package com.example.printerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {
    Button create_pdf;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        create_pdf = findViewById(R.id.btn_create_pdf);

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        create_pdf.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                createPDFFile(Comman.getAppPath(MainActivity.this)+"test_pdf");

                            }
                        });
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                });

    }

    private void createPDFFile(String path) {
        if (new File(path).exists()) new  File(path).delete();
        try {
            Document  document = new Document();
            //save
            PdfWriter.getInstance(document,new FileOutputStream(path));
            //open to write
            document.open();

            //setting
            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor("Mohit");
            document.addCreator("Sandeep");

            //Font Setting
            BaseColor color = new BaseColor(0,153,204,255);
            float font_size = 20.0f;
            float valueFont_size= 26.0f;
            BaseFont fontName=BaseFont.createFont("fontType.otf","UTF-8",BaseFont.EMBEDDED);

            Font titleFont =new Font(fontName,36.0f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Oder Details", Element.ALIGN_CENTER,titleFont);


            Font orderNumberFont =new Font(fontName,font_size,Font.NORMAL,BaseColor.RED);
            addNewItem(document,"Oder No", Element.ALIGN_LEFT,orderNumberFont);

            Font orderNumberValueFont =new Font(fontName,font_size,Font.NORMAL,BaseColor.RED);
            addNewItem(document,"Oder No", Element.ALIGN_LEFT,orderNumberValueFont);

            addLineSeperator(document);
            addNewItem(document,"Order Date",Element.ALIGN_LEFT,orderNumberFont);
            addNewItem(document,"01/01/2021",Element.ALIGN_LEFT,orderNumberValueFont);

            addLineSeperator(document);

            addNewItem(document,"Acc Name",Element.ALIGN_LEFT,orderNumberFont);
            addNewItem(document,"sandeep",Element.ALIGN_LEFT,orderNumberValueFont);

            addLineSeperator(document);



            ///Here Crate more methods to set more elements and and data with custom size and fonts
            //order detail
            //items
            //and many more

            document.close();
            Toast.makeText(MainActivity.this,"Success",Toast.LENGTH_LONG).show();
            //Print the PDF file
            printPDF();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addLineSeperator(Document document) throws DocumentException {
        LineSeparator lineSeparator=new LineSeparator();
        lineSeparator.setLineColor(BaseColor.GREEN);
        addLineSpace(document);
        document.add(new Chunk(lineSeparator));
        addLineSpace(document);
    }

    private void addLineSpace(Document document) throws DocumentException {
        document.add(new Paragraph(""));
    }

    private void printPDF() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            PrintManager printManager=(PrintManager) getSystemService(Context.PRINT_SERVICE);

            try {
                PrintDocumentAdapter printDocumentAdapter = new com.example.printer.pdfDocomentAdapter(MainActivity.this,Comman.getAppPath(MainActivity.this)+"test_pdf.pdf");
                printManager.print("Document",printDocumentAdapter,new PrintAttributes.Builder().build());
            }catch (Exception e){
                Log.e("abc",""+e.getMessage());
            }

        }
    }

    private void addNewItem(Document document, String text, int alignCenter,Font font) throws DocumentException {
        Chunk chunk = new Chunk(text);
        Paragraph  paragraph = new Paragraph(chunk);
        paragraph.setAlignment(alignCenter);
        document.add(paragraph);

    }
}