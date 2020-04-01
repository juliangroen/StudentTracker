package com.jgroen.juliangroenstudenttracker.utils;

import androidx.room.TypeConverter;

import java.util.Date;

public class Converters {
    @TypeConverter
    public static Date timestampToDate (Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp (Date date) {
        return date == null ? null : date.getTime();
    }
}
