package de.helpmeifyoucan.helpmeifyoucan.controllers.database;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import de.helpmeifyoucan.helpmeifyoucan.models.AddressModel;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.AddressUpdate;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.Collections;
import java.util.Optional;

public class AddressModelController extends AbstractModelController<AddressModel> {

    private final UserModelController userModelController = new UserModelController();

    public AddressModelController() {
        super("address", AddressModel.class);
        IndexOptions options = new IndexOptions();
        options.unique(true);
        super.getCollection().createIndex(Indexes.ascending("street", "zipCode", "country", "district"), options);
    }

    public AddressModel save(AddressModel address) {
        return super.save(address.calculateHash().generateId());
    }

    public AddressModel get(ObjectId id) {
        return super.getById(id);
    }

    public Optional<AddressModel> exists(Bson filter) {

        return super.exists(filter);
    }

    public AddressModel updateExistingField(Bson updatedFields, ObjectId address) {
        var filter = new Document("_id", address);

        return super.updateExistingFields(filter, updatedFields);
    }

    public AddressModel updateUserField(AddressModel address) {
        var updatedFields = new Document();
        updatedFields.put("users", address.getUsers());
        return this.updateExistingField(updatedFields, address.getId());
    }

    public void handleUserControllerAddressDelete(ObjectId addressId, ObjectId userId) {
        deleteUserFromAddress(this.get(addressId), userId);
    }

    public AddressModel addUserToAddress(AddressModel address, ObjectId id) {
        return this.updateUserField(address.addUserAddress(id));
    }

    public AddressModel deleteUserFromAddress(AddressModel address, ObjectId id) {
        return this.updateUserField(address.removeUserAddress(id));
    }

    public AddressModel updateAddress(ObjectId addressId, AddressUpdate addressUpdate, ObjectId userId) throws Exception {
        var address = this.exists(Filters.eq("_id", addressId));

        if (address.isEmpty()) {
            throw new Exception();
        }
        var existingAddress = address.get();
        if (existingAddress.noUserReferences() || (existingAddress.getUsers().size() == 1 && existingAddress.getUsers().contains(userId))) {


            return this.updateExistingField(addressUpdate.toDocument(), addressId);
        } else {
            this.deleteUserFromAddress(existingAddress, userId);
            var newAddress = existingAddress.mergeWithAddressUpdate(addressUpdate).setUsers(Collections.singletonList(userId));
            userModelController.exchangeAddress(userId, existingAddress.getId(), newAddress.getId());

            var filter = Filters.eq("hashCode", newAddress.getHashCode());
            var addressExists = this.exists(filter);

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
        }
        return null;
    }

}