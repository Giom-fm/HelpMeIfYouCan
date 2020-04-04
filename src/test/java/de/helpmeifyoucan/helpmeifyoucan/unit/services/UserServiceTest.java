package de.helpmeifyoucan.helpmeifyoucan.unit.services;

import com.mongodb.MongoWriteException;
import de.helpmeifyoucan.helpmeifyoucan.StaticDbClear;
import de.helpmeifyoucan.helpmeifyoucan.models.AddressModel;
import de.helpmeifyoucan.helpmeifyoucan.models.UserModel;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.UserUpdate;
import de.helpmeifyoucan.helpmeifyoucan.services.AddressService;
import de.helpmeifyoucan.helpmeifyoucan.services.UserService;
import de.helpmeifyoucan.helpmeifyoucan.utils.errors.AddressExceptions.AddressNotFoundException;
import de.helpmeifyoucan.helpmeifyoucan.utils.errors.AuthExceptions.PasswordMismatchException;
import de.helpmeifyoucan.helpmeifyoucan.utils.errors.UserExceptions;
import de.helpmeifyoucan.helpmeifyoucan.utils.errors.UserExceptions.UserNotFoundException;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static com.mongodb.client.model.Filters.eq;
import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
public class UserServiceTest {

    @Autowired
    private StaticDbClear clear;
    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AddressService addressService;

    private UserModel testUser;


    @Before
    public void setUpTest() {
        testUser = new UserModel().setName("Marc").setLastName("Jaeger").setPassword(passwordEncoder.encode("password1")).setEmail("test@Mail.de").setPhoneNr("901890");

    }


    @Test
    public void withTwoUsersSavedInCollection_TheOnesMatchingTheFilterShouldBeReturned() {
        this.userService.save(new UserModel().setName("Name").setLastName("Jaeger").setEmail("abc@mail.de"));
        this.userService.save(new UserModel().setName("Name").setLastName("fournier").setEmail("bsc@mail.de"));
        this.userService.save(testUser);


        var result = this.userService.getAllByFilter(eq("name", "Name"));

        assertEquals(result.size(), 2);


    }


    @Test
    public void userTryingToSaveAlreadyExistingAddress_existingAddressShouldBeAdded() {
        AddressModel existingAddress = new AddressModel().setCountry("Germany").setDistrict("Hamburg").setStreet("testStreet").setZipCode("22391").setHouseNumber("13");
        AddressModel addedAddress = new AddressModel().setCountry("Germany").setDistrict("Hamburg").setStreet("testStreet").setZipCode("22391").setHouseNumber("13");

        this.addressService.save(existingAddress);

        this.userService.save(testUser);

        this.userService.handleUserAddressAddRequest(testUser.getId(), addedAddress, false);

        assertEquals(this.userService.get(testUser.getId()).getUserAddress(), existingAddress.getId());

    }


    @Test
    public void givenValidUserToSave_RetrievedUserShouldBeEqualToSavedUser() {

        this.userService.save(testUser);

        UserModel savedUser = userService.get(testUser.getId());
        assertEquals(savedUser, testUser);
    }

    @Test(expected = UserNotFoundException.class)
    public void givenNotExistingObjectId_NotFoundShouldBeThrown() {
        this.userService.get(new ObjectId());
    }

    @Test
    public void afterSavingUser_ExistsShouldBeTrue() {
        this.userService.save(testUser);
        assertTrue(this.userService.exists(eq(testUser.getId())));
    }

    @Test
    public void givenNotExistingId_ExistsShouldBeFalse() {
        assertFalse(this.userService.exists(eq(new ObjectId())));
    }


