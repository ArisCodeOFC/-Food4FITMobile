package br.com.food4fit.converter;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

public class DateConverter {
    public static @TypeConverter Date toDate(Long dateLong){
        return dateLong == null ? null: new Date(dateLong);
    }

    public static @TypeConverter Long fromDate(Date date){
        return date == null ? null : date.getTime();
    }
}
