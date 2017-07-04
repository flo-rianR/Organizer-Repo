package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
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
    private static final String KEY_LATITUTE = "latitute";
    private static final String KEY_LONGITUTE = "longitute";

    private static final String[] COLUMNS = {KEY_ID, KEY_LIST, KEY_DESCRIPTION, KEY_CREATEDATE, KEY_LOCATION, KEY_LATITUTE, KEY_LONGITUTE};

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
                KEY_LOCATION + " TEXT, " +
                KEY_LATITUTE + " REAL, " +
                KEY_LONGITUTE + " REAL" +
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
        //values.put(KEY_LOCATION, entry.getLocation());
        //values.put(KEY_LATITUTE, entry.getLatitute());
        //values.put(KEY_LONGITUTE, entry.getLongitute());
        db.insert(TABLE_ENTRY,
                null,
                values);
        db.close();
    }

    public void addLocationToEntry(String list, String description, String location, double latitute, double longitute)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_ENTRY + " SET " + KEY_LOCATION + "='" + location + "' WHERE " + KEY_LIST + "='" + list + "' AND " + KEY_DESCRIPTION + "='" + description + "'";
        db.execSQL(query);
        query = "UPDATE " + TABLE_ENTRY + " SET " + KEY_LATITUTE + "='" + latitute + "' WHERE " + KEY_LIST + "='" + list + "' AND " + KEY_DESCRIPTION + "='" + description + "'";
        db.execSQL(query);
        query = "UPDATE " + TABLE_ENTRY + " SET " + KEY_LONGITUTE + "='" + longitute + "' WHERE " + KEY_LIST + "='" + list + "' AND " + KEY_DESCRIPTION + "='" + description + "'";
        db.execSQL(query);
        db.close();
    }

    public void addList(String list)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LIST, list);
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
                entry.setLatitute(cursor.getDouble(5));
                entry.setLongitute(cursor.getDouble(6));

                entries.add(entry);
            } while(cursor.moveToNext());
        }
        return entries;
    }

    public List<Entry> getListEntries(String list)
    {
        List<Entry> entries  = new LinkedList<>();
        String query = "SELECT * FROM " + TABLE_ENTRY + " WHERE " + KEY_LIST + " = " + "\"" + list + "\" AND NOT (" + KEY_DESCRIPTION +
                                                                                                        " IS NULL OR "+ KEY_CREATEDATE +
                                                                                                        " IS NULL)";
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
                entry.setLatitute(cursor.getDouble(5));
                entry.setLongitute(cursor.getDouble(6));

                entries.add(entry);
            } while(cursor.moveToNext());
        }
        return entries;
    }

    public List<String> getAllLists()
    {
        List<String> lists = new LinkedList<>();
        String query = "SELECT " +  KEY_LIST + " FROM " + TABLE_ENTRY + " GROUP BY " + KEY_LIST;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        String list;
        if(cursor.moveToFirst()) {
            do {
                list = cursor.getString(0);
                lists.add(list);
            } while (cursor.moveToNext());
        }

        return lists;
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

    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "message" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);

        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);

            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {

                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;

        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){
            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }
    }

}

