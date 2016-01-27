package com.myproject.remindme.eventbus;

import com.myproject.remindme.database.data.Idea;

/**
 * Created by Alexey on 1/21/2016.
 */
public class IdeasChangeEvent {

    /**
     * Элементы для данного поля находятся в AbstractDAta
     */
    private int operation;

    /**
     * Элемент
     */
    private Idea idea;

    private String search;

    public IdeasChangeEvent(int operation, String search) {
        this.operation = operation;
        this.search = search;
        this.idea = null;
    }

    public IdeasChangeEvent(int operation, Idea idea) {
        this.operation = operation;
        this.idea = idea;
    }

    public int getOperation() {
        return operation;
    }

    public Idea getIdea() {
        return idea;
    }

    public String getSearch() {
        return search;
    }
}
