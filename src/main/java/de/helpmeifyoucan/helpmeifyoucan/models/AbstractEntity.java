package de.helpmeifyoucan.helpmeifyoucan.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.helpmeifyoucan.helpmeifyoucan.utils.ObjectIdMapping;
import org.bson.types.ObjectId;

public abstract class AbstractEntity {
    @JsonSerialize(converter = ObjectIdMapping.class)
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
