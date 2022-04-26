package com.example.clipplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
            Toast.makeText(this, "Selected Show Winners", Toast.LENGTH_LONG).show();
        }
        else if(id == R.id.menuEnd) { // signs user out and returns to main menu
            this.mAuth.signOut();
            MainActivity.isLogged = false;
            Intent intent = new Intent(this, MainActivity.class);
            this.startActivity(intent);
        }
        return true;
    }
}