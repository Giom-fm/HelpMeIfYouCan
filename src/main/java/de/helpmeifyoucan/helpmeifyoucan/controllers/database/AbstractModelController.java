package de.helpmeifyoucan.helpmeifyoucan.controllers.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import de.helpmeifyoucan.helpmeifyoucan.models.AbstractEntity;
import de.helpmeifyoucan.helpmeifyoucan.utils.Database;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.Optional;

public abstract class AbstractModelController<T extends AbstractEntity> {

    private final MongoDatabase database = Database.getDatabase();
    private MongoCollection<T> collection;

    public AbstractModelController(String collectionName, Class<T> collectionClass) {
        this.collection = database.getCollection(collectionName, collectionClass);
    }

    // TODO INSERTMANY
    protected T save(T entity) {
        this.collection.insertOne(entity);
        return entity;
    }

    protected T getById(ObjectId id) {
        var filter = Filters.eq("_id", id);
        return collection.find(filter).first();

    }

    protected boolean delete(Bson filter) {
        return this.collection.deleteOne(filter).getDeletedCount() != 0;

    }

    protected T getByFilter(Bson filter) {
        return this.collection.find(filter).first();
    }

    protected T replaceExisting(Bson filter, T entity) {
        var findRepOptions = new FindOneAndReplaceOptions();
        findRepOptions.returnDocument(ReturnDocument.AFTER);
        return this.collection.findOneAndReplace(filter, entity, findRepOptions);
    }

    protected T updateExistingFields(Bson filter, Bson fieldsToUpdate) {
        var updateOptions = new FindOneAndUpdateOptions();
        updateOptions.returnDocument(ReturnDocument.AFTER).upsert(false);
        return this.collection.findOneAndUpdate(filter, fieldsToUpdate, updateOptions);
    }

    protected Optional<T> exists(Bson filter) {
        return Optional.ofNullable(getByFilter(filter));
    }

    protected MongoCollection<T> getCollection() {
        return this.collection;
    }
}
