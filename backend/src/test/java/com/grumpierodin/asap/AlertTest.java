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
import com.grumpierodin.asap.utils.JsonUtils;
import org.assertj.core.api.Fail;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import static org.junit.Assert.*;

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

    @Test
    public void CheckAlertRender() {
        try {
            String alertJson = "{\"message\":\"testing 1\", \"timestamp\":\"2021-06-06 17:17:00.000\", \"host\":\"localhost\", \"application\":\"app\"}";
            Event event = JsonUtils.jsonToObject(alertJson, Event.class);
            System.out.println(event.getMessage());
            assertEquals("testing 1",event.getMessage());

            String s = "[{\"name\":\"Change Me\",\"uuid\":\"ae1e63ca-3dc6-432c-8494-289ebb97eea2\",\"correlationId\":\"{{uuid}}\",\"regex\":\"(.+)\",\"outputTemplate\":\"{{$0}}\",\"schedules\":[{\"name\":\"Default\",\"uuid\":\"ab503946-5c8f-480e-aed8-79cf3b65f16c\",\"startTime\":\"00:00\",\"endTime\":\"23:59\",\"days\":[\"Sun\",\"Mon\",\"Tue\",\"Wed\",\"Thu\",\"Fri\",\"Sat\"]}],\"actions\":[]}]";
            Rules rules = new Rules(s);

            System.out.println(rules.getRulesAsList().get(0).getUuid());
            assertEquals("ae1e63ca-3dc6-432c-8494-289ebb97eea2",rules.getRulesAsList().get(0).getUuid());

            for(Rule rule : rules.getRules()){
                Alert alert = new Alert(event, rule);
                System.out.println(alert.isAlertAble());
                assertTrue(alert.isAlertAble());
                alert.renderAlert();
                System.out.println(alert.getAlertText());
                assertEquals("testing 1",alert.getAlertText());
                System.out.println(alert.getAlertCorrelationId());
                assertEquals("ae1e63ca-3dc6-432c-8494-289ebb97eea2",alert.getAlertCorrelationId());
            }
        } catch (Exception e) {
            Fail.fail(e.getLocalizedMessage());
        }
    }
    @Test
    public void CheckAlertRenderScheduleFail() {
        try {
            String alertJson = "{\"message\":\"testing 1\", \"timestamp\":\"2021-06-06 17:17:00.000\", \"host\":\"localhost\", \"application\":\"app\"}";
            Event event = JsonUtils.jsonToObject(alertJson, Event.class);

            System.out.println(event.getMessage());
            assertEquals("testing 1",event.getMessage());

            String s = "[{\"name\":\"Change Me\",\"uuid\":\"ae1e63ca-3dc6-432c-8494-289ebb97eea2\",\"correlationId\":\"{{uuid}}\",\"regex\":\"(.+)\",\"outputTemplate\":\"{{$0}}\",\"schedules\":[{\"name\":\"Default\",\"uuid\":\"ab503946-5c8f-480e-aed8-79cf3b65f16c\",\"startTime\":\"00:00\",\"endTime\":\"23:59\",\"days\":[\"Mon\",\"Tue\",\"Wed\",\"Thu\",\"Fri\",\"Sat\"]}],\"actions\":[]}]";
            Rules rules = new Rules(s);

            System.out.println(rules.getRulesAsList().get(0).getUuid());
            assertEquals("ae1e63ca-3dc6-432c-8494-289ebb97eea2",rules.getRulesAsList().get(0).getUuid());
            // timestamp is a sunday so should fail to meet schedule criteria and not render alertText/AlertCorrelationId
            for(Rule rule : rules.getRules()){
                Alert alert = new Alert(event, rule);
                System.out.println(alert.isAlertAble());
                assertFalse(alert.isAlertAble());
                alert.renderAlert();
                System.out.println(alert.getAlertText());
                assertNotEquals("testing 1",alert.getAlertText());
                System.out.println(alert.getAlertCorrelationId());
                assertNotEquals("ae1e63ca-3dc6-432c-8494-289ebb97eea2",alert.getAlertCorrelationId());
            }
        } catch (Exception e) {
            Fail.fail(e.getLocalizedMessage());
        }

    }
}
