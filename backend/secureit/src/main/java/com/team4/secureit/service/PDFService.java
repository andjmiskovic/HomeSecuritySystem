package com.team4.secureit.service;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.team4.secureit.model.LogEntry;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

@Service
public class PDFService {

    public ByteArrayInputStream createPDF(List<LogEntry> infoLogs, List<LogEntry> warningLogs, List<LogEntry> errorLogs) throws IOException {
        Document document = new Document(PageSize.A4);
        FileOutputStream output = new FileOutputStream("src/main/resources/report.pdf");
        try {
            PdfWriter.getInstance(document, output);
            document.open();

            Paragraph title = new Paragraph("REPORT");
            document.add(title);

            Paragraph infos = new Paragraph("INFO");
            document.add(infos);
            for (LogEntry logEntry : infoLogs) {
                Paragraph info = new Paragraph(logEntry.getTimestamp() + " :: " + logEntry.getMessage());
                document.add(info);
            }

            Paragraph warnings = new Paragraph("WARNINGS");
            document.add(warnings);
            for (LogEntry logEntry : warningLogs) {
                Paragraph warning = new Paragraph(logEntry.getTimestamp() + " :: " + logEntry.getMessage());
                document.add(warning);
            }

            Paragraph errors = new Paragraph("ERRORS");
            document.add(errors);
            for (LogEntry logEntry : errorLogs) {
                Paragraph error = new Paragraph(logEntry.getTimestamp() + " :: " + logEntry.getMessage());
                document.add(error);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
            output.close();
        }

        // Read the PDF file into a byte array
        File readFile = new File("src/main/resources/report.pdf");
        FileInputStream fileInputStream = new FileInputStream(readFile);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, bytesRead);
        }
        fileInputStream.close();

        // Create a ByteArrayInputStream from the byte array
        byte[] pdfBytes = byteArrayOutputStream.toByteArray();
        return new ByteArrayInputStream(pdfBytes);
    }
}
