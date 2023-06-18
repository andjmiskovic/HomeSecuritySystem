package com.team4.secureit.repository;

import com.team4.secureit.model.LogEntry;
import com.team4.secureit.model.LogSource;
import com.team4.secureit.model.LogType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public class CustomLogRepositoryImpl implements CustomLogRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<LogEntry> findAllByCriteria(String pattern, LogSource source, UUID sourceId, UUID userId, LogType type) {
        Criteria criteria = new Criteria();

        if (pattern != null)
            criteria = criteria.and("message").regex(pattern, "i");

        if (source != null)
            criteria = criteria.and("source").is(source);

        if (sourceId != null)
            criteria = criteria.and("sourceId").is(sourceId);

        if (userId != null)
            criteria = criteria.and("userId").is(userId);

        if (type != null)
            criteria = criteria.and("type").is(type);

        Query query = new Query(criteria);
        return mongoTemplate.find(query, LogEntry.class);
    }

    @Override
    public List<LogEntry> findUserLogsByCriteria(String pattern, UUID deviceId, UUID userId, LogType type) {
        Criteria criteria = new Criteria();

        if (pattern != null)
            criteria = criteria.and("message").regex(pattern, "i");

        if (deviceId != null)
            criteria = criteria.and("sourceId").is(deviceId);

        if (type != null)
            criteria = criteria.and("type").is(type);

        criteria = criteria.and("userId").is(userId);
        criteria = criteria.and("source").in(LogSource.DEVICE_MANAGEMENT, LogSource.DEVICE_MONITORING);

        Query query = new Query(criteria);
        return mongoTemplate.find(query, LogEntry.class);
    }

    @Override
    public List<LogEntry> findAllForDeviceInTimeRange(Date startDate, Date endDate, UUID deviceId) {
//        Query query = new Query();
//        if (startDate != null)
//            query.


            return null;
    }
}
