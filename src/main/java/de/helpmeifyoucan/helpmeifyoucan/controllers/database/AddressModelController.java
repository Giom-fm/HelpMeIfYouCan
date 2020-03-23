package de.helpmeifyoucan.helpmeifyoucan.controllers.database;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import de.helpmeifyoucan.helpmeifyoucan.models.AddressModel;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.Optional;

public class AddressModelController extends AbstractModelController<AddressModel> {


    public AddressModelController() {
        super("address", AddressModel.class);
        IndexOptions options = new IndexOptions();
        options.unique(true);
        super.getCollection().createIndex(Indexes.ascending("street", "zipCode", "country", "district"), options);
    }

    public void save(AddressModel address) {
        super.save(address.calculateHash());
    }

    public AddressModel get(ObjectId id) {
        return super.getById(id);
    }

    public Optional<AddressModel> exists(Bson filter) {

        return super.exists(filter);
    }

    public AddressModel update(ObjectId id, AddressModel address) {
        var filter = new Document("_id", id);
        return super.updateExisting(filter, address);
    }

    public void delete(ObjectId id) {
        var filter = Filters.eq("_id", id);
        super.delete(filter);
    }

}