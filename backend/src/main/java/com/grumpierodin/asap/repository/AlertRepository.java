package com.grumpierodin.asap.repository;

import com.grumpierodin.asap.repository.data.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

/* useful info on JPA Repository/Service method naming.
 *  https://docs.spring.io/spring-data/jpa/docs/1.5.0.RELEASE/reference/html/jpa.repositories.html
 */

public interface AlertRepository extends JpaRepository<Alert, String> {
    @Query("SELECT a FROM Alert a WHERE a.event.timestamp BETWEEN ?1 AND ?2 AND a.rule.uuid=?3 AND a.alertCorrelationId=?4")
    List<Alert> findEventsByTimestampBetweenAndRuleId(LocalDateTime from, LocalDateTime to, String ruleId, String correlationId);
    @Query("SELECT a FROM Alert a WHERE a.publish=true")
    List<Alert> findPublishedEventsPublished();
}
