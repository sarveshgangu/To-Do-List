package com.example.to_doapplication.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.to_doapplication.Edit_Task;
import com.example.to_doapplication.Edit_list;
import com.example.to_doapplication.Model.LIST;
import com.example.to_doapplication.R;
import com.example.to_doapplication.Database.listDatabaseHandler;
import com.example.to_doapplication.ViewList;

import java.util.List;

public class List_adapter extends RecyclerView.Adapter<List_adapter.ViewHolder>{
    Context context;
    public void ViewHolder(Context context){
        this.context=context;
    }
    private List<LIST> todolist;
    private ViewList activity;
    private listDatabaseHandler db;
    public List_adapter(ViewList activity,listDatabaseHandler db){
        this.activity=activity;
        this.db=db;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemview= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout,parent,false);
        return new ViewHolder(itemview);
    }
    public void onBindViewHolder(ViewHolder holder,int position){
        db.openDatabase();
        LIST item= todolist.get(position);
        holder.name.setText(item.getListname());
        holder.items.setText(item.getItems());
    }

    public void setLists(List<LIST> todolist){
        this.todolist=todolist;
        notifyDataSetChanged();
    }
    public void deletelist(int position){
        LIST item = todolist.get(position);
        todolist.remove(position);
        db.deleteList(item.getId());
        notifyItemRemoved(position);
    }
    public void editlist(int position){
        LIST item=todolist.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("listname", item.getListname());
        bundle.putString("items",item.getItems());
        Edit_list fragment = new Edit_list();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), Edit_Task.TAG);
    }
    @Override
    public int getItemCount() {
        return todolist.size();
    }

    public Context getContext() {
        return activity;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name,items;
        ViewHolder(View view){
            super(view);
            name=view.findViewById(R.id.Listname);
            items=view.findViewById(R.id.items);
        }
    }
}
