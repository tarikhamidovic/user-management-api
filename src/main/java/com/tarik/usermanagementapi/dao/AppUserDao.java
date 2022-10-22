package com.tarik.usermanagementapi.dao;

import com.tarik.usermanagementapi.model.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserDao extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByEmail(String email);
}
