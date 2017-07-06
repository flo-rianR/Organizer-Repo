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
import android.util.Log;
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

import java.util.ArrayList;
import java.util.List;

import database.Entry;
import database.ListModel;
import database.MySQLiteHelper;

import static android.R.attr.name;
import static android.R.attr.start;
import static android.R.id.list;

public class MainActivity extends AppCompatActivity {


    private MySQLiteHelper eDB;
    ArrayAdapter<String> adapter;
    List<ListModel> listModels;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        eDB = new MySQLiteHelper(this);
        listModels = eDB.getAllLists();
        listViewInit();
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);

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

        }
        return super.onOptionsItemSelected(item);
    }

    public void addListDialog(View view)
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
                listModels = eDB.getAllLists();
                adapter.add(nameString);
                adapter.notifyDataSetChanged();
            }
        });
        builder.create().show();
    }

    private List<String> listToString()
    {

        List<String> listString = new ArrayList<>();
        for (ListModel lm : listModels)
        {
            Log.d("DEBUG", "Test");
            listString.add(lm.getList());
        }
        return listString;
    }

    private void listViewInit()
    {
        ListView listView = (ListView) findViewById(R.id.listview);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listToString());
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
        i.putExtra("list", listModels.get(position).getId());
        startActivity(i);
    }

}
