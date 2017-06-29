package com.hswgt.florian.organizer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import database.Entry;
import database.MySQLiteHelper;

/**
 * Created by Florian on 29.06.2017.
 */

public class listActivity extends AppCompatActivity
{
    private MySQLiteHelper eDB;
    ArrayAdapter<String> adapter;
    List<Entry> entries;
    List<String> lists;

    @Override
    protected void onCreate(Bundle saveInstanceState)
    {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_list);
        eDB = new MySQLiteHelper(this);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        entries = eDB.getAllEntries();
        listViewInit();
        //listStringInit();
        //listInit();
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if(id == android.R.id.home)
        {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private ArrayList<String> getEntryAsString(List<Entry> entries)
    {
        ArrayList<String> entriesString = new ArrayList<>();
        for (int i = 0; i < entries.size(); i++)
        {
            entriesString.add(i, entries.get(i).getList() + ", " + entries.get(i).getDescription());
        }

        return entriesString;
    }

    private void listViewInit()
    {
        final ArrayList<String> entriesString = getEntryAsString(this.entries);
        ListView listView = (ListView) findViewById(R.id.entriesListView);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, entriesString);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Entry entry = entries.get(position);
                eDB.deleteEntry(entry);
                adapter.remove(adapter.getItem(position));
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void listStringInit()
    {
        final ArrayList<String> entriesString = new ArrayList<>(eDB.getAllLists());
        ListView listView = (ListView) findViewById(R.id.entriesListView);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, entriesString);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Entry entry = entries.get(position);
                eDB.deleteEntry(entry);
                adapter.remove(adapter.getItem(position));
                adapter.notifyDataSetChanged();
            }
        });
    }

    private ArrayList<String> getListAsString(List<Entry> list)
    {
        ArrayList<String> listString = new ArrayList<>();
        for (int i = 0; i < list.size(); i++)
        {
            listString.add(i, list.get(i).getList() + ", " + list.get(i).getDescription());
        }

        return listString;
    }

    private void listInit()
    {
        final ArrayList<String> entriesString = getListAsString(eDB.getListEntries("Liste1"));
        ListView listView = (ListView) findViewById(R.id.entriesListView);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, entriesString);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Entry entry = entries.get(position);
                eDB.deleteEntry(entry);
                adapter.remove(adapter.getItem(position));
                adapter.notifyDataSetChanged();
            }
        });
    }
}
