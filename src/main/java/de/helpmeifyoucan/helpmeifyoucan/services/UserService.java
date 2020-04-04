package de.helpmeifyoucan.helpmeifyoucan.services;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import de.helpmeifyoucan.helpmeifyoucan.models.AddressModel;
import de.helpmeifyoucan.helpmeifyoucan.models.UserModel;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.AddressUpdate;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.UserUpdate;
import de.helpmeifyoucan.helpmeifyoucan.utils.errors.AddressExceptions.AddressNotFoundException;
import de.helpmeifyoucan.helpmeifyoucan.utils.errors.AuthExceptions.PasswordMismatchException;
import de.helpmeifyoucan.helpmeifyoucan.utils.errors.UserExceptions;
import de.helpmeifyoucan.helpmeifyoucan.utils.errors.UserExceptions.UserNotFoundException;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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

    public void createIndex() {
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
            throw new UserNotFoundException(id);
        }
        return user;
    }

    //TODO
    public UserModel getByEmail(String email) throws UserNotFoundException {
        var filter = Filters.eq("email", email);
        return Optional.ofNullable(super.getByFilter(filter)).orElseThrow(() -> new UserExceptions.UserNotFoundByEmailException(email));
    }

    /**
     * We want to update the Uses given fields, so we first check if the password is
     * correct, we then create a filter for these fields, to update them in the db
     * we then update the user and return the updated user
     *
     * @param updatedFields the fields to update
     * @param id            the user id to update
     * @return the updated User
     */

    public UserModel update(UserUpdate updatedFields, ObjectId id) {

        // FIXME soll in Zukunft vom Authmanager übernommen werden -> Endpunkt update
        // wird dann
        // nur aufgerufen wenn es kein Auth exception gab.
        var hashedPassword = passwordEncoder.matches(updatedFields.getCurrentPassword(), this.get(id).getPassword());
        if (!hashedPassword) {
            throw new PasswordMismatchException();
        }

        var updateFilter = eq(id);

        return super.updateExistingFields(updateFilter, updatedFields.toFilter());

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

    public UserModel handleUserAddressAddRequest(ObjectId userId, AddressModel address, boolean lazy) {

        var user = this.get(userId);

        var addedAddress = this.addressService.handleUserServiceAddressAdd(address, userId);

        this.addAddressToUser(user, addedAddress);

        return lazy ? user.setUserAddress(addedAddress.getId()) : user.setFullAddress(addedAddress);

    }

    public UserModel handleUserAddressUpdateRequest(ObjectId userId, AddressUpdate update, boolean lazy) {
        var updatingUser = this.get(userId);

        var updatedAddress = this.addressService.handleUserServiceAddressUpdate(updatingUser.getUserAddress(), update,
                userId);

        if (!updatedAddress.getId().equals(updatingUser.getUserAddress())) {
            this.exchangeAddress(userId, updatedAddress.getId());
        }
        return lazy ? updatingUser.setUserAddress(updatedAddress.getId()) : updatingUser.setFullAddress(updatedAddress);
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
    public UserModel addAddressToUser(UserModel user, AddressModel address) {

        return this.updateUserAddressField(user.setUserAddress(address.getId()));

    }

    /**
     * We want to delete one and add the other address reference to a user
     *
     * @param userId       the user to apply changes to
     * @param addressToAdd address to add
     */

    public UserModel exchangeAddress(ObjectId userId, ObjectId addressToAdd) {
        var user = this.get(userId);
        return this.updateUserAddressField(user.setUserAddress(addressToAdd));

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
        addressService.handleUserServiceAddressDelete(addressId, user.getId());
        try {
            return this.updateUserAddressField(user.setUserAddress(null));
        } catch (UnsupportedOperationException e) {
            // REVIEW UnsupportedOperationException ??
            throw new AddressNotFoundException(addressId);
        }

    }

    /**
     * We want to submit the changed address references to the db, we do so by
     * quering the db for the userid and change the address field to actual value
     *
     * @param user the user to update
     * @return the updated user
     */
    public UserModel updateUserAddressField(UserModel user) {
        Bson updatedFields = set("userAddress", user.getUserAddress());

        var filter = Filters.eq("_id", user.getId());

        return Optional.ofNullable(super.updateExistingFields(filter, updatedFields)).orElseThrow(() -> new UserNotFoundException(user.getEmail()));

    }

    @Autowired
    public void setAddressModelController(AddressService addressModelController) {
        this.addressService = addressModelController;
    }

    @Autowired
    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
}
