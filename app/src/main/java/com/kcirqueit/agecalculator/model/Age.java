package com.kcirqueit.agecalculator.model;

public class Age {

    private int days;
    private int month;
    private int year;

    public Age(int days, int month, int year) {
        this.days = days;
        this.month = month;
        this.year = year;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
