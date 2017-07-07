package Adapter;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hswgt.florian.organizer.R;

import java.util.List;

import database.EntryModel;
import database.MySQLiteHelper;

/**
 * Created by Florian on 29.06.2017.
 */

public class RecyclerEntryAdapter extends RecyclerView.Adapter<RecyclerEntryAdapter.MyViewHolder> implements View.OnClickListener
{
    private List<EntryModel> entries;
    private MySQLiteHelper eDB;


    class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView descriptionText;
        public ImageButton deleteButton;
        public Button tmpbutton;

        public MyViewHolder(View view)
        {
            super(view);
            descriptionText = (TextView) view.findViewById(R.id.descriptionTextView);
            deleteButton = (ImageButton) view.findViewById(R.id.deleteButton);
            tmpbutton = (Button) view.findViewById(R.id.tmpButton);
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
        //if(entryModel.getDescription() == null || entryModel.getCreated_At() == null) return;
            holder.descriptionText.setText(entryModel.getDescription());
            Log.d("Debug", entryModel.getDescription());


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
