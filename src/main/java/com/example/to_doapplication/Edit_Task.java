package com.example.to_doapplication;

import static java.util.Calendar.YEAR;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.to_doapplication.Database.taskDatabaseHandler;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;

public class Edit_Task extends BottomSheetDialogFragment {
    public static final String TAG = "ActionBottomDialog";
    private TextView newTaskName,newDesc,newDate,newTime;
    private Button newTaskSaveButton;

    private taskDatabaseHandler db;

    public static Edit_Task newInstance(){
        return new Edit_Task();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_add_task, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newTaskName = requireView().findViewById(R.id.newTaskname);
        newDesc = requireView().findViewById(R.id.description);
        newDate = requireView().findViewById(R.id.date);
        newTime = requireView().findViewById(R.id.time);
        newTaskSaveButton = getView().findViewById(R.id.saveButton);

        boolean isUpdate = false;

        final Bundle bundle = getArguments();
        if(bundle != null){
            isUpdate = true;
            String task = bundle.getString("task");
            newTaskName.setText(task);
            newDesc.setText(bundle.getString("description"));
            String dead=bundle.getString("deadline");
            newDate.setText(bundle.getString("date"));
            newTime.setText(bundle.getString("time"));
            assert task != null;
            if(task.length()>0)
                newTaskSaveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
        }

        db = new taskDatabaseHandler(getActivity());
        db.openDatabase();
        newDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HandleDate();
            }
        });
        newTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HandleTime();
            }
        });


        newTaskName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    newTaskSaveButton.setEnabled(false);
                    newTaskSaveButton.setTextColor(Color.GRAY);
                }
                else{
                    newTaskSaveButton.setEnabled(true);
                    newTaskSaveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        final boolean finalIsUpdate = isUpdate;
        newTaskSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = newTaskName.getText().toString();
                String desc= newDesc.getText().toString();
                String dead=newDate.getText().toString()+","+newTime.getText().toString();
                if(text.length()==0||newTime.getText().toString().length()==0||newDate.getText().toString().length()==0){
                    Toast.makeText(getContext(),"Insufficient details" ,Toast.LENGTH_LONG).show();
                }
                else{
                    if(finalIsUpdate){
                        db.updateTask(bundle.getInt("id"),text,desc,dead);
                    }

                }


                dismiss();
            }
        });
    }
    private void HandleTime() {
        Calendar calendar=Calendar.getInstance();
        int Hour=calendar.get(Calendar.HOUR_OF_DAY);
        int Minute=calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog=new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                if(minute>=10&&hour>=10) {
                    String s = hour + ":" + minute;
                    newTime.setText(s);
                }else if(hour>=10&&minute<10){
                    String s = hour + ":0" + minute;
                    newTime.setText(s);
                }else if(hour<10&&minute>=10) {
                    String s = "0"+hour + ":" + minute;
                    newTime.setText(s);
                }else if(hour<10&&minute<10) {
                    String s = "0"+ hour + ":0" + minute;
                    newTime.setText(s);
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
        DatePickerDialog datePickerDialog =new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                String s= date+"/"+(month+1)+"/"+year;
                newDate.setText(s);
            }
        },Year,Month,Day);
        datePickerDialog.show();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog){
        Activity activity = getActivity();
        if(activity instanceof DialogCloseListner)
            ((DialogCloseListner)activity).handleDialogClose(dialog);
    }
}
