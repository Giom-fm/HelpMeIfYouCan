package de.helpmeifyoucan.helpmeifyoucan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



import de.helpmeifyoucan.helpmeifyoucan.utils.Database;

@SpringBootApplication
public class HelpmeifyoucanApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelpmeifyoucanApplication.class, args);
		var test = Database.getDatabase();
		System.out.println(test.getName());
	}

}
