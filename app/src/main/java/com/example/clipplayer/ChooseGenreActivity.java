package com.example.clipplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;

public class ChooseGenreActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_genre);
        this.mAuth = FirebaseAuth.getInstance();
        ArrayList<String> genreList = new ArrayList<String>(Arrays.asList(new String[]{"Blues", "Classical", "Country", "Disco", "Hiphop", "Jazz", "Metal", "Pop", "Reggae", "Rock"}));
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, genreList);

        ListView listView = (ListView) findViewById(R.id.list_library);
        listView.setAdapter(adapter);

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
}