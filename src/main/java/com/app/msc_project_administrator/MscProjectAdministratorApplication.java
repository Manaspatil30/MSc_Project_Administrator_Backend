package com.app.msc_project_administrator;

import com.app.msc_project_administrator.auth.AuthenticationService;
import com.app.msc_project_administrator.auth.RegisterRequest;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static com.app.msc_project_administrator.user.Role.*;

@SpringBootApplication
public class MscProjectAdministratorApplication {

	public static void main(String[] args) {
		SpringApplication.run(MscProjectAdministratorApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(
			AuthenticationService service
	) {
		return args -> {
//			var admin = RegisterRequest.builder()
//					.firstname("Admin")
//					.lastname("Admin")
//					.email("admin@mail.com")
//					.password("password")
//					.role(MOD_OWNER)
//					.build();
//			System.out.println("Admin token: " + service.register(admin).getAccessToken());
//
//			var manager = RegisterRequest.builder()
//					.firstname("Student")
//					.lastname("Student")
//					.email("student@mail.com")
//					.password("password")
//					.role(STUDENT)
//					.build();
//			System.out.println("Student token: " + service.register(manager).getAccessToken());
//
//			var user = RegisterRequest.builder()
//					.firstname("Supervisor")
//					.lastname("Supervisor")
//					.email("super@mail.com")
//					.password("password")
//					.role(ACADEMIC)
//					.build();
//			System.out.println("Supervisor token: " + service.register(user).getAccessToken());

		};
	}
}
