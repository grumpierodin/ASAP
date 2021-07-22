package com.grumpierodin.asap.websocket;

import com.grumpierodin.asap.security.AsapUserDetailsService;
import com.grumpierodin.asap.security.repository.UserRepository;
import com.grumpierodin.asap.utils.JsonUtils;
import com.grumpierodin.asap.websocket.channels.ChannelHandlerInvoker;
import com.grumpierodin.asap.websocket.channels.ChannelHandlerResolver;
import com.grumpierodin.asap.websocket.model.IncomingMessage;
import com.grumpierodin.asap.websocket.model.RealTimeSession;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Optional;

@Component
public class SocketTextHandler extends TextWebSocketHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(SocketTextHandler.class);
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected AsapUserDetailsService userService;
    private ChannelHandlerResolver channelHandlerResolver;
    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) {
        LOGGER.debug("WebSocket connection established");

        RealTimeSession session = new RealTimeSession(webSocketSession);
        String token = session.getToken();

        try {
            Optional<UserDetails> user = userService.loadUserByJwtToken(token);
            if(user.isPresent()) {
                long userId = userRepository.findByUsername(user.get().getUsername()).get().getId();
                session.setUserId(userId);
                session.reply("authenticated");
            } else {
                LOGGER.debug("Invalid JWT token value: {}", token);
                session.fail("authentication failed");
            }
        } catch (JwtException exception) {
            LOGGER.debug("Invalid JWT token value: {}", token);
            session.fail("authentication failed");
        }
    }
    public SocketTextHandler(UserRepository userRepository, AsapUserDetailsService userService,
                                      ChannelHandlerResolver channelHandlerResolver) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.channelHandlerResolver = channelHandlerResolver;
    }
    @Override
    protected void handleTextMessage(WebSocketSession webSocketSession, TextMessage message) {
        RealTimeSession session = new RealTimeSession(webSocketSession);
        LOGGER.debug("RealTimeSession[{}] Received message `{}`", session.id(), message.getPayload());

        IncomingMessage incomingMessage = JsonUtils.jsonToObject(message.getPayload(), IncomingMessage.class);
        if (incomingMessage == null) {
            session.error("Illegal format of incoming message: " + message.getPayload());
            return;
        }

        ChannelHandlerResolver channelHandlerResolve;
        ChannelHandlerInvoker invoker = channelHandlerResolver.findInvoker(incomingMessage);
        if (invoker == null) {
            String errorMessage = "No handler found for action `" + incomingMessage.getAction() +
                    "` at channel `" + incomingMessage.getChannel() + "`";
            session.error(errorMessage);
            LOGGER.error("RealTimeSession[{}] {}", session.id(), errorMessage);
            return;
        }
        if(session.wrapped().isOpen()) {
            invoker.handle(incomingMessage, session);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus status) {
        RealTimeSession session = new RealTimeSession(webSocketSession);
        SubscriptionHub.unsubscribeAll(session);
        LOGGER.debug("RealTimeSession[{}] Unsubscribed all channels after disconnecting", session.id());
    }
}