package com.team4.secureit.service;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.team4.secureit.model.LogEntry;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class PDFService {

    public ResponseEntity<Resource> createPDF(List<LogEntry> infoLogs, List<LogEntry> warningLogs, List<LogEntry> errorLogs) throws IOException {
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

            Paragraph infos = new Paragraph("Info");
            document.add(infos);
            for (LogEntry logEntry : infoLogs) {
                Paragraph info = new Paragraph(logEntry.getType() + " :: " + logEntry.getTimestamp() + " :: " + logEntry.getMessage());
                document.add(info);
            }

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
        return getPdf("src/main/resources/report.pdf");
    }

    public ResponseEntity<Resource> getPdf(String filename) throws IOException {
        Path storedFilePath = Paths.get("").resolve(filename);

        Resource resource = new UrlResource(storedFilePath.toUri());
        if (!resource.exists() || !resource.isReadable())
            throw new FileNotFoundException("File not found.");

        String extension = FilenameUtils.getExtension(filename); // dot not included (i.e. "png")

        return ResponseEntity.ok()
                .contentType(extensionToMediaType(extension))
                .body(resource);
    }

    private MediaType extensionToMediaType(String extension) {
        switch (extension) {
            case "png":
                return MediaType.IMAGE_PNG;
            case "jpg":
            case "jpeg":
                return MediaType.IMAGE_JPEG;
            case "pdf":
                return MediaType.APPLICATION_PDF;
            default:
                return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}
