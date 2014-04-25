/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cvent.rfp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author yxie
 */
public final class DateFormatHelper {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.s";
    private static final String DATE_ONLY_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm:ss";

    private static final String DATE_PREFIX_FOR_TIME = "1900-01-01";
    private static final String TIME_POSTFIX_FOR_DATE = "00:00:00.0";
    
    private DateFormatHelper() {
    }

    public static String getDateFormat() {
        return DATE_FORMAT;
    }

    public static String getDateOnlyFormat() {
        return DATE_ONLY_FORMAT;
    }

    public static String getTimeFormat() {
        return TIME_FORMAT;
    }

    public static String getDatePrefixForTime() {
        return DATE_PREFIX_FOR_TIME;
    }

    public static String getTimePostfixForDate() {
        return TIME_POSTFIX_FOR_DATE;
    }

    public static String dateFormat(String dateString) throws ParseException {
        try {
            Date date = parseDate(dateString);
            return new SimpleDateFormat(DATE_FORMAT).format(date);
        } catch (ParseException ex) {
            throw ex;
        }
    }

    public static String dateOnlyFormat(String dateString) throws ParseException {
        try {
            Date date = parseDate(dateString);
            return new SimpleDateFormat(DATE_ONLY_FORMAT).format(date);
        } catch (ParseException ex) {
            throw ex;
        }
    }

    public static String timeFormat(String dateString) throws ParseException {
        try {
            Date date = parseDate(dateString);
            return new SimpleDateFormat(TIME_FORMAT).format(date);
        } catch (ParseException ex) {
            throw ex;
        }
    }

    public static Date parseDate(String dateString) throws ParseException {
        return new SimpleDateFormat(DATE_FORMAT).parse(dateString);
    }

}
