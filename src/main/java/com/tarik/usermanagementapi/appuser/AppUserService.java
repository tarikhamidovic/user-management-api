package com.tarik.usermanagementapi.appuser;

import com.tarik.usermanagementapi.exception.AppUserNotFoundException;
import com.tarik.usermanagementapi.exception.AppUserEmailConstraintViolationException;
import com.tarik.usermanagementapi.permission.Permission;
import com.tarik.usermanagementapi.permission.PermissionDao;
import com.tarik.usermanagementapi.status.Status;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AppUserService {

    private final AppUserDao appUserDao;
    private final PermissionDao permissionDao;

    public AppUserService(AppUserDao appUserDao, PermissionDao permissionDao) {
        this.appUserDao = appUserDao;
        this.permissionDao = permissionDao;
    }

    public List<AppUserViewModel> getAppUsers() {
        return appUserDao.findAll().stream()
                .map(AppUserViewModel::new)
                .toList();
    }


    public AppUser createAppUser(AppUserViewModel appUser) {
        appUserDao.findByEmail(appUser.email()).ifPresent((existingAppUser) -> {
            throw new AppUserEmailConstraintViolationException(
                    "User with email address " + existingAppUser.getEmail() + " already exists"
            );
        });

        List<Permission> permissions = permissionDao
                .getPermissionsByPermissionNames(appUser.permissions());

        AppUser appUserWithEncodedPasswordAndDefaultStatus = AppUser.builder()
                .firstName(appUser.firstName())
                .lastName(appUser.lastName())
                .username(appUser.firstName() + appUser.lastName() + UUID.randomUUID())
                .email(appUser.email())
                .password(BCrypt.hashpw(appUser.password(), BCrypt.gensalt()))
                .status(Status.ACTIVE)
                .permissions(permissions)
                .build();

        return appUserDao.save(appUserWithEncodedPasswordAndDefaultStatus);
    }

    public AppUserViewModel updateAppUser(Long appUserId, AppUserViewModel appUser) {
        return appUserDao.findById(appUserId)
                .map(existingAppUser -> {
                    existingAppUser.setFirstName(appUser.firstName());
                    existingAppUser.setLastName(appUser.lastName());
                    existingAppUser.setEmail(appUser.email());
                    existingAppUser.setStatus(appUser.status());
                    AppUser createAppUser = appUserDao.save(existingAppUser);

                    return new AppUserViewModel(createAppUser);
                })
                .orElseThrow(() -> new AppUserNotFoundException("User with id " + appUserId + " not found"));
    }

    public void deleteAppUserById(Long appUserId) {
        AppUser existingAppUser = appUserDao.findById(appUserId)
                .orElseThrow(() -> new AppUserNotFoundException("User with id " + appUserId + " not found"));

        appUserDao.deleteById(existingAppUser.getId());
    }

}
