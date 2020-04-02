package de.helpmeifyoucan.helpmeifyoucan.services;

import com.mongodb.WriteConcern;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.*;
import com.mongodb.client.result.DeleteResult;
import de.helpmeifyoucan.helpmeifyoucan.models.AbstractEntity;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

@Repository
public abstract class AbstractService<T extends AbstractEntity> {

    private MongoDatabase database;
    private MongoCollection<T> collection;


    @Autowired
    public AbstractService(MongoDatabase database) {
        this.database = database;
    }

    protected T save(T entity) {
        this.collection.insertOne(entity);
        return entity;
    }


    protected List<T> getAllByFilter(Bson filter) {
        return this.collection.find(filter).into(new LinkedList<>());

    }

    protected T getById(ObjectId id) {
        var filter = Filters.eq("_id", id);
        return collection.find(filter).first();

    }

    protected DeleteResult delete(Bson filter) {

        return this.collection.deleteOne(filter);
    }

    public boolean deleteById(ObjectId id) {
        var filter = eq("_id", id);

        return this.delete(filter).wasAcknowledged();
    }

    protected boolean exists(Bson filter) {
        return this.getOptional(filter).isPresent();
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

    public Optional<T> getOptional(Bson filter) {
        return Optional.ofNullable(getByFilter(filter));
    }

    protected void createCollection(String collectionName, Class<T> collectionClass) {
        this.collection = database.getCollection(collectionName, collectionClass).withWriteConcern(WriteConcern.W1);
        System.out.println(collection.getWriteConcern());
    }

    protected void createIndex(Bson indexes, IndexOptions options) {
        this.collection.createIndex(indexes, options);
    }

    public MongoCollection<T> getCollection() {
        return this.collection;
    }
}
