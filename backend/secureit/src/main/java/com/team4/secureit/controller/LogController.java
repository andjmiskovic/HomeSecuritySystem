package com.team4.secureit.controller;

import com.team4.secureit.model.LogEntry;
import com.team4.secureit.model.LogSource;
import com.team4.secureit.model.LogType;
import com.team4.secureit.model.User;
import com.team4.secureit.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/logs")
public class LogController {

    @Autowired
    private LogService logService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PROPERTY_OWNER')")
    public List<LogEntry> getLogsByCriteria(
            @RequestParam(required = false) String regex,
            @RequestParam(required = false) LogSource source,
            @RequestParam(required = false) UUID sourceId,
            @RequestParam(required = false) UUID userId,
            @RequestParam(required = false) LogType type,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        return logService.findByCriteria(regex, source, sourceId, userId, type, user);
    }
}
