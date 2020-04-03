package de.helpmeifyoucan.helpmeifyoucan.services;

import static com.mongodb.client.model.Filters.eq;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import de.helpmeifyoucan.helpmeifyoucan.models.AddressModel;
import de.helpmeifyoucan.helpmeifyoucan.models.UserModel;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.AddressUpdate;
import de.helpmeifyoucan.helpmeifyoucan.utils.errors.AddressExceptions.AddressNotFoundException;
import de.helpmeifyoucan.helpmeifyoucan.utils.errors.UserExceptions.UserNotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class AddressServiceTest {

    @Autowired
    private AddressService addressService;

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private AddressModel testAddress;

    private UserModel testUser;

    @Before
    public void setUpTest() {

        testUser = new UserModel().setName("Marc").setLastName("Jaeger")
                .setPassword(passwordEncoder.encode("password1")).setEmail("test@Mail.de");
        testAddress = new AddressModel().setCountry("Germany").setDistrict("Hamburg").setStreet("testStreet")
                .setZipCode("22391").setHouseNumber("13");

    }

    @Test
    public void givenValidAddressToSave_RetrievedAddressShouldBeEqualToSavedAddress() {
        this.addressService.save(testAddress);

        AddressModel retrievedAddress = this.addressService.get((testAddress.getId()));

        assertEquals(testAddress, retrievedAddress);
    }

    @Test(expected = AddressNotFoundException.class)
    public void givenNotExistingObjectId_NotFoundShouldBeThrown() {
        this.addressService.get(new ObjectId());
    }

    @Test
    public void givenExistingObjectId_ExistsShouldBeTrue() {
        this.addressService.save(testAddress);
        assertTrue(addressService.exists(eq(this.testAddress.getId())));
    }

    @Test
    public void givenNotExistingObjectId_ExistsShouldBeFalse() {
        assertFalse(addressService.exists(eq(new ObjectId())));
    }

    @Test(expected = AddressNotFoundException.class)
    public void givenNotExistingObjectId_FieldsCannotBeUpdatedAndExceptionIsThrown() {
        AddressUpdate update = new AddressUpdate().setHouseNumber("15");

        this.addressService.updateExistingField(update.toFilter(), new ObjectId());
    }

    @Test
    public void givenExistingObjectIdAndUpdate_AddressFieldsShouldBeUpdatedAccordingly() {
        this.addressService.save(testAddress);
        AddressUpdate update = new AddressUpdate().setHouseNumber("15");

        AddressModel updatedAddress = this.addressService.updateExistingField(update.toFilter(), testAddress.getId());

        assertEquals(updatedAddress.getHouseNumber(), updatedAddress.getHouseNumber());
        updatedAddress.setHouseNumber(testAddress.getHouseNumber());

        assertEquals(updatedAddress, testAddress);
    }

    // REVIEW
    /*
     * @Test(expected = AddressNotFoundException.class) public void
     * givenNullAddress_UpdateFailsAndThrowsException() {
     * this.addressService.updateUserField(new AddressModel()); }
     */

    @Test
    public void givenValidAddress_UserFieldShouldBeUpdatedAccordingly() {
        this.addressService.save(testAddress);

        ObjectId dummy = new ObjectId();
        testAddress.addUserAddress(dummy);

        AddressModel updatedAddress = this.addressService.updateUserField(testAddress);

        assertTrue(updatedAddress.getUsers().contains(dummy));
    }

    @Test(expected = UserNotFoundException.class)
    public void givenAddressNotContainingUserAddress_ExceptionShouldBeThrown() {
        this.addressService.deleteUserFromAddress(testAddress, new ObjectId());
    }

    @Test
    public void givenSavedAddressWithOneUserRef_AddressShouldBeDeletedAfterUserRemoval() {
        testUser.setId(new ObjectId());
        this.addressService.save(testAddress.addUserAddress(testUser.getId()));
        this.addressService.deleteUserFromAddress(this.addressService.get(testAddress.getId()), testUser.getId());
        assertFalse(this.addressService.exists(eq(testAddress.getId())));
    }

    @Test
    public void givenSavedAddressWithTwoUserRefs_OneUserShouldBeRemoved() {
        testUser.setId(new ObjectId());

        ObjectId idTobeRemoved = new ObjectId();

        testAddress.addUserAddress(idTobeRemoved);
        testAddress.addUserAddress(testUser.getId());
        this.addressService.save(testAddress);

        AddressModel updatedAddress = this.addressService
                .deleteUserFromAddress(this.addressService.get(testAddress.getId()), idTobeRemoved);

        assertFalse(updatedAddress.containsUser(idTobeRemoved));
        assertTrue(updatedAddress.containsUser(testUser.getId()));
    }

    @Test(expected = AddressNotFoundException.class)
    public void givenIncorrectAddress_ExceptionShouldBeThrown() {
        this.addressService.updateAddress(new ObjectId(), new AddressUpdate(), new ObjectId());
    }

    @Test
    public void givenCorrectIdAndHasNoOtherReferences_AddressShouldBeUpdatedAccordingly() {
        testUser.setId(new ObjectId());
        testAddress.addUserAddress(testUser.getId());

        this.addressService.save(testAddress);

        AddressUpdate update = new AddressUpdate().setHouseNumber("15");

        AddressModel updatedAddress = this.addressService.updateAddress(testAddress.getId(), update, testUser.getId());

        assertEquals(updatedAddress.getHouseNumber(), update.houseNumber);

        updatedAddress.setHouseNumber("13");

        assertEquals(testAddress, updatedAddress);
    }

    @Test
    public void givenCorrectIdAndHasNoOtherReferencesButUpdatedAddressExists_UserRefShouldBeChangedToExistingAndUserRefAddedToAddress() {
        testUser.setId(new ObjectId());

        AddressModel existingAddress = new AddressModel().setCountry("Germany").setDistrict("Hamburg")
                .setStreet("testStreet").setZipCode("22391").setHouseNumber("15");

        testAddress.addUserAddress(testUser.getId());

        this.addressService.save(testAddress);
        this.addressService.save(existingAddress);

        AddressUpdate update = new AddressUpdate().setHouseNumber("15");

        this.addressService.updateAddress(testAddress.getId(), update, testUser.getId());

        AddressModel withUserRef = this.addressService.get(existingAddress.getId());

        assertTrue(withUserRef.containsUser(testUser.getId()));

        assertFalse(this.addressService.exists(eq(testAddress.getId())));

    }

    @Test
    public void givenAddressWithTwoReferencesAndUpdatedAddressNotExisting_UpdatedAddressShouldBeSavedAndUserRefsUpdatedAccordingly() {
        this.userService.save(testUser.setUserAddress(testAddress.generateId().getId()));
        UserModel secondUser = new UserModel().setName("Guillaume");
        this.userService.save(secondUser);

        testAddress.addUserAddress(secondUser.getId());

        testAddress.addUserAddress(testUser.getId());

        this.addressService.save(testAddress);

        AddressUpdate update = new AddressUpdate().setHouseNumber("15");

        AddressModel newAddress = this.addressService.updateAddress(testAddress.getId(), update, testUser.getId());

        assertTrue(newAddress.containsUser(testUser.getId()) && newAddress.getUsers().size() == 1);

        AddressModel oldAddress = this.addressService.get(testAddress.getId());

        assertTrue(oldAddress.containsUser(secondUser.getId()));
        assertEquals(oldAddress.getUsers().size(), 1);

    }

    @Test
    public void givenAddressWithTwoReferencesAndUpdatedAddressExisting_UpdatedReferencesShouldBeChangedAccordingly() {
        this.userService.save(testUser.setUserAddress(testAddress.generateId().getId()));
        UserModel secondUser = new UserModel().setName("Guillaume");
        this.userService.save(secondUser);

        testAddress.addUserAddress(secondUser.getId());

        testAddress.addUserAddress(testUser.getId());

        this.addressService.save(testAddress);

        AddressModel existingAddress = new AddressModel().setCountry("Germany").setDistrict("Hamburg")
                .setStreet("testStreet").setZipCode("22391").setHouseNumber("15");

        this.addressService.save(existingAddress);

        AddressUpdate update = new AddressUpdate().setHouseNumber("15");

        this.addressService.updateAddress(testAddress.getId(), update, testUser.getId());

        AddressModel updatedAddress = this.addressService.get(existingAddress.getId());
        assertTrue(updatedAddress.containsUser(testUser.getId()));

        AddressModel oldAddress = this.addressService.get(testAddress.getId());

        assertTrue(oldAddress.containsUser(secondUser.getId()));
        assertEquals(oldAddress.getUsers().size(), 1);
    }

    @Before
    public void clearCollection() {
        addressService.getCollection().drop();
        this.userService.createIndex();

        this.userService.getCollection().drop();
        this.userService.createIndex();

    }

}
