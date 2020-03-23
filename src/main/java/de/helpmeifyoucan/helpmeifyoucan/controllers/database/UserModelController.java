package de.helpmeifyoucan.helpmeifyoucan.controllers.database;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import de.helpmeifyoucan.helpmeifyoucan.models.AddressModel;
import de.helpmeifyoucan.helpmeifyoucan.models.UserModel;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

public class UserModelController extends AbstractModelController<UserModel> {


    private final AddressModelController addressModelController = new AddressModelController();

    public UserModelController() {
        super("users", UserModel.class);
        IndexOptions options = new IndexOptions();
        options.unique(true);
        super.getCollection().createIndex(Indexes.ascending("email"), options);
    }

    public void save(UserModel user) {
        super.save(user);
    }

    public UserModel get(ObjectId id) {
        return super.getById(id);
    }

    public UserModel getByEmail(String email) {
        var filter = Filters.eq("email", email);
        return super.getByFilter(filter);
    }

    public UserModel update(ObjectId id, UserModel user) {
        var filter = new Document("_id", id);
        return super.updateExisting(filter, user);
    }

    public Optional<UserModel> exists(Bson filter) {
        return super.exists(filter);
    }

    public void delete(ObjectId id) {
        super.delete(Filters.eq("_id", id));
    }

    private void addAddress(ObjectId userId, ObjectId addressId) {
        var user = super.getById(userId);
        var addresses = getAddresses(user);
        addresses.add(addressId);
        this.update(user.getId(), user.setAddresses(addresses));
    }

    public void handleUserAddressAdd(ObjectId id, AddressModel address) {
        var addressFilter = eq("hashCode", address.calculateHash().getHashCode());
        var dbAddress = addressModelController.exists(addressFilter);
        if (dbAddress.isPresent()) {
            addAddress(id, dbAddress.get().getId());
        } else {
            addressModelController.save(address);
            addAddress(id, address.getId());
        }
    }

    public void deleteUserAddress(ObjectId userId, AddressModel address) {
        var user = super.getById(userId);
        var addresses = getAddresses(user);
        addresses.remove(address.getId());
        this.update(user.getId(), user.setAddresses(addresses));

    }

    public List<ObjectId> getAddresses(UserModel user) {
        return new ArrayList<>(user.getAddresses());

    }

}
