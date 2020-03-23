package de.helpmeifyoucan.helpmeifyoucan;

import de.helpmeifyoucan.helpmeifyoucan.controllers.database.UserModelController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class HelpmeifyoucanApplication extends SpringBootServletInitializer {

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public UserModelController userController() {
		return new UserModelController();
	}

	public static void main(String[] args) {
		SpringApplication.run(HelpmeifyoucanApplication.class, args);

	}

}
