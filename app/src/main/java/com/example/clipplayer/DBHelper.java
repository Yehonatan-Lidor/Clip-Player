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

    private static final String[] allColumns = {COLUMN_ID, COLUMN_NAME, COLUMN_GENRE, COLUMN_UID};

    private static final String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS " +
            TABLE_RECORD + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_NAME + " TEXT," +
            COLUMN_GENRE + " TEXT," +
            COLUMN_UID + " TEXT );";

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


    // get the user back with the id
    // also possible to return only the id
    public void insert(Song song)
    {
        database = getWritableDatabase(); // get access to write the database
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, song.getName());
        values.put(COLUMN_GENRE, song.getGenre());
        values.put(COLUMN_UID, song.getUid());
        long id = database.insert(TABLE_RECORD, null, values);
        database.close();
    }

    // remove a specific user from the table
    public void deleteUser(Song song)
    {
        this.deleteByName(song.getName(), song.getName());
    }

    public void deleteByName(String uid, String name )
    {
        database = getWritableDatabase(); // get access to write e data
        database.delete(TABLE_RECORD, COLUMN_UID + " = " + uid + " AND " + COLUMN_NAME + " = " + name, null);
        database.close(); // close the database

    }


    // update a specific user
    public void update(ModelUser user)
    {
        database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, user.getId());
        values.put(COLUMN_NAME, user.getUserName());
        values.put(COLUMN_SCORE, user.getScore());
        database.update(TABLE_RECORD, values, COLUMN_ID + "=" + user.getId(), null);
        database.close();

    }

    // return all rows in table
    public ArrayList<ModelUser> selectAll()
    {
        database = getReadableDatabase(); // get access to read the database
        ArrayList<ModelUser> users = new ArrayList<>();
        String sortOrder = COLUMN_SCORE + " DESC"; // sorting by score
        Cursor cursor = database.query(TABLE_RECORD, allColumns, null, null, null, null, sortOrder); // cursor points at a certain row
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                int score = cursor.getInt(cursor.getColumnIndex(COLUMN_SCORE));
                long id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
                ModelUser user= new ModelUser(name, score, id);
                users.add(user);
            }
        }
        database.close();
        return users;
    }

    //
    // I prefer using this one...
    //
    public ArrayList<ModelUser> genericSelectByUserName(String userName)
    {
        String[] vals = { userName };
        // if using the rawQuery
        // String query = "SELECT * FROM " + TABLE_RECORD + " WHERE " + COLUMN_NAME + " = ?";
        String column = COLUMN_NAME;
        return select(column,vals);
    }


    // INPUT: notice two options rawQuery should look like
    // rawQuery("SELECT id, name FROM people WHERE name = ? AND id = ?", new String[] {"David", "2"});
    // OUTPUT: arraylist - number of elements accordingly
    public ArrayList<ModelUser> select(String column,String[] values)
    {
        database = getReadableDatabase(); // get access to read the database
        ArrayList<ModelUser> users = new ArrayList<>();
        // Two options,
        // since query cannot be created in compile time there is no difference
        //Cursor cursor = database.rawQuery(query, values);
        Cursor cursor= database.query(TABLE_RECORD, allColumns, COLUMN_NAME +" = ? ", values, null, null, null); // cursor points at a certain row
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                int score = cursor.getInt(cursor.getColumnIndex(COLUMN_SCORE));
                long id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
                ModelUser user= new ModelUser(name, score, id);
                users.add(user);
            }// end while
        } // end if
        database.close();
        return users;
    }

}
