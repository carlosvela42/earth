package co.jp.nej.earth.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import static co.jp.nej.earth.model.constant.Constant.*;

public class DateUtil {

    private static final SimpleDateFormat earthDF = new SimpleDateFormat(
            DatePattern.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS_SSS);

    private static final SimpleDateFormat earthShortDF = new SimpleDateFormat(DatePattern.YYYYMMDD);

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
     * @param pattern
     * @return
     */
    // Get current date as string
    public static Date getCurrentDate(){
        return new Date();
    }

    /**
     * Get current date as string.
     *
     * @param pattern
     * @return
     */
    public static String getCurrentDateString() {

        return earthDF.format(getCurrentDate());
    }

    public static String getCurrentShortDateString() {

        return earthShortDF.format(getCurrentDate());
    }
}
