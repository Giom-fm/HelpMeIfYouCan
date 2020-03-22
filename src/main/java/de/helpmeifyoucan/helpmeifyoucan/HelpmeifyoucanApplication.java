package de.helpmeifyoucan.helpmeifyoucan;


import de.helpmeifyoucan.helpmeifyoucan.controllers.EntityController;
import de.helpmeifyoucan.helpmeifyoucan.models.Address;
import de.helpmeifyoucan.helpmeifyoucan.models.User;
import de.helpmeifyoucan.helpmeifyoucan.utils.ClassName;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static java.util.Collections.singletonList;

@SpringBootApplication
public class HelpmeifyoucanApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelpmeifyoucanApplication.class, args);
		EntityController connect = new EntityController();
		var testUser = new User().setName("Peter").setAddresses(singletonList(new Address().setZipCode(22391)));

		var testAddress = new Address().setZipCode(22391);

		connect.saveUser(testUser);
		connect.saveUser(testAddress);

		connect.getEntityByIdAndClass(testUser.getId(), ClassName.User);
	}

}
