package de.helpmeifyoucan.helpmeifyoucan.controllers.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import de.helpmeifyoucan.helpmeifyoucan.models.AbstractEntity;
import de.helpmeifyoucan.helpmeifyoucan.utils.Database;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.Optional;

public abstract class AbstractEntityController<T extends AbstractEntity> {

    static final MongoDatabase database = Database.getDatabase();

    protected void save(MongoCollection<T> collection, T entity) {
        collection.insertOne(entity);
    }

    protected T getById(MongoCollection<T> collection, ObjectId id) {
        var filter = new Document("_id", id);
        return collection.find(filter).first();
    }

    protected void delete(MongoCollection<T> collection, ObjectId id) {
        var filter = Filters.eq("_id", id);
        collection.deleteOne(filter);
    }

    protected T getByFilter(MongoCollection<T> collection, Bson filter) {
        return collection.find(filter).first();
    }

    protected void updateExisting(MongoCollection<T> collection, Document filter, T entity) {
        collection.findOneAndReplace(filter, entity);
    }

    protected Optional<T> exists(MongoCollection<T> collection, Bson filter) {
        return Optional.ofNullable(getByFilter(collection, filter));
    }
}
