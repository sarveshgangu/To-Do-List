package com.example.to_doapplication.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.to_doapplication.Model.LIST;

import java.util.ArrayList;
import java.util.List;

public class listDatabaseHandler extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String NAME = "ListDatabase";
    private static final String LIST_TABLE = "lists";
    private static final String ID = "id";
    private static final String LIST = "list";
    private static final String ITEMS = "items";
    private static final String CREATE_LIST_TABLE = "CREATE TABLE " + LIST_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + LIST + " TEXT, " + ITEMS + " TEXT)";
    private SQLiteDatabase db;
    public listDatabaseHandler(Context context){
        super(context,NAME,null,VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_LIST_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
        db.execSQL("DROP TABLE IF EXISTS "+LIST_TABLE);
        onCreate(db);
    }
    public void openDatabase(){
        db=this.getWritableDatabase();
    }
    public void insertList(com.example.to_doapplication.Model.LIST newlist){
        ContentValues cv = new ContentValues();
        cv.put(LIST,newlist.getListname());
        cv.put(ITEMS,newlist.getItems());
        db.insert(LIST_TABLE, null, cv);
    }
    @SuppressLint("Range")
    public List<com.example.to_doapplication.Model.LIST> getAllLists(){
        List<com.example.to_doapplication.Model.LIST> todolist=new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try{
            cur = db.query(LIST_TABLE, null, null, null, null, null, null, null);
            if(cur != null){
                if(cur.moveToFirst()){
                    do{
                        com.example.to_doapplication.Model.LIST list = new LIST();
                        list.setId(cur.getInt(cur.getColumnIndex(ID)));
                        list.setItems(cur.getString(cur.getColumnIndex(ITEMS)));
                        list.setListname(cur.getString(cur.getColumnIndex(LIST)));
                        todolist.add(list);
                    }
                    while(cur.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            assert cur != null;
            cur.close();
        }
        return todolist;
    }
    public void updateList(int id, String listname,String items) {
        ContentValues cv = new ContentValues();
        cv.put(LIST, listname);
        cv.put(ITEMS,items);
        db.update(LIST_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }
    public void deleteList(int id){
        db.delete(LIST_TABLE, ID + "= ?", new String[] {String.valueOf(id)});
    }
}
