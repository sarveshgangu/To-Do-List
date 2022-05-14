package com.example.to_doapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.to_doapplication.Model.LIST;
import com.example.to_doapplication.Database.listDatabaseHandler;

public class Add_list extends AppCompatActivity {
    private EditText listname,items;
    private Button savebutton;
    private listDatabaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);
        listname=findViewById(R.id.newListname);
        items=findViewById(R.id.items);
        boolean isupdate=false;
        final Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            isupdate=true;
            String listname=bundle.getString("listname");
            String items=bundle.getString("items");
        }
        db=new listDatabaseHandler(Add_list.this);
        db.openDatabase();
        savebutton=findViewById(R.id.save_listButton);
        final boolean Isupdate=isupdate;
        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = listname.getText().toString();
                if(text.length()==0){
                    Toast.makeText(Add_list.this ,"Atleast list name must be given" ,Toast.LENGTH_LONG).show();
                }
                else {
                    if(!Isupdate) {
                        LIST list = new LIST();
                        list.setListname(listname.getText().toString());
                        list.setItems(items.getText().toString());
                        db.insertList(list);
                        Toast.makeText(Add_list.this, "List saved successfully", Toast.LENGTH_LONG).show();
                    }else{
                        db.updateList(bundle.getInt("id"), text,bundle.getString("items"));
                    }
                    finish();
                }
            }
        });
    }

}