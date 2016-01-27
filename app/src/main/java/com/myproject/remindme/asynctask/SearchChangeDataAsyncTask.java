package com.myproject.remindme.asynctask;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.myproject.remindme.R;
import com.myproject.remindme.adapter.data.AbstractAdapter;

/**
 * Created by Alexey on 1/20/2016.
 */
public class SearchChangeDataAsyncTask extends AsyncTask<AbstractAdapter, Void, Void> {

    private String search;
    private AbstractAdapter adapter;
    private ProgressBar progressBar;
    private TextView textNotFound;

    public SearchChangeDataAsyncTask(View view, String search) {
        this.search = search;
        progressBar = (ProgressBar) view.findViewById(R.id.progressBarLoadData);
        textNotFound = (TextView) view.findViewById(R.id.textNotFound);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);
        textNotFound.setText(R.string.exception_search_not_found);
    }

    @Override
    protected Void doInBackground(AbstractAdapter... params) {
        for (AbstractAdapter adapter : params) {
            this.adapter = adapter;
            this.adapter.filter(search);
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