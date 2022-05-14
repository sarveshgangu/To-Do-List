package com.example.to_doapplication;

import static java.util.Calendar.YEAR;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.to_doapplication.Model.Task;
import com.example.to_doapplication.Database.taskDatabaseHandler;

import java.util.Calendar;

public class Add_task extends AppCompatActivity {
    public static final String TAG = "Add_the_task";
    private TextView taskname,Date,Time,Description;
    private Button savebutton;
    private taskDatabaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        taskname=findViewById(R.id.newTaskname);
        Date=findViewById(R.id.date);
        Time=findViewById(R.id.time);
        boolean isupdate=false;
        final Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            isupdate=true;
            String taskname=bundle.getString("task");
            String deadline=bundle.getString("deadline");
            String description=bundle.getString("description");

        }
        db=new taskDatabaseHandler(Add_task.this);
        db.openDatabase();
        Description=findViewById(R.id.description);
        savebutton=findViewById(R.id.saveButton);

        Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HandleDate();
            }
        });
        Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HandleTime();
            }
        });
        final boolean Isupdate=isupdate;
        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = taskname.getText().toString();
                String dete= Date.getText().toString();
                String teme= Date.getText().toString();
                if(text.length()==0||dete.length()==0||teme.length()==0){
                    Toast.makeText(Add_task.this ,"Insufficient details" ,Toast.LENGTH_LONG).show();
                }
                else {
                    if(!Isupdate) {
                        Task task = new Task();
                        task.setTaskname(text);
                        task.setDescription(Description.getText().toString());
                        String s = Date.getText().toString() + "," + Time.getText().toString();
                        task.setDeadline(s);
                        task.setStatus(0);
                        db.insertTask(task);
                        Toast.makeText(Add_task.this, "Task saved successfully", Toast.LENGTH_LONG).show();
                    }else{
                        db.updateTask(bundle.getInt("id"), text,bundle.getString("description"),bundle.getString("deadline"));
                    }
                    finish();
                }
            }
        });
    }



    private void HandleTime() {
        Calendar calendar=Calendar.getInstance();
        int Hour=calendar.get(Calendar.HOUR_OF_DAY);
        int Minute=calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog=new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                if(minute>=10&&hour>=10) {
                    String s = hour + ":" + minute;
                    Time.setText(s);
                }else if(hour>=10&&minute<10){
                    String s = hour + ":0" + minute;
                    Time.setText(s);
                }else if(hour<10&&minute>=10) {
                    String s = "0"+hour + ":" + minute;
                    Time.setText(s);
                }else if(hour<10&&minute<10) {
                    String s = "0"+ hour + ":0" + minute;
                    Time.setText(s);
                }
            }
        },Hour,Minute,true);
        timePickerDialog.show();
    }

    private void HandleDate() {
        Calendar calendar=Calendar.getInstance();
        int Year=calendar.get(YEAR);
        int Month=calendar.get(Calendar.MONTH);
        int Day=calendar.get(Calendar.DATE);
        DatePickerDialog datePickerDialog =new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                String s= date+"/"+(month+1)+"/"+year;
                Date.setText(s);
            }
        },Year,Month,Day);
        datePickerDialog.show();
    }



}