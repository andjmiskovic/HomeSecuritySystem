package com.team4.secureit.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;

@Data
@Document(collection = "logs")
@AllArgsConstructor
@NoArgsConstructor
public class LogEntry {
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
}
