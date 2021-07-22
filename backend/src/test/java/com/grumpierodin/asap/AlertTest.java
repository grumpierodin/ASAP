package com.grumpierodin.asap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.grumpierodin.asap.repository.data.Alert;
import com.grumpierodin.asap.repository.data.Event;
import com.grumpierodin.asap.repository.data.Rule;
import com.grumpierodin.asap.repository.data.Rules;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AlertTest {
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
    public void CheckAlertRender() {
        try {
            String alertJson = "{\"message\":\"testing 1\", \"timestamp\":\"2021-06-06 17:17:00.000\", \"host\":\"localhost\", \"application\":\"app\"}";
            Event event = mapper.readValue(alertJson, Event.class);

            System.out.println(event.getMessage());
            assertTrue("testing 1".equals(event.getMessage()));

            String s = "[{\"name\":\"Change Me\",\"uuid\":\"ae1e63ca-3dc6-432c-8494-289ebb97eea2\",\"correlationId\":\"{{uuid}}\",\"regex\":\"(.+)\",\"outputTemplate\":\"{{$0}}\",\"schedules\":[{\"name\":\"Default\",\"uuid\":\"ab503946-5c8f-480e-aed8-79cf3b65f16c\",\"startTime\":\"00:00\",\"endTime\":\"23:59\",\"days\":[\"Sun\",\"Mon\",\"Tue\",\"Wed\",\"Thu\",\"Fri\",\"Sat\"]}],\"actions\":[]}]";
            Rules rules = new Rules(s);

            System.out.println(rules.getRulesAsList().get(0).getUuid());
            assertTrue("ae1e63ca-3dc6-432c-8494-289ebb97eea2".equals(rules.getRulesAsList().get(0).getUuid()));

            for(Rule rule : rules.getRules()){
                Alert alert = new Alert(event, rule);
                System.out.println(alert.isAlertAble());
                assertTrue(alert.isAlertAble());
                alert.renderAlert();
                System.out.println(alert.getAlertText());
                assertTrue("testing 1".equals(alert.getAlertText()));
                System.out.println(alert.getAlertCorrelationId());
                assertTrue("ae1e63ca-3dc6-432c-8494-289ebb97eea2".equals(alert.getAlertCorrelationId()));
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void CheckAlertRenderScheduleFail() {
        try {
            String alertJson = "{\"message\":\"testing 1\", \"timestamp\":\"2021-06-06 17:17:00.000\", \"host\":\"localhost\", \"application\":\"app\"}";
            Event event = mapper.readValue(alertJson, Event.class);

            System.out.println(event.getMessage());
            assertTrue("testing 1".equals(event.getMessage()));

            String s = "[{\"name\":\"Change Me\",\"uuid\":\"ae1e63ca-3dc6-432c-8494-289ebb97eea2\",\"correlationId\":\"{{uuid}}\",\"regex\":\"(.+)\",\"outputTemplate\":\"{{$0}}\",\"schedules\":[{\"name\":\"Default\",\"uuid\":\"ab503946-5c8f-480e-aed8-79cf3b65f16c\",\"startTime\":\"00:00\",\"endTime\":\"23:59\",\"days\":[\"Mon\",\"Tue\",\"Wed\",\"Thu\",\"Fri\",\"Sat\"]}],\"actions\":[]}]";
            Rules rules = new Rules(s);

            System.out.println(rules.getRulesAsList().get(0).getUuid());
            assertTrue("ae1e63ca-3dc6-432c-8494-289ebb97eea2".equals(rules.getRulesAsList().get(0).getUuid()));
            // timestamp is a sunday so should fail to meet schedule criteria and not render alertText/AlertCorrelationId
            for(Rule rule : rules.getRules()){
                Alert alert = new Alert(event, rule);
                System.out.println(alert.isAlertAble());
                assertFalse(alert.isAlertAble());
                alert.renderAlert();
                System.out.println(alert.getAlertText());
                assertFalse("testing 1".equals(alert.getAlertText()));
                System.out.println(alert.getAlertCorrelationId());
                assertFalse("ae1e63ca-3dc6-432c-8494-289ebb97eea2".equals(alert.getAlertCorrelationId()));
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
