package com.cloud.admin.repository;

import com.cloud.model.manage.BlackIp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface BlackIpRepository extends JpaRepository<BlackIp, Long> {


    Optional<BlackIp> findByIpAddress(String ipAddress);

}
