package de.helpmeifyoucan.helpmeifyoucan;

import com.mongodb.MongoWriteException;
import de.helpmeifyoucan.helpmeifyoucan.models.AddressModel;
import de.helpmeifyoucan.helpmeifyoucan.models.UserModel;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.UserUpdate;
import de.helpmeifyoucan.helpmeifyoucan.services.AddressService;
import de.helpmeifyoucan.helpmeifyoucan.services.UserService;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;

import static com.mongodb.client.model.Filters.eq;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class UserModelServiceTest {


    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AddressService addressService;

    private UserModel testUser;


    @Before
    public void setUpTest() {
        testUser = new UserModel().setName("Marc").setLastName("Jaeger").setPassword(passwordEncoder.encode("password1")).setEmail("test@Mail.de");

    }

    @Test
    public void givenValidUserToSave_RetrievedUserShouldBeEqualToSavedUser() {

        this.userService.save(testUser);

        UserModel savedUser = userService.get(testUser.getId());
        assertEquals(savedUser, testUser);
    }

    @Test(expected = ResponseStatusException.class)
    public void givenNotExistingObjectId_NotFoundShouldBeThrown() {
        this.userService.get(new ObjectId());
    }

    @Test
    public void afterSavingUser_ExistsShouldBeTrue() {
        this.userService.save(testUser);
        assertTrue(this.userService.exits(eq(testUser.getId())));
    }


    @Test
    public void afterSavingUserAndDeletingUser_UserShouldNotExistAnymore() {
        this.userService.save(testUser);

        this.userService.delete(testUser.getId());

        assertFalse(this.userService.exits(eq(testUser.getId())));
    }

    @Test(expected = MongoWriteException.class)
    public void afterSavingUserWithIndex_SavingUserWithSameIndexIsNotPossible() {

        this.userService.save(testUser);

        UserModel duplicateEmailUser = new UserModel().setName("Marc").setEmail("test@Mail.de");

        this.userService.save(duplicateEmailUser);
    }

    @Test
    public void afterSavingUserWithEmail_ItShouldBeFoundByEmail() {

        this.userService.save(testUser);
        UserModel retrievedUser = this.userService.getByEmail(testUser.getEmail());
        assertEquals(testUser, retrievedUser);
    }

    @Test(expected = ResponseStatusException.class)
    public void givenNotExistingEmail_NotFoundShouldThrowNotFound() {
        this.userService.getByEmail("notExisting");

    }

    @Test
    public void existsOnNotExistingUser_ShouldBeFalse() {
        assertFalse(this.userService.exits(eq(new ObjectId())));
    }

    @Test(expected = ResponseStatusException.class)
    public void givenWrongPasswordForUpdate_WrongPasswordShouldBeThrown() {
        this.userService.save(testUser);

        UserUpdate wrongUpdate = new UserUpdate();

        wrongUpdate.setCurrentPassword("wrongPassword");

        this.userService.update(wrongUpdate, testUser.getId());

    }

    @Test
    public void givenCorrectPasswordForUpdate_UpdatedFieldShouldBeUpdatedCorrectly() {
        this.userService.save(testUser);

        UserUpdate nameUpdate = new UserUpdate();

        nameUpdate.setCurrentPassword("password1");

        nameUpdate.setLastName("West");

        nameUpdate.setName("Kanye");

        UserModel updatedUser = this.userService.update(nameUpdate, testUser.getId());

        assertEquals(updatedUser.getLastName(), "West");
        assertEquals(updatedUser.getName(), "Kanye");
    }

    @Test(expected = ResponseStatusException.class)
    public void givenIncorrectUserId_AddressAddShouldThrowError() {
        AddressModel address = new AddressModel().setCountry("germany");

        this.userService.handleUserAddressAddRequest(new ObjectId(), address);
    }

    @Test
    public void givenCorrectUserIdAndAddress_CorrectAddressIdShouldBeAddedToUser() {
        AddressModel address = new AddressModel().setCountry("Germany").setDistrict("Hamburg");

        this.userService.save(testUser);

        UserModel updatedUser = this.userService.handleUserAddressAddRequest(testUser.getId(), address);

        assertTrue(updatedUser.getAddresses().contains(address.getId()));
    }


    @Test
    public void afterAddingAddressToUser_ItShouldBeRemovedSuccessfully() {
        AddressModel address = new AddressModel().setCountry("Germany").setDistrict("Hamburg").setStreet("testStreet").setZipCode("22391").setHouseNumber(13);
        this.userService.save(testUser);

        this.userService.handleUserAddressAddRequest(testUser.getId(), address);
        userService.handleUserAddressDeleteRequest(testUser.getId(), address.getId());

        assertTrue(testUser.noAddressReferences());
    }

    @Test
    public void afterGivenCorrectAddressId_AddressShouldBeAddedToUser() {
        this.userService.save(testUser);
        AddressModel address = new AddressModel().setCountry("Germany").setDistrict("Hamburg").setStreet("testStreet").setZipCode("22391").setHouseNumber(13);

        UserModel updatedUser = this.userService.addAddressToUser(testUser, address.generateId());

        assertTrue(updatedUser.getAddresses().contains(address.getId()));
    }


    public void whenGivenIncorrectUserId_AddAddressShouldThrowException() {
        testUser.setId(new ObjectId());
        AddressModel address = new AddressModel().setCountry("Germany").setDistrict("Hamburg").setStreet("testStreet").setZipCode("22391").setHouseNumber(13);

        UserModel updatedUser = this.userService.addAddressToUser(testUser, address.generateId());

    }

    @Test
    public void givenUserWithAddedAddressAndAddressToDelete_ItShouldExchangeAddressesAccordingly() {
        AddressModel addressToDelete = new AddressModel().generateId();

        testUser.addAddress(addressToDelete.getId());
        this.userService.save(testUser);

        AddressModel addressToAdd = new AddressModel().generateId();

        UserModel updatedUser = this.userService.exchangeAddress(testUser.getId(), addressToDelete.getId(), addressToAdd.getId());
        assertTrue(updatedUser.getAddresses().size() == 1 && updatedUser.getAddresses().contains(addressToAdd.getId()));
    }

    @Test(expected = ResponseStatusException.class)
    public void givenNotExistingUserId_ExchangeAddressesShouldThrowException() {
        AddressModel addressToDelete = new AddressModel().generateId();

        testUser.addAddress(addressToDelete.getId());

        testUser.setId(new ObjectId());

        AddressModel addressToAdd = new AddressModel().generateId();

        UserModel updatedUser = this.userService.exchangeAddress(testUser.getId(), addressToDelete.getId(), addressToAdd.getId());
    }

    @Test
    public void givenCorrectAddressIdAndUserId_AddressShouldBeDeletedFromUser() {
        testUser.setId(new ObjectId());
        AddressModel address = new AddressModel().setCountry("Germany").setDistrict("Hamburg").setStreet("testStreet").setZipCode("22391").setHouseNumber(13).generateId();
        this.addressService.save(address.addUserAddress(testUser.getId()));

        testUser.addAddress(address.getId());

        this.userService.save(testUser);

        UserModel updatedUser = this.userService.deleteAddressFromUser(this.userService.get(testUser.getId()), address.getId());

        assertTrue(updatedUser.noAddressReferences());

    }

    @Test(expected = ResponseStatusException.class)
    public void givenWrongUserIdAddressIdCombination_MethodShouldThrowException() {
        testUser.setId(new ObjectId());
        AddressModel address = new AddressModel().setCountry("Germany").setDistrict("Hamburg").setStreet("testStreet").setZipCode("22391").setHouseNumber(13).generateId();
        this.addressService.save(address.addUserAddress(testUser.getId()));

        testUser.addAddress(address.getId());

        this.userService.save(testUser);

        UserModel updatedUser = this.userService.deleteAddressFromUser(testUser, address.getId());
    }

    @Test(expected = ResponseStatusException.class)
    public void givenWrongUserId_FieldsShouldNotBeUpdatedAndExceptionShouldBeThrown() {
        this.userService.updateUserAddressField(testUser.addAddress(new ObjectId()));
    }

    @Test
    public void givenRightUser_AddressFieldShouldBeUpdatedAccordingly() {
        AddressModel address = new AddressModel().setCountry("Germany").setDistrict("Hamburg").setStreet("testStreet").setZipCode("22391").setHouseNumber(13).generateId();
        this.userService.save(testUser);
        UserModel updatedUser = this.userService.updateUserAddressField(testUser.addAddress(address.getId()));

        assertTrue(updatedUser.getAddresses().contains(address.getId()));

    }


    @Before
    public void clearCollection() {
        userService.getCollection().drop();
        this.userService.createIndex();

    }


}
