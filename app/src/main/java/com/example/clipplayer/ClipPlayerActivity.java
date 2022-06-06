package com.example.clipplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class ClipPlayerActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, MediaPlayer.OnCompletionListener {

    private Song[] songs;
    private String genre;
    private MediaPlayer mediaPlayer;
    private DBHelper dbHelper;
    private FirebaseAuth mAuth;
    private int index;
    private TextView currentSong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clip_player);

        this.mAuth = FirebaseAuth.getInstance();
        this.currentSong = findViewById(R.id.clip_playing);
        this.dbHelper = new DBHelper(this);
        this.genre = this.getIntent().getStringExtra("genre");
        this.songs = this.dbHelper.selectAllGenre(this.mAuth.getUid(), this.genre).toArray(new Song[0]);

        // play the first clip
        this.playClip();

        // create listview of clips in rotation
        ArrayList<String> songList = new ArrayList<>();
        for(Song s: this.songs)
        {
            songList.add(s.getName());
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, songList);

        ListView listView = (ListView) findViewById(R.id.list_library);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);



    }

    // function called when user chooses a clip from the listview
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.index = position;
        this.playClip();
    }

    // function called when user presses the next clip button
    public void musicNext(View view) {
        // update index
        this.index++;
        if(this.index == this.songs.length)
            this.index = 0;
        this.playClip();
    }

    // continues to play clip
    public void musicplay(View view) {
        this.mediaPlayer.start();
    }

    // pauses current clip
    public void musicpause(View view) {
        this.mediaPlayer.pause();
    }

    // moves to previous song by moving the index two steps back and skipping to the next song
    public void musicPrev(View view) {
        this.index--;
        if(this.index < 0)
            this.index = this.songs.length - 1;
        this.playClip();
    }

    public void updateCurrentClip()
    {
        this.currentSong.setText("Current Clip: " + this.songs[index].getName());
    }

    public void playClip()
    {
        this.updateCurrentClip(); // update textview

        // release media player if it exists
        if(this.mediaPlayer != null) {
            this.mediaPlayer.stop();
            this.mediaPlayer.release();
        }

        // create new media player
        this.mediaPlayer = new MediaPlayer();
        // set function to call automaticly when clip ends
        this.mediaPlayer.setOnCompletionListener(this);
        try {
            // prepare playing clip
            this.mediaPlayer.setDataSource( this.songs[this.index].getPath());
            this.mediaPlayer.prepare();
            this.mediaPlayer.start();
        } catch (IOException e) {
            Toast.makeText(this, "File doesn't exist!!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            Log.e("PlayClipErr", e.getMessage());
        }
    }

    // function automaticly called when clips stops playing, skips to the next clip in rotation
    @Override
    public void onCompletion(MediaPlayer mp) {
        // update index
        this.index++;
        if(this.index == this.songs.length)
            this.index = 0;
        this.playClip();

    }

    @Override
    public void onBackPressed() {
        if(this.mediaPlayer != null)
        {
            this.mediaPlayer.stop();
            this.mediaPlayer.release();
        }
        super.onBackPressed();
    }


}