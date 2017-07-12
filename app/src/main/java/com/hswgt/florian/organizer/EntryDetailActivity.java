package com.hswgt.florian.organizer;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.net.Uri;
import android.os.DropBoxManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import database.EntryModel;
import database.MySQLiteHelper;

import static android.R.attr.id;

public class EntryDetailActivity extends AppCompatActivity {

    EditText edtName;
    EditText edtDescription;
    TextView location;
    TextView street;
    TextView date;
    double lat;
    double longit;
    MySQLiteHelper eDB;
    Bundle bundle;

    Button changeButton;

    ImageView theentryImage;
    final Calendar c = Calendar.getInstance();

    final int REQUEST_CODE_GALLERY = 999;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_detail);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        eDB = new MySQLiteHelper(this);
        bundle = getIntent().getExtras();
        init_Elements();
        Log.d("debugAnzahl", "in Entry");

        /////////////////////////////////////////////////////////
        changeButton = (Button)findViewById(R.id.btnChange);
        theentryImage = (ImageView)findViewById(R.id.entryimage);


        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);

            }
        });

        ////////////////////////////////////////////////////////
    }
    private void init_Elements()
    {
        edtName = (EditText) findViewById(R.id.edtName);
        edtDescription = (EditText) findViewById(R.id.edtDescription);
        location = (TextView) findViewById(R.id.LocationText);
        street = (TextView) findViewById(R.id.StreetText);
        date = (TextView) findViewById(R.id.DateText);

        if(bundle.getBoolean("addEditFlag"))
        {
            EntryModel entryModel = eDB.getEntrybyID(bundle.getInt("entry"));
            edtName.setText(entryModel.getName());
            edtDescription.setText(entryModel.getDescription());
            location.setText(entryModel.getLocation());
            street.setText(entryModel.getStreet());
            date.setText(entryModel.getDate());
            lat = entryModel.getLatitute();
            longit = entryModel.getLongitute();
        }

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_entrydetail, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                notSaveDialog();
                return true;
            case R.id.save:
                if(bundle.getBoolean("addEditFlag")) updateEntry();
                if(!bundle.getBoolean("addEditFlag")) saveEntry();
                break;
        }//excelsior
        return super.onOptionsItemSelected(item);
    }


    public void addLocation(View view)
    {
        int requestCode = 1;
        final Intent i = new Intent(EntryDetailActivity.this, MapsActivity.class);
//        Bundle extras = new Bundle();
//        extras.putLong("list", id);
//        extras.putString("description", description);
//        i.putExtras(extras);
        startActivityForResult(i, requestCode);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        ///////////////////////////////////////////////////////////////////////////////////

        super.onActivityResult(requestCode, resultCode, data);

        Bitmap bp = (Bitmap) data.getExtras().get("data");
        theentryImage.setImageBitmap(bp);

        //////////////////////////////////////////////////////////////////////////////////////
        if(requestCode == 1)
        {
            if(resultCode == RESULT_OK)
            {
                location.setText(data.getStringExtra("locationkey"));
                street.setText(data.getStringExtra("streetkey"));
                lat = data.getDoubleExtra("latkey", 0);
                longit = data.getDoubleExtra("longkey", 0);
            }
        }
    }

    DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            date = (TextView) findViewById(R.id.DateText);
            String dateString = dayOfMonth + "." + (month+1) + "." + year;
            date.setText(dateString);
        }
    };

    public void addDate(View view)
    {
        new DatePickerDialog(EntryDetailActivity.this, datePickerListener, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void notSaveDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Wollen Sie den Eintrag vor dem Beenden speichern?");
        builder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                saveEntry();
            }
        });
        builder.setNegativeButton("Nein", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EntryDetailActivity.this.finish();
            }
        });
        builder.setNeutralButton("Abbrechen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    private void saveEntry()
    {
        if (edtName.getText().toString().equals(""))
        {
            Toast.makeText(this, "Bitte Namen eingeben!", Toast.LENGTH_SHORT).show();
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String date = sdf.format(new Date());

        EntryModel entry = new EntryModel();

        entry.setName(edtName.getText().toString());
        entry.setDescription(edtDescription.getText().toString());
        entry.setCreated_At(date);
        entry.setDate(this.date.getText().toString());
        entry.setLatitute(lat);
        entry.setLongitute(longit);
        entry.setLocation(location.getText().toString());
        entry.setStreet(street.getText().toString());
        entry.setForeign_key(getIntent().getIntExtra("list", -1));

        eDB.addEntry(entry);
        Toast.makeText(this, "Eintrag gespeichert", Toast.LENGTH_SHORT).show();

        this.finish();

    }

    private void updateEntry()
    {
        if (edtName.getText().toString().equals(""))
        {
            Toast.makeText(this, "Bitte Namen eingeben!", Toast.LENGTH_SHORT).show();
            return;
        }
        EntryModel entry = new EntryModel();

        entry.setName(edtName.getText().toString());
        entry.setDescription(edtDescription.getText().toString());
        entry.setDate(this.date.getText().toString());
        entry.setLatitute(lat);
        entry.setLongitute(longit);
        entry.setLocation(location.getText().toString());
        entry.setStreet(street.getText().toString());

        eDB.updateEntry(entry);
        Toast.makeText(this, "Eintrag geändert", Toast.LENGTH_SHORT).show();

        this.finish();
    }


}
