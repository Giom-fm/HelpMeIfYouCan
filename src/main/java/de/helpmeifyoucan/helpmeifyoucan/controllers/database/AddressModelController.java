package de.helpmeifyoucan.helpmeifyoucan.controllers.database;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import de.helpmeifyoucan.helpmeifyoucan.models.AddressModel;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.AddressUpdate;
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

    /**
     * When changes to User references occur, wen want the db model to change accodingly, we do so by
     * update the objects useres field in the db
     *
     * @param address the address to update
     * @return the updated Address
     */

    public AddressModel updateUserField(AddressModel address) {
        Bson updatedFields = set("users", address.getUsers());

        return this.updateExistingField(updatedFields, address.getId());
    }

    /**
     * UserController calls this Method to process AddressModel after it deleted a User from its references
     *
     * @param addressId the edited Address
     * @param userId    the User who held the address
     */
    public void handleUserControllerAddressDelete(ObjectId addressId, ObjectId userId) {
        try {
            this.deleteUserFromAddress(this.get(addressId), userId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessages.ADDRESS_NOT_FOUND);
        }
    }


    /**
     * We want to add a user to addresses user references and do so by updating the addresses users field
     *
     * @param address the address to update
     * @param id      Users id to add
     * @return the updated address
     */
    public AddressModel addUserToAddress(AddressModel address, ObjectId id) {
        return this.updateUserField(address.addUserAddress(id));
    }

    /**
     * We want to delete a User from a Addresses references and insert it back into the db. We do not want
     * to insert Address with no referneces back into the db, so we check for it
     *
     * @param address Address to modify /delete
     * @param userId  user To delete from Address
     */

    //FIXME THIS WILL NOT BE VALID WITH ADDED REFERENCES
    public void deleteUserFromAddress(AddressModel address, ObjectId userId) {

        var addressWithUserRemoved = address.removeUserAddress(userId);
        if (addressWithUserRemoved.noUserReferences()) {
            this.delete(addressWithUserRemoved.getId());
        } else {
            this.updateUserField(addressWithUserRemoved);
        }
    }


    /**
     * When updating a address we need to take care of the following cases:
     * the address does not exist: so we throw an exception
     * The address has no other users than the requesting user referencing to it: we can update with the given update
     * The address has other users referring to it: we need to delete the requesting user from the addresses userreferences,
     * create a new address, check if its hashcode already and add the requesting user to its or the matching address user references
     *
     * @param addressId     the Address to update
     * @param addressUpdate the update changes
     * @param userId        the user who requested the update
     * @return the new or updated address
     * @throws Exception address not found
     */
    public AddressModel updateAddress(ObjectId addressId, AddressUpdate addressUpdate, ObjectId userId) {
        var address = this.getOptional(Filters.eq("_id", addressId));

        if (address.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessages.ADDRESS_NOT_FOUND);
        }
        var existingAddress = address.get();
        if (existingAddress.noUserReferences()
                || (existingAddress.getUsers().size() == 1 && existingAddress.getUsers().contains(userId))) {
            var fields = addressUpdate.toFilter();
            return this.updateExistingField(fields, addressId);
        } else {
            this.deleteUserFromAddress(existingAddress, userId);
            var newAddress = existingAddress.mergeWithAddressUpdate(addressUpdate);

            var filter = Filters.eq("hashCode", newAddress.getHashCode());
            var addressExists = this.getOptional(filter);

            if (addressExists.isEmpty()) {
                userModelController.exchangeAddress(userId, existingAddress.getId(), newAddress.setUsers(Collections.singletonList(userId)).getId());
                return this.save(newAddress);
            } else {
                userModelController.exchangeAddress(userId, existingAddress.getId(), addressExists.get().getId());
                return addUserToAddress(addressExists.get(), userId);
            }
        }
    }


    public void delete(ObjectId id) {
        var filter = Filters.eq("_id", id);

        super.delete(filter);
    }


    @Autowired
    public void setUserModelController(UserModelController userModelController) {
        this.userModelController = userModelController;
    }

}