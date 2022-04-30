package com.grumpierodin.asap;

import com.grumpierodin.asap.repository.data.Event;
import com.grumpierodin.asap.utils.JsonUtils;
import org.assertj.core.api.Fail;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import static org.junit.Assert.assertEquals;

public class EventSerializeTest {
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
    public void CheckEventJsonString() {
        try {
            String s = "{\"message\":\"testing 1\", \"timestamp\":\"2021-06-06 17:17:00.000\", \"host\":\"localhost\", \"application\":\"app\"}";
            Event event = JsonUtils.jsonToObject(s, Event.class);
            System.out.println(event.getMessage());
            assertEquals("testing 1",event.getMessage());
        } catch(Exception e) {
            Fail.fail(e.getLocalizedMessage());
        }
    }
    @Test
    public void CheckPartialEventJsonString() {
        try {
            String s = "{\"message\":\"testing 1\"}";
            Event event = JsonUtils.jsonToObject(s, Event.class);
            System.out.println(event.getMessage());
            System.out.println(event.getTimestamp());
            assertEquals("testing 1",event.getMessage());
        } catch (Exception e) {
            Fail.fail(e.getLocalizedMessage());
        }
    }
}
