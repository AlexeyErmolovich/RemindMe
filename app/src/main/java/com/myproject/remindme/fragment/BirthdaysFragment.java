package com.myproject.remindme.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myproject.remindme.R;
import com.myproject.remindme.adapter.data.BirthdaysAdapter;
import com.myproject.remindme.asynctask.ChangeDataAsyncTask;
import com.myproject.remindme.asynctask.LoadDataBirthdaysAsyncTask;
import com.myproject.remindme.asynctask.SearchChangeDataAsyncTask;
import com.myproject.remindme.database.data.AbstractData;
import com.myproject.remindme.database.data.Birthday;
import com.myproject.remindme.database.handler.DatabaseHandler;
import com.myproject.remindme.eventbus.BirthdaysChangeEvent;
import com.myproject.remindme.service.BirthdaysService;
import com.squareup.otto.Subscribe;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Алексей on 14/01/2016.
 */
@SuppressLint("ValidFragment")
public class BirthdaysFragment extends AbstractTabFragment {

    private static final int LAYOUT = R.layout.fragment_birthdays;

    private static BirthdaysFragment fragment;

    private BirthdaysAdapter adapter;
    private DatabaseHandler.BirthdaysTable handler;

    public static BirthdaysFragment getInstance(Context context) {
        if (BirthdaysFragment.fragment == null) {
            fragment = new BirthdaysFragment(context);
            fragment.setTitle(context.getString(R.string.tab_item_birthdays));
        }
        fragment.setContext(context);
        return fragment;
    }

    @SuppressLint("ValidFragment")
    private BirthdaysFragment(Context context) {
        handler = DatabaseHandler.getInstance(context).getBirthdaysTable();
        registrationClassInEventBus();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
        context.startService(new Intent(context, BirthdaysService.class));
        initListView();
        return view;
    }

    @Subscribe
    public void onBirthdaysChange(BirthdaysChangeEvent event) {
        Birthday birthday = event.getBirthday();
        ChangeDataAsyncTask changeData = new ChangeDataAsyncTask(view);
        if (event.getOperation() == AbstractData.ELEMENT_DELETE) {
            handler.remove(birthday);
            adapter.remove(birthday);
            changeData.execute(adapter);
        } else if (event.getOperation() == AbstractData.ELEMENT_ADD) {
            handler.add(birthday);
            adapter.add(birthday);
            changeData.execute(adapter);
        } else if (event.getOperation() == AbstractData.ELEMENT_REVISE) {
            handler.update(birthday);
            changeData.execute(adapter);
        } else if (event.getOperation() == AbstractData.ELEMENT_SEARCH) {
            SearchChangeDataAsyncTask searchChangeData = new SearchChangeDataAsyncTask(view, event.getSearch());
            searchChangeData.execute(adapter);
        }
    }

    private void initListView() {
        LoadDataBirthdaysAsyncTask loadData = new LoadDataBirthdaysAsyncTask(getActivity(), view);
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
