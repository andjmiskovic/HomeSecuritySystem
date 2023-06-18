package com.team4.secureit.service;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.team4.secureit.dto.request.ReportRequest;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class PDFService {

    public ByteArrayInputStream createPDF(ReportRequest reportRequest) throws IOException {
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

            Paragraph warnings = new Paragraph();

            // add rest here

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
        return new ByteArrayInputStream(FileUtils.readFileToByteArray(new File(String.valueOf(file))));
    }
}
