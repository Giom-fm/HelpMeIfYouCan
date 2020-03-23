package de.helpmeifyoucan.helpmeifyoucan.controllers.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import de.helpmeifyoucan.helpmeifyoucan.models.Address;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

public class AddressController extends AbstractEntityController<Address> {

    private final MongoCollection<Address> collection = database.getCollection("addresses", Address.class);

    public AddressController() {
        IndexOptions options = new IndexOptions();
        options.unique(true);
        collection.createIndex(Indexes.ascending("street", "zipCode", "country", "district"), options);
    }

    public void save(Address address) {
        super.save(collection, address.calculateHash());
    }

    public Address get(ObjectId id) {
        return super.getById(collection, id);
    }

    public Optional<Address> exists(Address address) {
        Bson filter = eq("hashCode", address.calculateHash().getHashCode());
        return super.exists(this.collection, filter);
    }

    public void update(ObjectId id, Address address) {
        var filter = new Document("_id", id);

        super.updateExisting(this.collection, filter, address);
    }


}