package com.tarik.usermanagementapi.appuser;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserService {

    private final AppUserDao appUserDao;

    public AppUserService(AppUserDao appUserDao) {
        this.appUserDao = appUserDao;
    }

    public List<AppUser> getAppUsers() {
        return appUserDao.findAll();
    }

    public AppUser createAppUser(AppUser appUser) {
        return appUserDao.save(appUser);
    }
}
