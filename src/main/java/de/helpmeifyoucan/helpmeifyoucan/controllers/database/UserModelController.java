package de.helpmeifyoucan.helpmeifyoucan.controllers.database;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import de.helpmeifyoucan.helpmeifyoucan.models.AddressModel;
import de.helpmeifyoucan.helpmeifyoucan.models.UserModel;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.UserUpdate;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

public class UserModelController extends AbstractModelController<UserModel> {

    private static AddressModelController addressModelController = new AddressModelController();

    public UserModelController() {
        super("users", UserModel.class);
        IndexOptions options = new IndexOptions();
        options.unique(true);
        super.getCollection().createIndex(Indexes.ascending("email"), options);
    }

    public UserModel save(UserModel user) {
        return super.save(user);
    }

    public UserModel get(ObjectId id) {
        return super.getById(id);
    }

    public UserModel getByEmail(String email) {
        var filter = Filters.eq("email", email);
        return super.getByFilter(filter);
    }

    public UserModel update(UserUpdate updatedFields, ObjectId userId) {
        var filter = new Document("_id", userId);

        return super.updateExistingFields(filter, updatedFields.toDocument());
    }

    public Optional<UserModel> exists(Bson filter) {
        return super.exists(filter);
    }

    public void delete(ObjectId id) {
        super.delete(Filters.eq("_id", id));
    }

    public void deleteByEmail(String email) {
        super.delete(Filters.eq("email", email));
    }


    public void handleUserAddressAddRequest(ObjectId id, AddressModel address) {
        var addressFilter = eq("hashCode", address.calculateHash().getHashCode());
        var dbAddress = addressModelController.exists(addressFilter);
        var user = this.get(id);
        if (dbAddress.isPresent()) {
            addAddressToUser(user, dbAddress.get());
        } else {
            addressModelController.save(address);
            addAddressToUser(user, address);
        }
    }

    public void handleUserAddressDeleteRequest(ObjectId userId, ObjectId addressId) {
        deleteAddressFromUser(this.get(userId), addressId);
    }

    // user address operations
    private void addAddressToUser(UserModel user, AddressModel address) {
        user.addAddress(address.getId());
        updateUserAddressField(user);
        addressModelController.addUserToAddress(address, user.getId());
    }

    public void exchangeAddress(ObjectId userId, ObjectId addressToDelete, ObjectId addressToAdd) {
        var user = this.get(userId);
        this.updateUserAddressField(user.removeAddress(addressToDelete).addAddress(addressToAdd));

    }

    public void deleteAddressFromUser(UserModel user, ObjectId address) {
        this.updateUserAddressField(user.removeAddress(address));
        addressModelController.handleUserControllerAddressDelete(address, user.getId());
    }

    private UserModel updateUserAddressField(UserModel user) {
        var updatedFields = new Document();
        updatedFields.put("addresses", user.getAddresses());
        var filter = Filters.eq("_id", user.getId());
        return this.updateExistingFields(updatedFields, filter);
    }


}
