package com.team4.secureit.repository;

import com.team4.secureit.model.LogEntry;
import com.team4.secureit.model.LogSource;
import com.team4.secureit.model.LogType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
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
    public List<LogEntry> findAllForDeviceInTimeRange(Date startDate, Date endDate, LogType type, UUID deviceId) {
        Query query = new Query();
        if (deviceId != null)
            query.addCriteria(Criteria.where("sourceId").is(deviceId));
        if (startDate != null && endDate != null)
            if (startDate.equals(endDate))
                query.addCriteria(Criteria.where("timestamp").gte(atStartOfDay(startDate)).lte(atEndOfDay(endDate)));
            else
                query.addCriteria(Criteria.where("timestamp").gte(startDate).lte(endDate));
        if (type != null)
            query.addCriteria(Criteria.where("type").is(type));
        return mongoTemplate.find(query, LogEntry.class);
    }

    private static Date atStartOfDay(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        return localDateTimeToDate(startOfDay);
    }

    private static Date atEndOfDay(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
        return localDateTimeToDate(endOfDay);
    }

    private static LocalDateTime dateToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    private static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
