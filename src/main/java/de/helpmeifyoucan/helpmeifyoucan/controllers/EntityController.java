package de.helpmeifyoucan.helpmeifyoucan.controllers;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import de.helpmeifyoucan.helpmeifyoucan.models.AbstractEntity;
import de.helpmeifyoucan.helpmeifyoucan.models.Address;
import de.helpmeifyoucan.helpmeifyoucan.models.User;
import de.helpmeifyoucan.helpmeifyoucan.utils.ClassName;
import de.helpmeifyoucan.helpmeifyoucan.utils.Database;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.eq;

public class EntityController {

    MongoDatabase database = Database.getDatabase();


    public <E extends AbstractEntity> void saveEntity(E entity) {
        getCollection(entity.getClassName()).insertOne(entity);
    }

    public <E extends AbstractEntity> E getEntityByIdAndClass(ObjectId id, ClassName className) {
        MongoCollection<E> collection = getCollection(className);
        return collection.find(eq("_id", id)).first();

    }


    //FIXME
    private <E extends AbstractEntity> MongoCollection<E> getCollection(ClassName className) {


        MongoCollection<? extends AbstractEntity> collection = null;

        switch (className) {
            case User:
                collection = database.getCollection("users", User.class);
                break;
            case Address:
                collection = database.getCollection("addresses", Address.class);
                break;
        }
        return (MongoCollection<E>) collection;

    }
}


