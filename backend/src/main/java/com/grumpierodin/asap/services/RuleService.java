package com.grumpierodin.asap.services;

import com.grumpierodin.asap.repository.AlertFilterRepository;
import com.grumpierodin.asap.repository.RuleRepository;
import com.grumpierodin.asap.repository.ScheduleRepository;
import com.grumpierodin.asap.repository.data.Rule;
import com.grumpierodin.asap.repository.data.Rules;
import com.grumpierodin.asap.utils.AsapFileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class RuleService {
    @Autowired
    AlertFilterRepository alertFilterRepository;
    @Autowired
    RuleRepository ruleRepository;
    @Autowired
    ScheduleRepository scheduleRepository;
    AsapFileUtils fileutils = new AsapFileUtils();
    Rules rules = new Rules();
    public List<Rule> getAllRules() {
        if(rules.getRules().size()<1) {
            try {
                this.loadRules();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return rules.getRulesAsList();
    }
    public void loadRules() throws IOException {
        this.rules = fileutils.readRulesFromFile();
        scanForDbChnages();
    }

    public void save(List<Rule> rules) throws IOException {
        this.rules = new Rules(rules);
        scanForDbChnages();
        fileutils.saveCurrent(this.rules);
    }

    void scanForDbChnages() {
        for(Rule rule : rules.getRulesAsList()) {
            alertFilterRepository.saveAll(rule.getAlertFilters());
            scheduleRepository.saveAll(rule.getSchedules());
        }
        ruleRepository.saveAll(rules.getRulesAsList());
    }
}
