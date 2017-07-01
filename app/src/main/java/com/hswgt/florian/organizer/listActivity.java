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
import android.widget.EditText;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import Adapter.RecyclerEntryAdapter;
import database.Entry;
import database.MySQLiteHelper;

/**
 * Created by Florian on 29.06.2017.
 */

public class listActivity extends AppCompatActivity {
    private MySQLiteHelper eDB;
    ArrayAdapter<String> adapter;
    private RecyclerEntryAdapter recyclerAdapter;
    List<Entry> entries;
    List<String> lists;
    LinkedList<Entry> entryList = null;


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
        entries = eDB.getAllEntries();
        listInit();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list, menu);

        Drawable drawable = menu.findItem(R.id.addEntryButton).getIcon();
        if (drawable != null) {
            drawable.mutate();
            drawable.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.addEntryButton:
                addListDialog();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void addListDialog() {
        final Entry entry = new Entry();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final EditText input = new EditText(this);
        input.setHint("Beschreibung");
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        builder.setTitle("Neuen Eintrag erstellen");
        builder.setView(input);
        builder.setNegativeButton("Abbrechen", null);
        builder.setPositiveButton("Hinzufügen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String descriptionString = input.getText().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
                String date = sdf.format(new Date());
                entry.setList(getIntent().getStringExtra("list"));
                entry.setDescription(descriptionString);
                entry.setCreated_At(date);
                entry.setLocation("here");
                eDB.addEntry(entry);
                entryList.add(entry);
                recyclerAdapter.notifyDataSetChanged();
            }
        });
        builder.create().show();
    }


    private ArrayList<String> getEntryAsString(List<Entry> entries) {
        ArrayList<String> entriesString = new ArrayList<>();
        for (int i = 0; i < entries.size(); i++) {
            entriesString.add(i, entries.get(i).getList() + ", " + entries.get(i).getDescription());
        }

        return entriesString;
    }


    private void listInit() {
        Intent i = getIntent();
        String list = i.getStringExtra("list");
        entryList = (LinkedList<Entry>) eDB.getListEntries(list);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.entryrecycler);

        recyclerAdapter = new RecyclerEntryAdapter(entryList, eDB);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.notifyDataSetChanged();

        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Entry entry = entries.get(position);
                eDB.deleteEntry(entry);
                adapter.remove(adapter.getItem(position));
                adapter.notifyDataSetChanged();
            }
        });*/
    }
}
