package de.helpmeifyoucan.helpmeifyoucan.models;

import de.helpmeifyoucan.helpmeifyoucan.utils.ClassName;
import org.bson.types.ObjectId;

public abstract class AbstractEntity {

    private ObjectId id;

    public AbstractEntity setId(ObjectId id)
    {
        this.id = id;
        return this;
    }

    public ObjectId getId()
    {
        return id;
    }

    public abstract ClassName getClassName();

}
