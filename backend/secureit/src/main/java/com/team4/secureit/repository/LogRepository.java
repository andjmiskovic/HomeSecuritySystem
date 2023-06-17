package com.team4.secureit.repository;

import com.team4.secureit.model.LogEntry;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface LogRepository extends MongoRepository<LogEntry, UUID>, CustomLogRepository {
}
