package com.example.clipplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class ChooseGenreActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_genre);
        this.mAuth = FirebaseAuth.getInstance();

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
            Intent intent = new Intent(this, AddSongActivity.class);
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