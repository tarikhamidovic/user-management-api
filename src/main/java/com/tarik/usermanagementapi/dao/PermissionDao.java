package com.tarik.usermanagementapi.dao;

import com.tarik.usermanagementapi.model.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface PermissionDao extends JpaRepository<Permission, Long> {

    @Query("SELECT p FROM Permission p WHERE p.permissionName IN :permissionNames")
    List<Permission> getPermissionsByPermissionNames(
            @Param(value = "permissionNames") Collection<String> permissionNames
    );
}
