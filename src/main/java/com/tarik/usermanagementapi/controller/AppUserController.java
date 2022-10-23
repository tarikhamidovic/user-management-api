package com.tarik.usermanagementapi.controller;

import com.tarik.usermanagementapi.service.AppUserService;
import com.tarik.usermanagementapi.model.view.AppUserViewModel;
import com.tarik.usermanagementapi.model.exception.AppUserEmailConstraintViolationException;
import com.tarik.usermanagementapi.model.exception.AppUserNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    public Page<AppUserViewModel> getAppUsers(
            @RequestParam int page,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email
    ) {
        PageRequest pageRequest =
                sortBy == null ? PageRequest.of(page, 10) : PageRequest.of(page, 10, Sort.by(sortBy));

        if (firstName != null && !firstName.isBlank()) {
            return appUserService.getAppUsersFilteringFirstName(firstName, pageRequest);
        }
        if (lastName != null && !lastName.isBlank()) {
            return appUserService.getAppUsersFilteringLastName(lastName, pageRequest);
        }
        if (email != null && !email.isBlank()) {
            return appUserService.getAppUsersFilteringEmail(email, pageRequest);
        }

        return appUserService.getAppUsers(pageRequest);
    }

    @GetMapping("/{appUserId}")
    public AppUserViewModel getAppUserById(@PathVariable Long appUserId) {
        try {
            return appUserService.getAppUserById(appUserId);
        } catch (AppUserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
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
