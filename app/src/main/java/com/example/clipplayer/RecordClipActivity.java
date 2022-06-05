package com.example.clipplayer;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;

public class RecordClipActivity extends AppCompatActivity {

    private static final int REQUEST_AUDIO_PERMISSION_CODE = 1;
    private static String genre = "";
    private String fileName;
    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;
    private DBHelper dbHelper;
    private EditText fileNameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_clip);
        this.fileName = "";
        this.dbHelper = new DBHelper(this);
        this.fileNameText = this.findViewById(R.id.fileOutputName);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // this method is called when user will
        // grant the permission for audio recording.
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_AUDIO_PERMISSION_CODE:
                if (grantResults.length > 0) {
                    boolean permissionToRecord = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean permissionToStore = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (permissionToRecord && permissionToStore) {
                        Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean CheckPermissions() {
        // this method is used to check permission
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void RequestPermissions() {
        // this method is used to request the
        // permission for audio recording and storage.
        ActivityCompat.requestPermissions(RecordClipActivity.this, new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE}, REQUEST_AUDIO_PERMISSION_CODE);
    }

    public void chooseGenreBtn(View view) {
        String[] genreList = {"Blues", "Classical", "Country", "Disco", "Hiphop", "Jazz", "Metal", "Pop", "Reggae", "Rock"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick a genre");
        builder.setItems(genreList, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RecordClipActivity.genre = genreList[which];
            }
        });
        builder.show();
    }

    public void recordBtn(View view) {
        this.fileName = this.fileNameText.getText().toString();
        if (CheckPermissions()) { // check if permisions are given
            this.mRecorder = new MediaRecorder();

            this.mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            this.mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            this.mRecorder.setOutputFile(getExternalCacheDir().getAbsolutePath() + "/" + this.fileName + ".3gp");
            this.mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            try {

                this.mRecorder.prepare();
            } catch (IOException e) {
                Log.e("TAG", "prepare() failed");
            }
            this.mRecorder.start();
            Toast.makeText(this, "Recording has started", Toast.LENGTH_SHORT).show();

        } else {

            RequestPermissions();
        }
    }

    public void pauseBtn(View view) {
        if (CheckPermissions())
        {
            if(this.mRecorder != null) {

                // below method will release
                // the media recorder class.
                this.mRecorder.release();
                Toast.makeText(this, "Recording has ended", Toast.LENGTH_SHORT).show();
                this.mRecorder = null;
            }
            else
            {
                Toast.makeText(this, "Recording didn't start, please try again after recording", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {

            RequestPermissions();
        }

    }

    public void playBtn(View view) {
        this.mPlayer = new MediaPlayer();
        try {
            this.mPlayer.setDataSource(getExternalCacheDir().getAbsolutePath() + "/" + this.fileName + ".3gp");
            this.mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    stopPlay();
                }
            });
            this.mPlayer.prepare();
            this.mPlayer.start();
        } catch (IOException e) {
            Log.e(MainActivity.class.getSimpleName() + ":playRecording()", "prepare() failed");
        }
    }

    public void stopPlay()
    {
        if(this.mPlayer != null)
        {
            this.mPlayer.release();
            this.mPlayer = null;
        }
    }
    public void addSongBtn(View view) {

        Song s = new Song(this.fileName,
                RecordClipActivity.genre,
                FirebaseAuth.getInstance().getUid(),
                getExternalCacheDir().getAbsolutePath() + "/" + this.fileName + ".3gp"
        );
        this.dbHelper.insert(s);
        RecordClipActivity.genre = "";
        this.fileName = "";
        this.fileNameText.setText("");
        this.fileNameText.setHint("Enter Clip Name");

    }

}