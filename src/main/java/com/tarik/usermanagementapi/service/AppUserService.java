package com.tarik.usermanagementapi.service;

import com.tarik.usermanagementapi.model.entity.AppUser;
import com.tarik.usermanagementapi.dao.AppUserDao;
import com.tarik.usermanagementapi.model.view.AppUserViewModel;
import com.tarik.usermanagementapi.model.exception.AppUserNotFoundException;
import com.tarik.usermanagementapi.model.exception.AppUserEmailConstraintViolationException;
import com.tarik.usermanagementapi.model.entity.Permission;
import com.tarik.usermanagementapi.dao.PermissionDao;
import com.tarik.usermanagementapi.model.enums.Status;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    public Page<AppUserViewModel> getAppUsers(PageRequest pageRequest) {
        return appUserDao.findAll(pageRequest)
                .map(AppUserViewModel::new);
    }

    public Page<AppUserViewModel> getAppUsersFilteringFirstName(String firstName, PageRequest pageRequest) {
        return appUserDao.findByFirstNameContaining(firstName, pageRequest)
                .map(AppUserViewModel::new);
    }

    public Page<AppUserViewModel> getAppUsersFilteringLastName(String lastName, PageRequest pageRequest) {
        return appUserDao.findByLastNameContaining(lastName, pageRequest)
                .map(AppUserViewModel::new);
    }

    public Page<AppUserViewModel> getAppUsersFilteringEmail(String email, PageRequest pageRequest) {
        return appUserDao.findByEmailContaining(email, pageRequest)
                .map(AppUserViewModel::new);
    }

    public AppUserViewModel getAppUserById(Long appUserId) {
        return appUserDao.findById(appUserId)
                .map(AppUserViewModel::new)
                .orElseThrow(() -> new AppUserNotFoundException("User with id " + appUserId + " not found"));
    }

    public void createAppUser(AppUserViewModel appUser) {
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

        appUserDao.save(appUserWithEncodedPasswordAndDefaultStatus);
    }

    public AppUserViewModel updateAppUser(Long appUserId, AppUserViewModel appUser) {
        List<Permission> permissions = permissionDao
                .getPermissionsByPermissionNames(appUser.permissions());

        return appUserDao.findById(appUserId)
                .map(existingAppUser -> {
                    existingAppUser.setFirstName(appUser.firstName());
                    existingAppUser.setLastName(appUser.lastName());
                    existingAppUser.setEmail(appUser.email());
                    existingAppUser.setStatus(appUser.status());
                    existingAppUser.setPermissions(permissions);
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
