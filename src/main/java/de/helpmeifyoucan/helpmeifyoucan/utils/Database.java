package de.helpmeifyoucan.helpmeifyoucan.utils;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import de.helpmeifyoucan.helpmeifyoucan.Config;

public class Database {

    private static MongoClient client = null;

    private Database() {
    }

    public static MongoDatabase getDatabase() {
        if (Database.client == null) {
            String uri = String.format("mongodb+srv://%s:%s@%s", Config.DATABASE_USER, Config.DATABASE_PASSWORD,
                    Config.DATABASE_ADDRESS);
            Database.client = MongoClients.create(uri);
        }
        return Database.client.getDatabase(Config.DATABASE_NAME);
    }
}