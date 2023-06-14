package com.team4.secureit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team4.secureit.dto.request.DeviceMessage;
import com.team4.secureit.service.DeviceMonitoringService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static com.team4.secureit.util.HttpRequestUtils.getRawRequestBody;

@RestController
@RequestMapping(path = "/monitor")
public class DeviceMonitoringController {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private DeviceMonitoringService deviceMonitoringService;

    @PostMapping("/send")
    public void processMessage(@RequestParam(value = "signature") String signature, HttpServletRequest request) throws IOException {
        String rawRequestBody = getRawRequestBody(request);
        DeviceMessage message = objectMapper.readValue(rawRequestBody, DeviceMessage.class);

        deviceMonitoringService.processMessage(message, rawRequestBody, signature);
    }

}
