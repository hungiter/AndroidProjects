package com.demo.myapplication.model;

import java.util.ArrayList;

public class DataTheoDoi {
    private String user;
    private ArrayList<String> truyenTranh;

    public DataTheoDoi(String user, ArrayList<String> truyenTranh) {
        this.user = user;
        this.truyenTranh = truyenTranh;
    }

    public DataTheoDoi(String user) {
        this.user = user;
        this.truyenTranh = new ArrayList<>();
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void addTruyen(String text){
        this.truyenTranh.add(text);
    }

    public void removeTruyen(String text){
        this.truyenTranh.remove(text);
    }

    public ArrayList<String> getTruyen() {
        return truyenTranh;
    }

    public void setTruyen(ArrayList<String> truyenTranh) {
        this.truyenTranh = truyenTranh;
    }
}
