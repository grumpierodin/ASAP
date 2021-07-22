package com.grumpierodin.asap.repository.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Rules {
    private Collection<Rule> rules;
    public Rules() {
        this.rules = new ArrayList<Rule>() ;
        //this.rules.add(new Rule());
    }

    public Rules(Collection<Rule> rules) {
        this.rules = rules;
    }

    public Rules(Rule[] rules) {
        this.rules = Arrays.asList(rules);
    }

    public Rules(String json) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false)
                .registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule());
        try {
            setRules(mapper.readValue(json, Rule[].class));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            this.rules = new ArrayList<Rule>() ;
            this.rules.add(new Rule());
        }
    }

    public List<Rule> getRulesAsList() {
        return (List<Rule>) rules;
    }
    public List<Rule> getRules() {
        return (List<Rule>) rules;
    }

    public void setRules(Collection<Rule> rules) {
        this.rules = rules;
    }
    public void setRules(Rule[] rules) {
        this.rules = Arrays.asList(rules);
    }
}
