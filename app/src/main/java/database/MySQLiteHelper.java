package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static android.R.id.list;
import static database.MySQLiteHelper.ENTRY_TABLE.KEY_IMAGE;
import static database.MySQLiteHelper.ENTRY_TABLE.KEY_LATITUTE;
import static database.MySQLiteHelper.ENTRY_TABLE.KEY_LOCATION;
import static database.MySQLiteHelper.ENTRY_TABLE.KEY_LONGITUTE;


/**
 * Created by Florian on 10.06.2017.
 */

public class MySQLiteHelper extends SQLiteOpenHelper
{

    class LIST_TABLE
    {
        protected static final String KEY_ID = "id";
        protected static final String KEY_LIST = "list";
        protected static final String KEY_DATE = "create_At";
    }

    class ENTRY_TABLE
    {
        protected static final String KEY_ID = "id";
        protected static final String KEY_NAME = "name";
        protected static final String KEY_DESCRIPTION = "description";
        protected static final String KEY_CREATEDATE = "create_At";
        protected static final String KEY_DATE = "date";
        ////////////////////////////////////////////////////////////////////////////////////
        protected static final String KEY_IMAGE = "entryimage";
        ////////////////////////////////////////////////////////////////////////////////////
        protected static final String KEY_LOCATION = "location";
        protected static final String KEY_STREET = "street";
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
                LIST_TABLE.KEY_LIST + " TEXT NOT NULL, " +
                LIST_TABLE.KEY_DATE + " TEXT);";


        String CREATE_ENTRY_TABLE = "CREATE TABLE " + TABLE_ENTRY +
                " ( " +
                ENTRY_TABLE.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ENTRY_TABLE.KEY_NAME + " TEXT NOT NULL, " +
                ENTRY_TABLE.KEY_DESCRIPTION + " TEXT, " +
                ENTRY_TABLE.KEY_CREATEDATE + " TEXT, " +
                ENTRY_TABLE.KEY_DATE + " TEXT, " +
                ////////////////////////////////////////////////////////////////////////////////////
                KEY_IMAGE + " BLOB, " +
                ////////////////////////////////////////////////////////////////////////////////////
                KEY_LOCATION + " TEXT, " +
                ENTRY_TABLE.KEY_STREET + " TEXT, " +
                KEY_LATITUTE + " REAL, " +
                KEY_LONGITUTE + " REAL, " +
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

    public long addEntry(EntryModel entryModel)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ENTRY_TABLE.KEY_NAME, entryModel.getName());
        values.put(ENTRY_TABLE.KEY_DESCRIPTION, entryModel.getDescription());
        values.put(ENTRY_TABLE.KEY_CREATEDATE, entryModel.getCreated_At());
        values.put(ENTRY_TABLE.KEY_DATE, entryModel.getDate());
        ///////////////////////////////////////////////////////////////////////////////////
        values.put(KEY_IMAGE, entryModel.getEntryImage());
        ///////////////////////////////////////////////////////////////////////////////////
        values.put(ENTRY_TABLE.KEY_FOREIGN, entryModel.getForeign_key());
        values.put(ENTRY_TABLE.KEY_LOCATION, entryModel.getLocation());
        values.put(ENTRY_TABLE.KEY_LATITUTE, entryModel.getLatitute());
        values.put(ENTRY_TABLE.KEY_LONGITUTE, entryModel.getLongitute());
        values.put(ENTRY_TABLE.KEY_STREET, entryModel.getStreet());
        long id = db.insert(TABLE_ENTRY, null,values);

        db.close();
        return id;
    }

    public void addLocationToEntry(long id, String location, String street, double latitute, double longitute)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_ENTRY + " SET " + KEY_LOCATION + "='" + location + "' WHERE " + ENTRY_TABLE.KEY_ID + "='" + id + "'";
        db.execSQL(query);
        query = "UPDATE " + TABLE_ENTRY + " SET " + ENTRY_TABLE.KEY_STREET + "='" + street + "' WHERE " + ENTRY_TABLE.KEY_ID + "='" + id + "'";
        db.execSQL(query);
        query = "UPDATE " + TABLE_ENTRY + " SET " + KEY_LATITUTE + "='" + latitute + "' WHERE " + ENTRY_TABLE.KEY_ID + "='" + id + "'";
        db.execSQL(query);
        query = "UPDATE " + TABLE_ENTRY + " SET " + KEY_LONGITUTE + "='" + longitute + "' WHERE " + ENTRY_TABLE.KEY_ID + "='" + id + "'";
        db.execSQL(query);
        db.close();
    }




    // convert from bitmap to byte array
    public class DbBitmapUtility {


        public static byte[] getBytes(Bitmap bitmap) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
            return stream.toByteArray();
        }

        // convert from byte array to bitmap
        public static Bitmap getImage(byte[] image) {
            return BitmapFactory.decodeByteArray(image, 0, image.length);
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
/*
    public void insertImagetoEntry(long id, byte[] entryimage) {
        SQLiteDatabase db = this.getWritableDatabase();

        String imagequery = "UPDATE" + TABLE_ENTRY + "SET " + ENTRY_TABLE.KEY_IMAGE + "='" + "' WHERE " + ENTRY_TABLE.KEY_ID + "='" + id + "'";;
        SQLiteStatement statement = db.compileStatement(imagequery);
        statement.clearBindings();

        statement.bindBlob(3, entryimage);


    }
*/
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void addList(ListModel listModel)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LIST_TABLE.KEY_LIST, listModel.getList());
        values.put(LIST_TABLE.KEY_DATE, listModel.getCreate_At());
        db.insert(TABLE_LIST,
                null,
                values);
        db.close();
    }



    public List<EntryModel> getListEntries(int id)
    {
        List<EntryModel> entries  = new LinkedList<>();
        String query = "SELECT * FROM " + TABLE_ENTRY + " WHERE " + ENTRY_TABLE.KEY_FOREIGN + " = " + "'" + id + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        EntryModel entryModel;
        if(cursor.moveToFirst())
        {
            do{
                entryModel = new EntryModel();
                entryModel.setID(Integer.parseInt(cursor.getString(0)));
                entryModel.setName(cursor.getString(1));
                entryModel.setDescription(cursor.getString(2));
                entryModel.setCreated_At(cursor.getString(3));
                entryModel.setDate(cursor.getString(4));
                /////////////////////////////////////////////////
                //entryModel.setEntryImage(cursor.getEntryImage(5));
                /////////////////////////////////////////////////
                entryModel.setLocation(cursor.getString(5));
                entryModel.setLatitute(cursor.getDouble(6));
                entryModel.setLongitute(cursor.getDouble(7));
                entryModel.setForeign_key(cursor.getInt(8));

                entries.add(entryModel);
            } while(cursor.moveToNext());
        }
        db.close();
        return entries;
    }

    public List<ListModel> getAllLists()
    {
        List<ListModel> lists = new LinkedList<>();
        String query = "SELECT * FROM " + TABLE_LIST;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()) {
            do {
                ListModel list = new ListModel();
                list.setId(cursor.getInt(0));
                list.setList(cursor.getString(1));
                list.setCreate_At(cursor.getString(2));

                lists.add(list);
            } while (cursor.moveToNext());
        }

        return lists;
    }

    public void deleteEntry(EntryModel entryModel)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ENTRY,
                ENTRY_TABLE.KEY_ID + " = ?",
                new String[]{String.valueOf(entryModel.getID())});

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

