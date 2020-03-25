package de.helpmeifyoucan.helpmeifyoucan.models.dtos;

import de.helpmeifyoucan.helpmeifyoucan.utils.Database;
import org.bson.BsonDocument;
import org.bson.Document;

public class UserUpdate {

    //TODO
    public BsonDocument toDocument() {
        return new Document().toBsonDocument(AddressUpdate.class, Database.pojoCodec);
    }
}
