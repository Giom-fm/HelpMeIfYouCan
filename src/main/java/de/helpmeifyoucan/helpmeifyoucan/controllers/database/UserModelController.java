package de.helpmeifyoucan.helpmeifyoucan.controllers.database;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import de.helpmeifyoucan.helpmeifyoucan.models.AddressModel;
import de.helpmeifyoucan.helpmeifyoucan.models.UserModel;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.UserUpdate;
import de.helpmeifyoucan.helpmeifyoucan.utils.ErrorMessages;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

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

    public UserModel save(UserModel user) {
        return super.save(user);
    }

    public UserModel get(ObjectId id) {
        var user = super.getById(id);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessages.USER_NOT_FOUND);
        }
        return user;
    }

    public UserModel getByEmail(String email) {
        var filter = Filters.eq("email", email);
        var user = super.getByFilter(filter);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessages.USER_NOT_FOUND);
        }
        return user;
    }

    public UserModel update(UserUpdate updatedFields, ObjectId id) {
        var filter = Filters.eq("_id", id);
        return super.updateExistingFields(filter, updatedFields.toDocument());
    }

    public Optional<UserModel> exists(Bson filter) {
        return super.exists(filter);
    }

    public void delete(ObjectId id) {
        if (!super.delete(Filters.eq("_id", id))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessages.USER_NOT_FOUND);
        }
    }

    public void handleUserAddressAddRequest(ObjectId id, AddressModel address) {
        var addressFilter = eq("hashCode", address.calculateHash().getHashCode());
        var dbAddress = addressModelController.exists(addressFilter);
        var user = this.get(id);
        if (dbAddress.isPresent()) {
            this.addAddressToUser(user, dbAddress.get());
        } else {
            this.addressModelController.save(address);
            this.addAddressToUser(user, address);
        }
    }

    public void handleUserAddressDeleteRequest(ObjectId userId, ObjectId addressId) {
        this.deleteAddressFromUser(this.get(userId), addressId);
    }

    // user address operations
    private void addAddressToUser(UserModel user, AddressModel address) {
        user.addAddress(address.getId());
        this.updateUserAddressField(user);
        this.addressModelController.addUserToAddress(address, user.getId());
    }

    public void exchangeAddress(ObjectId userId, ObjectId addressToDelete, ObjectId addressToAdd) {
        var user = this.get(userId);
        this.updateUserAddressField(user.removeAddress(addressToDelete).addAddress(addressToAdd));
    }

    public void deleteAddressFromUser(UserModel user, ObjectId address) {
        this.updateUserAddressField(user.removeAddress(address));
        this.addressModelController.handleUserControllerAddressDelete(address, user.getId());
    }

    private UserModel updateUserAddressField(UserModel user) {
        var updatedFields = new Document();
        updatedFields.put("addresses", user.getAddresses());
        var filter = Filters.eq("_id", user.getId());
        return this.updateExistingFields(updatedFields, filter);
    }
}
