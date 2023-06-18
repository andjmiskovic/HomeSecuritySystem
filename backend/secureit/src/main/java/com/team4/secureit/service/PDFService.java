package com.team4.secureit.service;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.team4.secureit.model.LogEntry;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

@Service
public class PDFService {

    public ByteArrayInputStream createPDF(List<LogEntry> warningLogs, List<LogEntry> errorLogs) throws IOException {
        Document document = new Document(PageSize.A4);
        FileOutputStream file = new FileOutputStream("src/main/resources/report.pdf");
        try {
            PdfWriter.getInstance(document, file);
            document.open();

            Image logo = Image.getInstance("src/main/resources/logo.png");
            logo.setAlignment(Image.ALIGN_CENTER);
            document.add(logo);

            Paragraph title = new Paragraph("Report");
            document.add(title);

            Paragraph warnings = new Paragraph("Warnings");
            document.add(warnings);
            for (LogEntry logEntry : warningLogs) {
                Paragraph warning = new Paragraph(logEntry.getType() + " :: " + logEntry.getTimestamp() + " :: " + logEntry.getMessage());
                document.add(warning);
            }
            Paragraph errors = new Paragraph("Errors");
            document.add(errors);
            for (LogEntry logEntry : errorLogs) {
                Paragraph error = new Paragraph(logEntry.getType() + " :: " + logEntry.getTimestamp() + " :: " + logEntry.getMessage());
                document.add(error);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
        File readFile = new File("src/main/resources/report.pdf");
        String data = FileUtils.readFileToString(readFile, "UTF-8");
        System.out.println(data);
        return new ByteArrayInputStream(FileUtils.readFileToByteArray(readFile));
    }
}
