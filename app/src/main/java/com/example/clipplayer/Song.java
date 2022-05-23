package com.example.clipplayer;

public class Song {
    private String name;
    private String genre;
    private String uid;
    private String path;
    private byte[] data;
    public Song(String name, String genre, String uid, byte[] data, String path) {
        this.name = name;
        this.genre = genre;
        this.uid = uid;
        this.data = data;
        this.path = path;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
