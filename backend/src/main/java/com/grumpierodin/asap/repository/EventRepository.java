package com.grumpierodin.asap.repository;

import com.grumpierodin.asap.repository.data.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, String> {
}
