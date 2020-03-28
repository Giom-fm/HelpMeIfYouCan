package de.helpmeifyoucan.helpmeifyoucan;

import com.mongodb.client.MongoDatabase;
import de.helpmeifyoucan.helpmeifyoucan.utils.Database;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.zalando.logbook.Logbook;

@SpringBootApplication
public class HelpmeifyoucanApplication extends SpringBootServletInitializer {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public static MongoDatabase setDatabase() {
        return new Database().getDatabase();
    }

    @Bean
    public static Logbook logBook() {
        return Logbook.create();
    }

    public static void main(String[] args) {
        SpringApplication.run(HelpmeifyoucanApplication.class, args);
    }

}
