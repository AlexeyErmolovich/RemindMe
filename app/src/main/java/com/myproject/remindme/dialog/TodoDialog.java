package com.myproject.remindme.dialog;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.myproject.remindme.R;
import com.myproject.remindme.adapter.data.TodoItemAdapter;
import com.myproject.remindme.database.data.AbstractData;
import com.myproject.remindme.database.data.Todo;
import com.myproject.remindme.eventbus.BusProvider;
import com.myproject.remindme.eventbus.TodoChangeEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexey on 1/22/2016.
 */
@SuppressLint("ValidFragment")
public class TodoDialog extends DialogFragment {

    public final static String TAG = "TodoDialog";
    public final static int TODO_ADD = 1;
    public final static int TODO_REVISE = 2;

    private View view;
    private EditText editHeader;
    private EditText editKeyword;
    private Todo todo;
    private int operation;
    private TodoItemAdapter adapter;

    @SuppressLint("ValidFragment")
    public TodoDialog(int operation) {
        this(operation, null);
    }

    @SuppressLint("ValidFragment")
    public TodoDialog(int operation, Todo todo) {
        this.operation = operation;
        this.todo = todo;
        if (operation != TODO_ADD && operation != TODO_REVISE) {
            throw new IllegalArgumentException("Operation not found");
        }
        if (operation == TODO_REVISE && todo == null) {
            throw new IllegalArgumentException("Element idea is equal to zero");
        }
        if (todo == null) {
            this.todo = new Todo("Header", "Keyword");
            this.todo.setTodo(new ArrayList<Todo.TodoItem>());
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        view = inflater.inflate(R.layout.dialog_todo, null);

        initToolBar();
        initEditHeader();
        initButtonAddItem();
        initListViewTodo();
        initEditKeyword();

        builder.setView(view)
                .setNegativeButton(R.string.button_cancel, null);

        if (operation == TODO_ADD) {
            builder.setPositiveButton(R.string.button_add, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onClickTodoAdd();
                }
            });
        } else if (operation == TODO_REVISE) {
            builder.setPositiveButton(R.string.button_revise, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onClickTodoRevise();
                }
            });
        }

        return builder.create();
    }

    private void initButtonAddItem() {
        final EditText editTodo = (EditText) view.findViewById(R.id.editTodo);
        ImageButton button = (ImageButton) view.findViewById(R.id.button_add_todoitem);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = editTodo.getText().toString();
                if (!s.isEmpty()) {
                    editTodo.setText("");
                    adapter.add(todo.getTodoItem(false, s));
                }
            }
        });
    }

    private void onClickTodoRevise() {
        String handler = editHeader.getText().toString();
        if (handler.isEmpty()) {
            handler = getString(R.string.todo_add);
        }
        String keyword = editKeyword.getText().toString();
        if (keyword.isEmpty()) {
            keyword = getString(R.string.todo);
        }
        if (adapter.getCount() == 0) {
            isEmpty("");
        }
        List<Todo.TodoItem> todoItems = todo.getTodo();
        for (int i = 0; i < adapter.getCount(); i++) {
            Todo.TodoItem item = (Todo.TodoItem) adapter.getItem(i);
            if (!todoItems.contains(item)) {
                todoItems.add(item);
            }
        }
        todo.setTodo(todoItems);
        BusProvider.getInstance().post(new TodoChangeEvent(AbstractData.ELEMENT_REVISE, this.todo));
    }

    private void onClickTodoAdd() {
        String handler = editHeader.getText().toString();
        if (handler.isEmpty()) {
            handler = getString(R.string.todo_add); 
        }
        String keyword = editKeyword.getText().toString();
        if (keyword.isEmpty()) {
            keyword = getString(R.string.todo);
        }
        this.todo = new Todo(handler, keyword);
        if (adapter.getCount() == 0) {
            isEmpty("");
        }
        List<Todo.TodoItem> todoItems = new ArrayList<>();
        for (int i = 0; i < adapter.getCount(); i++) {
            todoItems.add((Todo.TodoItem) adapter.getItem(i));
        }
        todo.setTodo(todoItems);
        BusProvider.getInstance().post(new TodoChangeEvent(AbstractData.ELEMENT_ADD, this.todo));
    }

    private boolean isEmpty(String s) {
        if (s.isEmpty()) {
            Toast.makeText(getActivity(), getString(R.string.exception_not_fill_fields), Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    private void initEditHeader() {
        editHeader = (EditText) view.findViewById(R.id.editHeader);
        if (operation == TODO_REVISE) {
            editHeader.setText(todo.getHeader());
        }
    }

    private void initListViewTodo() {
        ListView listView = (ListView) view.findViewById(R.id.listview_todo_items);
        List<Todo.TodoItem> todoItems = new ArrayList<>(todo.getTodo());
        if (operation == TODO_REVISE) {
            adapter = new TodoItemAdapter((FragmentActivity) getActivity(), todo);
        } else {
            adapter = new TodoItemAdapter((FragmentActivity) getActivity(), todoItems);
        }
        listView.setAdapter(adapter);
    }

    private void initEditKeyword() {
        editKeyword = (EditText) view.findViewById(R.id.editKeyword);
        if (operation == TODO_REVISE) {
            editKeyword.setText(todo.getKeyword());
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initToolBar() {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar_todo);
        toolbar.setNavigationIcon(R.drawable.ic_todo);
        if (operation == TODO_ADD) {
            toolbar.setTitle(getString(R.string.todo_add));
        } else if (operation == TODO_REVISE) {
            toolbar.setTitle(getString(R.string.todo_revise));
        }
    }
}