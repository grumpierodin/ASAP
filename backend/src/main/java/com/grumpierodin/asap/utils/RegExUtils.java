package com.grumpierodin.asap.utils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//
//db schema should include guid, correlation id, timestamp and message as per output template.
// potentially use h2 db as can then use count and time range type sql queries rather than map.
//
public class RegExUtils {
    private static Set<String> getNamedGroupCandidates(String regex) {
        Set<String> namedGroups = new TreeSet<>();
        Matcher m = Pattern.compile("\\(\\?<([a-zA-Z][a-zA-Z0-9]*)>").matcher(regex);
        while (m.find()) {
            namedGroups.add(m.group(1));
        }

        return namedGroups;
    }

    private static Map<String, String> getMatchedParams(Matcher match) {
        Map<String, String> groups = new HashMap<>();
        boolean isMatched = match.find();
        if (isMatched) {
            if (match.groupCount() > 0) {
                int count = match.groupCount();
                for (int i = 0; i < count; i++) {
                    groups.putIfAbsent("$" + i, match.group(i));
                }
                for(String groupName : getNamedGroupCandidates(match.pattern().pattern())) {
                    groups.putIfAbsent(groupName, match.group(groupName));
                }
            }
            groups.putIfAbsent("$matched", "true");
        }
        return groups;
    }

    public static Map<String, String> makeMatch(String regexPattern, String text) {
        Pattern pattern = Pattern.compile(regexPattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        return getMatchedParams(matcher);
    }

    public static String renderTemplate(String template, Map<String, String> data) {
        String output = template;
        for(Map.Entry<String, String> entry : data.entrySet()) {
            String regexText = entry.getKey().replace("$", "[$]");
            regexText = "[{][{]+" + regexText + "[}][}]+";
            output = output.replaceAll(regexText, entry.getValue());
        }
        return output;
    }
}
