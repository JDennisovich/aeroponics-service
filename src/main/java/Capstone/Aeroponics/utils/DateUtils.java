package Capstone.Aeroponics.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;

import org.springframework.data.util.Pair;

public class DateUtils {

    public static final String YYYY = "yyyy";

    public static final String YYYY_MM_DD = "MM-dd-yyyy";

    public static final String YYYYMMDD = "yyyyMMdd";

    /**
     * Now date.
     *
     * @return the date
     */
    public static Date now() {
        return new Date();
    }

    /**
     * Now calendar calendar.
     *
     * @return the calendar
     */
    public static Calendar nowCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now());
        return calendar;
    }

    /**
     * Now timestamp timestamp.
     *
     * @return the timestamp
     */
    public static Timestamp nowTimestamp() {
        return new Timestamp(now().getTime());
    }

    /**
     * Parse date date.
     *
     * @param date the date
     * @return the date
     * @throws Exception the exception
     */
    public static Date parseDate(String date) throws Exception {
        return new SimpleDateFormat(YYYY_MM_DD).parse(date);
    }

    /**
     * Year to local date local date.
     *
     * @param year the year
     * @return the local date
     */
    public static LocalDate yearToLocalDate(String year) {
        DateTimeFormatter format = new DateTimeFormatterBuilder()
                .appendPattern(YYYY)
                .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
                .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                .toFormatter();

        return LocalDate.parse(year, format);
    }

    /**
     * Date to pattern string.
     *
     * @param date    the date
     * @param pattern the pattern
     * @return the string
     */
    public static String dateToPattern(Date date, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }

    /**
     * Date to yyyymmdd string.
     *
     * @param date the date
     * @return the string
     */
    public static String dateToYYYYMMDD(Date date) {
        return dateToPattern(date, YYYYMMDD);
    }

    public static boolean isDateValid(String dateString, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setLenient(false);
        try {
            sdf.parse(dateString);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * Gets today.
     *
     * @return the today
     */
    public static Pair<Date, Date> getToday() {
        return Pair.of(getStartOfTheDay(), getEndOfTheDay());
    }

    /**
     * Gets start of the day.
     *
     * @return the start of the day
     */
    public static Date getStartOfTheDay() {
        Calendar calendar = nowCalendar();
        setTimeToBeginningOfDay(calendar);
        return calendar.getTime();
    }

    /**
     * Gets end of the day.
     *
     * @return the end of the day
     */
    public static Date getEndOfTheDay() {
        Calendar calendar = nowCalendar();
        setTimeToEndOfDay(calendar);
        return calendar.getTime();
    }

    /**
     * Gets this week.
     *
     * @return the this week
     */
    public static Pair<Date, Date> getThisWeek() {
        return Pair.of(getStartOfTheWeek(), getEndOfTheWeek());
    }

    /**
     * Gets start of the week.
     *
     * @return the start of the week
     */
    public static Date getStartOfTheWeek() {
        Calendar calendar = nowCalendar();
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        setTimeToBeginningOfDay(calendar);
        return calendar.getTime();
    }

    /**
     * Gets end of the week.
     *
     * @return the end of the week
     */
    public static Date getEndOfTheWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getStartOfTheWeek());
        int difference = Calendar.SATURDAY - calendar.get(Calendar.DAY_OF_WEEK);
        calendar.add(Calendar.DATE, difference);
        setTimeToEndOfDay(calendar);
        return calendar.getTime();
    }

    /**
     * Gets this month.
     *
     * @return the this month
     */
    public static Pair<Date, Date> getThisMonth() {
        return Pair.of(getStartOfTheMonth(), getEndOfTheMonth());
    }

    /**
     * Gets start of the month.
     *
     * @return the start of the month
     */
    public static Date getStartOfTheMonth() {
        Calendar calendar = nowCalendar();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        setTimeToBeginningOfDay(calendar);
        return calendar.getTime();
    }

    /**
     * Gets end of the month.
     *
     * @return the end of the month
     */
    public static Date getEndOfTheMonth() {
        Calendar calendar = nowCalendar();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        setTimeToEndOfDay(calendar);
        return calendar.getTime();
    }

    /**
     * Gets this year.
     *
     * @return the this year
     */
    public static Pair<Date, Date> getThisYear() {
        return Pair.of(getStartOfTheYear(), getEndOfTheYear());
    }

    /**
     * Gets start of the year.
     *
     * @return the start of the year
     */
    public static Date getStartOfTheYear() {
        LocalDate now = LocalDate.now();
        return getStartOfTheYear(now);
    }

    /**
     * Gets end of the year.
     *
     * @return the end of the year
     */
    public static Date getEndOfTheYear() {
        LocalDate now = LocalDate.now();
        return getEndOfTheYear(now);
    }

    /**
     * Gets year range.
     *
     * @param year the year
     * @return the year range
     */
    public static Pair<Date, Date> getYearRange(Integer year) {
        String yearString = String.valueOf(year);
        return Pair.of(getStartOfTheYear(yearString), getEndOfTheYear(yearString));
    }

    /**
     * Gets year range.
     *
     * @param year the year
     * @return the year range
     */
    public static Pair<Date, Date> getYearRange(String year) {
        return Pair.of(getStartOfTheYear(year), getEndOfTheYear(year));
    }

    /**
     * Gets start of the year.
     *
     * @param year the year
     * @return the start of the year
     */
    public static Date getStartOfTheYear(String year) {
        return getStartOfTheYear(yearToLocalDate(year));
    }

    /**
     * Gets end of the year.
     *
     * @param year the year
     * @return the end of the year
     */
    public static Date getEndOfTheYear(String year) {
        return getEndOfTheYear(yearToLocalDate(year));
    }

    /**
     * Gets start of the year.
     *
     * @param date the date
     * @return the start of the year
     */
    public static Date getStartOfTheYear(LocalDate date) {
        Instant instant = date.with(TemporalAdjusters.firstDayOfYear())
                .atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant();
        return Date.from(instant);
    }

    /**
     * Gets end of the year.
     *
     * @param date the date
     * @return the end of the year
     */
    public static Date getEndOfTheYear(LocalDate date) {
        Instant instant = date.with(TemporalAdjusters.lastDayOfYear())
                .atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant();
        return Date.from(instant);
    }

    /**
     * Is after boolean.
     *
     * @param date1 the date 1
     * @param date2 the date 2
     * @return the boolean
     */
    public static boolean isAfter(Date date1, Date date2) {
        return date1.after(date2);
    }

    /**
     * Is overlapping boolean.
     *
     * @param startDate the start date
     * @param endDate   the end date
     * @return the boolean
     */
    public static boolean isOverlapping(Date startDate, Date endDate) {
        return startDate.after(endDate);
    }

    /**
     * To timestamp timestamp.
     *
     * @param date the date
     * @return the timestamp
     */
    public static Timestamp toTimestamp(Date date) {
        Timestamp timestamp = new Timestamp(date.getTime());
        return timestamp;
    }

    /**
     * Gets yesterday range.
     *
     * @return the yesterday range
     */
    public static Pair<Date, Date> getYesterdayRange() {
        return Pair.of(getLastNDays(-1, true), getLastNDays(-1, false));
    }

    /**
     * Gets last n days and today.
     *
     * @param days the days
     * @return the last n days and today
     */
    public static Pair<Date, Date> getLastNDaysAndToday(int days) {
        return Pair.of(getLastNDays(days, true), getEndOfTheDay());
    }

    /**
     * Gets last n days.
     *
     * @param days         the days
     * @param isStartOfDay the is start of day
     * @return the last n days
     */
    public static Date getLastNDays(int days, boolean isStartOfDay) {
        Calendar calendar = nowCalendar();
        calendar.add(Calendar.DAY_OF_MONTH, days);

        if (isStartOfDay) {
            setTimeToBeginningOfDay(calendar);
        } else {
            setTimeToEndOfDay(calendar);
        }

        return calendar.getTime();
    }

    private static void setTimeToBeginningOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    private static void setTimeToEndOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
    }
}

