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
		};
	}
}
