package com.example.clipplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;

public class ChooseGenreActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private FirebaseAuth mAuth;
    private String[]genres = {"Blues", "Classical", "Country", "Disco", "Hiphop", "Jazz", "Metal", "Pop", "Reggae", "Rock"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_genre);
        this.mAuth = FirebaseAuth.getInstance();
        ArrayList<String> genreList = new ArrayList<String>(Arrays.asList(genres));
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, genreList);

        ListView listView = (ListView) findViewById(R.id.genre_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);



    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {

        int id = item.getItemId();
        if(id == R.id.add_song){ // will transfer user to song adding activity
            Intent intent = new Intent(ChooseGenreActivity.this, AddSongActivity.class);
            this.startActivity(intent);
        }
        else if(id == R.id.record_clip)
        {
           Intent intent = new Intent(this, RecordClipActivity.class);
           this.startActivity(intent);
        }
        else if(id == R.id.menuEnd) { // signs user out and returns to main menu
            this.mAuth.signOut();
            Intent intent = new Intent(this, MainActivity.class);
            this.startActivity(intent);
        }
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DBHelper dbHelper = new DBHelper(this);
        Song[] songs = dbHelper.selectAllGenre(this.mAuth.getUid(), this.genres[position]).toArray(new Song[0]);
        if(songs.length == 0)
        {
            Toast.makeText(this, "Library is empty, please add clips to play.", Toast.LENGTH_SHORT).show();
        }
        else {
            Intent i = new Intent(this, ClipPlayerActivity.class);
            i.putExtra("genre", this.genres[position]);
            this.startActivity(i);
        }
    }
}