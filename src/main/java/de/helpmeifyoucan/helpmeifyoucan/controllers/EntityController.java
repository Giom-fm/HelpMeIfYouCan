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

    public <E extends AbstractEntity> void saveUser(E entity) {

        getCollection(entity.getClassName()).insertOne(entity);
    }

    public <E extends AbstractEntity> E getEntityByIdAndClass(ObjectId id, ClassName className) {
        MongoCollection collection = getCollection(className);
        E value = (E) collection.find(eq("_id", id)).first();

        return value;

    }


    //FIXME
    private <E extends AbstractEntity> MongoCollection getCollection(ClassName entityClass) {
        MongoCollection collection = null;

        switch (entityClass) {
            case User:
                collection = database.getCollection("users", User.class);
                break;

            case Address:
                collection = database.getCollection("addresses", Address.class);
                break;

        }

        return collection;
    }


}
