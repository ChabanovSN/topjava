package ru.javawebinar.topjava.util;

import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


    public static <T extends Comparable<T>> boolean isBetween(T data, T start, T end) {
        return data.compareTo(start) >= 0 && data.compareTo(end) <= 0;
    }
    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }

    public static LocalDate parseLocalDate(String currentDate, LocalDate date) {
        return StringUtils.isEmpty(currentDate) ? date : LocalDate.parse(currentDate);
    }

    public static LocalTime parseLocalTime(String currentTime, LocalTime time){
        return StringUtils.isEmpty(currentTime) ? time : LocalTime.parse(currentTime);
    }
}
