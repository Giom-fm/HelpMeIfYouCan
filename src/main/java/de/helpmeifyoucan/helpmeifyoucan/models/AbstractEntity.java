package de.helpmeifyoucan.helpmeifyoucan.models;

import org.bson.types.ObjectId;

public abstract class AbstractEntity {

    private ObjectId id;

    public ObjectId setId(ObjectId id) {
        this.id = id;
        return this.id;
    }

    public ObjectId getId() {
        return id;
    }

    public abstract AbstractEntity generateId();
}
