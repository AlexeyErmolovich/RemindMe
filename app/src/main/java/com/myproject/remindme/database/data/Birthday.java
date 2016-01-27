package com.myproject.remindme.database.data;

import java.text.DateFormat;
import java.util.GregorianCalendar;

/**
 * Created by Алексей on 15/01/2016.
 */
public class Birthday extends AbstractData {

    private String firstName;
    private String lastName;
    private GregorianCalendar birthday;
    private String relationship;

    public Birthday(String firstName, String lastName, GregorianCalendar birthday, String relationship) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.relationship = relationship;
        this.setDateAdded(new GregorianCalendar());
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public GregorianCalendar getBirthday() {
        return birthday;
    }

    public String getBirthdayString() {
        return DateFormat.getDateInstance().format(birthday.getTime());
    }

    public String getRelationship() {
        return relationship;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setBirthday(GregorianCalendar birthday) {
        this.birthday = birthday;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

}
