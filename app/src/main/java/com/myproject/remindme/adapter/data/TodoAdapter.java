package com.myproject.remindme.adapter.data;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.myproject.remindme.R;
import com.myproject.remindme.database.data.AbstractData;
import com.myproject.remindme.database.data.Todo;
import com.myproject.remindme.dialog.TodoDialog;
import com.myproject.remindme.eventbus.BusProvider;
import com.myproject.remindme.eventbus.TodoChangeEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexey on 1/22/2016.
 */
public class TodoAdapter extends AbstractAdapter<Todo> {
    public TodoAdapter(FragmentActivity activity, View view, List<Todo> todoList) {
        this.dataList = todoList;
        this.drawableList = todoList;
        this.activity = activity;
        this.layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.textNotFound = (TextView) view.findViewById(R.id.textNotFound);
        this.textNotFound.setText(R.string.exception_not_found_todo);
        this.setLayout(R.layout.listview_item_todo);
    }

    @Override
    public void filter(String search) {
        if (search.isEmpty()) {
            drawableList = dataList;
            this.textNotFound.setText(R.string.exception_not_found_todo);
        } else {
            drawableList = new ArrayList<>();
            for (Todo todo : dataList) {
                if (todo.getHeader().contains(search)) {
                    drawableList.add(todo);
                } else if (todo.getTodo().contains(todo.getTodoItem(false, search))) {
                    drawableList.add(todo);
                } else if (todo.getKeyword().contains(search)) {
                    drawableList.add(todo);
                }
            }
        }
    }

    @Override
    protected void showInformation(int position, View view) {
        Todo todo = (Todo) getItem(position);
        TextView textHandler = (TextView) view.findViewById(R.id.textHeader);
        ListView listViewTodo = (ListView) view.findViewById(R.id.listview_todo_items);
        TextView textKeyword = (TextView) view.findViewById(R.id.textKeyword);
        textHandler.setText(todo.getHeader());
        listViewTodo.setAdapter(new TodoItemAdapter(activity, todo));
        textKeyword.setText(todo.getKeyword());
    }

    @Override
    protected void showPopupMenu(final View view) {
        PopupMenu popupMenu = new PopupMenu(this.getActivity(), view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_listview, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Todo todo = (Todo) getItem(view.getId());
                if (item.getItemId() == R.id.menu_item_listview_delete) {
                    BusProvider.getInstance().post(new TodoChangeEvent(AbstractData.ELEMENT_DELETE, todo));
                } else if (item.getItemId() == R.id.menu_item_listview_revise) {
                    TodoDialog dialog = new TodoDialog(TodoDialog.TODO_REVISE, todo);
                    dialog.show(getActivity().getFragmentManager(), TodoDialog.TAG);
                }
                return true;
            }
        });
        popupMenu.show();
    }
}
