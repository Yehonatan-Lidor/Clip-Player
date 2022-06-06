package com.example.clipplayer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class AddSongActivity extends AppCompatActivity {
    private static final int PICKFILE_REQUEST_CODE = 1;
    private final String[] GENRES = {"Blues", "Classical", "Country", "Disco", "Hiphop", "Jazz", "Metal", "Pop", "Reggae", "Rock"};
    private FirebaseAuth mAuth;
    private static String genre = "";
    private Uri uri;
    private byte[] inputData;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_song);

        this.mAuth = FirebaseAuth.getInstance();
        this.uri = null;
        this.dbHelper = new DBHelper(this);
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
        if(AddSongActivity.genre.equals("") || this.uri == null)
        {
            Toast.makeText(this, "Genre / audio file were not chosen!!", Toast.LENGTH_SHORT).show();
        }
        else
        {

            File f = new File(this.uri.getPath());
            Log.d("ClipAdded:" , f.getPath());
            Song song = new Song(f.getName(), AddSongActivity.genre,this.mAuth.getUid(), f.getPath().split(":", 0)[1]);
            this.dbHelper.insert(song);
            AddSongActivity.genre = "";
            this.uri = null;
        }
    }

    public void chooseFileBtn(View view)
    {
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        // Ask specifically for something that can be opened:
        chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
        chooseFile.setType("*/*");
        startActivityForResult(
                Intent.createChooser(chooseFile, "Choose a file"),
                PICKFILE_REQUEST_CODE
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICKFILE_REQUEST_CODE && resultCode == RESULT_OK) {
            this.uri = data.getData();
        }
        else
        {
            Toast.makeText(this, "Did not choose file correctly", Toast.LENGTH_SHORT).show();
        }
        String path = this.uri.getPath();
        if(path.substring(path.lastIndexOf(".") + 1).equals("mp3")) // check if file extension is valid
        {
            InputStream iStream = null;
            try {
                iStream = getContentResolver().openInputStream(uri);
                this.inputData = this.getBytes(iStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(this, "File chosen was not of proper format, please choose .mp3 files only.", Toast.LENGTH_LONG).show();
            this.uri = null;
        }

    }
    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }


}
