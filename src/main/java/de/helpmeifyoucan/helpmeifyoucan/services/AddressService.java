package de.helpmeifyoucan.helpmeifyoucan.services;

import com.mongodb.client.MongoDatabase;
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

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

@Service
public class AddressService extends AbstractService<AddressModel> {


    @Autowired
    public AddressService(MongoDatabase dataBase) {
        super(dataBase);
        super.createCollection("address", AddressModel.class);
        this.createIndex();
    }

    public void createIndex() {
        IndexOptions options = new IndexOptions();
        options.unique(true);

        super.createIndex(Indexes.ascending("street", "zipCode", "country", "district", "houseNumber"), options);
    }

    public AddressModel save(AddressModel address) {
        return super.save(address.calculateHash());
    }

    public AddressModel get(ObjectId id) {
        var address = super.getById(id);
        if (address == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessages.ADDRESS_NOT_FOUND);
        }
        return address;
    }

    public AddressModel getById(ObjectId id) {
        return super.getById(id);
    }

    public boolean exists(Bson filter) {
        return super.getOptional(filter).isPresent();
    }


    public boolean deleteById(ObjectId id) {
        var filter = eq("_id", id);

        return super.delete(filter).getDeletedCount() > 0;
    }

    public Optional<AddressModel> getOptional(Bson filter) {
        return super.getOptional(filter);
    }

    public AddressModel updateExistingField(Bson updatedFields, ObjectId address) {
        var filter = eq(address);
        var updatedAddress = super.updateExistingFields(filter, updatedFields);

        if (updatedAddress == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessages.USER_NOT_FOUND);
        }

        return updatedAddress;
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
     * We want to add a user to addresses user references and do so by updating the addresses users field
     *
     * @param address the address to update
     * @param userId  Users userId to add
     * @return the updated address
     */
    public AddressModel addUserToAddress(AddressModel address, ObjectId userId) {
        return this.updateUserField(address.addUserAddress(userId));
    }


    public AddressModel handleUserServiceAddressAdd(AddressModel addressToAdd, ObjectId userId) {
        var addressFilter = eq("hashCode", addressToAdd.calculateHash().getHashCode());
        var dbAddress = this.getOptional(addressFilter);

        if (dbAddress.isPresent()) {
            return this.addUserToAddress(dbAddress.get(), userId);
        } else {
            return this.save(addressToAdd.addUserAddress(userId));
        }

    }

    /**
     * User want to update his userAddress, so give him this entrypoint into the controller to manage the workflow
     *
     * @param update the update to perform
     * @param userId this updating user
     * @return the id of the new or updated Address
     */

    public AddressModel handleUserServiceAddressUpdate(ObjectId addressToUpdate, AddressUpdate update, ObjectId userId) {


        if (addressToUpdate == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessages.ADDRESS_NOT_FOUND);
        }
        return this.updateAddress(addressToUpdate, update, userId);
    }


    /**
     * UserController calls this Method to process AddressModel after it deleted a User from its references
     *
     * @param addressId the edited Address
     * @param userId    the User who held the address
     */
    public void handleUserServiceAddressDelete(ObjectId addressId, ObjectId userId) {
        try {
            this.deleteUserFromAddress(this.get(addressId), userId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessages.ADDRESS_NOT_FOUND);
        }
    }


    /**
     * We want to delete a User from a Addresses references and insert it back into the db. We do not want
     * to insert Address with no referneces back into the db, so we check for it
     *
     * @param address Address to modify /delete
     * @param userId  user To delete from Address
     */

    //FIXME THIS WILL NOT BE VALID WITH ADDED REFERENCES
    public AddressModel deleteUserFromAddress(AddressModel address, ObjectId userId) {
        if (!address.containsUser(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessages.USER_NOT_FOUND);
        }
        var addressWithUserRemoved = address.removeUserAddress(userId);

        if (addressWithUserRemoved.noUserReferences()) {
            if (this.deleteById(addressWithUserRemoved.getId())) {
                return address;
            }
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessages.DELETE_NOT_ACKNOWLEDGED);

        } else {
            return this.updateUserField(addressWithUserRemoved);
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
     */
    public AddressModel updateAddress(ObjectId addressId, AddressUpdate addressUpdate, ObjectId userId) {
        var address = this.getOptional(eq("_id", addressId));

        if (address.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessages.ADDRESS_NOT_FOUND);
        }
        var existingAddress = address.get();
        var mergeWithAddressUpdate = existingAddress.mergeWithAddressUpdate(addressUpdate);
        var filter = eq("hashCode", mergeWithAddressUpdate.getHashCode());
        var optionalAddressModel = this.getOptional(filter);

        if (existingAddress.noUserReferences() || (existingAddress.getUsers().size() == 1 && existingAddress.getUsers().contains(userId))) {

            return this.updateAddressWithNoOtherReferences(addressUpdate, existingAddress, optionalAddressModel, userId);

        } else {

            return this.updateAddressWithOtherReferences(mergeWithAddressUpdate, existingAddress, optionalAddressModel, userId);
        }
    }

    /**
     * We want to update an address with other users referring to it. We do so by first eleting the requesting user from the old address and then check if the updated address already exits in the db
     * If yes, we will add the user to this addresses user references
     * if no we will create a new address
     *
     * @param mergedAddress            the updated Address
     * @param addressToUpdate          the old address to update
     * @param potentialExistingAddress the if existing address amtching the merged address
     * @param userId                   the user
     * @return the Updated /saved address
     */
    public AddressModel updateAddressWithOtherReferences(AddressModel mergedAddress, AddressModel addressToUpdate, Optional<AddressModel> potentialExistingAddress, ObjectId userId) {

        if (!addressToUpdate.containsUser(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessages.USER_NOT_FOUND);
        }

        this.deleteUserFromAddress(addressToUpdate, userId);

        if (potentialExistingAddress.isPresent()) {
            return addUserToAddress(potentialExistingAddress.get(), userId);
        } else {
            return this.save(mergedAddress.setUsers(Collections.singletonList(userId)).generateId());
        }
    }

    /**
     * We want to update an address with no users referring to it. So we query the db for an address matching our updated address, if we find one, we will add the user to this addresses user references and safe it
     * otherwise we will just update the existing address
     *
     * @param existingAddress          the existing address to be updated
     * @param potentialExistingAddress the if existing address matching the updated address
     * @param userId                   the updating user
     * @return the updated address
     */

    private AddressModel updateAddressWithNoOtherReferences(AddressUpdate addressUpdate, AddressModel existingAddress, Optional<AddressModel> potentialExistingAddress, ObjectId userId) {

        if (potentialExistingAddress.isPresent()) {
            this.deleteUserFromAddress(existingAddress, userId);
            return this.addUserToAddress(potentialExistingAddress.get(), userId);
        } else {
            return this.updateExistingField(addressUpdate.toFilter(), existingAddress.getId());
        }
    }


}