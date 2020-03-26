package de.helpmeifyoucan.helpmeifyoucan.utils;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import de.helpmeifyoucan.helpmeifyoucan.config.Config;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class Database {

    private MongoClient client;

    public static CodecRegistry pojoCodec;

    public MongoDatabase database;


    public Database() {
        setDatabase();
    }


    private void setDatabase() {

        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        pojoCodec = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                pojoCodecRegistry);
        String uri = String.format("%s://%s:%s@%s", Config.DATABASE_PROTOCOL, Config.DATABASE_USER,
                Config.DATABASE_PASSWORD, Config.DATABASE_HOST);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(uri)).codecRegistry(pojoCodec).build();
        this.client = MongoClients.create(settings);

        this.database = client.getDatabase(Config.DATABASE_NAME);
    }

    public MongoDatabase getDatabase() {
        return this.database;
    }

}