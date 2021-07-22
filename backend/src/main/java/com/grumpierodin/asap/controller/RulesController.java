package com.grumpierodin.asap.controller;

import com.grumpierodin.asap.exception.RuleSaveException;
import com.grumpierodin.asap.repository.data.Rule;
import com.grumpierodin.asap.services.RuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
public class RulesController {
    private static final Logger log = LoggerFactory.getLogger(RulesController.class);
    private final RuleService ruleService;

    public RulesController(RuleService ruleService) {
        this.ruleService = ruleService;
    }

    @GetMapping("/api/rules")
    public List<Rule> getAllRules() {
        log.debug("got request: "+ this.ruleService.getAllRules().toString());
        return this.ruleService.getAllRules();
    }

    @PostMapping(path = "/api/rules", consumes = "application/json", produces = "application/json")
    public void setRules(@RequestBody List<Rule> rules) {
        log.debug("posting.... "+ rules.get(0).getName());
        try {
            this.ruleService.save(rules);
            for(Rule rule : this.ruleService.getAllRules())
            {
                System.out.println("rule.... "+ rule.toString());
            }
        } catch (IOException e) {
            throw new RuleSaveException(e.getCause() + e.getMessage());
        }
    }
}
