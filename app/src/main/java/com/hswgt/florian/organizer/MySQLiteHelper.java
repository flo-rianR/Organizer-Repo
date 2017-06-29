package com.hswgt.florian.organizer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Florian on 10.06.2017.
 */

public class MySQLiteHelper extends SQLiteOpenHelper
{

    private static final String TABLE_ENTRY = "entry";

    private static final String KEY_ID = "id";
    private static final String KEY_LIST = "list";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_CREATEDATE = "create_At";
    private static final String KEY_LOCATION = "location";

    private static final String[] COLUMNS = {KEY_ID, KEY_LIST, KEY_DESCRIPTION, KEY_CREATEDATE, KEY_LOCATION};

    private static final int DATABASE_VERSION = 4;

    private static final String DATABASE_NAME = "EntryDB";

    public MySQLiteHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_ENTRY_TABLE = "CREATE TABLE " + TABLE_ENTRY +
                " ( " +
                KEY_ID + " INTEGER PRIMARY KEY, " +
                KEY_LIST + " TEXT, " +
                KEY_DESCRIPTION + " TEXT, " +
                KEY_CREATEDATE + " TEXT, " +
                KEY_LOCATION + " TEXT" +
                " )";
        db.execSQL(CREATE_ENTRY_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        String dropIfEx = "DROP TABLE IF EXISTS " + TABLE_ENTRY;
        db.execSQL(dropIfEx);
        this.onCreate(db);
    }

    public void addEntry(Entry entry)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LIST, entry.getList());
        values.put(KEY_DESCRIPTION, entry.getDescription());
        values.put(KEY_CREATEDATE, entry.getCreated_At());
        values.put(KEY_LOCATION, entry.getLocation());
        db.insert(TABLE_ENTRY,
                null,
                values);
        db.close();
    }

    public List<Entry> getAllEntries()
    {
        List<Entry> entries = new LinkedList<>();
        String query = "SELECT * FROM " + TABLE_ENTRY;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Entry entry;
        if(cursor.moveToFirst())
        {
            do{
                entry = new Entry();
                entry.setID(Integer.parseInt(cursor.getString(0)));
                entry.setList(cursor.getString(1));
                entry.setDescription(cursor.getString(2));
                entry.setCreated_At(cursor.getString(3));
                entry.setLocation(cursor.getString(4));

                entries.add(entry);
            } while(cursor.moveToNext());
        }
        return entries;
    }

    public List<Entry> getListEntries(String list)
    {
        List<Entry> entries  = new LinkedList<>();
        String query = "SELECT * FROM " + TABLE_ENTRY + " WHERE " + KEY_LIST + " = " + list;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Entry entry;
        if(cursor.moveToFirst())
        {
            do{
                entry = new Entry();
                entry.setID(Integer.parseInt(cursor.getString(0)));
                entry.setList(cursor.getString(1));
                entry.setDescription(cursor.getString(2));
                entry.setCreated_At(cursor.getString(3));
                entry.setLocation(cursor.getString(4));

                entries.add(entry);
            } while(cursor.moveToNext());
        }
        return entries;
    }

    public void deleteEntry(Entry entry)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ENTRY,
                KEY_ID + " = ?",
                new String[]{String.valueOf(entry.getID())});

        db.close();
    }

    public void deleteEntryByID(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ENTRY,
                KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }
    //test
}

