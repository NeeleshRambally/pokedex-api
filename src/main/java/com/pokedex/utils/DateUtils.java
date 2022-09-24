package com.pokedex.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import static java.time.LocalDateTime.parse;
import static java.time.format.DateTimeFormatter.BASIC_ISO_DATE;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtils {
    public static LocalDateTime toDateTime(String timeStamp) {
        if (timeStamp == null) {
            return null;
        }
        try {
            return yearMmDateTHHMmsSsMilliSecondsToDateTime(timeStamp);
        } catch (DateTimeParseException e) {
            return yyyyMmDDToDateTime(timeStamp);
        }
    }
    private static LocalDateTime yearMmDateTHHMmsSsMilliSecondsToDateTime(String yearMmDateTHHMmsSsMilliSecondsToDateTime) {
        try {
            return parse(yearMmDateTHHMmsSsMilliSecondsToDateTime);
        } catch (DateTimeParseException exception) {
            return parse(yearMmDateTHHMmsSsMilliSecondsToDateTime, ISO_LOCAL_DATE_TIME);
        }
    }
    public static LocalDateTime yyyyMmDDToDateTime(String yyyyMmDD) {
        return yyyyMmDD == null ? null : dateStringToLocalDate(yyyyMmDD).atStartOfDay();
    }
    public static LocalDate dateStringToLocalDate(String yyyyMmDd) {
        return yyyyMmDd == null ? null : LocalDate.parse(yyyyMmDd, BASIC_ISO_DATE);
    }
}
