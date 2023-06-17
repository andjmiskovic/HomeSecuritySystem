package com.team4.secureit.repository;

import com.team4.secureit.model.LogEntry;
import com.team4.secureit.model.LogSource;
import com.team4.secureit.model.LogType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class CustomLogRepositoryImpl implements CustomLogRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<LogEntry> findAllByCriteria(String pattern, LogSource source, UUID sourceId, UUID userId, LogType type) {
        Query query = new Query();

        if (pattern != null)
            query.addCriteria(Criteria.where("message").regex(pattern, "i"));

        if (source != null)
            query.addCriteria(Criteria.where("source").is(source));

        if (sourceId != null)
            query.addCriteria(Criteria.where("sourceId").is(sourceId));

        if (userId != null)
            query.addCriteria(Criteria.where("userId").is(userId));

        if (type != null)
            query.addCriteria(Criteria.where("type").is(type));

        return mongoTemplate.find(query, LogEntry.class);
    }

    @Override
    public List<LogEntry> findUserLogsByCriteria(String pattern, UUID deviceId, UUID userId, LogType type) {
        Query query = new Query();

        if (pattern != null)
            query.addCriteria(Criteria.where("message").regex(pattern, "i"));

        if (deviceId != null)
            query.addCriteria(Criteria.where("sourceId").is(deviceId));
        else
            query.addCriteria(Criteria.where("userId").is(userId));

        if (type != null)
            query.addCriteria(Criteria.where("type").is(type));

        query.addCriteria(Criteria.where("source").in(LogSource.DEVICE_MANAGEMENT, LogSource.DEVICE_MONITORING));

        return mongoTemplate.find(query, LogEntry.class);
    }
}
