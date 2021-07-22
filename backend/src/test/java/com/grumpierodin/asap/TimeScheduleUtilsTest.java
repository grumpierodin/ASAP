package com.grumpierodin.asap;

import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import com.grumpierodin.asap.utils.TimeScheduleUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TimeScheduleUtilsTest {
    @org.junit.Rule
    public TestRule watcher = new TestWatcher() {
        protected void starting(Description description) {
            System.out.println(System.lineSeparator()+"====== " + description.getMethodName());
        }
        protected void finished(Description description) {
            System.out.println(System.lineSeparator()+"------ " + description.getMethodName());
        }
    };
    @Test
    public void checkDaysInSchedule() {
        TimeScheduleUtils utils = new TimeScheduleUtils();
        String[] days = {"MONDAY", "FRIDAY", "SATURDAY"};
        Set<String> dayList = new HashSet<>(Arrays.asList(days));
        assertTrue(utils.isWithinSchedule("2021-05-24 17:00",utils.makeSchedule("2021-05-24 17:00","07:00", "23:59", dayList)));
        assertFalse(utils.isWithinSchedule("2021-05-26 17:00",utils.makeSchedule("07:00", "23:59", dayList)));
        assertFalse(utils.isWithinSchedule("2021-05-29 17:00",utils.makeSchedule("22:00", "01:00", dayList)));
        assertTrue(utils.isWithinSchedule("2021-06-01 00:00",utils.makeSchedule("2021-05-31 17:00","22:00", "01:00", dayList)));
        assertTrue(utils.isWithinSchedule("2021-05-31 17:00",utils.makeSchedule("2021-05-31 17:00","07:00", "23:59", dayList)));
    }
}
