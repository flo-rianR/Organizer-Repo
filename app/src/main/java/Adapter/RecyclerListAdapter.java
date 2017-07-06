package Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

import database.ListModel;
import database.MySQLiteHelper;

/**
 * Created by Florian on 07.07.2017.
 */

public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerEntryAdapter.MyViewHolder>
{
    private List<ListModel> lists;
    private MySQLiteHelper eDB;

    @Override
    public RecyclerEntryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerEntryAdapter.MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
