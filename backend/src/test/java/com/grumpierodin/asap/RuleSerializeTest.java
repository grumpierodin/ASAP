package com.grumpierodin.asap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.grumpierodin.asap.repository.data.Rule;
import com.grumpierodin.asap.repository.data.Rules;
import com.grumpierodin.asap.repository.data.Schedule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import com.grumpierodin.asap.utils.TimeScheduleUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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
    ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false)
            .registerModule(new ParameterNamesModule())
            .registerModule(new Jdk8Module())
            .registerModule(new JavaTimeModule());
    @Test
    public void checkRuleFromJSON() throws JsonProcessingException {
        String s = "[{\"name\":\"Change Me\",\"uuid\":\"ae1e63ca-3dc6-432c-8494-289ebb97eea2\",\"correlationId\":\"{{GUID}}\",\"regex\":\"(.+)\",\"outputTemplate\":\"{{$0}}\",\"schedules\":[{\"name\":\"Default\",\"uuid\":\"ab503946-5c8f-480e-aed8-79cf3b65f16c\",\"startTime\":\"00:00\",\"endTime\":\"23:59\",\"days\":[\"Sun\",\"Mon\",\"Tue\",\"Wed\",\"Thu\",\"Fri\",\"Sat\"]}],\"actions\":[]}]";
        Rule[] rules = mapper.readValue(s, Rule[].class);
        System.out.println(rules.length);
        TimeScheduleUtils utils = new TimeScheduleUtils();
        String[] days = rules[0].getSchedules().get(0).getDays();
        Set<String> dayList = new HashSet<>(Arrays.asList(days));

        System.out.println(rules[0].getName());
        System.out.println(rules[0].getUuid());

        assertTrue("ae1e63ca-3dc6-432c-8494-289ebb97eea2".equals(rules[0].getUuid()));

        Schedule schedule = rules[0].getSchedules().get(0);
        Collection<TimeScheduleUtils.TimeRange> ranges = utils.makeSchedule(schedule.getStartTime().toString(), schedule.getEndTime().toString(), dayList);
        for(TimeScheduleUtils.TimeRange item : ranges) {
            System.out.println(item.getTimeFrom() +" - "+ item.getTimeTo());
        }
        assertTrue(utils.isWithinSchedule(LocalDateTime.now(), utils.makeSchedule(LocalDateTime.now(), schedule.getStartTime().toString(), schedule.getEndTime().toString(), dayList)));
    }
    @Test
    public void checkRulesFromJSON() throws JsonProcessingException {
        String s = "[{\"name\":\"Change Me\",\"uuid\":\"ae1e63ca-3dc6-432c-8494-289ebb97eea2\",\"correlationId\":\"{{GUID}}\",\"regex\":\"(.+)\",\"outputTemplate\":\"{{$0}}\",\"schedules\":[{\"name\":\"Default\",\"uuid\":\"ab503946-5c8f-480e-aed8-79cf3b65f16c\",\"startTime\":\"00:00\",\"endTime\":\"23:59\",\"days\":[\"Sun\",\"Mon\",\"Tue\",\"Wed\",\"Thu\",\"Fri\",\"Sat\"]}],\"actions\":[]}]";
        Rules rules = new Rules(mapper.readValue(s, Rule[].class));
        System.out.println(rules.getRulesAsList().get(0).getUuid());
        assertTrue("ae1e63ca-3dc6-432c-8494-289ebb97eea2".equals(rules.getRulesAsList().get(0).getUuid()));
    }
    @Test
    public void checkRulesFromJSONString() {
        String s = "[{\"name\":\"Change Me\",\"uuid\":\"ae1e63ca-3dc6-432c-8494-289ebb97eea2\",\"correlationId\":\"{{GUID}}\",\"regex\":\"(.+)\",\"outputTemplate\":\"{{$0}}\",\"schedules\":[{\"name\":\"Default\",\"uuid\":\"ab503946-5c8f-480e-aed8-79cf3b65f16c\",\"startTime\":\"00:00\",\"endTime\":\"23:59\",\"days\":[\"Sun\",\"Mon\",\"Tue\",\"Wed\",\"Thu\",\"Fri\",\"Sat\"]}],\"actions\":[]}]";
        Rules rules = new Rules(s);
        System.out.println(rules.getRulesAsList().get(0).getUuid());
        assertTrue("ae1e63ca-3dc6-432c-8494-289ebb97eea2".equals(rules.getRulesAsList().get(0).getUuid()));
    }
}
