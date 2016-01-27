package com.myproject.remindme.asynctask;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.myproject.remindme.R;
import com.myproject.remindme.adapter.data.IdeasAdapter;
import com.myproject.remindme.database.data.Idea;
import com.myproject.remindme.database.handler.DatabaseHandler;

import java.util.List;

/**
 * Created by Alexey on 1/21/2016.
 */
public class LoadDataIdeasAsyncTask extends AsyncTask<DatabaseHandler.IdeasTable, Void, IdeasAdapter> {

    private FragmentActivity activity;
    private View view;

    private ProgressBar progressBar;
    private ListView listView;

    public LoadDataIdeasAsyncTask(FragmentActivity activity, View view) {
        this.activity = activity;
        this.view = view;
        listView = (ListView) view.findViewById(R.id.listview_ideas);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBarLoadData);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);
    }

    @SuppressWarnings("ResourceType")
    @Override
    protected IdeasAdapter doInBackground(DatabaseHandler.IdeasTable... params) {
        for (DatabaseHandler.IdeasTable handler : params) {
            List<Idea> ideaList = handler.getAll();
            IdeasAdapter adapter = new IdeasAdapter(activity, view, ideaList);
            listView.setAdapter(adapter);
            return adapter;
        }
        return null;
    }

    @Override
    protected void onPostExecute(IdeasAdapter ideasAdapter) {
        super.onPostExecute(ideasAdapter);
        ideasAdapter.isEmpty();
        progressBar.setVisibility(View.GONE);
    }
}
