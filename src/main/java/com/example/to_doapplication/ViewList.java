package com.example.to_doapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;

import com.example.to_doapplication.Model.LIST;
import com.example.to_doapplication.Database.listDatabaseHandler;
import com.example.to_doapplication.adapter.List_adapter;

import java.util.ArrayList;
import java.util.List;

public class ViewList extends AppCompatActivity implements DialogCloseListner{

    private RecyclerView listsrecyclerview;
    private List_adapter listadapter;
    private List<LIST> todolist;
    private listDatabaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list);
        listsrecyclerview=findViewById(R.id.listsRecyclerView);
        todolist=new ArrayList<>();
        db=new listDatabaseHandler(ViewList.this);
        db.openDatabase();
        listsrecyclerview.setLayoutManager(new LinearLayoutManager(ViewList.this));

        listadapter=new List_adapter(this,db);
        listsrecyclerview.setAdapter(listadapter);
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new listItemtouchHelper(listadapter));
        itemTouchHelper.attachToRecyclerView(listsrecyclerview);
        todolist=db.getAllLists();
        listadapter.setLists(todolist);
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        todolist = db.getAllLists();
        listadapter.setLists(todolist);
        listadapter.notifyDataSetChanged();
    }
}