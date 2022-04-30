package com.grumpierodin.asap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.grumpierodin.asap.repository.data.Rule;
import com.grumpierodin.asap.repository.data.Rules;
import com.grumpierodin.asap.repository.data.Schedule;
import com.grumpierodin.asap.utils.JsonUtils;
import com.grumpierodin.asap.utils.TimeScheduleUtils;
import org.assertj.core.api.Fail;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RuleSerializeTest {
    @org.junit.Rule
    public TestRule watcher = new TestWatcher() {

        protected void starting(Description description) {
            System.out.println(System.lineSeparator()+"====== " + description.getMethodName());
        }
        protected void finished(Description description) {
            System.out.println(System.lineSeparator()+"finishing " + description.getMethodName());
        }
    };
    @Test
    public void checkRuleFromJSON(){
        try {
            String s = "[{\"name\":\"Change Me\",\"uuid\":\"ae1e63ca-3dc6-432c-8494-289ebb97eea2\",\"correlationId\":\"{{GUID}}\",\"regex\":\"(.+)\",\"outputTemplate\":\"{{$0}}\",\"schedules\":[{\"name\":\"Default\",\"uuid\":\"ab503946-5c8f-480e-aed8-79cf3b65f16c\",\"startTime\":\"00:00\",\"endTime\":\"23:59\",\"days\":[\"Sun\",\"Mon\",\"Tue\",\"Wed\",\"Thu\",\"Fri\",\"Sat\"]}],\"actions\":[]}]";
            Rule[] rules = JsonUtils.jsonToObject(s, Rule[].class);
            System.out.println(rules.length);
            TimeScheduleUtils utils = new TimeScheduleUtils();
            String[] days = rules[0].getSchedules().get(0).getDays();
            Set<String> dayList = new HashSet<>(Arrays.asList(days));

            System.out.println(rules[0].getName());
            System.out.println(rules[0].getUuid());

            assertEquals("ae1e63ca-3dc6-432c-8494-289ebb97eea2",rules[0].getUuid());

            Schedule schedule = rules[0].getSchedules().get(0);
            Collection<TimeScheduleUtils.TimeRange> ranges = utils.makeSchedule(schedule.getStartTime().toString(), schedule.getEndTime().toString(), dayList);
            for (TimeScheduleUtils.TimeRange item : ranges) {
                System.out.println(item.getTimeFrom() + " - " + item.getTimeTo());
            }
            assertTrue(TimeScheduleUtils.isWithinSchedule(LocalDateTime.now(), utils.makeSchedule(LocalDateTime.now(), schedule.getStartTime().toString(), schedule.getEndTime().toString(), dayList)));
        } catch (Exception e) {
            Fail.fail(e.getLocalizedMessage());
        }
    }
    @Test
    public void checkRulesFromJSON()  {
        try {
            String s = "[{\"name\":\"Change Me\",\"uuid\":\"ae1e63ca-3dc6-432c-8494-289ebb97eea2\",\"correlationId\":\"{{GUID}}\",\"regex\":\"(.+)\",\"outputTemplate\":\"{{$0}}\",\"schedules\":[{\"name\":\"Default\",\"uuid\":\"ab503946-5c8f-480e-aed8-79cf3b65f16c\",\"startTime\":\"00:00\",\"endTime\":\"23:59\",\"days\":[\"Sun\",\"Mon\",\"Tue\",\"Wed\",\"Thu\",\"Fri\",\"Sat\"]}],\"actions\":[]}]";
            Rules rules = new Rules(JsonUtils.jsonToObject(s, Rule[].class));
            System.out.println(rules.getRulesAsList().get(0).getUuid());
            assertEquals("ae1e63ca-3dc6-432c-8494-289ebb97eea2",rules.getRulesAsList().get(0).getUuid());
        } catch (Exception e) {
            Fail.fail(e.getLocalizedMessage());
        }
    }
    @Test
    public void checkRulesFromJSONString() {
        String s = "[{\"name\":\"Change Me\",\"uuid\":\"ae1e63ca-3dc6-432c-8494-289ebb97eea2\",\"correlationId\":\"{{GUID}}\",\"regex\":\"(.+)\",\"outputTemplate\":\"{{$0}}\",\"schedules\":[{\"name\":\"Default\",\"uuid\":\"ab503946-5c8f-480e-aed8-79cf3b65f16c\",\"startTime\":\"00:00\",\"endTime\":\"23:59\",\"days\":[\"Sun\",\"Mon\",\"Tue\",\"Wed\",\"Thu\",\"Fri\",\"Sat\"]}],\"actions\":[]}]";
        Rules rules = new Rules(s);
        System.out.println(rules.getRulesAsList().get(0).getUuid());
        assertEquals("ae1e63ca-3dc6-432c-8494-289ebb97eea2",rules.getRulesAsList().get(0).getUuid());
    }
}
