package com.hzcu.order.dto;

public class LoginResponse {
    private String token;
    private Object user;

    public LoginResponse() {}

    public LoginResponse(String token, Object user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Object getUser() {
        return user;
    }

    public void setUser(Object user) {
        this.user = user;
    }

    public static LoginResponseBuilder builder() {
        return new LoginResponseBuilder();
    }

    public static class LoginResponseBuilder {
        private String token;
        private Object user;

        public LoginResponseBuilder token(String token) {
            this.token = token;
            return this;
        }

        public LoginResponseBuilder user(Object user) {
            this.user = user;
            return this;
        }

        public LoginResponse build() {
            return new LoginResponse(token, user);
        }
    }
}
