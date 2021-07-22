package com.grumpierodin.asap.repository.data;

public class RealtimeService {
    String realTimeServerUrl;
    public String token;

    public RealtimeService(String realTimeServerUrl, String token) {
        this.realTimeServerUrl = realTimeServerUrl;
        this.token = token;
    }

    public String getRealTimeServerUrl() {
        return realTimeServerUrl;
    }

    public void setRealTimeServerUrl(String realTimeServerUrl) {
        this.realTimeServerUrl = realTimeServerUrl;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "RealtimeService{" +
                "realTimeServerUrl='" + realTimeServerUrl + '\'' +
                '}';
    }
}
