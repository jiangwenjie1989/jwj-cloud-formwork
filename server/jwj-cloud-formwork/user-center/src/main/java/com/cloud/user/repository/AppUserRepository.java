package com.cloud.user.repository;

import com.cloud.model.user.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface AppUserRepository extends JpaRepository<AppUser, Long> {


    Optional<AppUser> findByPhone(String phone);

    Optional<AppUser> findByUsername(String username);

}
