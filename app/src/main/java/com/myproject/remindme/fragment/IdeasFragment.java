package com.myproject.remindme.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myproject.remindme.R;
import com.myproject.remindme.adapter.data.IdeasAdapter;
import com.myproject.remindme.asynctask.ChangeDataAsyncTask;
import com.myproject.remindme.asynctask.LoadDataIdeasAsyncTask;
import com.myproject.remindme.asynctask.SearchChangeDataAsyncTask;
import com.myproject.remindme.database.data.AbstractData;
import com.myproject.remindme.database.data.Idea;
import com.myproject.remindme.database.handler.DatabaseHandler;
import com.myproject.remindme.eventbus.IdeasChangeEvent;
import com.squareup.otto.Subscribe;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Алексей on 14/01/2016.
 */
@SuppressLint("ValidFragment")
public class IdeasFragment extends AbstractTabFragment {

    private static final int LAYOUT = R.layout.fragment_ideas;

    private static IdeasFragment fragment;

    private IdeasAdapter adapter;
    private DatabaseHandler.IdeasTable handler;

    public static IdeasFragment getInstance(Context context) {
        if (IdeasFragment.fragment == null) {
            fragment = new IdeasFragment(context);
            fragment.setTitle(context.getString(R.string.tab_item_ideas));
        }
        fragment.setContext(context);
        return fragment;
    }

    @SuppressLint("ValidFragment")
    private IdeasFragment(Context context) {
        handler = DatabaseHandler.getInstance(context).getIdeasTable();
        registrationClassInEventBus();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
        initListView();
        return view;
    }

    @Subscribe
    public void onIdeasChange(IdeasChangeEvent event) {
        Idea idea = event.getIdea();
        ChangeDataAsyncTask changeData = new ChangeDataAsyncTask(view);
        if (event.getOperation() == AbstractData.ELEMENT_DELETE) {
            handler.remove(idea);
            adapter.remove(idea);
            changeData.execute(adapter);
        } else if (event.getOperation() == AbstractData.ELEMENT_ADD) {
            handler.add(idea);
            adapter.add(idea);
            changeData.execute(adapter);
        } else if (event.getOperation() == AbstractData.ELEMENT_REVISE) {
            handler.update(idea);
            changeData.execute(adapter);
        } else if (event.getOperation() == AbstractData.ELEMENT_SEARCH) {
            SearchChangeDataAsyncTask searchChangeData = new SearchChangeDataAsyncTask(view, event.getSearch());
            searchChangeData.execute(adapter);
        }
    }

    private void initListView() {
        LoadDataIdeasAsyncTask loadData = new LoadDataIdeasAsyncTask(getActivity(), view);
        loadData.execute(handler);
        try {
            adapter = loadData.get(5, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
