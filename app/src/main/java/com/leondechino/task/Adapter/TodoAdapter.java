package com.leondechino.task.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leondechino.task.Data.DatabaseHandler;
import com.leondechino.task.MainActivity;
import com.leondechino.task.Model.TodoModel;
import com.leondechino.task.R;
import com.leondechino.task.UpdateTask;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {
    private List<TodoModel> todoList;
    private MainActivity activity;
    DatabaseHandler db;
    public TodoAdapter(MainActivity activity, DatabaseHandler db){
        this.activity = activity;
        this.db = db;
    }
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewtype){
        View intemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task, parent, false);
        return new ViewHolder(intemView);
    }

    public void onBindViewHolder(ViewHolder holder, int poss){

        TodoModel item = todoList.get(poss);
        holder.task.setText(item.getTask());
        holder.task.setChecked(item.getStatus()!=0);
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
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(activity, view);
                popup.getMenuInflater().inflate(R.menu.taskmod, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.menuUpdate:
                                // Do something when "Edit" option is clicked
                                editItem(holder.getAdapterPosition());
                                return true;
                            case R.id.menuDelete:
                                // Do something when "Delete" option is clicked
                                deleteItem(holder.getAdapterPosition());
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popup.show();
            }
        });
    }

    public int getItemCount(){return todoList.size();}

    public void setTasks(List<TodoModel> todoList){
        this.todoList = todoList;
        notifyDataSetChanged();
    }
    public void editItem(int position) {
        TodoModel item = todoList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());
        UpdateTask fragment = new UpdateTask();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), UpdateTask.TAG);
    }

    public void deleteItem(int position) {
        TodoModel item = todoList.get(position);
        db.deleteTask(item.getId());
        todoList.remove(position);
        notifyItemRemoved(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox task;
        Button btn;
        ViewHolder(View view){
            super(view);
            task = view.findViewById(R.id.chbTask);
            btn = view.findViewById(R.id.btnTask);
        }
    }
}
