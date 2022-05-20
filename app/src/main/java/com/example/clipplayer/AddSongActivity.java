package com.example.clipplayer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AddSongActivity extends AppCompatActivity {
    private final String[] GENRES = {"Blues", "Classical", "Country", "Disco", "Hiphop", "Jazz", "Metal", "Pop", "Reggae", "Rock"};
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private EditText songName;
    private static String genre;
    private StorageReference storageRef;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        this.mDatabase = FirebaseDatabase.getInstance();
        this.mAuth = FirebaseAuth.getInstance();
        this.storageRef = FirebaseStorage.getInstance().getReference();
        this.songName = this.findViewById(R.id.song_name);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_song);
    }

    public void chooseGenreBtn(View view)
    {
        String[] genreList = {"Blues", "Classical", "Country", "Disco", "Hiphop", "Jazz", "Metal", "Pop", "Reggae", "Rock"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick a genre");
        builder.setItems(genreList, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AddSongActivity.genre = genreList[which];
            }
        });
        builder.show();
    }

    public void addSongBtn(View view)
    {
        String song = this.songName.getText().toString();
    }


}
