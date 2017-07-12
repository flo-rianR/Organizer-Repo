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
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
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

import Adapter.RecyclerListAdapter;
import Listener.RecyclerItemClickListener;
import database.EntryModel;
import database.ListModel;
import database.MySQLiteHelper;

public class MainActivity extends AppCompatActivity {


    private MySQLiteHelper eDB;
    //ArrayAdapter<String> adapter;
    private RecyclerListAdapter recyclerListAdapter;
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

    @Override
    protected void onResume() {
        super.onResume();
        updateRecycler();
    }

    public void addListDialog(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater linf = LayoutInflater.from(this);
        final View inflater = linf.inflate(R.layout.dialog_addlist, null);
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        final ListModel listModel = new ListModel();

        final EditText input = (EditText) inflater.findViewById(R.id.addListEdittext);

        builder.setTitle("Neue Liste erstellen");
        builder.setView(inflater);
        builder.setNegativeButton("Abbrechen", null);
        builder.setPositiveButton("Hinzufügen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nameString = input.getText().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                String date = sdf.format(new Date());
                listModel.setList(nameString);
                listModel.setCreate_At(date);
                eDB.addList(listModel);
                updateRecycler();
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
        recyclerListAdapter = new RecyclerListAdapter(listModels, eDB);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.listrecycler);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerListAdapter);
        updateRecycler();

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Log.d("DEBUG", String.valueOf(position));
                        show(position);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                }));
      //  recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener()
      //  {
      //      @Override
      //      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      //          show(position);
      //      }
      // });
    }

    public void show (int position)
    {
        final Intent i = new Intent(this, listActivity.class);
        i.putExtra("list", listModels.get(position).getId());
        Log.d("debug id", String.valueOf(listModels.get(position).getId()));
        startActivity(i);
    }

    private void updateRecycler()
    {
        LinkedList<ListModel> swaplist = (LinkedList<ListModel>) eDB.getAllLists();
        recyclerListAdapter.swap(swaplist);
    }

}
