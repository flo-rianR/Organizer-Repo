package com.hswgt.florian.organizer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import Adapter.RecyclerEntryAdapter;
import database.Entry;
import database.MySQLiteHelper;

import static android.R.id.list;


/**
 * Created by Florian on 29.06.2017.
 */

public class listActivity extends AppCompatActivity {
    private MySQLiteHelper eDB;
    private RecyclerEntryAdapter recyclerAdapter;
    LinkedList<Entry> entryList = null;
    private String description;
    private long id;


    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_list);
        eDB = new MySQLiteHelper(this);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        listInit();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void addEntryDialog(View view)
    {
        final Entry entry = new Entry();

        LayoutInflater linf = LayoutInflater.from(this);
        final View inflater = linf.inflate(R.layout.dialog_addentry, null);
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setTitle("Neuen Eintrag erstellen");
        dialog.setView(inflater);

        final EditText descriptionEditText = (EditText) inflater.findViewById(R.id.descriptionEditText);

        dialog.setPositiveButton("Hinzufügen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                description = descriptionEditText.getText().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                String date = sdf.format(new Date());
                entry.setDescription(description);
                entry.setForeign_key(getIntent().getIntExtra("list", -1));
                entry.setCreated_At(date);
                id = eDB.addEntry(entry);
                entryList.add(entry);
                recyclerAdapter.notifyDataSetChanged();
                dialog.cancel();
                addLocationDialog();
            }
        });
        dialog.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    public void addLocationDialog()
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setTitle("Ort hinzufügen?");
        dialog.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final Intent i = new Intent(listActivity.this, MapsActivity.class);
                Bundle extras = new Bundle();
                extras.putLong("list", id);
                extras.putString("description", description);
                i.putExtras(extras);
                startActivity(i);
            }
        });
        dialog.setNegativeButton("Nein", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.show();

    }



    private void listInit() {
        Intent i = getIntent();
        int list = i.getIntExtra("list", -1);
        Log.d("deBug", String.valueOf(list));
        entryList = (LinkedList<Entry>) eDB.getListEntries(list);
        Log.d("debug size", String.valueOf(entryList.size()));
        for(Entry e : entryList)
        {
            Log.d("Debug list", e.getDescription());
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.entryrecycler);

        recyclerAdapter = new RecyclerEntryAdapter(entryList, eDB);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.notifyDataSetChanged();
    }

}
