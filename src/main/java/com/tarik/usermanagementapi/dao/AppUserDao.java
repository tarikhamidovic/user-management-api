package com.tarik.usermanagementapi.dao;

import com.tarik.usermanagementapi.model.entity.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserDao extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByEmail(String email);
    Page<AppUser> findByFirstNameContaining(String firstName, Pageable pageable);
    Page<AppUser> findByLastNameContaining(String lastName, Pageable pageable);
    Page<AppUser> findByEmailContaining(String email, Pageable pageable);
}
