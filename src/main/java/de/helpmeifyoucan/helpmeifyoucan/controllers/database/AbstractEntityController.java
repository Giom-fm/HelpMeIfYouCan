package de.helpmeifyoucan.helpmeifyoucan.controllers.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import de.helpmeifyoucan.helpmeifyoucan.models.AbstractEntity;
import de.helpmeifyoucan.helpmeifyoucan.utils.Database;
import org.bson.types.ObjectId;
import com.mongodb.client.model.Filters;

public abstract class AbstractEntityController<T extends AbstractEntity> {

    private final MongoDatabase database = Database.getDatabase();

    public void save(T entity) {
        var collection = this.getCollection(entity.getClass());
        collection.insertOne(entity);
    }

    protected T get(ObjectId id, Class<T> clazz) {
        var collection = this.getCollection(clazz);
        return collection.find(Filters.eq("_id", id), clazz).first();
    }

    //FIXME 
    private MongoCollection<T> getCollection(Class<? extends AbstractEntity> clazz) {
        return (MongoCollection<T>) this.database.getCollection(clazz.getSimpleName(), clazz);
    }

}
