package de.helpmeifyoucan.helpmeifyoucan.config;

import com.mongodb.client.MongoDatabase;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.helpmeifyoucan.helpmeifyoucan.dao.Database;

@Configuration
public class AppConfig {

 
    @Bean
    public MongoDatabase setDatabase(DatabaseConfig config) {
        return new Database(config).getDatabase();
    }
}