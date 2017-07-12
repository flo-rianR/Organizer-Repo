package Adapter;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hswgt.florian.organizer.R;

import java.util.LinkedList;
import java.util.List;

import database.EntryModel;
import database.ListModel;
import database.MySQLiteHelper;

import static android.R.attr.entries;

/**
 * Created by Florian on 07.07.2017.
 */

public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListAdapter.MyViewHolder> implements View.OnClickListener
{
    private List<ListModel> lists;
    private MySQLiteHelper eDB;

    class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView listName;
        public TextView listEntryCount;
        public TextView listCreateDate;
        public ImageButton deleteBtn;

        public MyViewHolder(View view)
        {
            super(view);
            listName = (TextView) view.findViewById(R.id.ListName);
            listEntryCount = (TextView) view.findViewById(R.id.entrycounttext);
            listCreateDate = (TextView) view.findViewById(R.id.createDatelist);
            deleteBtn = (ImageButton) view.findViewById(R.id.deleteListIBtn);
        }

    }

    public RecyclerListAdapter(List<ListModel> lists, MySQLiteHelper eDB)
    {
        this.lists = lists;
        this.eDB = eDB;
    }

    @Override
    public RecyclerListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_recyclerlist, parent, false);
        MyViewHolder holder = new MyViewHolder(itemView);
        holder.deleteBtn.setOnClickListener(RecyclerListAdapter.this);
        holder.deleteBtn.setTag(holder);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerListAdapter.MyViewHolder holder, int position) {
        ListModel listModel = lists.get(position);
        holder.listName.setText(listModel.getList());
        holder.listEntryCount.setText("Einträge: " + eDB.getListEntries(listModel.getId()).size());
        holder.listCreateDate.setText("Erstellt am " + listModel.getCreate_At());

    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    @Override
    public void onClick(final View v) {

        final MyViewHolder holder = (MyViewHolder) v.getTag();
        new AlertDialog.Builder(v.getContext())
                .setTitle("Löschen")
                .setMessage("Wollen Sie die Liste wirklich löschen?")
                .setNegativeButton("Nein", null)
                .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(holder.deleteBtn.getId() == v.getId())
                        {
                            deleteCompleteList(holder);
                            lists.remove(holder.getAdapterPosition());
                            notifyItemRemoved(holder.getAdapterPosition());
                            notifyItemRangeChanged(holder.getAdapterPosition(), lists.size());
                            notifyDataSetChanged();
                        }
                    }
                }).create().show();
    }

    private void deleteCompleteList(MyViewHolder holder)
    {
        List<EntryModel> entries = eDB.getListEntries(lists.get(holder.getAdapterPosition()).getId());
        for(EntryModel em : entries)
        {
            eDB.deleteEntry(em);
        }
        eDB.deleteList(lists.get(holder.getAdapterPosition()));
    }

    public void swap(LinkedList<ListModel> datas)
    {
        lists.clear();
        lists.addAll(datas);
        notifyDataSetChanged();
    }

}
