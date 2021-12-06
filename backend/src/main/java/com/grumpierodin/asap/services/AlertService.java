package com.grumpierodin.asap.services;

import com.grumpierodin.asap.repository.AlertRepository;
import com.grumpierodin.asap.repository.EventRepository;
import com.grumpierodin.asap.repository.RuleRepository;
import com.grumpierodin.asap.repository.data.*;
import com.grumpierodin.asap.websocket.AlertUpdater;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/* useful info on JPA Repository/Service method naming.
 *  https://docs.spring.io/spring-data/jpa/docs/1.5.0.RELEASE/reference/html/jpa.repositories.html
 */

@Component
public class AlertService {
    private final Logger LOGGER = LoggerFactory.getLogger(AlertService.class);
    @Autowired
    RuleService ruleService;
    @Autowired
    RuleRepository ruleRepository;
    @Autowired
    AlertRepository alertRepository;
    @Autowired
    EventRepository eventRepository;
    @Autowired
    AlertUpdater alertUpdater;
/*
 *  generically loop through all rules for a match, when matched save alert then check if any alert filters apply
 *  most processing is in Alert.class whith alert filtering being achieved in AlertService.
 */
    public void process(Event event) {
        boolean someMatched = false;
        Event tempEvent = event;
        for(Rule rule : ruleService.getAllRules()) {
            Alert alert = new Alert(tempEvent, rule);
            alert.getEvent().setAlert(alert);
            alert.renderAlert();
            if(alert.getAlertCorrelationId()!=null) {
                alertRepository.save(alert);
                eventRepository.save(tempEvent);
                if (alert.isAlertAble() && !isAlertFiltered(alert)) {
                    alert.setPublish(true);
                    LOGGER.info("Alert : {}", alert);
                    alertRepository.save(alert);
                    publish(alert);
                    tempEvent = new Event(tempEvent.getMessage(), tempEvent.getTimestamp().toString(), tempEvent.getHost(), tempEvent.getApplication());
                }
            }
        }
    }

    void publish(Alert alert) {
        Optional<Alert> originalAlert = findById(alert.getUuid());
        if(originalAlert.isPresent()) {
            LOGGER.info("Alert is            {}", alert);
            LOGGER.info("originalAlert in db {}", originalAlert.get());
            alertUpdater.AlertAdded(originalAlert.get());
        }
    }

    public Optional<Alert> findById(String id) {
        return alertRepository.findById(id);
    }

    public List<Alert> findPublishedEventsPublished() { return alertRepository.findPublishedEventsPublished(); }

    public void update(Alert alert) {
        Optional<Alert> originalAlert = findById(alert.getUuid());
        if(originalAlert.isPresent()) {
            originalAlert.get().setAlertLevel(alert.getAlertLevel());
            originalAlert.get().setAlertState(alert.getAlertState());
            LOGGER.info("originalAlert in db {}", originalAlert.get());
            alertRepository.save(originalAlert.get());
            alertUpdater.AlertUpdated(originalAlert.get());
        }
    }
    public List<Alert> findEventsByTimestampBetweenAndRuleId(LocalDateTime from, LocalDateTime to, String ruleId, String correlationId) {
        List<Alert> tempdata = alertRepository.findEventsByTimestampBetweenAndRuleId(from, to, ruleId, correlationId);
        return tempdata;
    }

    public boolean hasNonExpiredAlerts(Alert alert, AlertFilter filter) {
        LOGGER.debug("hasNonExpiredAlerts");
        LOGGER.debug("alert  {}", alert);
        String ruleId = alert.getRule().getUuid();
        String correlationId = alert.getAlertCorrelationId();
        LocalDateTime alertExpiry = alert.getEvent().getTimestamp();
        LocalDateTime alertDate = alert.getEvent().getTimestamp().minusSeconds(filter.getExpirySeconds());
        List<Alert> alerts = findEventsByTimestampBetweenAndRuleId(alertDate, alertExpiry, ruleId, correlationId);
        return alerts.size()>=filter.getExpiryThreshold();
    }

    public boolean isAlertFiltered(Alert alert) {
        if(alert.getRule().getAlertFilters()!=null){
            boolean filtered = false;
            for(AlertFilter filter : alert.getRule().getAlertFilters()) {
                if(hasNonExpiredAlerts(alert, filter)) {
                    if(filter.getAlertFilterType()== AlertFilterType.Suppress) {
                        filtered=true;
                    }
                }
                if(!hasNonExpiredAlerts(alert, filter)) {
                    if(filter.getAlertFilterType()==AlertFilterType.Breach) {
                        filtered=true;
                    } else {
                        filtered=false;
                    }
                }
            }
            return filtered;
        }
        return false;
    }
}
