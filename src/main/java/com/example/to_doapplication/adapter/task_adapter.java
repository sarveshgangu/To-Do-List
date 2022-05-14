package com.example.to_doapplication.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.to_doapplication.Edit_Task;
import com.example.to_doapplication.Model.Task;
import com.example.to_doapplication.R;
import com.example.to_doapplication.Database.taskDatabaseHandler;
import com.example.to_doapplication.View_task;

import java.util.List;

public class task_adapter extends RecyclerView.Adapter<task_adapter.ViewHolder> {
    Context context;

    private taskDatabaseHandler db;
    public void ViewHolder(Context context){
        this.context=context;
    }
    private List<Task> Tasklist;
    private View_task activity;
    public task_adapter(taskDatabaseHandler db,View_task activity){
        this.activity=activity;
        this.db=db;
    }
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View itemview= LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout,parent,false);
        return new ViewHolder(itemview);
    }
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Task item = Tasklist.get(position);
        holder.task.setText(item.getTaskname());
        holder.task.setChecked(toBoolean(item.getStatus()));
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    db.updateStatus(item.getId(), 1);

                } else {
                    db.updateStatus(item.getId(), 0);

                }
            }
        });
        holder.description.setText(item.getDescription());
        holder.Deadline.setText(item.getDeadline());
    }
    @Override
    public int getItemCount() {
        return Tasklist.size();
    }
    public Context getContext() {
        return activity;
    }
    public void deleteItem(int position) {
        Task item = Tasklist.get(position);
        Tasklist.remove(position);
        db.deleteTask(item.getId());

        notifyItemRemoved(position);
    }
    public void editItem(int position) {
        Task item = Tasklist.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTaskname());
        bundle.putString("dead",item.getDeadline());
        bundle.putString("description", item.getDescription());
        Edit_Task fragment = new Edit_Task();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), Edit_Task.TAG);
    }


    private boolean toBoolean(int n) {
        return n != 0;
    }
    public void setTasks(List<Task> Tasklist){
        this.Tasklist= Tasklist;
        notifyDataSetChanged();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox task;
        TextView description;
        TextView Deadline;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            task= itemView.findViewById(R.id.todoCheckBox);
            description=itemView.findViewById(R.id.description);
            Deadline=itemView.findViewById(R.id.date_time);
        }
    }
}
