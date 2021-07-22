package com.grumpierodin.asap.controller;

import com.grumpierodin.asap.repository.data.RealtimeService;
import com.grumpierodin.asap.security.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class AuthenticateController {

    private final UserService userService;
    private final String realTimeServerUrl;
    public AuthenticateController(@Value("${app.real-time-server-url}") String realTimeServerUrl, UserService userService) {
        this.realTimeServerUrl = realTimeServerUrl;
        this.userService = userService;
    }
    @GetMapping("/api/realtime/service")
    public RealtimeService getRealtimeService() {
        String username = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String token = userService.getToken(username).get();
        return new RealtimeService(this.realTimeServerUrl, token);
    }
}