    @Test
    public void afterSavingUserAndDeletingUser_UserShouldNotExistAnymore() {
        this.userService.save(testUser);

        this.userService.deleteById(testUser.getId());

        assertFalse(this.userService.exists(eq(testUser.getId())));
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

    @Test(expected = UserExceptions.UserNotFoundByEmailException.class)
    public void givenNotExistingEmail_NotFoundShouldThrowNotFound() {
        this.userService.getByEmail("notExisting");

    }

    @Test
    public void existsOnNotExistingUser_ShouldBeFalse() {
        assertFalse(this.userService.exists(eq(new ObjectId())));
    }

    @Test(expected = PasswordMismatchException.class)
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

    @Test(expected = UserNotFoundException.class)
    public void givenIncorrectUserId_AddressAddShouldThrowError() {
        AddressModel address = new AddressModel().setCountry("germany");

        this.userService.handleUserAddressAddRequest(new ObjectId(), address, false);
    }

    @Test
    public void givenCorrectUserIdAndAddress_CorrectAddressIdShouldBeAddedToUser() {
        AddressModel address = new AddressModel().setCountry("Germany").setDistrict("Hamburg");

        this.userService.save(testUser);

        UserModel updatedUser = this.userService.handleUserAddressAddRequest(testUser.getId(), address, true);

        assertEquals(updatedUser.getUserAddress(), address.getId());
    }


    @Test
    public void afterAddingAddressToUser_ItShouldBeRemovedSuccessfully() {
        AddressModel address = new AddressModel().setCountry("Germany").setDistrict("Hamburg").setStreet("testStreet").setZipCode("22391").setHouseNumber("13");
        this.userService.save(testUser);

        this.userService.handleUserAddressAddRequest(testUser.getId(), address, false);
        userService.handleUserAddressDeleteRequest(testUser.getId(), address.getId());

        assertTrue(testUser.noAddressReference());
    }

    @Test
    public void afterGivenCorrectAddressId_AddressShouldBeAddedToUser() {
        this.userService.save(testUser);
        AddressModel address = new AddressModel().setCountry("Germany").setDistrict("Hamburg").setStreet("testStreet").setZipCode("22391").setHouseNumber("13");

        UserModel updatedUser = this.userService.addAddressToUser(testUser, address.generateId());

        assertEquals(updatedUser.getUserAddress(), address.getId());
    }


    public void whenGivenIncorrectUserId_AddAddressShouldThrowException() {
        testUser.setId(new ObjectId());
        AddressModel address = new AddressModel().setCountry("Germany").setDistrict("Hamburg").setStreet("testStreet").setZipCode("22391").setHouseNumber("13");

        UserModel updatedUser = this.userService.addAddressToUser(testUser, address.generateId());

    }

    @Test
    public void givenUserWithAddedAddressAndAddressToDelete_ItShouldExchangeAddressesAccordingly() {
        AddressModel addressToDelete = new AddressModel().generateId();

        testUser.setUserAddress(addressToDelete.getId());
        this.userService.save(testUser);

        AddressModel addressToAdd = new AddressModel().generateId();

        UserModel updatedUser = this.userService.exchangeAddress(testUser.getId(), addressToAdd.getId());
        assertEquals(updatedUser.getUserAddress(), (addressToAdd.getId()));
    }

    @Test(expected = UserNotFoundException.class)
    public void givenNotExistingUserId_ExchangeAddressesShouldThrowException() {
        AddressModel addressToDelete = new AddressModel().generateId();

        testUser.setUserAddress(addressToDelete.getId());

        testUser.setId(new ObjectId());

        AddressModel addressToAdd = new AddressModel().generateId();

        UserModel updatedUser = this.userService.exchangeAddress(testUser.getId(), addressToAdd.getId());
    }

    @Test
    public void givenCorrectAddressIdAndUserId_AddressShouldBeDeletedFromUser() {
        testUser.setId(new ObjectId());
        AddressModel address = new AddressModel().setCountry("Germany").setDistrict("Hamburg").setStreet("testStreet").setZipCode("22391").setHouseNumber("13").generateId();
        this.addressService.save(address.addUserAddress(testUser.getId()));

        testUser.setUserAddress(address.getId());

        this.userService.save(testUser);

        UserModel updatedUser = this.userService.deleteAddressFromUser(this.userService.get(testUser.getId()), address.getId());

        assertTrue(updatedUser.noAddressReference());

    }

    @Test(expected = AddressNotFoundException.class)
    public void givenWrongUserIdAddressIdCombination_MethodShouldThrowException() {
        testUser.setId(new ObjectId());
        AddressModel address = new AddressModel().setCountry("Germany").setDistrict("Hamburg").setStreet("testStreet").setZipCode("22391").setHouseNumber("13").generateId();
        this.addressService.save(address.addUserAddress(testUser.getId()));

        testUser.setUserAddress(address.getId());

        this.userService.save(testUser);

        this.userService.deleteAddressFromUser(testUser, new ObjectId());
    }



    @Test(expected = UserNotFoundException.class)
    public void givenWrongUserId_FieldsShouldNotBeUpdatedAndExceptionShouldBeThrown() {
        this.userService.updateUserAddressField(testUser.setUserAddress(new ObjectId()));
    }

    @Test
    public void givenRightUser_AddressFieldShouldBeUpdatedAccordingly() {
        AddressModel address = new AddressModel().setCountry("Germany").setDistrict("Hamburg").setStreet("testStreet").setZipCode("22391").setHouseNumber("13").generateId();
        this.userService.save(testUser);
        UserModel updatedUser = this.userService.updateUserAddressField(testUser.setUserAddress(address.getId()));

        assertEquals(updatedUser.getUserAddress(), address.getId());

    }


    @Before
    public void clearCollection() {
        clear.clearDb();

    }


}
