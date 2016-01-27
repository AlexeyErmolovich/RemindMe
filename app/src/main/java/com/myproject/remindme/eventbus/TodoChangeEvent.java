package com.myproject.remindme.eventbus;

import com.myproject.remindme.database.data.Todo;

/**
 * Created by Alexey on 1/22/2016.
 */
public class TodoChangeEvent {
    /**
     * Элементы для данного поля находятся в AbstractDAta
     */
    private int operation;

    /**
     * Элемент
     */
    private Todo todo;

    private String search;

    public TodoChangeEvent(int operation, String search) {
        this.operation = operation;
        this.search = search;
        this.todo = null;
    }

    public TodoChangeEvent(int operation, Todo todo) {
        this.operation = operation;
        this.todo = todo;
    }

    public int getOperation() {
        return operation;
    }

    public Todo getTodo() {
        return todo;
    }

    public String getSearch() {
        return search;
    }
}
