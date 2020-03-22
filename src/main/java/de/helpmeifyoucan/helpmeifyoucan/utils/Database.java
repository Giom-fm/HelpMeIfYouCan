package de.helpmeifyoucan.helpmeifyoucan.utils;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import de.helpmeifyoucan.helpmeifyoucan.Config;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class Database {

    private static MongoClient client = null;

    private Database() {
    }

    public static MongoDatabase getDatabase() {
        if (Database.client == null) {

            CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());

            CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                    pojoCodecRegistry);

            String uri = String.format("mongodb+srv://%s:%s@%s", Config.DATABASE_USER, Config.DATABASE_PASSWORD,
                    Config.DATABASE_ADDRESS);

            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(new ConnectionString(uri)).codecRegistry(codecRegistry).build();

            Database.client = MongoClients.create(settings);
        }
        return Database.client.getDatabase(Config.DATABASE_NAME);
    }
}