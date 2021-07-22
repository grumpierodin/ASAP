package com.grumpierodin.asap.repository.data;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.UUID;

@Entity
public class Schedule {
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final String[] days;
    private final String name;

    public String getUuid() {
        return uuid;
    }
    @Id
    private final String uuid;

    public String getName() {
        return name;
    }
    @JsonSerialize(using = LocalTimeSerializer.class)
    public LocalTime getStartTime() {
        return startTime;
    }
    @JsonSerialize(using = LocalTimeSerializer.class)
    public LocalTime getEndTime() {
        return endTime;
    }

    public String[] getDays() {
        return convertDays(days);
    }

    public Schedule() {
        this.startTime = LocalTime.parse("00:00:00");
        this.endTime = LocalTime.parse("23:59:59");
        this.days = convertDays(new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"});
        this.name = "schedule";
        this.uuid = UUID.randomUUID().toString();
    }
    public Schedule(String startTime, String endTime, String[] days, String name, String uuid) {
        this.startTime = LocalTime.parse(startTime);
        this.endTime = LocalTime.parse(endTime);
        this.days = convertDays(days);
        this.name = name;
        this.uuid = uuid;
    }
    private String[] convertDays(String[] days) {
        ArrayList<String> fullDays = new ArrayList<String>();
        for(String name : days) {
            switch(name) {
                case "Sun":
                    fullDays.add(DayOfWeek.SUNDAY.name());
                    break;
                case "Mon":
                    fullDays.add(DayOfWeek.MONDAY.name());
                    break;
                case "Tue":
                    fullDays.add(DayOfWeek.TUESDAY.name());
                    break;
                case "Wed":
                    fullDays.add(DayOfWeek.WEDNESDAY.name());
                    break;
                case "Thu":
                    fullDays.add(DayOfWeek.THURSDAY.name());
                    break;
                case "Fri":
                    fullDays.add(DayOfWeek.FRIDAY.name());
                    break;
                case "Sat":
                    fullDays.add(DayOfWeek.SATURDAY.name());
                    break;
                default:
                    fullDays.add(name);
                    break;
            }
        }
        return fullDays.toArray(new String[0]);
    }

}
