package de.helpmeifyoucan.helpmeifyoucan.dao;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import de.helpmeifyoucan.helpmeifyoucan.config.DatabaseConfig;
import de.helpmeifyoucan.helpmeifyoucan.utils.HelpCategoryEnumSerializer;
import de.helpmeifyoucan.helpmeifyoucan.utils.PostStatusEnumSerializer;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Component
public class Database {

    private MongoClient client;

    public MongoDatabase database;

    @Autowired
    public Database(DatabaseConfig config) {
        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());

        CodecRegistry pojoCodec = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), CodecRegistries.fromCodecs(new PostStatusEnumSerializer(), new HelpCategoryEnumSerializer()), pojoCodecRegistry);

        var stringBuilder = new StringBuilder();
        stringBuilder.append(config.getProtocol()).append("://");

        if (config.getUser() != null && config.getPassword() != null) {
            stringBuilder.append(config.getUser()).append(":").append(config.getPassword()).append("@");
        }
        stringBuilder.append(config.getHost());
        var uri = new ConnectionString(stringBuilder.toString());
        var settings = MongoClientSettings.builder().applyConnectionString(uri).codecRegistry(pojoCodec).build();
        this.client = MongoClients.create(settings);

        this.database = this.client.getDatabase(config.getName()).withWriteConcern(WriteConcern.ACKNOWLEDGED);
    }

    public MongoDatabase getDatabase() {
        return this.database;
    }

}