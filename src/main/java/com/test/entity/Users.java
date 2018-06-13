package com.test.entity;

public class Users {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public Users(){}

    public Users(String username, String password) {
        this.username = username;
        this.password = password;
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
