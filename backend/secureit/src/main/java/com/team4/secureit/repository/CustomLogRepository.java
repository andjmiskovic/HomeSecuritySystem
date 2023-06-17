package com.team4.secureit.repository;

import com.team4.secureit.model.LogEntry;
import com.team4.secureit.model.LogSource;
import com.team4.secureit.model.LogType;

import java.util.List;
import java.util.UUID;

public interface CustomLogRepository {

    List<LogEntry> findByCriteria(String message, LogSource source, UUID sourceId, UUID userId, LogType type);

}
