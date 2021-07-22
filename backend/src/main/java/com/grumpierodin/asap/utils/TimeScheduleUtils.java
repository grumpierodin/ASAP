package com.grumpierodin.asap.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class TimeScheduleUtils {
    public static class TimeRange {
        LocalDateTime timeFrom;
        LocalDateTime timeTo;

        public TimeRange() {
            this.timeFrom = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
            this.timeTo = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999);
        }

        public TimeRange(LocalDateTime timeFrom, LocalDateTime timeTo) {
            this.timeFrom = timeFrom;
            this.timeTo = timeTo;
        }

        public LocalDateTime getTimeFrom() {
            return timeFrom;
        }

        public LocalDateTime getTimeTo() {
            return timeTo;
        }
    }

    public boolean isWithinSchedule(String timestamp, Collection<TimeRange> schedule) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime time = LocalDateTime.parse(timestamp, formatter);
        return isWithinSchedule(time, schedule);
    }

    public static boolean isWithinSchedule(LocalDateTime timestamp, Collection<TimeRange> schedule) {
        boolean isAllowed = false;
        for(TimeRange item : schedule) {
            if(item.timeFrom.isBefore(timestamp) || item.timeFrom.isEqual(timestamp)) {
                if(item.timeTo.isAfter(timestamp) || item.timeTo.isEqual(timestamp)) {
                    isAllowed =true;
                    break;
                }
            }
        }
        return isAllowed;
    }
    public Collection<TimeRange> makeSchedule(String now, String timeFrom, String timeTo, Set<String> days) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime time = LocalDateTime.parse(now, formatter);
        return makeSchedule(time, timeFrom, timeTo, days);
    }

    public Collection<TimeRange> makeSchedule(String timeFrom, String timeTo, Set<String> days) {
        LocalDateTime now = LocalDateTime.now();
        return makeSchedule(now, timeFrom, timeTo, days);
    }
    public Collection<TimeRange> makeSchedule(LocalDateTime now, String timeFrom, String timeTo, Set<String> days) {
        // 7 days in week so create 7 days from now of dates from schedule
        LocalTime scheduleTime = LocalTime.parse(timeFrom);
        LocalTime scheduleTime2 = LocalTime.parse(timeTo);
        return makeSchedule(now, scheduleTime, scheduleTime2, days);
    }
    public static Collection<TimeRange> makeSchedule(LocalDateTime now, LocalTime scheduleTime, LocalTime scheduleTime2, String[] days) {
        Set<String> daysAsSet = new HashSet<String>(Arrays.asList(days));
        return makeSchedule(now, scheduleTime, scheduleTime2, daysAsSet);
    }
    public static Collection<TimeRange> makeSchedule(LocalDateTime now, LocalTime scheduleTime, LocalTime scheduleTime2, Set<String> days) {
        Collection<TimeRange> times = new ArrayList<>();
        // 7 days in week so create 7 days from now of dates from schedule
        LocalDate rollingDate = now.toLocalDate();
        for(int i=0; i < 7; i++) {
            LocalDateTime schedule = LocalDateTime.of(rollingDate, scheduleTime);
            LocalDateTime schedule2 = LocalDateTime.of(rollingDate, scheduleTime2);
            if(schedule.isAfter(schedule2)) {
                schedule2 = schedule2.plus(1, ChronoUnit.DAYS);
            }
            if(days.contains(schedule.getDayOfWeek().name())) {
                times.add(new TimeRange(schedule, schedule2));
            }
            rollingDate = rollingDate.plus(1, ChronoUnit.DAYS);
        }
        return times;
    }
}
