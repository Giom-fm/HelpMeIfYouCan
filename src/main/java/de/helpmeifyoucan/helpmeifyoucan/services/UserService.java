package de.helpmeifyoucan.helpmeifyoucan.services;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import de.helpmeifyoucan.helpmeifyoucan.models.AddressModel;
import de.helpmeifyoucan.helpmeifyoucan.models.UserModel;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.UserUpdate;
import de.helpmeifyoucan.helpmeifyoucan.utils.ErrorMessages;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

@Service
public class UserService extends AbstractService<UserModel> {

    private AddressService addressService;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(MongoDatabase database, BCryptPasswordEncoder passwordEncoder) {
        super(database);
        super.createCollection("users", UserModel.class);
        this.passwordEncoder = passwordEncoder;
        createIndex();
    }

    private void createIndex() {
        IndexOptions options = new IndexOptions();
        options.unique(true);

        super.createIndex(Indexes.ascending("email"), options);
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

        var hashedPassword = passwordEncoder.matches(updatedFields.getCurrentPassword(), this.get(id).getPassword());
        if (!hashedPassword) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.PASSWORD_WRONG);
        }

        var updateFilter = eq(id);

        var updatedUser = super.updateExistingFields(updateFilter, updatedFields.toFilter());
        if (updatedUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessages.USER_NOT_FOUND);
        }
        return updatedUser;
    }

    public Optional<UserModel> getOptional(Bson filter) {
        return super.getOptional(filter);
    }

    public void delete(ObjectId id) {
        super.delete(Filters.eq("_id", id));
    }

    /**
     * The user requests to add a new address to his addresses, as we dont want to
     * have redundant addresses in our db, we first calculate the new addresses
     * hash, and query the db for it. If we find a matching address, we want to add
     * the user to the addresses userreferneces and vice versa If no address is
     * found, we will create a new entity and update references accordingly
     *
     * @param userId  User to add the address to
     * @param address the from the request mapped new address
     * @return the Updated User
     */

    public UserModel handleUserAddressAddRequest(ObjectId userId, AddressModel address) {
        var addressFilter = eq("hashCode", address.calculateHash().getHashCode());
        var dbAddress = addressService.getOptional(addressFilter);
        var user = this.get(userId);
        if (dbAddress.isPresent()) {
            addressService.addUserToAddress(dbAddress.get(), user.getId());
            return this.addAddressToUser(user, dbAddress.get());

        } else {
            addressService.save(address.addUserAddress(userId));
            return this.addAddressToUser(user, address);
        }
    }

    /**
     * We want to get the User Model of the calling user, so we fetch it from db and
     * delegate deleting to another method
     *
     * @param userId    User to delete address from
     * @param addressId Address to delete
     * @return the edited User
     */
    public UserModel handleUserAddressDeleteRequest(ObjectId userId, ObjectId addressId) {
        return this.deleteAddressFromUser(this.get(userId), addressId);
    }

    // user address operations

    /**
     * We want to add a User to the Addresses user References and vice versa, we to
     * this by adding address reference to user
     *
     * @param user    User to add address to
     * @param address Address to add
     * @return the updated user
     */
    private UserModel addAddressToUser(UserModel user, AddressModel address) {

        return this.updateUserAddressField(user.addAddress(address.getId()));

    }

    /**
     * We want to delete one and add the other address reference to a user
     *
     * @param userId          the user to apply changes to
     * @param addressToDelete address to delete
     * @param addressToAdd    address to add
     */

    public void exchangeAddress(ObjectId userId, ObjectId addressToDelete, ObjectId addressToAdd) {
        var user = this.get(userId);
        updateUserAddressField(user.removeAddress(addressToDelete).addAddress(addressToAdd));

    }

    /**
     * We want to delete the given Address from Users Address List and let the
     * AddressmodelController handle the Addressmodel
     *
     * @param user      User to delete address from
     * @param addressId Address to delete
     * @return the edited User
     */
    public UserModel deleteAddressFromUser(UserModel user, ObjectId addressId) {
        addressService.handleUserControllerAddressDelete(addressId, user.getId());
        return this.updateUserAddressField(user.removeAddress(addressId));
    }

    /**
     * We want to submit the changed address references to the db, we do so by
     * quering the db for the userid and change the address field to actual value
     *
     * @param user the user to update
     * @return the updated user
     */
    private UserModel updateUserAddressField(UserModel user) {
        Bson updatedFields = set("addresses", user.getAddresses());

        var filter = Filters.eq("_id", user.getId());
        return this.updateExistingFields(filter, updatedFields);
    }

    @Autowired
    public void setAddressModelController(AddressService addressModelController) {
        this.addressService = addressModelController;
    }
}
