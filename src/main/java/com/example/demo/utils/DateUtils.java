package com.example.demo.utils;

import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtils {
    private static final ZoneId ZONE = ZoneId.of("UTC+8");

    public static String convertDateByPattern(String formatDate, String originalPattern, String toPattern) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(originalPattern);
        dateFormat.withLocale(LocaleContextHolder.getLocale());
        LocalDate date = LocalDate.parse(formatDate, dateFormat);
        return date.format(DateTimeFormatter.ofPattern(toPattern));
    }

    public static LocalDateTime parseLocalDateTime(String formatDate, String originalPattern) {
        return LocalDateTime.parse(formatDate, DateTimeFormatter.ofPattern(originalPattern));
    }

    public static Date parseDate(String formatDate, String originalPattern) {
        return Date.from(parseLocalDateTime(formatDate, originalPattern).atZone(ZONE).toInstant());
    }

    public static Long getMilliseconds(LocalDate date) {
        if (date == null) return 0L;
        return date.atTime(0, 0, 0).atZone(ZONE).toInstant().toEpochMilli();
    }

    public static Long getMilliseconds(LocalDateTime dateTime) {
        if (dateTime == null) return 0L;
        return dateTime.atZone(ZONE).toInstant().toEpochMilli();
    }
}