package com.myproject.remindme.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.myproject.remindme.fragment.AbstractTabFragment;
import com.myproject.remindme.fragment.BirthdaysFragment;
import com.myproject.remindme.fragment.IdeasFragment;
import com.myproject.remindme.fragment.TodoFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Алексей on 14/01/2016.
 */
public class TabsPagerFragmentAdapter extends FragmentPagerAdapter {

    private Map<Integer, AbstractTabFragment> tabs;

    public TabsPagerFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        initTabsMap(context);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs.get(position).getTitle();
    }

    @Override
    public Fragment getItem(int position) {
        return tabs.get(position);
    }

    @Override
    public int getCount() {
        return tabs.size();
    }

    private void initTabsMap(Context context) {
        tabs = new HashMap<>();
        tabs.put(0, IdeasFragment.getInstance(context));
        tabs.put(1, TodoFragment.getInstance(context));
        tabs.put(2, BirthdaysFragment.getInstance(context));
    }
}
