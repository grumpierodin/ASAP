package com.grumpierodin.asap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.grumpierodin.asap.repository.data.Event;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import static org.junit.Assert.assertTrue;

public class EventSerializeText {
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
    public void CheckEventJsonString() {
        try {
            String s = "{\"message\":\"testing 1\", \"timestamp\":\"2021-06-06T17:17:00.000\", \"host\":\"localhost\", \"application\":\"app\"}";
            Event event = mapper.readValue(s, Event.class);
            System.out.println(event.getMessage());
            assertTrue("testing 1".equals(event.getMessage()));

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void CheckPartialEventJsonString() {
        try {
            String s = "{\"message\":\"testing 1\"}";
            Event event = mapper.readValue(s, Event.class);
            System.out.println(event.getMessage());
            System.out.println(event.getTimestamp());
            assertTrue("testing 1".equals(event.getMessage()));

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
