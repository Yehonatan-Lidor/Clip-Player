package com.example.clipplayer;

public class Song {
    private String name;
    private String genre;
    private String uid;
    private byte[] data;
    public Song(String name, String genre, String uid, byte[] data) {
        this.name = name;
        this.genre = genre;
        this.uid = uid;
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
