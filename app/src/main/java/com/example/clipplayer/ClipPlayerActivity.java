package com.example.clipplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class ClipPlayerActivity extends AppCompatActivity {
    private Song[] songs;
    private String genre;
    private MediaPlayer mediaPlayer;
    private DBHelper dbHelper;
    private FirebaseAuth mAuth;
    private int index;
    private static boolean isOver = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clip_player);
        this.mAuth = FirebaseAuth.getInstance();
        this.dbHelper = new DBHelper(this);
        this.genre = this.getIntent().getStringExtra("genre");
        this.songs = this.dbHelper.selectAllGenre(this.mAuth.getUid(), this.genre).toArray(new Song[0]);


        this.mediaPlayer = new MediaPlayer();
        try {
            // play first song
            Log.d("PlayClip",  (this.songs[0].getPath()));
            this.mediaPlayer.setDataSource( this.songs[0].getPath()  );
            this.mediaPlayer.prepare();
            this.mediaPlayer.start();
            this.index = 0;
        } catch (IOException e) {
            Toast.makeText(this, "File doesn't exist!!", Toast.LENGTH_LONG).show();
        }

        ArrayList<String> songList = new ArrayList<>();
        for(Song s: this.songs)
        {
            songList.add(s.getName());
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, songList);

        ListView listView = (ListView) findViewById(R.id.list_library);
        listView.setAdapter(adapter);


    }

    public void nextClip()
    {
        this.index++;
        if(this.index == this.songs.length)
            this.index = 0;

        if(this.mediaPlayer.isPlaying()) {
            this.mediaPlayer.stop();
            this.mediaPlayer.release();
        }
        this.mediaPlayer = new MediaPlayer();
        try {
            // play first song
            this.mediaPlayer.setDataSource( this.songs[this.index].getPath());
            this.mediaPlayer.prepare();
            this.mediaPlayer.start();
            this.index = 0;
        } catch (IOException e) {
            Toast.makeText(this, "File doesn't exist!!", Toast.LENGTH_LONG).show();
        }
    }

    public void musicNext(View view) {
        this.nextClip();
    }

    public void musicplay(View view) {
        this.mediaPlayer.start();
    }

    public void musicpause(View view) {
        this.mediaPlayer.pause();
    }

    public void musicPrev(View view) {
        this.index--;
        if(this.index < 0)
            this.index = this.songs.length - 1;

        if(this.mediaPlayer.isPlaying()) {
            this.mediaPlayer.stop();
            this.mediaPlayer.release();
        }

        this.mediaPlayer = new MediaPlayer();
        try {
            // play first song
            this.mediaPlayer.setDataSource( this.songs[this.index].getPath());
            this.mediaPlayer.prepare();
            this.mediaPlayer.start();
            this.index = 0;
        } catch (IOException e) {
            Toast.makeText(this, "File doesn't exist!!", Toast.LENGTH_LONG).show();
        }
    }
}