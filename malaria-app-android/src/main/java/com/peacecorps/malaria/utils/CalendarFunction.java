package com.peacecorps.malaria.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarFunction {

    private static final int[] daysOfMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30,
            31, 30, 31 };
    private static final int[] daysOfMonthLeap = { 31, 29, 31, 30, 31, 30, 31, 31, 30,
            31, 30, 31 };

    /**Method to give no. of days in month. */
    public static int getNumberOfDaysInMonth(int month, int year)
    {
        if(isLeapYear(year))
        {
            return daysOfMonthLeap[month];
        }
        else
            return daysOfMonth[month];
    }

    /**Check whether it is a leap layer**/
    public static boolean isLeapYear(int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        return cal.getActualMaximum(Calendar.DAY_OF_YEAR) > 365;
    }

    /*Setting the Date Object to Human Readable Format**/
    public static String getHumanDateFormat(String ats,int aMonth)
    {
        String aYear=ats.substring(0, 4);
        String aDate=ats.substring(Math.max(ats.length() - 2, 0));
        ats=aYear+"-"+aMonth+"-"+aDate;
        return ats;
    }

    /*Getting the Date Object from the String**/
    public static Date getDateObject(String s)
    {
        Date dobj=null;

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        try {
            dobj= sdf.parse(s);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        return dobj;
    }

    /*Getting the Day of Week from the String**/
    public static int getDayofWeek(Date d)
    {
        Calendar cal=Calendar.getInstance();
        cal.setTime(d);
        int day=cal.get(Calendar.DAY_OF_WEEK);
        return day;
    }
    /*Getting no. of Days between two interval**/
    public static long getNumberOfDays(Date d1,Date d2) {
        long interval = 0;
        Calendar c= Calendar.getInstance();
        c.setTime(d1);
        long ld1 = c.getTimeInMillis();
        c.setTime(d2);
        long ld2=c.getTimeInMillis();
        long oneDay = 1000 * 60 * 60 * 24;
        interval = (ld2-ld1) / oneDay;
        return interval;
    }

    /**Finding the No. of weekly days between two dates for calculating Adherence**/
    public static int getIntervalWeekly(Date s, Date e, int weekday)
    {
        Calendar startCal;
        Calendar endCal;
        startCal = Calendar.getInstance();
        startCal.setTime(s);
        endCal = Calendar.getInstance();
        endCal.setTime(e);
        int medDays = 0;
        //If working dates are same,then checking what is the day on that date.
        if ((startCal.getTimeInMillis() == endCal.getTimeInMillis()) && (startCal.get(Calendar.DAY_OF_WEEK) == weekday)) {
                ++medDays;
                return medDays;
        }
        /*If start date is coming after end date, Then shuffling Dates and storing dates
        by incrementing upto end date in do-while part.*/
        if (startCal.getTimeInMillis() > endCal.getTimeInMillis()) {
            startCal.setTime(e);
            endCal.setTime(s);
        }

        do {

            if (startCal.get(Calendar.DAY_OF_WEEK)==weekday) {
                ++medDays;
            }
            startCal.add(Calendar.DAY_OF_MONTH, 1);
        } while (startCal.getTimeInMillis() <= endCal.getTimeInMillis());

        if(startCal.get(Calendar.DAY_OF_WEEK)==endCal.get(Calendar.DAY_OF_WEEK) && (startCal.get(Calendar.DAY_OF_WEEK)==weekday))
            ++medDays;

        return medDays;
    }

    /**Finding the No. of days between two dates for calculating adherence of daily drugs**/
    public static int getIntervalDaily(Date s, Date e)
    {
        long sLong=s.getTime();
        long eLong=e.getTime();

        long oneDay=24*60*60*1000;

        long interval=(eLong-sLong)/oneDay;

        int interv=(int)interval+1;

        return interv;

    }


}
