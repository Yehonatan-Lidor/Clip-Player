package com.example.clipplayer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import androidx.annotation.Nullable;

import com.example.clipplayer.Song;


public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASENAME = "songs.db";
    private static final String TABLE_RECORD = "songs";
    private static final int DATABASEVERSION = 1;

    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_GENRE = "genre";
    private static final String COLUMN_UID = "uid";
    private static final String COLUMN_DATA = "data";


    private static final String[] allColumns = {COLUMN_ID, COLUMN_NAME, COLUMN_GENRE, COLUMN_UID};

    private static final String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS " +
            TABLE_RECORD + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_NAME + " TEXT," +
            COLUMN_GENRE + " TEXT," +
            COLUMN_UID + " TEXT"+
            COLUMN_DATA + "BLOB);";

    private SQLiteDatabase database; // access to table

    public DBHelper(@Nullable Context context) {
        super(context, DATABASENAME, null, DATABASEVERSION);
    }


    // creating the database
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL(CREATE_TABLE_USER);
    }

    // in case of version upgrade -> new schema
    // database version
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_RECORD);
        onCreate(sqLiteDatabase);
    }


    //
    public void insert(Song song)
    {
        database = getWritableDatabase(); // get access to write the database
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, song.getName());
        values.put(COLUMN_GENRE, song.getGenre());
        values.put(COLUMN_UID, song.getUid());
        values.put(COLUMN_DATA, song.getData());
        long id = database.insert(TABLE_RECORD, null, values);
        database.close();
    }

    // remove a specific user from the table
    public void deleteUser(Song song)
    {
        this.deleteByName(song.getUid(), song.getName());
    }

    public void deleteByName(String uid, String name )
    {
        database = getWritableDatabase(); // get access to write e data
        database.delete(TABLE_RECORD, COLUMN_UID + " = " + uid + " AND " + COLUMN_NAME + " = " + name, null);
        database.close(); // close the database

    }


    // return all rows of a certain user
    public ArrayList<Song> selectAll(String uid)
    {
        database = getReadableDatabase(); // get access to read the database
        ArrayList<Song> songs = new ArrayList<>();
        Cursor cursor = database.query(TABLE_RECORD, allColumns, COLUMN_UID + "=" + uid, null, null, null, null); // cursor points at a certain row
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                String genre = cursor.getString(cursor.getColumnIndex(COLUMN_GENRE));
                byte[] data = cursor.getBlob(cursor.getColumnIndex(COLUMN_DATA));
                Song song= new Song(name, genre, uid, data);
                songs.add(song);
            }
        }
        database.close();
        return songs;
    }

    // returns an array with all the songs in a certain genre
    public ArrayList<Song> selectAllGenre(String uid, String genre)
    {
        database = getReadableDatabase(); // get access to read the database
        ArrayList<Song> songs = new ArrayList<>();
        Cursor cursor = database.query(TABLE_RECORD, allColumns, COLUMN_UID + "=" + uid + " AND" + COLUMN_GENRE +  "=" + genre, null, null, null, null); // cursor points at a certain row
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                byte[] data = cursor.getBlob(cursor.getColumnIndex(COLUMN_DATA));
                Song song= new Song(name, genre, uid, data);
                songs.add(song);
            }
        }
        database.close();
        return songs;
    }


}
