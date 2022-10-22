package com.tarik.usermanagementapi.appuser;

import com.tarik.usermanagementapi.exception.AppUserEmailConstraintViolationException;
import com.tarik.usermanagementapi.exception.AppUserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/app-users")
public class AppUserController {

    private final AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping
    public List<AppUserViewModel> getAppUsers() {
        return appUserService.getAppUsers();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createAppUser(@RequestBody AppUserViewModel appUser) {
        try {
            appUserService.createAppUser(appUser);
        } catch (AppUserEmailConstraintViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }

    @PutMapping("/{appUserId}")
    public AppUserViewModel updateAppUser(@PathVariable Long appUserId, @RequestBody AppUserViewModel appUser) {
        try {
            return appUserService.updateAppUser(appUserId, appUser);
        } catch (AppUserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @DeleteMapping("/{appUserId}")
    public void deleteAppUser(@PathVariable Long appUserId) {
        try {
            appUserService.deleteAppUserById(appUserId);
        } catch (AppUserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}
