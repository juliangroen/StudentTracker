package com.jgroen.juliangroenstudenttracker.utils;

import android.util.Log;

import androidx.lifecycle.ViewModelProvider;

import com.jgroen.juliangroenstudenttracker.features.course.CourseViewModel;
import com.jgroen.juliangroenstudenttracker.features.term.TermEntity;
import com.jgroen.juliangroenstudenttracker.features.term.TermViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public interface TrackerUtilities {

    public static Calendar longToCalendar(Long longForm) {
        Calendar cal = Calendar.getInstance();
        Date date = new Date();
        date.setTime(longForm);
        cal.setTime(date);
        return cal;
    }

    public static String longToDateString(Long longForm) {
        Calendar cal = longToCalendar(longForm);
        return new SimpleDateFormat("MMM dd yyyy").format(cal.getTime());
    }

    public static int[] longToDateArray(Long longForm) {
        Calendar cal = longToCalendar(longForm);
        int[] dateArray = new int[3];
        dateArray[0] = cal.get(Calendar.YEAR);
        dateArray[1] = cal.get(Calendar.MONTH);
        dateArray[2] = cal.get(Calendar.DAY_OF_MONTH);
        return dateArray;
    }

}
