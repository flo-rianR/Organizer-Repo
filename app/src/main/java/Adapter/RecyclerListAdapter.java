package Adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hswgt.florian.organizer.R;

import java.util.List;

import database.EntryModel;
import database.ListModel;
import database.MySQLiteHelper;

/**
 * Created by Florian on 07.07.2017.
 */

public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListAdapter.MyViewHolder>
{
    private List<ListModel> lists;
    private MySQLiteHelper eDB;

    class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView listName;
        public TextView listEntryCount;
        public TextView listCreateDate;

        public MyViewHolder(View view)
        {
            super(view);
            listName = (TextView) view.findViewById(R.id.ListName);
            listEntryCount = (TextView) view.findViewById(R.id.entrycounttext);
            listCreateDate = (TextView) view.findViewById(R.id.createDatelist);
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


}
