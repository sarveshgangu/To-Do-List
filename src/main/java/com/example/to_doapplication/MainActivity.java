package com.example.to_doapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView Addtask,viewList,Addlist;
    private Button viewtask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewtask=(Button) findViewById(R.id.Viewtasks);
        viewtask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, View_task.class);
                startActivity(intent);
            }
        });
        Addtask=(TextView) findViewById(R.id.Addtasks);
        Addtask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Add_task.class);
                startActivity(intent);
            }
        });
        viewList=(TextView) findViewById(R.id.View_Lists);
        viewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ViewList.class);
                startActivity(intent);
            }
        });
        Addlist=(TextView)findViewById(R.id.Add_list);
        Addlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Add_list.class);
                startActivity(intent);
            }
        });

    }
}