package com.tarik.usermanagementapi;

import com.github.javafaker.Faker;
import com.tarik.usermanagementapi.model.entity.AppUser;
import com.tarik.usermanagementapi.dao.AppUserDao;
import com.tarik.usermanagementapi.model.entity.Permission;
import com.tarik.usermanagementapi.dao.PermissionDao;
import com.tarik.usermanagementapi.model.enums.Status;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

@SpringBootApplication
public class UserManagementApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserManagementApiApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(PermissionDao permissionDao, AppUserDao appUserDao) {
        return args -> {
            Permission code = Permission.builder()
                    .permissionName("Code")
                    .build();
            Permission description = Permission.builder()
                    .permissionName("Description")
                    .build();

            Permission savedCode = permissionDao.save(code);
            Permission savedDescription = permissionDao.save(description);

            Faker faker = new Faker();

            Function<Integer, List<Permission>> getRandomPermissionsList = (Integer i) -> switch (i % 4) {
                case 0 -> List.of(savedCode);
                case 1 -> List.of(savedDescription);
                case 2 -> List.of(savedCode, savedDescription);
                default -> Collections.emptyList();
            };

            List<AppUser> appUsers = new ArrayList<>();
            for (int i = 0; i < 35; i++) {
                appUsers.add(
                        AppUser.builder()
                                .firstName(faker.name().firstName())
                                .lastName(faker.name().lastName())
                                .email(faker.internet().emailAddress())
                                .username(faker.name().username())
                                .password(faker.internet().password())
                                .status(i % 4 == 0 ? Status.INACTIVE : Status.ACTIVE)
                                .permissions(getRandomPermissionsList.apply(i))
                                .build()
                );
            }
            appUserDao.saveAll(appUsers);
        };
    }
}
