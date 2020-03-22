package de.helpmeifyoucan.helpmeifyoucan.models;

import org.bson.types.ObjectId;

public abstract class AbstractEntity {

    private ObjectId id;

    public AbstractEntity setId(ObjectId id) {
        this.id = id;
        return this;
    }

    public ObjectId getId() {
        return id;
    }
}
