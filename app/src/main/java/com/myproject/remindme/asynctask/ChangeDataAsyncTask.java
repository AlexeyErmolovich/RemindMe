package com.myproject.remindme.asynctask;

import android.os.AsyncTask;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;

import com.myproject.remindme.R;

/**
 * Created by Alexey on 1/19/2016.
 */
public class ChangeDataAsyncTask extends AsyncTask<BaseAdapter, Void, Void> {

    private ProgressBar progressBar;
    private BaseAdapter adapter;

    public ChangeDataAsyncTask(View view) {
        progressBar = (ProgressBar) view.findViewById(R.id.progressBarLoadData);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected Void doInBackground(BaseAdapter... params) {
        for (BaseAdapter adapter : params) {
            this.adapter = adapter;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        adapter.notifyDataSetChanged();
        adapter.isEmpty();
        progressBar.setVisibility(View.GONE);
    }
}
