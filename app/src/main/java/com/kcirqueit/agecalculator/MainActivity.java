package com.kcirqueit.agecalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import es.dmoral.toasty.Toasty;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.kcirqueit.agecalculator.model.Age;
import com.kcirqueit.agecalculator.model.AgeDetails;
import com.kcirqueit.agecalculator.model.RemainAge;
import com.kcirqueit.agecalculator.model.UpcomingBirthDay;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private Toolbar mToolbar;

    private EditText mCurrentDateEt, mBirtdDateEt;
    private Button mCalculateBt, mResetbt;
    private TextView mYearTv, mMonthTv, mDaysTv, mNextMonthTv, mNextDayTv;
    private TextView totalYearTv, totalMonthTv, totalWeekTv, totalDayTv, totalHourtv, totalMinuteTv, totalSecondTv;

    private TextView date1Tv, date2Tv, date3Tv, date4Tv, date5Tv, day1Tv, day2Tv, day3Tv, day4Tv, day5Tv;

    private ImageView mPickcurrDateIv, mPickBirthDateIv;

    private CardView mCardView;

    private int day;
    private int month;
    private int year;

    long currentTimeInMilli;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        MobileAds.initialize(this,
                getString(R.string.admob_app_id));

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Age Calculator");
        }

        String currentDate = AgeCalculator.getCurrentFormatedGet();
        mCurrentDateEt.setText(currentDate);
        currentTimeInMilli = AgeCalculator.dateStringtoMilli(currentDate);

        day = AgeCalculator.getDate(currentDate);
        month = AgeCalculator.getMonth(currentDate);
        year = AgeCalculator.getMonth(currentDate);

        mPickcurrDateIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();

                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int y, int m, int d) {

                        m++;

                        String monString = String.valueOf(m);
                        String dayString = String.valueOf(d);

                        if (d < 10) {
                            dayString = "0" + d;
                        }

                        if (m < 10) {
                            monString = "0" + m;
                        }

                        String dateString = dayString + "/" + monString + "/" + y;
                        mCurrentDateEt.setText(dateString);
                        currentTimeInMilli = AgeCalculator.dateStringtoMilli(dateString);
                        System.out.println(currentTimeInMilli);
                        day = d;
                        month = m;
                        year = y;

                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();
            }
        });

        mPickBirthDateIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                final DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int y, int m, int d) {

                        m++;

                        String dateString = d+"/"+m+"/"+y;
                        long birthTimeInMilli = AgeCalculator.dateStringtoMilli(dateString);


                        if (birthTimeInMilli >= currentTimeInMilli) {
                            mBirtdDateEt.setText("");
                            Toasty.error(MainActivity.this, "Birth date should not be greater than or equal current date.").show();
                        } else {

                            String monString = String.valueOf(m);
                            String dayString = String.valueOf(d);
                            if (d < 10) {
                                dayString = "0" + d;
                            }
                            if (m < 10) {
                                monString = "0" + m;
                            }

                            mBirtdDateEt.setText(dayString + "/" + monString + "/" + y);
                        }
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();
            }
        });

        mBirtdDateEt.addTextChangedListener(new TextWatcher() {

            private String current = "";
            private String ddmmyyyy = "DDMMYYYY";
            private Calendar cal = Calendar.getInstance();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
                    String cleanC = current.replaceAll("[^\\d.]|\\.", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC))
                        sel--;

                    if (clean.length() < 8) {
                        clean = clean + ddmmyyyy.substring(clean.length());
                    } else {
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day = Integer.parseInt(clean.substring(0, 2));
                        int mon = Integer.parseInt(clean.substring(2, 4));
                        int year = Integer.parseInt(clean.substring(4, 8));

                        mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                        cal.set(Calendar.MONTH, mon - 1);
                        year = (year < 1900) ? 1900 : (year > 2100) ? 2100 : year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE)) ? cal.getActualMaximum(Calendar.DATE) : day;
                        clean = String.format("%02d%02d%02d", day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    mBirtdDateEt.setText(current);
                    mBirtdDateEt.setSelection(sel < current.length() ? sel : current.length());
                    //mPurchaseDate = mDateConverter.getDateInUnix(current);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mCurrentDateEt.addTextChangedListener(new TextWatcher() {
            private String current = "";
            private String ddmmyyyy = "DDMMYYYY";
            private Calendar cal = Calendar.getInstance();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
                    String cleanC = current.replaceAll("[^\\d.]|\\.", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC))
                        sel--;

                    if (clean.length() < 8) {
                        clean = clean + ddmmyyyy.substring(clean.length());
                    } else {
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day = Integer.parseInt(clean.substring(0, 2));
                        int mon = Integer.parseInt(clean.substring(2, 4));
                        int year = Integer.parseInt(clean.substring(4, 8));

                        mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                        cal.set(Calendar.MONTH, mon - 1);
                        year = (year < 1900) ? 1900 : (year > 2100) ? 2100 : year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE)) ? cal.getActualMaximum(Calendar.DATE) : day;
                        clean = String.format("%02d%02d%02d", day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    mCurrentDateEt.setText(current);
                    mCurrentDateEt.setSelection(sel < current.length() ? sel : current.length());
                    //mPurchaseDate = mDateConverter.getDateInUnix(current);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        mCalculateBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String currentDate = mCurrentDateEt.getText().toString();
                String birthDate = mBirtdDateEt.getText().toString();

                if (currentDate.isEmpty()) {
                    mCurrentDateEt.setError("This field should not be empty!");
                    return;
                } else if (birthDate.isEmpty()) {
                    mBirtdDateEt.setError("This field should not be empty!");
                    return;
                } else if (AgeCalculator.isValidDate(currentDate) == false) {
                    mCurrentDateEt.setError("Date is not valid: dd/MM/yyyy");
                    return;
                } else if (AgeCalculator.isValidDate(birthDate) == false) {
                    mBirtdDateEt.setError("Date is not valid: dd/MM/yyyy");
                    return;
                } else if (AgeCalculator.getDateToString(currentDate).getTime() <= AgeCalculator.getDateToString(birthDate).getTime() ) {
                    mBirtdDateEt.setError("Birth date should not be greater than or equal current date.");
                    return;
                }

                displayResult(currentDate, birthDate);
                mCardView.setVisibility(View.VISIBLE);

            }
        });

        mResetbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBirtdDateEt.setText("");
                mCardView.setVisibility(View.GONE);
                reset();
            }
        });

    }

    private void displayResult(String currentDate, String birthDate) {

        Age age = AgeCalculator.findAge(currentDate, birthDate);
        setAgeData(age);
        setNextBirthDayInfo(currentDate, birthDate);

        AgeDetails ageDetails = AgeCalculator.getDetailsInfo(currentDate, birthDate);
        setDetailsInfo(ageDetails);

        List<UpcomingBirthDay> upcomingBirthDays = AgeCalculator.getUpcomingBirthDay(birthDate, currentDate);
        if (upcomingBirthDays != null) {
            setUpcomingBirthDay(upcomingBirthDays);
        }

        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }


    }

    private void setUpcomingBirthDay(List<UpcomingBirthDay> upcomingBirthDays) {

        date1Tv.setText(upcomingBirthDays.get(0).getDate());
        date2Tv.setText(upcomingBirthDays.get(1).getDate());
        date3Tv.setText(upcomingBirthDays.get(2).getDate());
        date4Tv.setText(upcomingBirthDays.get(3).getDate());
        date5Tv.setText(upcomingBirthDays.get(4).getDate());

        day1Tv.setText(upcomingBirthDays.get(0).getDay());
        day2Tv.setText(upcomingBirthDays.get(1).getDay());
        day3Tv.setText(upcomingBirthDays.get(2).getDay());
        day4Tv.setText(upcomingBirthDays.get(3).getDay());
        day5Tv.setText(upcomingBirthDays.get(4).getDay());

    }

    private void setDetailsInfo(AgeDetails ageDetails) {


        totalYearTv.setText(ageDetails.getYear() + "");
        totalMonthTv.setText(ageDetails.getMonth() + "");
        totalWeekTv.setText(ageDetails.getWeek() + "");
        totalDayTv.setText(ageDetails.getDay() + "");
        totalHourtv.setText(ageDetails.getHour() + "");
        totalMinuteTv.setText(ageDetails.getMinute() + "");
        totalSecondTv.setText(ageDetails.getSecond() + "");

    }

    private void setNextBirthDayInfo(String currentDate, String birthDate) {

        int curDate = AgeCalculator.getDate(currentDate);
        int curMonth = AgeCalculator.getMonth(currentDate);
        int curyear = AgeCalculator.getYear(currentDate);

        int bDate = AgeCalculator.getDate(birthDate);
        int bMonth = AgeCalculator.getMonth(birthDate);
        int bYear = AgeCalculator.getYear(birthDate);


        RemainAge remainAge = AgeCalculator.getNextbrithDay(curDate, curMonth + 1, curyear,
                bDate, bMonth + 1, bYear);

        mNextDayTv.setText(remainAge.getDay() + "");
        mNextMonthTv.setText(remainAge.getMonth() + "");


    }

    private void initView() {

        mToolbar = findViewById(R.id.main_toolbar);
        mCurrentDateEt = findViewById(R.id.current_date_et);
        mBirtdDateEt = findViewById(R.id.birth_date_et);
        mCalculateBt = findViewById(R.id.calculate_bt);
        mResetbt = findViewById(R.id.reset_bt);
        mYearTv = findViewById(R.id.year_tv);
        mMonthTv = findViewById(R.id.month_tv);
        mDaysTv = findViewById(R.id.day_tv);
        mNextDayTv = findViewById(R.id.n_day_tv);
        mNextMonthTv = findViewById(R.id.n_month_tv);
        mPickcurrDateIv = findViewById(R.id.pick_current_date_iv);
        mPickBirthDateIv = findViewById(R.id.pick_birth_date_iv);

        totalYearTv = findViewById(R.id.total_year_tv);
        totalMonthTv = findViewById(R.id.total_month_tv);
        totalWeekTv = findViewById(R.id.total_week_tv);
        totalDayTv = findViewById(R.id.total_day_tv);
        totalHourtv = findViewById(R.id.total_hour_tv);
        totalMinuteTv = findViewById(R.id.total_minute_tv);
        totalSecondTv = findViewById(R.id.total_second_tv);

        date1Tv = findViewById(R.id.date1_tv);
        date2Tv = findViewById(R.id.date2_tv);
        date3Tv = findViewById(R.id.date3_tv);
        date4Tv = findViewById(R.id.date4_tv);
        date5Tv = findViewById(R.id.date5_tv);

        day1Tv = findViewById(R.id.day1_tv);
        day2Tv = findViewById(R.id.day2_tv);
        day3Tv = findViewById(R.id.day3_tv);
        day4Tv = findViewById(R.id.day4_tv);
        day5Tv = findViewById(R.id.day5_tv);

        mCardView = findViewById(R.id.upcomming_card);
    }

    public void setAgeData(Age age) {
        mDaysTv.setText(age.getDays() + "");
        mMonthTv.setText(age.getMonth() + "");
        mYearTv.setText(age.getYear() + "");
    }

    public void reset() {

        mDaysTv.setText("0");
        mMonthTv.setText("0");
        mYearTv.setText("0");
        mNextMonthTv.setText("0");
        mNextDayTv.setText("0");

        totalYearTv.setText("0");
        totalMonthTv.setText("0");
        totalWeekTv.setText("0");
        totalDayTv.setText("0");
        totalHourtv.setText("0");
        totalMinuteTv.setText("0");
        totalSecondTv.setText("0");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.privacy_menu:
                startActivity(new Intent(this, PrivacyActivity.class));
                break;
            case R.id.share_menu:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");

                String subject = "Newspapers App";
                String body = "Top 10 newspaper of any country in the world.";

                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_TEXT, body);

                startActivity(Intent.createChooser(intent, "Share with"));
                break;
            case R.id.more_menu:
                Intent intent2 = new Intent(Intent.ACTION_VIEW);
                intent2.setData(Uri.parse("https://play.google.com/store/apps/developer?id=K+Cirque+Apps"));
                startActivity(intent2);
                break;
            case R.id.rate_menu:
                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.kcirqueit.agecalculator");
                Intent rateIntent = new Intent(Intent.ACTION_VIEW, uri);

                startActivity(rateIntent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
