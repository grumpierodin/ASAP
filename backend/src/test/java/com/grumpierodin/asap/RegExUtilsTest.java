package com.grumpierodin.asap;

import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import com.grumpierodin.asap.utils.RegExUtils;

import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RegExUtilsTest {
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
    public void checkGroupNames() {
        RegExUtils utils = new RegExUtils();
        String regexPattern ="(?<text>\\w+)(\\s+)(?<id>[0-9]+)+";
        Map<String, String> result = utils.makeMatch(regexPattern,"item 123");
        assertTrue(result.containsKey("text"));
        assertFalse(result.containsKey("junk"));
    }
    @Test
    public void checkMatches() {
        RegExUtils utils = new RegExUtils();
        String regexPattern ="(?<text>\\w+)(\\s+)(?<id>[0-9]+)+";
        Map<String, String> result = utils.makeMatch(regexPattern, "item 123");
        assertTrue(result.containsKey("text"));
        assertTrue(result.getOrDefault("text", "").equalsIgnoreCase("item"));
    }
    @Test
    public void checkRender() {
        RegExUtils utils = new RegExUtils();
        String regexPattern ="(?<text>\\w+)(\\s+)(?<id>[0-9]+)+";
        Map<String, String> result = utils.makeMatch(regexPattern, "item 124");
        assertTrue(result.containsKey("text"));
        assertTrue(result.getOrDefault("text", "").equalsIgnoreCase("item"));
        assertFalse(utils.renderTemplate("{{$0}}", result).equalsIgnoreCase("{{$0}}"));
        assertFalse(utils.renderTemplate("{{text}}", result).equalsIgnoreCase("{{text}}"));
    }
}
