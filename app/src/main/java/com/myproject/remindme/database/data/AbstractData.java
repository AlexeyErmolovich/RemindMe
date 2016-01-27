package com.myproject.remindme.database.data;

import java.util.GregorianCalendar;

/**
 * Created by Алексей on 15/01/2016.
 */
public abstract class AbstractData implements Comparable<AbstractData> {

    public final static int ELEMENT_ADD = 100;
    public final static int ELEMENT_REVISE = 110;
    public final static int ELEMENT_DELETE = 120;
    public final static int ELEMENT_UPDATE = 130;
    public final static int ELEMENT_SEARCH = 140;

    private long id;
    private GregorianCalendar dateAdded;

    @Override
    public int compareTo(AbstractData another) {
        return this.dateAdded.compareTo(another.getDateAdded());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        if (id < 0)
            throw new IllegalArgumentException("id argument is null");
        else
            this.id = id;
    }

    public GregorianCalendar getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(GregorianCalendar dateAdded) {
        this.dateAdded = dateAdded;
    }
}
