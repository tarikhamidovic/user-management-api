package com.tarik.usermanagementapi.controller;

import com.tarik.usermanagementapi.service.AppUserService;
import com.tarik.usermanagementapi.model.view.AppUserViewModel;
import com.tarik.usermanagementapi.model.exception.AppUserEmailConstraintViolationException;
import com.tarik.usermanagementapi.model.exception.AppUserNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/app-users")
public class AppUserController {

    private final AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping
    public Page<AppUserViewModel> getAppUsers(@RequestParam int page) {
        PageRequest pageRequest = PageRequest.of(page, 10);
        return appUserService.getAppUsers(pageRequest);
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
