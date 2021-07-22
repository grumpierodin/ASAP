package com.grumpierodin.asap.repository;

import com.grumpierodin.asap.repository.data.Rule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RuleRepository extends JpaRepository<Rule, String> {
}
