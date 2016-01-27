package com.myproject.remindme.adapter.data;

import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.myproject.remindme.R;

import java.util.List;

/**
 * Created by Alexey on 1/20/2016.
 */
public abstract class AbstractAdapter<T extends Object> extends BaseAdapter {

    private int layout;

    protected LayoutInflater layoutInflater;
    protected FragmentActivity activity;
    protected TextView textNotFound;
    protected List<T> dataList;
    protected List<T> drawableList;
    protected View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showPopupMenu(v);
        }
    };

    @Override
    public int getCount() {
        return drawableList.size();
    }

    @Override
    public Object getItem(int position) {
        return drawableList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = this.layoutInflater.inflate(this.layout, parent, false);
            ImageButton button = (ImageButton) view.findViewById(R.id.menu_item_button);
            button.setOnClickListener(onClickListener);
            button.setId(position);
        }
        showInformation(position, view);
        return view;
    }

    @Override
    public boolean isEmpty() {
        boolean empty = drawableList.isEmpty();
        if (empty) {
            this.textNotFound.setVisibility(View.VISIBLE);
        } else {
            this.textNotFound.setVisibility(View.GONE);
        }
        return empty;
    }

    protected abstract void showInformation(int position, View view);

    protected abstract void showPopupMenu(final View view);

    public abstract void filter(String search);

    public FragmentActivity getActivity() {
        return activity;
    }

    public void setLayout(int layout) {
        this.layout = layout;
    }

    public void add(T object) {
        dataList.add(object);
    }

    public void remove(T object) {
        dataList.remove(object);
    }

    public List<T> getDataList() {
        return this.dataList;
    }
}
