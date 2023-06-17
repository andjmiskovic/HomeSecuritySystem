package com.team4.secureit.service;

import com.team4.secureit.exception.ForbiddenSearchCriteriaException;
import com.team4.secureit.model.*;
import com.team4.secureit.repository.DeviceRepository;
import com.team4.secureit.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LogService {

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    public void log(String message, LogType type) {
        LogEntry entry = new LogEntry(message, type);
        logRepository.save(entry);
    }

    public void log(String message, LogSource source, LogType type) {
        LogEntry entry = new LogEntry(message, source, type);
        logRepository.save(entry);
    }

    public void log(String message, LogSource source, UUID sourceId, LogType type) {
        LogEntry entry = new LogEntry(message, source, sourceId, type);
        logRepository.save(entry);
    }

    public void log(String message, LogSource source, UUID sourceId, UUID userId, LogType type) {
        LogEntry entry = new LogEntry(message, source, sourceId, userId, type);
        logRepository.save(entry);
    }

    public List<LogEntry> findByCriteria(String message, LogSource source, UUID sourceId, UUID userId, LogType type, User user) {
        if (user.getRole().equals(Role.ROLE_ADMIN))
            return logRepository.findByCriteria(message, source, sourceId, userId, type);

        if (userId != null && !userId.equals(user.getId()))
            throw new ForbiddenSearchCriteriaException();

        if (source != null && (source.equals(LogSource.DEVICE_MANAGEMENT) || source.equals(LogSource.DEVICE_MONITORING))) {
            PropertyOwner owner = (PropertyOwner) user;
            deviceRepository.findByUserAndId(owner, sourceId).orElseThrow(ForbiddenSearchCriteriaException::new);
        }

        return logRepository.findByCriteria(message, source, sourceId, userId, type);
    }
}
