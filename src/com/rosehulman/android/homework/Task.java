package com.rosehulman.android.homework;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Task implements Comparable<Task> {

    private int mId;
    private String mName;
    private GregorianCalendar mDateDue;
    
    public int getId() { return mId; }
    public void setId(int id) { mId = id; }
    
    public String getName() { return mName; }
    public void setName(String name) { mName = name; }
    
    public GregorianCalendar getDateDue() { return mDateDue; }
    public int getYearDue() { return getDateDue().get(Calendar.YEAR); }
    public int getMonthDue() { return getDateDue().get(Calendar.MONTH); }
    public int getDayOfMonthDue() { return getDateDue().get(Calendar.DAY_OF_MONTH); }
    
    public void setDateDue(GregorianCalendar dateDue) { mDateDue = dateDue; }
    public void setDateDue(int year, int month, int dayOfMonth) {
        mDateDue = new GregorianCalendar();
        
        /* "Truncate" the calendar to the day */
        mDateDue.set(Calendar.HOUR, 0);
        mDateDue.set(Calendar.MINUTE, 0);
        mDateDue.set(Calendar.SECOND, 0);
        mDateDue.set(Calendar.MILLISECOND, 0);
        
        mDateDue.set(Calendar.YEAR, year);
        mDateDue.set(Calendar.MONTH, month + 1); // Calendar uses zero-indexed constants for months
        mDateDue.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    }
    
    @Override
    public String toString() { return getName(); }
    
    public int compareTo(Task another) {
        GregorianCalendar otherCalendar = another.getDateDue();
        if (otherCalendar.compareTo(getDateDue()) == 0) return getName().compareTo(another.getName());
        return -otherCalendar.compareTo(getDateDue());
    }
}
