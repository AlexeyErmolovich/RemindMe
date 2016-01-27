package com.myproject.remindme.database.data;

import java.util.GregorianCalendar;

/**
 * Created by Alexey on 1/21/2016.
 */
public class Idea extends AbstractData {

    private String header;
    private String idea;
    private String keyword;

    public Idea(String header, String idea, String keyword) {
        this.header = header;
        this.idea = idea;
        this.keyword = keyword;
        this.setDateAdded(new GregorianCalendar());
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getIdea() {
        return idea;
    }

    public void setIdea(String idea) {
        this.idea = idea;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
