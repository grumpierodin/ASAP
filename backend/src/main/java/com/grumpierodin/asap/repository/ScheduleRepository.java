package com.grumpierodin.asap.repository;

import com.grumpierodin.asap.repository.data.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, String> {
}
