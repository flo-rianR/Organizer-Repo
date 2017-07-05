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

import static android.R.attr.id;
import static database.MySQLiteHelper.ENTRY_TABLE.KEY_DESCRIPTION;


/**
 * Created by Florian on 10.06.2017.
 */

public class MySQLiteHelper extends SQLiteOpenHelper
{

    class LIST_TABLE
    {
        protected static final String KEY_ID = "id";
        protected static final String KEY_LIST = "list";
    }

    class ENTRY_TABLE
    {
        protected static final String KEY_ID = "id";
        protected static final String KEY_DESCRIPTION = "description";
        protected static final String KEY_CREATEDATE = "create_At";
        protected static final String KEY_DATE = "date";
        protected static final String KEY_LOCATION = "location";
        protected static final String KEY_LATITUTE = "latitute";
        protected static final String KEY_LONGITUTE = "longitute";
        protected static final String KEY_FOREIGN = "fk_list";
    }

    private static final String TABLE_LIST = "list";
    private static final String TABLE_ENTRY = "entry";
    //private static final String[] COLUMNS = {KEY_ID, KEY_LIST, KEY_DESCRIPTION, KEY_CREATEDATE, KEY_LOCATION, KEY_LATITUTE, KEY_LONGITUTE};
    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "EntryDB";

    public MySQLiteHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_LIST_TABLE = "CREATE TABLE " + TABLE_LIST +
                " ( " +
                LIST_TABLE.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                LIST_TABLE.KEY_LIST + " TEXT NOT NULL);";


        String CREATE_ENTRY_TABLE = "CREATE TABLE " + TABLE_ENTRY +
                " ( " +
                ENTRY_TABLE.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_DESCRIPTION + " TEXT, " +
                ENTRY_TABLE.KEY_CREATEDATE + " TEXT, " +
                ENTRY_TABLE.KEY_DATE + " TEXT, " +
                ENTRY_TABLE.KEY_LOCATION + " TEXT, " +
                ENTRY_TABLE.KEY_LATITUTE + " REAL, " +
                ENTRY_TABLE.KEY_LONGITUTE + " REAL, " +
                ENTRY_TABLE.KEY_FOREIGN + " INTEGER, " +
                "FOREIGN KEY (" + ENTRY_TABLE.KEY_FOREIGN + ") REFERENCES " + TABLE_LIST + "(" + LIST_TABLE.KEY_ID + "));";

        db.execSQL(CREATE_LIST_TABLE);
        db.execSQL(CREATE_ENTRY_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        String dropIfEx = "DROP TABLE IF EXISTS " + TABLE_ENTRY;
        db.execSQL(dropIfEx);
        this.onCreate(db);
    }

    public long addEntry(Entry entry)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ENTRY_TABLE.KEY_DESCRIPTION, entry.getDescription());
        values.put(ENTRY_TABLE.KEY_CREATEDATE, entry.getCreated_At());
        values.put(ENTRY_TABLE.KEY_DATE, entry.getDate());
        values.put(ENTRY_TABLE.KEY_FOREIGN, entry.getForeign_key());
        //values.put(KEY_LOCATION, entry.getLocation());
        //values.put(KEY_LATITUTE, entry.getLatitute());
        //values.put(KEY_LONGITUTE, entry.getLongitute());
        long id = db.insert(TABLE_ENTRY,
                null,
                values);
        db.close();
        return id;
    }

    public void addLocationToEntry(long id, String description, String location, double latitute, double longitute)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_ENTRY + " SET " + ENTRY_TABLE.KEY_LOCATION + "='" + location + "' WHERE " + ENTRY_TABLE.KEY_ID + "='" + id + "'";// AND " + KEY_DESCRIPTION + "='" + description + "'";
        db.execSQL(query);
        query = "UPDATE " + TABLE_ENTRY + " SET " + ENTRY_TABLE.KEY_LATITUTE + "='" + latitute + "' WHERE " + ENTRY_TABLE.KEY_ID + "='" + id + "'"; //AND " + KEY_DESCRIPTION + "='" + description + "'";
        db.execSQL(query);
        query = "UPDATE " + TABLE_ENTRY + " SET " + ENTRY_TABLE.KEY_LONGITUTE + "='" + longitute + "' WHERE " + ENTRY_TABLE.KEY_ID + "='" + id + "'";// AND " + KEY_DESCRIPTION + "='" + description + "'";
        db.execSQL(query);
        db.close();
    }

    public void addList(String list)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LIST_TABLE.KEY_LIST, list);
        db.insert(TABLE_LIST,
                null,
                values);
        db.close();
    }



    public List<Entry> getListEntries(int id)
    {
        List<Entry> entries  = new LinkedList<>();
        String query = "SELECT * FROM " + TABLE_ENTRY + " WHERE " + ENTRY_TABLE.KEY_FOREIGN + " = " + "'" + id + "'"; // AND NOT (" + KEY_DESCRIPTION +" IS NULL OR "+ KEY_CREATEDATE + " IS NULL)";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Entry entry;
        if(cursor.moveToFirst())
        {
            do{
                entry = new Entry();
                entry.setID(Integer.parseInt(cursor.getString(0)));
                entry.setDescription(cursor.getString(1));
                entry.setCreated_At(cursor.getString(2));
                entry.setDate(cursor.getString(3));
                entry.setLocation(cursor.getString(4));
                entry.setLatitute(cursor.getDouble(5));
                entry.setLongitute(cursor.getDouble(6));
                entry.setForeign_key(cursor.getInt(7));

                entries.add(entry);
            } while(cursor.moveToNext());
        }
        return entries;
    }

    public List<ListModel> getAllLists()
    {
        List<ListModel> lists = new LinkedList<>();
        String query = "SELECT * FROM " + TABLE_LIST;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        ListModel list = new ListModel();
        if(cursor.moveToFirst()) {
            do {
                list.setId(cursor.getInt(0));
                list.setList(cursor.getString(1));
                lists.add(list);
            } while (cursor.moveToNext());
        }

        return lists;
    }

    public void deleteEntry(Entry entry)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ENTRY,
                ENTRY_TABLE.KEY_ID + " = ?",
                new String[]{String.valueOf(entry.getID())});

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

