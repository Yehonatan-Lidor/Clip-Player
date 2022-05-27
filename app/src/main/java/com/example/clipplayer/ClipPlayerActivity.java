package com.example.clipplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class ClipPlayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clip_player);
        ArrayList<String> genreList = new ArrayList<String>(Arrays.asList(new String[]{"song1", "song2", "song3", "song4", "song5"}));
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, genreList);

        ListView listView = (ListView) findViewById(R.id.list_library);
        listView.setAdapter(adapter);
    }

    public void musicNext(View view) {
    }

    public void musicplay(View view) {
    }

    public void musicpause(View view) {
    }

    public void musicPrev(View view) {
    }
}