package com.grumpierodin.asap.security;

import javax.validation.constraints.NotNull;

/**
 * Adapted from examples by Mary Ellen Bowman
 */
public class LoginDto {
    @NotNull
    private String username;

    @NotNull
    private String password;

    /**
     * Default constructor
     */
    protected LoginDto() {
    }

    /**
     * Full constructor
     * @param username
     * @param password
     */
    public LoginDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
