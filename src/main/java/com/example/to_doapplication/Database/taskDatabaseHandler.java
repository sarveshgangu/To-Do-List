package com.example.to_doapplication.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.to_doapplication.Model.Task;

import java.util.ArrayList;
import java.util.List;

public class taskDatabaseHandler extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String NAME = "toDoListDatabase";
    private static final String TODO_TABLE = "todo";
    private static final String ID = "id";
    private static final String TASK = "task";
    private static final String STATUS = "status";
    private static final String DESCRRIPTION="description";
    private static final String DEADLINE="deadline";

    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK + " TEXT, "
            + STATUS + " INTEGER, "+DESCRRIPTION + " TEXT, "+DEADLINE+" TEXT)";
    private SQLiteDatabase db;
    public taskDatabaseHandler(Context context){
        super(context,NAME,null,VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TODO_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        // Create tables again
        onCreate(db);
    }

    public void openDatabase() {
        db = this.getWritableDatabase();
    }
    public void insertTask(Task task){
        ContentValues cv = new ContentValues();
        cv.put(TASK, task.getTaskname());
        cv.put(DESCRRIPTION,task.getDescription());
        cv.put(DEADLINE,task.getDeadline());
        cv.put(STATUS, 0);
        db.insert(TODO_TABLE, null, cv);
    }
    @SuppressLint("Range")
    public List<Task> getAllTasks(){
        List<Task> taskList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try{
            cur = db.query(TODO_TABLE, null, null, null, null, null, null, null);
            if(cur != null){
                if(cur.moveToFirst()){
                    do{
                        Task task = new Task();
                        task.setId(cur.getInt(cur.getColumnIndex(ID)));
                        task.setTaskname(cur.getString(cur.getColumnIndex(TASK)));
                        task.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));
                        task.setDescription(cur.getString(cur.getColumnIndex(DESCRRIPTION)));
                        task.setDeadline(cur.getString(cur.getColumnIndex(DEADLINE)));
                        taskList.add(task);
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
        return taskList;
    }
    public void updateStatus(int id, int status){
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        db.update(TODO_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }
    public void updateTask(int id, String taskname,String description,String deadline) {
        ContentValues cv = new ContentValues();
        cv.put(TASK, taskname);
        cv.put(DEADLINE,deadline);
        cv.put(DESCRRIPTION, description);
        db.update(TODO_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }
    public void deleteTask(int id){
        db.delete(TODO_TABLE, ID + "= ?", new String[] {String.valueOf(id)});
    }
}
