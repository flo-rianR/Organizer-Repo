package Adapter;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.hswgt.florian.organizer.R;

import java.util.LinkedList;
import java.util.List;

import database.EntryModel;
import database.MySQLiteHelper;

import static android.R.attr.button;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by Florian on 29.06.2017.
 */

public class RecyclerEntryAdapter extends RecyclerView.Adapter<RecyclerEntryAdapter.MyViewHolder> implements View.OnClickListener
{
    private List<EntryModel> entries;
    private MySQLiteHelper eDB;


    class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView nameText;
        public TextView descriptionText;
        public TextView createDateText;
        public ImageButton deleteButton;

        public MyViewHolder(View view)
        {
            super(view);
            nameText = (TextView) view.findViewById(R.id.entryNameText);
            descriptionText = (TextView) view.findViewById(R.id.entryDescText);
            createDateText = (TextView) view.findViewById(R.id.createDateText);
            deleteButton = (ImageButton) view.findViewById(R.id.delEntryIBtn);
        }
    }

    public RecyclerEntryAdapter(List<EntryModel> entries, MySQLiteHelper eDB)
    {
        this.entries = entries;
        this.eDB = eDB;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_recyclerentry, parent, false);
        MyViewHolder holder = new MyViewHolder(itemView);
        holder.deleteButton.setOnClickListener(RecyclerEntryAdapter.this);
        holder.deleteButton.setTag(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final EntryModel entryModel = entries.get(position);
        holder.nameText.setText(entryModel.getName());
        holder.createDateText.setText("Erstellt am: " +  entryModel.getCreated_At());
        holder.descriptionText.setText(entryModel.getDescription());
        Log.d("Debug", entryModel.getDescription());


    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    @Override
    public void onClick(final View v) {

        final MyViewHolder holder = (MyViewHolder) v.getTag();
        new AlertDialog.Builder(v.getContext())
                .setTitle("Löschen")
                .setMessage("Wollen Sie den Eintrag wirklich löschen?")
                .setNegativeButton("Nein", null)
                .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(holder.deleteButton.getId() == v.getId())
                        {
                            eDB.deleteEntry(entries.get(holder.getAdapterPosition()));
                            entries.remove(holder.getAdapterPosition());
                            notifyItemRemoved(holder.getAdapterPosition());
                            notifyItemRangeChanged(holder.getAdapterPosition(), entries.size());
                            notifyDataSetChanged();
                        }
                    }
                }).create().show();
    }


    public void swap(LinkedList<EntryModel> datas)
    {
        entries.clear();
        entries.addAll(datas);
        notifyDataSetChanged();
    }

    public void test(View view) {
        Log.d("debug pop", "show it");
    }
}
