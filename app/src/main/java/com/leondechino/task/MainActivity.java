package com.leondechino.task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.leondechino.task.Adapter.TodoAdapter;
import com.leondechino.task.Data.DatabaseHandler;
import com.leondechino.task.Model.TodoModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListener{

    private EditText txtAddTask;
    private RecyclerView taskRecycler;
    private TodoAdapter adapter;
    private List<TodoModel> list;

    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHandler(this);
        db.openDatabase();
        list = new ArrayList<>();

        txtAddTask = findViewById(R.id.txtInput);
        taskRecycler = findViewById(R.id.taskRecycler);
        taskRecycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TodoAdapter(this, db);
        taskRecycler.setAdapter(adapter);

        update();
    }
    public void AddTask(View view){
        if (txtAddTask.getText().toString().isEmpty()){
            Toast.makeText(this, "Necesitas agregar un texto", Toast.LENGTH_SHORT).show();
            return;
        }
        TodoModel task = new TodoModel();
        task.setTask(txtAddTask.getText().toString());
        task.setStatus(0);
        db.insertTask(task);
        txtAddTask.setText("");
        update();
    }
    public void onDismiss(@NonNull DialogInterface dialog){
        Activity activity = this;
        if(activity instanceof DialogCloseListener) ((DialogCloseListener)activity).handleDialogClose(dialog);
    }

    public void update(){
        list = db.getAllTasks();
        Collections.reverse(list);
        adapter.setTasks(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        update();
    }
}