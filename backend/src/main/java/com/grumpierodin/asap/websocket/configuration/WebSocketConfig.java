package com.grumpierodin.asap.websocket.configuration;

import com.grumpierodin.asap.websocket.SocketTextHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Value("${app.cors.urls}")
    String urls;
    @Value("${app.real-time-server-url}")
    String realTimeServerUrl;
    private SocketTextHandler requestDispatcher;

    public WebSocketConfig(SocketTextHandler requestDispatcher) {
        this.requestDispatcher = requestDispatcher;
    }
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        String[] corsUrls = urls.split(",");
        registry.addHandler(requestDispatcher, realTimeServerUrl).setAllowedOrigins(corsUrls).withSockJS();
    }
}
