package com.kcirqueit.agecalculator;

import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        AgeCalculator.getUpcomingBirthDay( "20/10/1994", "30/04/2019");






        assertEquals(4, 2 + 2);
    }
}