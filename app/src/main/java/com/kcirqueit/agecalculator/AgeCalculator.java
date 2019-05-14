package com.kcirqueit.agecalculator;

import com.kcirqueit.agecalculator.model.Age;
import com.kcirqueit.agecalculator.model.AgeDetails;
import com.kcirqueit.agecalculator.model.RemainAge;
import com.kcirqueit.agecalculator.model.UpcomingBirthDay;

import org.joda.time.LocalDate;
import org.joda.time.Months;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class AgeCalculator {

    private static int DAYS_YEAR = 365;

    /*public void showAge() {
        LocalDate l = LocalDate.of(1998, 04, 23); //specify year, month, date directly
        LocalDate now = LocalDate.now(); //gets localDate
        Period diff = Period.between(l, now); //difference between the dates is calculated
        System.out.println(diff.getYears() + "years" + diff.getMonths() + "months" + diff.getDays() + "days");
    }*/

    public static final String FORMAT = "dd/MM/yyyy";

    public static int calculateAgeWithJava7(
            Date birthDate,
            Date currentDate) {
        // validate inputs ...
        DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        int d1 = Integer.parseInt(formatter.format(birthDate));
        int d2 = Integer.parseInt(formatter.format(currentDate));
        int age = (d2 - d1) / 10000;
        return age;
    }

    public static void calculateAge(Date birthDate, Date currentDate) {
        int years = 0;
        int months = 0;
        int days = 0;

        //create calendar object for birth day
        Calendar birthDay = Calendar.getInstance();
        birthDay.setTimeInMillis(birthDate.getTime());

        //create calendar object for current day
        //long currentTime = System.currentTimeMillis();
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(currentDate.getTime());

        //Get difference between years
        years = now.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);
        int currMonth = now.get(Calendar.MONTH) + 1;
        int birthMonth = birthDay.get(Calendar.MONTH) + 1;

        //Get difference between months
        months = currMonth - birthMonth;

        //if month difference is in negative then reduce years by one
        //and calculate the number of months.
        if (months < 0) {
            years--;
            months = 12 - birthMonth + currMonth;
            if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE))
                months--;
        } else if (months == 0 && now.get(Calendar.DATE) < birthDay.get(Calendar.DATE)) {
            years--;
            months = 11;
        }

        //Calculate the days
        if (now.get(Calendar.DATE) > birthDay.get(Calendar.DATE))
            days = now.get(Calendar.DATE) - birthDay.get(Calendar.DATE);
        else if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE)) {
            int today = now.get(Calendar.DAY_OF_MONTH);
            now.add(Calendar.MONTH, -1);
            days = now.getActualMaximum(Calendar.DAY_OF_MONTH) - birthDay.get(Calendar.DAY_OF_MONTH) + today;
        } else {
            days = 0;
            if (months == 12) {
                years++;
                months = 0;
            }
        }
        //Create new Age object
        System.out.println(days + ", " + months + "," + years);
    }


    public static Age findAge(String currentDate, String birthDate) {

        int current_date = getDate(currentDate);
        int current_month = getMonth(currentDate) + 1;
        int current_year = getYear(currentDate);

        int birth_date = getDate(birthDate);
        int birth_month = getMonth(birthDate) + 1;
        int birth_year = getYear(birthDate);

        int month[] = {31, 28, 31, 30, 31, 30, 31,
                31, 30, 31, 30, 31};

        // if birth date is greater then current
        // birth_month, then do not count this month
        // and add 30 to the date so as to subtract
        // the date and get the remaining days
        if (birth_date > current_date) {
            current_month = current_month - 1;
            current_date = current_date + month[birth_month - 1];
        }

        // if birth month exceeds current month,
        // then do not count this year and add
        // 12 to the month so that we can subtract
        // and find out the difference
        if (birth_month > current_month) {
            current_year = current_year - 1;
            current_month = current_month + 12;
        }

        // calculate date, month, year
        int calculated_date = current_date - birth_date;
        int calculated_month = current_month - birth_month;
        int calculated_year = current_year - birth_year;

        // print the present age
       /* System.out.println("Present Age");
        System.out.println("Years: " + calculated_year +
                " Months: " + calculated_month + " Days: " +
                calculated_date);*/

        Age age = new Age(calculated_date, calculated_month, calculated_year);
        return age;
    }

    public static int getDate(String dateString) {

        DateFormat dateFormat = new SimpleDateFormat(FORMAT);
        Calendar calendar = Calendar.getInstance();

        try {
            Date date = dateFormat.parse(dateString);
            calendar.setTimeInMillis(date.getTime());

            return calendar.get(Calendar.DATE);

        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }

    }

    public static int getMonth(String dateString) {

        DateFormat dateFormat = new SimpleDateFormat(FORMAT);
        Calendar calendar = Calendar.getInstance();

        try {
            Date date = dateFormat.parse(dateString);
            calendar.setTimeInMillis(date.getTime());

            return calendar.get(Calendar.MONTH);

        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }

    }

    public static int getYear(String dateString) {

        DateFormat dateFormat = new SimpleDateFormat(FORMAT);
        Calendar calendar = Calendar.getInstance();
        try {
            Date date = dateFormat.parse(dateString);
            calendar.setTimeInMillis(date.getTime());

            return calendar.get(Calendar.YEAR);

        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static RemainAge getNextbrithDay(int curDay, int curMonth, int current_year, int birthday, int birthMonth, int birth_year) {

        curMonth = curMonth -1;
        birthMonth = birthMonth - 1;

        int remainDay = 0;

        // Using LocalDate object.
        LocalDate date1 = new LocalDate(current_year, curMonth, curDay);

        LocalDate date2 = new LocalDate(current_year, birthMonth, birthday);
        int months = Months.monthsBetween(date1, date2).getMonths();


        int month[] = {31, 28, 31, 30, 31, 30, 31,
                31, 30, 31, 30, 31};


        if (curMonth > birthMonth) {

            date2 = new LocalDate(current_year + 1, birthMonth, birthday);
            months = Months.monthsBetween(date1, date2).getMonths();

            //System.out.println("month 2:"+ months);
            int remainDayofCurMonth = month[curMonth - 1] - curDay;
            if (month[birthMonth] != birthday) {
                remainDay = remainDayofCurMonth + birthday;
                if (remainDay >= month[birthMonth]) {
                    remainDay = remainDay - month[birthMonth];
                }
            } else {
                remainDay = remainDayofCurMonth;
            }

            return new RemainAge(remainDay, months);

        }

        if (curMonth >= birthMonth && curMonth == birthMonth) {
            date2 = new LocalDate(current_year + 1, birthMonth, birthday);
            months = Months.monthsBetween(date1, date2).getMonths();

            //System.out.println("month 2:"+ months);
            int remainDayofCurMonth = month[curMonth - 1] - curDay;
            if (month[birthMonth] != birthday) {
                remainDay = remainDayofCurMonth + birthday;
                if (remainDay >= month[birthMonth]) {
                    remainDay = remainDay - month[birthMonth];
                }
            } else {
                remainDay = remainDayofCurMonth;
            }

            return new RemainAge(remainDay, months);
        }

        if (curDay <= birthday && curMonth <= birthMonth) {

            int remainDayofCurMonth = month[curMonth - 1] - curDay;
            if (month[birthMonth] != birthday) {
                remainDay = remainDayofCurMonth + birthday;
                if (remainDay >= month[birthMonth]) {
                    remainDay = remainDay - month[birthMonth];
                }
            } else {
                remainDay = remainDayofCurMonth;
            }
            return new RemainAge(remainDay, months);

        }
        int remainDayofCurMonth = month[curMonth - 1] - curDay;
        if (month[birthMonth] != birthday) {
            remainDay = remainDayofCurMonth + birthday;
            if (remainDay >= month[birthMonth]) {
                remainDay = remainDay - month[birthMonth];
            }
        } else {
            remainDay = remainDayofCurMonth;
        }
        return new RemainAge(remainDay, months);
    }

    public static AgeDetails getDetailsInfo(String curDate, String birthDate) {

        DateFormat dateFormat = new SimpleDateFormat(FORMAT);

        try {
            Date cDate = dateFormat.parse(curDate);
            Date bDate = dateFormat.parse(birthDate);

            Calendar calendar1 = Calendar.getInstance();
            Calendar calendar2 = Calendar.getInstance();
            calendar1.setTimeInMillis(cDate.getTime());
            calendar2.setTimeInMillis(bDate.getTime());


            long time = calendar1.getTimeInMillis() - calendar2.getTimeInMillis();

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(time);

            System.out.println(time);

            long seconds = time / 1000;

            long minute = seconds / 60;
            long hour = minute / 60;
            long day = hour / 24;
            long week = day / 7;
            long month = (long) (week / 4.345);
            long year = month / 12;

            AgeDetails ageDetails = new AgeDetails(seconds, minute, hour, day, week, month, year);

            return ageDetails;


        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }


    }


    public static String getCurrentFormatedGet() {
        DateFormat dateFormat = new SimpleDateFormat(FORMAT);
        String date = dateFormat.format(new Date());
        return date;
    }

    public static boolean isValidDate(String d) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(FORMAT);
            date = sdf.parse(d);
            if (!d.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        if (date == null) {
            return false;
        } else {
            return true;
        }
    }

    public static List<UpcomingBirthDay> getUpcomingBirthDay(String birthDay, String curDate) {

        List<UpcomingBirthDay> upcomingBirthDays = new ArrayList<>();

        int date = getDate(birthDay);
        int month = getMonth(birthDay) + 1;
        int year = getYear(birthDay);

        int cDate = getDate(curDate);
        int cMonth = getMonth(curDate) + 1;
        int cyear = getYear(curDate);


        if (cMonth > month) {
            ++cyear;
        }

        for (int i = 0; i < 5; i++) {

            String date2 = date + "/" + month + "/" + cyear;

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT);

            try {
                Date date1 = simpleDateFormat.parse(date2);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeZone(TimeZone.getDefault());
                calendar.setTime(date1);
                String dayName = getDayName(calendar.get(Calendar.DAY_OF_WEEK));

                SimpleDateFormat s = new SimpleDateFormat("dd MMM yyyy");
                String d = s.format(date1);
                System.out.println(d + ":" + dayName);
                UpcomingBirthDay upcomingBirthDay = new UpcomingBirthDay(d, dayName);
                upcomingBirthDays.add(upcomingBirthDay);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            cyear++;

        }


        return upcomingBirthDays;


    }

    public static String getDayName(int day) {
        switch (day) {
            case 1:
                return "Sunday";
            case 2:
                return "Monday";
            case 3:
                return "Tuesday";
            case 4:
                return "Wednesday";
            case 5:
                return "Thursday";
            case 6:
                return "Friday";
            case 7:
                return "Saturday";
        }
        return null;
    }

    public static long dateStringtoMilli(String date) {

        SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date1 = s.parse(date);
            return date1.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }

    }

    public static Date getDateToString(String dateString) {

        DateFormat format = new SimpleDateFormat(FORMAT);
        try {
            Date date = format.parse(dateString);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }


}
