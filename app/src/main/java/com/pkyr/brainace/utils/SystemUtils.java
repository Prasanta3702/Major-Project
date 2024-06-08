package com.pkyr.brainace.utils;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class SystemUtils {

    public static String getDate() {
        Calendar calendar = new GregorianCalendar();
        String[] months = new String[] {
                "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"
        };

        String day = calendar.get(Calendar.DAY_OF_MONTH)+"";
        String month = months[calendar.get(Calendar.MONTH)];
        String year = calendar.get(Calendar.YEAR)+"";

        return day+" "+month+" "+year;
    }
}
