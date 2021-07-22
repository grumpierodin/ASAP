package com.grumpierodin.asap.controller;

import com.grumpierodin.asap.repository.EventRepository;
import com.grumpierodin.asap.repository.data.Alert;
import com.grumpierodin.asap.repository.data.Event;
import com.grumpierodin.asap.services.AlertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
public class EventController {
    private static final Logger log = LoggerFactory.getLogger(EventController.class);
    @Autowired
    EventRepository eventRepository;
    @Autowired
    AlertService alertService;

    @GetMapping("/api/alert")
    public List<Alert> getAlertEvents() {
        List<Alert> alerts = alertService.findPublishedEventsPublished();
        Collections.sort(alerts);
        return alerts;
    }

    @PostMapping(path = "/api/event", consumes = "application/json", produces = "application/json")
    public void processEvent(@RequestBody Event event) {
        log.debug("posting.... "+ event.toString());
        alertService.process(event);
    }
    @PostMapping(path = "/api/alert", consumes = "application/json", produces = "application/json")
    public void processAlertUpdate(@RequestBody Alert alert) {
        log.info("posting alert update.... "+ alert.toString());
        alertService.update(alert);
    }
}
