package com.grumpierodin.asap.websocket;

import com.grumpierodin.asap.repository.data.Alert;
import com.grumpierodin.asap.utils.JsonUtils;
import com.grumpierodin.asap.websocket.model.AlertMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AlertUpdater {
    private final Logger LOGGER = LoggerFactory.getLogger(AlertUpdater.class);

    public void AlertUpdated(Alert alert) {
        AlertMessage message = new AlertMessage("alertUpdate", alert);
        LOGGER.info("In subscriptionHub update {}", message);
        SubscriptionHub.send("/api/realtime/alert", JsonUtils.toJson(message));
        LOGGER.info("Ending subscriptionHub update {}", message);
    }
    public void AlertAdded(Alert alert) {
        AlertMessage message = new AlertMessage("alertAdded", alert);
        LOGGER.info("In subscriptionHub Added {}", message);
        SubscriptionHub.send("/api/realtime/alert", JsonUtils.toJson(message));
        LOGGER.info("Ending subscriptionHub Added {}", message);
    }
}
