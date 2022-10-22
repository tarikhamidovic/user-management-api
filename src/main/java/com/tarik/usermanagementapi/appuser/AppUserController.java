package com.tarik.usermanagementapi.appuser;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/app-users")
public class AppUserController {

    private final AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping
    public List<AppUser> getAppUsers() {
        return appUserService.getAppUsers();
    }

    @PostMapping
    public AppUser createAppUser(@RequestBody AppUser appUser) {
        return appUserService.createAppUser(appUser);
    }
}
