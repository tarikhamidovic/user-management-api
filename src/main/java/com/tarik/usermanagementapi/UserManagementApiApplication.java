package com.tarik.usermanagementapi;

import com.tarik.usermanagementapi.permission.Permission;
import com.tarik.usermanagementapi.permission.PermissionDao;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class UserManagementApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserManagementApiApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(PermissionDao permissionDao) {
		return args -> {
			Permission code = Permission.builder()
					.permissionName("Code")
					.build();
			Permission description = Permission.builder()
					.permissionName("Description")
					.build();

			permissionDao.saveAll(List.of(code, description));
		};
	}
}
