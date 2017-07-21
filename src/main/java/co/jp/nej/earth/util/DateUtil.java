package co.jp.nej.earth.util;

import co.jp.nej.earth.model.constant.Constant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static co.jp.nej.earth.model.constant.Constant.DatePattern;

public class DateUtil {

    public static final int COUNTITEM = 7;
    public static final int FRIST = 0;
    private static final SimpleDateFormat earthDF = new SimpleDateFormat(
        DatePattern.DATE_FORMAT_YYYYMMDDHHMMSS999);

    private static final SimpleDateFormat earthShortDF = new SimpleDateFormat(DatePattern.YYYYMMDD);
    public static final int TWO = 1;
    public static final int THREE = 2;
    public static final int FOUR = 3;
    public static final int FIVE = 4;
    public static final int SIX = 5;
    public static final int END_INDEX_MONTH = 6;
    public static final int SEVEN = 6;
    public static final int END_INDEX_YEAR = 4;
    public static final int END_INDEX_DAY = 8;
    public static final int END_INDEX_HOUR = 10;
    public static final int END_INDEX_MINUTE = 12;
    public static final int END_INDEX_SECOND = 14;
    public static final int END_INDEX = 17;

    /**
     * Dateを指定のフォーマットへ変換.
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String convertSimpleDateFormat(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    public static String getCurrentDate(String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(new Date());
    }

    /**
     * Get System Date.
     *
     * @return
     */
    // Get current date as string
    public static Date getCurrentDate() {
        return new Date();
    }

    /**
     * Get current date as string.
     *
     * @return
     */
    public static String getCurrentDateString() {

        return earthDF.format(getCurrentDate());
    }

    public static String getCurrentShortDateString() {

        return earthShortDF.format(getCurrentDate());
    }


    /**
     * @param stringDate
     * @param pattern
     * @return
     * @throws Exception
     */
    public static Date convertStringSimpleDateFormat(String stringDate, String pattern) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.parse(stringDate);
    }

    public static boolean isDate(String dateTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(Constant.DatePattern.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
        boolean flag = true;
        try {
            dateFormat.parse(dateTime.trim());
        } catch (ParseException pe) {
            flag = false;
        }
        return flag;
    }

    public static String convertStringToDateFormat(String stringDate) throws Exception {
        String[] inputObj = new String[COUNTITEM];
        inputObj[FRIST] = stringDate.substring(0, END_INDEX_YEAR);
        inputObj[TWO] = stringDate.substring(END_INDEX_YEAR, END_INDEX_MONTH);
        inputObj[THREE] = stringDate.substring(END_INDEX_MONTH, END_INDEX_DAY);
        inputObj[FOUR] = stringDate.substring(END_INDEX_DAY, END_INDEX_HOUR);
        inputObj[FIVE] = stringDate.substring(END_INDEX_HOUR, END_INDEX_MINUTE);
        inputObj[SIX] = stringDate.substring(END_INDEX_MINUTE, END_INDEX_SECOND);
        inputObj[SEVEN] = stringDate.substring(END_INDEX_SECOND, END_INDEX);

        String dateString = String.format("%-4s/%-2s/%-2s %-2s:%-2s:%-2s.%-3s", inputObj);
        return dateString;
    }
}
