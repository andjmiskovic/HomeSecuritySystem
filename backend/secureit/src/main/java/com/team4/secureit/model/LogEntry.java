package com.team4.secureit.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Data
@Document(collection = "logs")
@AllArgsConstructor
@NoArgsConstructor
public class LogEntry {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_CYAN = "\u001B[36m";

    @Id
    private UUID id = UUID.randomUUID();

    private String message;

    private LogSource source;

    private UUID sourceId;

    private UUID userId;

    private LogType type;

    private Instant timestamp = Instant.now();

    public LogEntry(String message, LogType type) {
        this.message = message;
        this.type = type;
    }

    public LogEntry(String message, LogSource source, LogType type) {
        this.message = message;
        this.source = source;
        this.type = type;
    }

    public LogEntry(String message, LogSource source, UUID sourceId, LogType type) {
        this.message = message;
        this.source = source;
        this.sourceId = sourceId;
        this.type = type;
    }

    public LogEntry(String message, LogSource source, UUID sourceId, UUID userId, LogType type) {
        this.message = message;
        this.source = source;
        this.sourceId = sourceId;
        this.userId = userId;
        this.type = type;
    }

    @Override
    public String toString() {
        String formattedTimestamp = timestamp.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        return getColorByType() + formattedTimestamp + "  " + type + " [" + source + "] : " + message + ANSI_RESET;
    }

    private String getColorByType() {
        return switch (type.name()) {
            case "INFO" -> ANSI_CYAN;
            case "WARNING" -> ANSI_YELLOW;
            case "ERROR" -> ANSI_RED;
            default -> ANSI_RESET;
        };
    }
}
