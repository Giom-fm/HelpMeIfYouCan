package de.helpmeifyoucan.helpmeifyoucan.controllers.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import de.helpmeifyoucan.helpmeifyoucan.models.AbstractEntity;
import de.helpmeifyoucan.helpmeifyoucan.utils.Database;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.Optional;

public abstract class AbstractModelController<T extends AbstractEntity> {

    private final MongoDatabase database = Database.getDatabase();
    private MongoCollection<T> collection;

    public AbstractModelController(String collectionName, Class<T> collectionClass) {
        collection = database.getCollection(collectionName, collectionClass);

    }

    protected void save(T entity) {
        collection.insertOne(entity);
    }

    protected T getById(ObjectId id) {
        var filter = new Document("_id", id);
        return collection.find(filter).first();

    }

    protected void delete(Bson filter) {

        collection.deleteOne(filter);
    }

    protected T getByFilter(Bson filter) {
        return collection.find(filter).first();
    }

    protected void updateExisting(Document filter, T entity) {
        collection.findOneAndReplace(filter, entity);
    }

    protected Optional<T> exists(Bson filter) {
        return Optional.ofNullable(getByFilter(filter));
    }

    protected MongoCollection<T> getCollection() {
        return this.collection;
    }
}
