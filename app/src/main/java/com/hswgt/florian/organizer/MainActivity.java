package com.hswgt.florian.organizer;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import database.Entry;
import database.MySQLiteHelper;

import static android.R.attr.name;
import static android.R.attr.start;
import static android.R.id.list;

public class MainActivity extends AppCompatActivity {


    private MySQLiteHelper eDB;
    ArrayAdapter<String> adapter;
    List<String> entries;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        eDB = new MySQLiteHelper(this);
        entries = eDB.getAllLists();
        listViewInit();
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        Drawable drawable = menu.findItem(R.id.addListButton).getIcon();
        if(drawable != null)
        {
            drawable.mutate();
            drawable.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
            case R.id.dbView:
                final Intent i = new Intent(this, AndroidDatabaseManager.class);
                startActivity(i);
                return true;
            case R.id.addListButton:
                addListDialog();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void addListDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final EditText input = new EditText(this);
        input.setHint("Name");
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        builder.setTitle("Neue Liste erstellen");
        builder.setView(input);
        builder.setNegativeButton("Abbrechen", null);
        builder.setPositiveButton("Hinzufügen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nameString = input.getText().toString();
                eDB.addList(nameString);
                adapter.add(nameString);
                adapter.notifyDataSetChanged();
            }
        });
        builder.create().show();
    }

    private void listViewInit()
    {
        ListView listView = (ListView) findViewById(R.id.listview);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, entries);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                show(position);
            }
        });
    }

    public void show (int position)
    {
        final Intent i = new Intent(this, listActivity.class);
        i.putExtra("list", entries.get(position));
        startActivity(i);
    }
}
