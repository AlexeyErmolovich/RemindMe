package com.myproject.remindme.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myproject.remindme.R;
import com.myproject.remindme.adapter.data.TodoAdapter;
import com.myproject.remindme.asynctask.ChangeDataAsyncTask;
import com.myproject.remindme.asynctask.LoadDataTodoAsyncTask;
import com.myproject.remindme.asynctask.SearchChangeDataAsyncTask;
import com.myproject.remindme.database.data.AbstractData;
import com.myproject.remindme.database.data.Todo;
import com.myproject.remindme.database.handler.DatabaseHandler;
import com.myproject.remindme.eventbus.TodoChangeEvent;
import com.squareup.otto.Subscribe;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Алексей on 14/01/2016.
 */
@SuppressLint("ValidFragment")
public class TodoFragment extends AbstractTabFragment {

    private static final int LAYOUT = R.layout.fragment_todo;

    private static TodoFragment fragment;

    private TodoAdapter adapter;
    private DatabaseHandler.TodoTable handler;

    public static TodoFragment getInstance(Context context) {
        if (TodoFragment.fragment == null) {
            fragment = new TodoFragment(context);
            fragment.setTitle(context.getString(R.string.tab_item_todo));
        }
        fragment.setContext(context);
        return fragment;
    }

    @SuppressLint("ValidFragment")
    private TodoFragment(Context context) {
        handler = DatabaseHandler.getInstance(context).getTodoTable();
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
    public void onTodoChange(TodoChangeEvent event) {
        Todo todo = event.getTodo();
        ChangeDataAsyncTask changeData = new ChangeDataAsyncTask(view);
        if (event.getOperation() == AbstractData.ELEMENT_DELETE) {
            handler.remove(todo);
            adapter.remove(todo);
            changeData.execute(adapter);
        } else if (event.getOperation() == AbstractData.ELEMENT_ADD) {
            handler.add(todo);
            adapter.add(todo);
            changeData.execute(adapter);
        } else if (event.getOperation() == AbstractData.ELEMENT_REVISE) {
            handler.update(todo);
            changeData.execute(adapter);
        } else if (event.getOperation() == AbstractData.ELEMENT_SEARCH) {
            SearchChangeDataAsyncTask searchChangeData = new SearchChangeDataAsyncTask(view, event.getSearch());
            searchChangeData.execute(adapter);
        }
    }

    private void initListView() {
        LoadDataTodoAsyncTask loadData = new LoadDataTodoAsyncTask(getActivity(), view);
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
