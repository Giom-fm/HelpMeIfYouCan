package de.helpmeifyoucan.helpmeifyoucan.utils;


import com.fasterxml.jackson.databind.util.StdConverter;
import org.bson.types.ObjectId;

public class ObjectIdMapping extends StdConverter<ObjectId, String> {


    @Override
    public String convert(ObjectId objectId) {
        return objectId.toString();
    }
}

