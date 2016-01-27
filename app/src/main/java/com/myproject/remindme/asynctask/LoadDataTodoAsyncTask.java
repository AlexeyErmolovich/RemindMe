package com.myproject.remindme.asynctask;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.myproject.remindme.R;
import com.myproject.remindme.adapter.data.TodoAdapter;
import com.myproject.remindme.database.data.Todo;
import com.myproject.remindme.database.handler.DatabaseHandler;

import java.util.List;

/**
 * Created by Alexey on 1/22/2016.
 */
public class LoadDataTodoAsyncTask extends AsyncTask<DatabaseHandler.TodoTable, Void, TodoAdapter> {

    private FragmentActivity activity;
    private View view;

    private ProgressBar progressBar;
    private ListView listView;

    public LoadDataTodoAsyncTask(FragmentActivity activity, View view) {
        this.activity = activity;
        this.view = view;
        listView = (ListView) view.findViewById(R.id.listview_todo);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBarLoadData);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);
    }

    @SuppressWarnings("ResourceType")
    @Override
    protected TodoAdapter doInBackground(DatabaseHandler.TodoTable... params) {
        for (DatabaseHandler.TodoTable handler : params) {
            List<Todo> todoList = handler.getAll();
            TodoAdapter adapter = new TodoAdapter(activity, view, todoList);
            listView.setAdapter(adapter);
            return adapter;
        }
        return null;
    }

    @Override
    protected void onPostExecute(TodoAdapter todoAdapter) {
        super.onPostExecute(todoAdapter);
        todoAdapter.isEmpty();
        progressBar.setVisibility(View.GONE);
    }
}
