package Adapter;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hswgt.florian.organizer.R;

import java.util.List;

import database.Entry;
import database.MySQLiteHelper;

/**
 * Created by Florian on 29.06.2017.
 */

public class RecyclerEntryAdapter extends RecyclerView.Adapter<RecyclerEntryAdapter.MyViewHolder> implements View.OnClickListener
{
    private List<Entry> entries;
    MySQLiteHelper eDB;



    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView descriptionText;
        public Button deleteButton;

        public MyViewHolder(View view)
        {
            super(view);
            descriptionText = (TextView) view.findViewById(R.id.descriptionTextView);
            deleteButton = (Button) view.findViewById(R.id.deleteButton);
        }
    }

    public RecyclerEntryAdapter(List<Entry> entries, MySQLiteHelper eDB)
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
        final Entry entry = entries.get(position);
        holder.descriptionText.setText(entry.getDescription());
        Log.d("debug", entry.getDescription());


    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    @Override
    public void onClick(final View v) {
        new AlertDialog.Builder(v.getContext())
                .setTitle("Löschen")
                .setMessage("Wollen Sie den Eintrag wirklich löschen?")
                .setNegativeButton("Nein", null)
                .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MyViewHolder holder = (MyViewHolder) v.getTag();
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
}
