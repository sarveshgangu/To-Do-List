package com.example.to_doapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.example.to_doapplication.Model.Task;
import com.example.to_doapplication.Database.taskDatabaseHandler;
import com.example.to_doapplication.adapter.task_adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class View_task extends AppCompatActivity implements DialogCloseListner {

    private RecyclerView taskrecyclerview;
    private List<Task>tasklist;
    private task_adapter taskadapter;
    private taskDatabaseHandler db;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createNotificationchannel();
        setContentView(R.layout.activity_view_task);
        tasklist=new ArrayList<>();
        db=new taskDatabaseHandler(View_task.this);
        db.openDatabase();
        taskrecyclerview=findViewById(R.id.tasksRecyclerView);
        taskrecyclerview.setLayoutManager(new LinearLayoutManager(View_task.this));
        taskadapter=new task_adapter(db,View_task.this);
        taskrecyclerview.setAdapter(taskadapter);
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new RecyclerItemTouchHelper(taskadapter));
        itemTouchHelper.attachToRecyclerView(taskrecyclerview);
        tasklist=db.getAllTasks();
        for(Task x:tasklist){
            Calendar cal = Calendar.getInstance();
            String dead = x.getDeadline();
            if(x.getStatus()==0) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy,HH:mm");
                Date date = null;
                try {
                    date = sdf.parse(dead);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                cal.setTime(date);
                setAlarm(cal);
            }else{
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy,HH:mm");
                Date date = null;
                try {
                    date = sdf.parse(dead);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                cal.setTime(date);
                removeAlarm();
            }
        }
        taskadapter.setTasks(tasklist);
    }

    private void removeAlarm() {
        Intent intent=new Intent(View_task.this,AlarmReciever.class);
        pendingIntent=PendingIntent.getBroadcast(View_task.this,0,intent,0);
        if(alarmManager==null){
            alarmManager=(AlarmManager) getSystemService(Context.ALARM_SERVICE);
        }
        alarmManager.cancel(pendingIntent);
    }

    private void setAlarm(Calendar cal) {
        alarmManager =(AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent(View_task.this,AlarmReciever.class);
        pendingIntent=PendingIntent.getBroadcast(View_task.this,0,intent,0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(),pendingIntent);
    }

    private void createNotificationchannel() {
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.O){
            CharSequence name="task channel";
            String des="Channel for alarm manager";
            int importance= NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel=new NotificationChannel("timeup",name,importance);
            channel.setDescription(des);
            NotificationManager notificationManager= getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        tasklist = db.getAllTasks();
        for(Task x:tasklist){
            Calendar cal = Calendar.getInstance();
            String dead = x.getDeadline();
            if(x.getStatus()==0) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy,HH:mm");
                Date date = null;
                try {
                    date = sdf.parse(dead);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                cal.setTime(date);
                setAlarm(cal);
            }else{
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy,HH:mm");
                Date date = null;
                try {
                    date = sdf.parse(dead);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                cal.setTime(date);
                removeAlarm();
            }
        }
        taskadapter.setTasks(tasklist);
        taskadapter.notifyDataSetChanged();

    }
}