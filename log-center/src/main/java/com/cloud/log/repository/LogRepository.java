package com.cloud.log.repository;


import com.cloud.model.log.SysLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<SysLog, Long> {
}
