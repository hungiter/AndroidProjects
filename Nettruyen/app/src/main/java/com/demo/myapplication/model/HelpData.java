package com.demo.myapplication.model;

public class HelpData {
    String email, username, pass, access;

    public HelpData(String email, String username, String pass) {
        this.email = email;
        this.username = username;
        this.pass = pass;
        this.access = "none";
    }
    public HelpData(String email, String username, String pass, String access) {
        this.email = email;
        this.username = username;
        this.pass = pass;
        this.access = access;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }
}
