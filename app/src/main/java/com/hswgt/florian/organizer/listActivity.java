package com.hswgt.florian.organizer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import Adapter.RecyclerEntryAdapter;
import database.EntryModel;
import database.MySQLiteHelper;


/**
 * Created by Florian on 29.06.2017.
 */

public class listActivity extends AppCompatActivity {
    private MySQLiteHelper eDB;
    private RecyclerEntryAdapter recyclerAdapter;
    LinkedList<EntryModel> entryModelList = null;
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
        final EntryModel entryModel = new EntryModel();

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
                entryModel.setDescription(description);
                entryModel.setForeign_key(getIntent().getIntExtra("list", -1));
                entryModel.setCreated_At(date);
                id = eDB.addEntry(entryModel);
                entryModelList.add(entryModel);
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
        entryModelList = (LinkedList<EntryModel>) eDB.getListEntries(list);
        Log.d("debug size", String.valueOf(entryModelList.size()));
        for(EntryModel e : entryModelList)
        {
            Log.d("Debug list", e.getDescription());
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.entryrecycler);

        recyclerAdapter = new RecyclerEntryAdapter(entryModelList, eDB);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.notifyDataSetChanged();
    }

}
