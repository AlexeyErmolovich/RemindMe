package com.myproject.remindme.adapter.data;

import android.content.Context;
import android.graphics.Paint;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;

import com.myproject.remindme.R;
import com.myproject.remindme.database.data.Todo;
import com.myproject.remindme.database.handler.DatabaseHandler;

import java.util.List;

/**
 * Created by Alexey on 1/23/2016.
 */
public class TodoItemAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private List<Todo.TodoItem> todoItemList;
    private DatabaseHandler.TodoItemTable todoItemTable;
    private Todo todo;

    public TodoItemAdapter(FragmentActivity activity, Todo todo) {
        this.todo = todo;
        this.todoItemList = todo.getTodo();
        this.todoItemTable = DatabaseHandler.getInstance(activity).getTodoItemTable();
        this.layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public TodoItemAdapter(FragmentActivity activity, List<Todo.TodoItem> todoItemList) {
        this.todo = null;
        this.todoItemTable = null;
        this.todoItemList = todoItemList;
        this.layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return todoItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return todoItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final Todo.TodoItem todoItem = (Todo.TodoItem) getItem(position);
        if (view == null) {
            view = layoutInflater.inflate(R.layout.listview_item_todo_items, parent, false);
            ImageButton button = (ImageButton) view.findViewById(R.id.todo_item_delete);
            button.setId(position);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    remove(v.getId());
                }
            });
        }
        final CheckBox checkBox = (CheckBox) view.findViewById(R.id.todo_item_checked);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkBox.setPaintFlags(checkBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    checkBox.setPaintFlags(checkBox.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                }
                todoItem.setChecked(isChecked);
                if (todoItemTable != null) {
                    todoItemTable.update(todo.getId(), todoItem);
                }
            }
        });
        if (todoItem.isChecked()) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }
        checkBox.setText(todoItem.getTodoItem());

        return view;
    }

    public void add(Todo.TodoItem todoItem) {
        todoItemList.add(todoItem);
        notifyDataSetChanged();
        if (this.todoItemTable != null) {
            this.todoItemTable.add(todo.getId(), todoItem);
        }
    }

    public void remove(Todo.TodoItem todoItem) {
        todoItemList.remove(todoItem);
        notifyDataSetChanged();
        if (this.todoItemTable != null) {
            this.todoItemTable.remove(todoItem);
        }
    }

    public void remove(int position) {
        remove((Todo.TodoItem) getItem(position));
    }
}