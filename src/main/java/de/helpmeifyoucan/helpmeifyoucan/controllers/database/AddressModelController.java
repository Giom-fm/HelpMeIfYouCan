package de.helpmeifyoucan.helpmeifyoucan.controllers.database;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import de.helpmeifyoucan.helpmeifyoucan.models.Address;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.Optional;

public class AddressModelController extends AbstractModelController<Address> {


    public AddressModelController() {
        super("address", Address.class);
        IndexOptions options = new IndexOptions();
        options.unique(true);
        super.getCollection().createIndex(Indexes.ascending("street", "zipCode", "country", "district"), options);
    }

    public void save(Address address) {
        super.save(address.calculateHash());
    }

    public Address get(ObjectId id) {
        return super.getById(id);
    }

    public Optional<Address> exists(Bson filter) {

        return super.exists(filter);
    }

    public void update(ObjectId id, Address address) {
        var filter = new Document("_id", id);
        super.updateExisting(filter, address);
    }

    public void delete(ObjectId id) {
        var filter = Filters.eq("_id", id);
        super.delete(filter);
    }

}