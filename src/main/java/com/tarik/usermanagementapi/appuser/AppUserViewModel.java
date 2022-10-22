package com.tarik.usermanagementapi.appuser;

import com.tarik.usermanagementapi.permission.Permission;
import com.tarik.usermanagementapi.status.Status;

import java.util.List;

public record AppUserViewModel(
        Long id,
        String firstName,
        String lastName,
        String password,
        String email,
        Status status,
        List<String> permissions
) {
    public AppUserViewModel(AppUser appUser) {
        this(
                appUser.getId(),
                appUser.getFirstName(),
                appUser.getLastName(),
                appUser.getPassword(),
                appUser.getEmail(),
                appUser.getStatus(),
                appUser.getPermissions().stream().map(Permission::getPermissionName).toList()
        );
    }
}
