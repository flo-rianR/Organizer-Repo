package com.hswgt.florian.organizer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import database.Entry;
import database.MySQLiteHelper;

import static android.R.id.list;

public class MainActivity extends AppCompatActivity {


    private MySQLiteHelper eDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        eDB = new MySQLiteHelper(this);
    }

    public void confirm(View view)
    {
        Entry entry;
        boolean flag = true;
        EditText listEdit = (EditText)findViewById(R.id.listeEditText);
        EditText descriptionEdit = (EditText)findViewById(R.id.descriptionEditText);
        EditText createDateEdit = (EditText)findViewById(R.id.createDateEditText);
        EditText locationEdit = (EditText)findViewById(R.id.locationEditText);
        String listText = listEdit.getText().toString();
        String descriptionText = descriptionEdit.getText().toString();
        String createDateText = createDateEdit.getText().toString();
        String locationText = locationEdit.getText().toString();

        if(listText.equals(""))
        {
            Toast.makeText(MainActivity.this, "Bitte Namen eingeben", Toast.LENGTH_SHORT).show();
            flag = false;
        }
        if(descriptionText.equals(""))
        {
            Toast.makeText(MainActivity.this, "Bitte Vornamen eingeben", Toast.LENGTH_SHORT).show();
            flag = false;
        }
        if(createDateText.equals(""))
        {
            Toast.makeText(MainActivity.this, "Bitte Geburtstag eingeben", Toast.LENGTH_SHORT).show();
            flag = false;
        }
        if(locationText.equals(""))
        {
            Toast.makeText(MainActivity.this, "Bitte Geburtstag eingeben", Toast.LENGTH_SHORT).show();
            flag = false;
        }
        if(flag)
        {
            entry = new Entry(listText, descriptionText, createDateText, locationText);
            eDB.addEntry(entry);
            listEdit.setText("");
            descriptionEdit.setText("");
            createDateEdit.setText("");
            locationEdit.setText("");
            Toast.makeText(MainActivity.this, "Hinzufügen erfolgreich", Toast.LENGTH_SHORT).show();
        }


    }

    public void show (View view)
    {
        final Intent i = new Intent(this, listActivity.class);
        startActivity(i);
    }
}
