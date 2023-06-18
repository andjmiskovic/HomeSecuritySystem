package com.team4.secureit.service;

import com.team4.secureit.model.*;
import com.team4.secureit.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class LogService {

    @Autowired
    private LogRepository logRepository;

    public void log(String message, LogType type) {
        LogEntry entry = new LogEntry(message, type);
        System.out.println(entry);
        logRepository.save(entry);
    }

    public void log(String message, LogSource source, LogType type) {
        LogEntry entry = new LogEntry(message, source, type);
        System.out.println(entry);
        logRepository.save(entry);
    }

    public void log(String message, LogSource source, UUID sourceId, LogType type) {
        LogEntry entry = new LogEntry(message, source, sourceId, type);
        System.out.println(entry);
        logRepository.save(entry);
    }

    public void log(String message, LogSource source, UUID sourceId, UUID userId, LogType type) {
        LogEntry entry = new LogEntry(message, source, sourceId, userId, type);
        System.out.println(entry);
        logRepository.save(entry);
    }

    public List<LogEntry> findByCriteria(String pattern, LogSource source, UUID sourceId, UUID userId, LogType type, User user) {
        if (user.getRole().equals(Role.ROLE_ADMIN))
            return logRepository.findAllByCriteria(pattern, source, sourceId, userId, type);

        return logRepository.findUserLogsByCriteria(pattern, sourceId, user.getId(), type);
    }

    public List<LogEntry> findAllForDeviceInTimeRange(Date startDate, Date endDate, LogType type, UUID deviceId) {
        return logRepository.findAllForDeviceInTimeRange(startDate, endDate, type, deviceId);
    }
}
