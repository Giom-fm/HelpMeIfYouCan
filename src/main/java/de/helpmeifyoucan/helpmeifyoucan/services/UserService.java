package de.helpmeifyoucan.helpmeifyoucan.services;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import de.helpmeifyoucan.helpmeifyoucan.models.*;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.AddressUpdate;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.UserUpdate;
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
import static com.mongodb.client.model.Filters.in;
import static com.mongodb.client.model.Updates.*;

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


    // -----------------------------------------------------------------------
    // -----------------------------------------------------------------------
    // CRUD
    // -----------------------------------------------------------------------
    // -----------------------------------------------------------------------

    public UserModel getById(ObjectId id) {
        return Optional.ofNullable(super.getById(id)).orElseThrow(() -> new UserExceptions.UserNotFoundException(id));

    }

    public UserModel getWithAddress(ObjectId userId, boolean lazy) {

        var user = this.getById(userId);

        return !lazy && Optional.ofNullable(user.getUserAddress()).isPresent() ?
                user.setFullAddress(this.addressService.getById(user.getUserAddress())) : user;
    }

    public UserModel getByEmail(String email) {
        var filter = Filters.eq("email", email);
        return Optional.ofNullable(super.getByFilter(filter))
                .orElseThrow(() -> new UserExceptions.UserNotFoundByEmailException(email));
    }

    public AddressModel getUserAddress(ObjectId id) {
        var addressId = this.getById(id).getUserAddress();
        return this.addressService.getById(addressId);
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
        var hashedPassword = passwordEncoder.matches(updatedFields.getCurrentPassword(),
                this.getById(id).getPassword());
        if (!hashedPassword) {
            throw new PasswordMismatchException();
        }

        var updateFilter = eq(id);

        return super.updateExistingFields(updateFilter, updatedFields.toFilter());

    }

    // -----------------------------------------------------------------------
    // -----------------------------------------------------------------------
    // HANDLER
    // -----------------------------------------------------------------------
    // -----------------------------------------------------------------------

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

        var addedAddress = this.addressService.handleUserServiceAddressAdd(address, userId);

        var updatedUser = this.addAddressToUser(userId, addedAddress);

        return lazy ? updatedUser : updatedUser.setFullAddress(addedAddress);

    }

    public UserModel handleUserAddressUpdateRequest(ObjectId userId, AddressUpdate update, boolean lazy) {
        var updatingUser = this.getById(userId);

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
     * @param userId User to delete address from
     * @return the edited User
     */
    public UserModel handleUserAddressDeleteRequest(ObjectId userId) {
        var userToUpdate = this.getById(userId);
        return this.deleteAddressFromUser(userToUpdate, userToUpdate.getUserAddress());
    }

    public UserModel handleHelpModelAdd(Class<? extends AbstractHelpModel> modelClass, ObjectId modelId,
                                        ObjectId userId) {
        return this.addHelpModel(userId, modelId, modelClass);
    }

    public UserModel handleHelpModelDelete(Class<? extends AbstractHelpModel> modelClass, ObjectId modelId,
                                           ObjectId userId) {
        return this.deleteHelpModel(userId, modelId, modelClass);
    }

    public UserModel handleApplicationAdd(ObjectId userId, HelpModelApplication application) {
        return this.addApplication(userId, application);
    }

    public UserModel handleApplicationAccept(HelpModelApplication application, ObjectId acceptingUser) {
        return this.acceptApplication(application.getUser(), application, acceptingUser);
    }

    public UserModel handleApplicationDelete(ObjectId userId, ObjectId modelId) {
        return this.deleteApplication(userId, modelId);
    }

    // -----------------------------------------------------------------------
    // -----------------------------------------------------------------------
    // HELPER
    // -----------------------------------------------------------------------
    // -----------------------------------------------------------------------

    /**
     * We want to add a User to the Addresses user References and vice versa, we to
     * this by adding address reference to user
     *
     * @param user    User to add address to
     * @param address Address to add
     * @return the updated user
     */
    private UserModel addAddressToUser(ObjectId user, AddressModel address) {

        return this.updateUserAddressField(user, address.getId());

    }

    /**
     * We want to delete one and add the other address reference to a user
     *
     * @param userId       the user to apply changes to
     * @param addressToAdd address to add
     */

    private UserModel exchangeAddress(ObjectId userId, ObjectId addressToAdd) {
        return this.updateUserAddressField(userId, addressToAdd);

    }

    /**
     * We want to delete the given Address from Users Address List and let the
     * AddressmodelController handle the Addressmodel
     *
     * @param user      User to delete address from
     * @param addressId Address to delete
     * @return the edited User
     */
    private UserModel deleteAddressFromUser(UserModel user, ObjectId addressId) {
        addressService.handleUserServiceAddressDelete(addressId, user.getId());
        return this.updateUserAddressField(user.getId(), null);
    }

    /**
     * We want to submit the changed address references to the db, we do so by
     * quering the db for the userid and change the address field to actual value
     *
     * @param user the user to update
     * @return the updated user
     */
    private UserModel updateUserAddressField(ObjectId user, ObjectId address) {
        Bson updatedFields = set("userAddress", address);

        var filter = Filters.eq(user);

        return Optional.ofNullable(super.updateExistingFields(filter, updatedFields))
                .orElseThrow(() -> new UserNotFoundException(user));

    }

    private UserModel addHelpModel(ObjectId userId, ObjectId modelId, Class<? extends AbstractHelpModel> model) {
        var idFilter = eq(userId);
        return model.equals(HelpOfferModel.class) ? super.updateExistingFields(idFilter, push("helpOffers", modelId))
                : super.updateExistingFields(idFilter, push("helpRequests", modelId));
    }

    private UserModel deleteHelpModel(ObjectId userId, ObjectId modelId, Class<? extends AbstractHelpModel> model) {
        var idFilter = eq(userId);
        return model.equals(HelpOfferModel.class)
                ? super.updateExistingFields(idFilter, pull("helpOffers", eq(modelId)))
                : super.updateExistingFields(idFilter, pull("helpRequests", eq(modelId)));

    }

    private UserModel addApplication(ObjectId userId, HelpModelApplication application) {
        var idFilter = eq(userId);

        return this.updateExistingFields(idFilter, push("applications", application));
    }

    private UserModel acceptApplication(ObjectId acceptedUser, HelpModelApplication application,
            ObjectId acceptingUser) {
        var acceptingUserModel = this.getById(acceptingUser);

        application.addUserDetails(acceptingUserModel).setUser(acceptedUser);

        var pullApplicationFilter = pull("applications", in("requestId", application.getHelpModelId()));

        var pushAcceptedApplication = push("acceptedApplications", application);

        var idFilter = eq(acceptedUser);
        return this.updateExistingFields(idFilter, combine(pullApplicationFilter, pushAcceptedApplication));
    }

    private UserModel deleteApplication(ObjectId userId, ObjectId requestId) {
        var idFilter = eq(userId);

        return this.updateExistingFields(idFilter, pull("applications", in("requestId", requestId)));
    }

    @Autowired
    public void setAddressModelController(AddressService addressModelController) {
        this.addressService = addressModelController;
    }


}
