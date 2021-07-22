package com.grumpierodin.asap.websocket;

import com.grumpierodin.asap.websocket.channels.Action;
import com.grumpierodin.asap.websocket.channels.ChannelHandler;
import com.grumpierodin.asap.websocket.channels.ChannelValue;
import com.grumpierodin.asap.websocket.model.RealTimeSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ChannelHandler("/api/realtime/alert")
public class AlertChannelHandler {
    private static final Logger log = LoggerFactory.getLogger(AlertChannelHandler.class);
    @Action("subscribe")
    public void subscribe(RealTimeSession session, @ChannelValue String channel) {
        log.debug("RealTimeSession[{}] Subscribe to channel `{}`", session.id(), channel);
        SubscriptionHub.subscribe(session, channel);
    }

    @Action("unsubscribe")
    public void unsubscribe(RealTimeSession session, @ChannelValue String channel) {
        log.debug("RealTimeSession[{}] Unsubscribe from channel `{}`", session.id(), channel);
        SubscriptionHub.unsubscribe(session, channel);
    }
}
