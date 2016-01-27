package com.myproject.remindme.eventbus;

import com.myproject.remindme.database.data.Birthday;

/**
 * Created by Алексей on 16/01/2016.
 */
public final class BirthdaysChangeEvent {

    /**
     * Элементы для данного поля находятся в AbstractDAta
     */
    private int operation;

    /**
     * Элемент
     */
    private Birthday birthday;

    private String search;

    public BirthdaysChangeEvent(int operation, String search) {
        this.operation = operation;
        this.search = search;
        this.birthday = null;
    }

    public BirthdaysChangeEvent(int operation, Birthday birthday) {
        this.operation = operation;
        this.birthday = birthday;
    }

    public int getOperation() {
        return operation;
    }

    public Birthday getBirthday() {
        return birthday;
    }

    public String getSearch() {
        return search;
    }
}
