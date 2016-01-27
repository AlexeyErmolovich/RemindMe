package com.myproject.remindme.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

import com.myproject.remindme.eventbus.BusProvider;

/**
 * Created by Алексей on 15/01/2016.
 */
public abstract class AbstractTabFragment extends Fragment {

    protected Context context;
    protected View view;

    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    protected void registrationClassInEventBus() {
        BusProvider.getInstance().register(this);
    }
}
