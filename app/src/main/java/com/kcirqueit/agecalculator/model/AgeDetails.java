package com.kcirqueit.agecalculator.model;

public class AgeDetails {

    private long second, minute, hour, day, week, month, year;

    public AgeDetails(long second, long minute, long hour, long day, long week, long month, long year) {
        this.second = second;
        this.minute = minute;
        this.hour = hour;
        this.day = day;
        this.week = week;
        this.month = month;
        this.year = year;
    }


    public long getSecond() {
        return second;
    }

    public long getMinute() {
        return minute;
    }

    public long getHour() {
        return hour;
    }

    public long getDay() {
        return day;
    }

    public long getWeek() {
        return week;
    }

    public long getMonth() {
        return month;
    }

    public long getYear() {
        return year;
    }
}
