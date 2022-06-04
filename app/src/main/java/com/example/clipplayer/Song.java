package com.example.clipplayer;

public class Song {
    private String name;
    private String genre;
    private String uid;
    private String path;


    public Song(String name, String genre, String uid, String path) {
        this.name = name;
        this.genre = genre;
        this.uid = uid;
        this.path = path;
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
