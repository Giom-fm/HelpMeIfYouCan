package de.helpmeifyoucan.helpmeifyoucan.controllers.database;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import de.helpmeifyoucan.helpmeifyoucan.models.AddressModel;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.AddressUpdate;
import de.helpmeifyoucan.helpmeifyoucan.utils.ErrorMessages;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Optional;

import static com.mongodb.client.model.Updates.set;

@Service
public class AddressModelController extends AbstractModelController<AddressModel> {

    private UserModelController userModelController;

    @Autowired
    public AddressModelController(MongoDatabase dataBase) {
        super(dataBase);
        super.createCollection("address", AddressModel.class);
        this.createIndex();
    }

    private void createIndex() {
        IndexOptions options = new IndexOptions();
        options.unique(true);

        super.createIndex(Indexes.ascending("street", "zipCode", "country", "district"), options);
    }

    public AddressModel save(AddressModel address) {
        return super.save(address.calculateHash().generateId());
    }

    public AddressModel get(ObjectId id) {
        var address = super.getById(id);
        if (address == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessages.ADDRESS_NOT_FOUND);
        }
        return address;
    }

    public Optional<AddressModel> getOptional(Bson filter) {
        return super.getOptional(filter);
    }

    public AddressModel updateExistingField(Bson updatedFields, ObjectId address) {
        var filter = Filters.eq("_id", address);
        return super.updateExistingFields(filter, updatedFields);
    }

    public AddressModel updateUserField(AddressModel address) {
        Bson updatedFields = set("users", address.getUsers());

        return this.updateExistingField(updatedFields, address.getId());
    }

    public void handleUserControllerAddressDelete(ObjectId addressId, ObjectId userId) {
        this.deleteUserFromAddress(this.get(addressId), userId);
    }

    public AddressModel addUserToAddress(AddressModel address, ObjectId id) {
        return this.updateUserField(address.addUserAddress(id));
    }

    public AddressModel deleteUserFromAddress(AddressModel address, ObjectId id) {
        return this.updateUserField(address.removeUserAddress(id));
    }

    public AddressModel updateAddress(ObjectId addressId, AddressUpdate addressUpdate, ObjectId userId) throws Exception {
        var address = this.getOptional(Filters.eq("_id", addressId));

        if (address.isEmpty()) {
            throw new Exception();
        }
        var existingAddress = address.get();
        if (existingAddress.noUserReferences()
                || (existingAddress.getUsers().size() == 1 && existingAddress.getUsers().contains(userId))) {
            var fields = addressUpdate.toFilter();
            return this.updateExistingField(fields, addressId);
        } else {
            this.deleteUserFromAddress(existingAddress, userId);
            var newAddress = existingAddress.mergeWithAddressUpdate(addressUpdate).setUsers(Collections.singletonList(userId));
            userModelController.exchangeAddress(userId, existingAddress.getId(), newAddress.getId());

            var filter = Filters.eq("hashCode", newAddress.getHashCode());
            var addressExists = this.getOptional(filter);

            if (addressExists.isEmpty()) {
                return this.save(newAddress);
            } else {
                return addUserToAddress(addressExists.get(), userId);
            }
        }
    }


    public AddressModel delete(ObjectId id) {
        var filter = Filters.eq("_id", id);
        var addressToBeDeleted = super.getById(id);
        if (addressToBeDeleted.getUsers().isEmpty()) {
            super.delete(filter);
            return addressToBeDeleted;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessages.ADDRESS_NOT_FOUND);
    }

    @Autowired
    public void setUserModelController(UserModelController userModelController) {
        this.userModelController = userModelController;
    }

}