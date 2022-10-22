package com.tarik.usermanagementapi.appuser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserDao extends JpaRepository<AppUser, Long> { }
