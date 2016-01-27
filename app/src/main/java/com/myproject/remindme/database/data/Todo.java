package com.myproject.remindme.database.data;

import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Alexey on 1/22/2016.
 */
public class Todo extends AbstractData {

    private String header;
    private List<Todo.TodoItem> todo;
    private String keyword;

    public Todo(String header, List<Todo.TodoItem> todo, String keyword) {
        this.header = header;
        this.todo = todo;
        this.keyword = keyword;
        this.setDateAdded(new GregorianCalendar());
    }

    public Todo(String header, String keyword) {
        this.header = header;
        this.keyword = keyword;
        this.setDateAdded(new GregorianCalendar());
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public List<Todo.TodoItem> getTodo() {
        return todo;
    }

    public void setTodo(List<Todo.TodoItem> todo) {
        this.todo = todo;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public TodoItem getTodoItem(boolean checked, String todoItem) {
        return new TodoItem(checked, todoItem);
    }

    public class TodoItem {

        private long id;
        private boolean checked;
        private String todoItem;

        public TodoItem(boolean cheked, String todoItem) {
            this.checked = cheked;
            this.todoItem = todoItem;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }

        public String getTodoItem() {
            return todoItem;
        }

        public void setTodoItem(String todoItem) {
            this.todoItem = todoItem;
        }
    }
}

