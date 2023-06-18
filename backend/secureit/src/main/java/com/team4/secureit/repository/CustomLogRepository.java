package com.team4.secureit.repository;

import com.team4.secureit.model.LogEntry;
import com.team4.secureit.model.LogSource;
import com.team4.secureit.model.LogType;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface CustomLogRepository {

    List<LogEntry> findAllByCriteria(String message, LogSource source, UUID sourceId, UUID userId, LogType type);

    List<LogEntry> findUserLogsByCriteria(String pattern, UUID deviceId, UUID userId, LogType type);

    List<LogEntry> findAllForDeviceInTimeRange(Date startDate, Date endDate, UUID deviceId);
}
