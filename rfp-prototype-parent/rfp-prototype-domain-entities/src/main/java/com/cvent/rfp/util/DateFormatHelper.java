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
    private static final String dateFormat = "yyyy-MM-dd HH:mm:ss.s";
    private static final String dateOnlyFormat = "yyyy-MM-dd";
    private static final String timeFormat = "HH:mm:ss";
    
    private static final String datePrefixForTime = "1900-01-01";
    private static final String timePostfixForDate = "00:00:00.0";
    
    public static String getDateFormat()
    {
        return dateFormat;
    }
    
    public static String getDateOnlyFormat()
    {
        return dateOnlyFormat;
    }
    
    public static String getTimeFormat()
    {
        return timeFormat;
    }
    
    public static String getDatePrefixForTime()
    {
        return datePrefixForTime;
    }
    
    public static String getTimePostfixForDate()
    {
        return timePostfixForDate;
    }
    
    public static String dateFormat(String dateString) throws ParseException
    {
        try
        {
            Date date = parseDate(dateString);
            return new SimpleDateFormat(dateFormat).format(date);
        } catch (ParseException ex)
        {
            throw ex;
        }
    }
    
    public static String dateOnlyFormat(String dateString) throws ParseException
    {
        try
        {
            Date date = parseDate(dateString);
            return new SimpleDateFormat(dateOnlyFormat).format(date);
        } catch (ParseException ex)
        {
            throw ex;
        } 
    }
    
    public static String timeFormat(String dateString) throws ParseException
    {
        try
        {
            Date date = parseDate(dateString);
            return new SimpleDateFormat(timeFormat).format(date);
        } catch (ParseException ex)
        {
            throw ex;
        }
    }
    
    public static Date parseDate(String dateString) throws ParseException
    {
        return new SimpleDateFormat(dateFormat).parse(dateString);
    }
    
}
