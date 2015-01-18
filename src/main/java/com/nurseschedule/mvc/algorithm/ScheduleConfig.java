package com.nurseschedule.mvc.algorithm;

public class ScheduleConfig {

    public static final String[] fullJobNightRegex = { "^[^N]{5}[N]{2}$", "^[^N]{4}[N]{3}$", "^[N]{2,3}[R]{2}[^N]*$", "^[^N]*[N]{3}[R]{2}$[^N]*" };

    public static final String[] nightSeries = { "^[^N]*[N]{2,3}[R]{2,3}[^N]*$", "^[^N]*[N]{2,3}$" };

    public static final String[] halfTimeSeries = { "^[R]*[N]{2,3}[R]{2,3}[R]*$", "^[R]*[N]{2,3}$", "^[R]*[d]{2,3}[R]*$", "^[R]*[d]{1}[N]{2}[R]{2,3}$" };

    public static final int WORKWEEK_LENGTH = 5;

    public static final int WEEKEND_LEGTH = 2;

    public static final String[] WEKK_DAYS = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };

    public static final int FULL_TIME_NURSES_NUMBER = 12;

    public static final int HOURS_32_NURSES_NUMBER = 1;

    public static final int HOURS_20_NURSES_NUMBER = 3;

    public static final int WORKWEEK_DAY_SHIFT_NURSES_NUMBER = 9;

    public static final int WEEKEND_DAY_SHIFT_NURSES_NUMBER = 6;

    public static final int NIGHT_SHIFT_NURSES_NUMBER = 1;
    
    public static final int ALL_SCHEDULE_WEEK_NUMBER = 5;
}
