package com.team4.secureit.service;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.io.FileUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class PDFService {

    public static ByteArrayInputStream createPDF() throws FileNotFoundException {
        Document document = new Document(PageSize.A4);
        FileOutputStream file = new FileOutputStream("report.pdf");
        try {
            PdfWriter.getInstance(document, file);
            document.open();

            // Add a logo
            Image logo = Image.getInstance("src/main/resources/logo.png");
            logo.setAlignment(Image.ALIGN_CENTER);
            document.add(logo);

            // Add a title
            Paragraph title = new Paragraph("Report");
            document.add(title);

            // add rest here

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
        return new ByteArrayInputStream(FileUtils.readFileToByteArray(file));
    }
}
