package com.myproject.remindme.asynctask;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.myproject.remindme.R;
import com.myproject.remindme.adapter.data.BirthdaysAdapter;
import com.myproject.remindme.database.data.Birthday;
import com.myproject.remindme.database.handler.DatabaseHandler;

import java.util.List;

/**
 * Created by Alexey on 1/19/2016.
 */
public class LoadDataBirthdaysAsyncTask extends AsyncTask<DatabaseHandler.BirthdaysTable, Void, BirthdaysAdapter> {

    private FragmentActivity activity;
    private View view;

    private ProgressBar progressBar;
    private ListView listView;

    public LoadDataBirthdaysAsyncTask(FragmentActivity activity, View view) {
        this.activity = activity;
        this.view = view;
        listView = (ListView) view.findViewById(R.id.listView_birthdays);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBarLoadData);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);
    }

    @SuppressWarnings("ResourceType")
    @Override
    protected BirthdaysAdapter doInBackground(DatabaseHandler.BirthdaysTable... params) {
        for (DatabaseHandler.BirthdaysTable handler : params) {
            List<Birthday> birthdayList = handler.getAll();
            BirthdaysAdapter adapter = new BirthdaysAdapter(activity, view, birthdayList);
            listView.setAdapter(adapter);
            return adapter;
        }
        return null;
    }

    @Override
    protected void onPostExecute(BirthdaysAdapter birthdaysAdapter) {
        super.onPostExecute(birthdaysAdapter);
        birthdaysAdapter.isEmpty();
        progressBar.setVisibility(View.GONE);
    }
}
