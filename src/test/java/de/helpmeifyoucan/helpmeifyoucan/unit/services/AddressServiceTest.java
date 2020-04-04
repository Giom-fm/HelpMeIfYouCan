package de.helpmeifyoucan.helpmeifyoucan.unit.services;

import de.helpmeifyoucan.helpmeifyoucan.StaticDbClear;
import de.helpmeifyoucan.helpmeifyoucan.models.AddressModel;
import de.helpmeifyoucan.helpmeifyoucan.models.UserModel;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.AddressUpdate;
import de.helpmeifyoucan.helpmeifyoucan.services.AddressService;
import de.helpmeifyoucan.helpmeifyoucan.services.UserService;
import de.helpmeifyoucan.helpmeifyoucan.utils.errors.AddressExceptions.AddressNotFoundException;
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
public class AddressServiceTest {

@Autowired
private StaticDbClear clear;
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


        testUser = new UserModel().setName("Marc").setLastName("Jaeger").setPassword(passwordEncoder.encode("password1")).setEmail("test@Mail.de");
        testAddress = new AddressModel().setCountry("Germany").setDistrict("Hamburg").setStreet("testStreet").setZipCode("22391").setHouseNumber("13");

    }

    @Test
    public void givenValidAddressToSave_RetrievedAddressShouldBeEqualToSavedAddress() {
        this.addressService.save(testAddress);

        AddressModel retrievedAddress = this.addressService.getById((testAddress.getId()));

        assertEquals(testAddress, retrievedAddress);
    }


    @Test
    public void givenExistingObjectIdAndUpdate_AddressFieldsShouldBeUpdatedAccordingly() {
        this.testUser.setId(new ObjectId());
        this.addressService.handleUserServiceAddressAdd(testAddress, testUser.getId());
        AddressUpdate update = new AddressUpdate().setHouseNumber("15");

        AddressModel updatedAddress = this.addressService.handleUserServiceAddressUpdate(testAddress.getId(), update, testUser.getId());

        assertEquals(updatedAddress.getHouseNumber(), updatedAddress.getHouseNumber());
        updatedAddress.setHouseNumber(testAddress.getHouseNumber());

        assertEquals(updatedAddress, testAddress);
    }


    @Test
    public void givenValidAddress_UserFieldShouldBeUpdatedAccordingly() {

        testUser.setId(new ObjectId());
        this.addressService.handleUserServiceAddressAdd(testAddress, testUser.getId());

        var updatedAddress = this.addressService.getById(testAddress.getId());
        assertTrue(updatedAddress.getUsers().contains(testUser.getId()));
    }


    @Test
    public void givenSavedAddressWithTwoUserRefs_OneUserShouldBeRemoved() {
        testUser.setId(new ObjectId());

        ObjectId idTobeRemoved = new ObjectId();

        testAddress.addUserAddress(idTobeRemoved);
        this.addressService.handleUserServiceAddressAdd(testAddress, testUser.getId());

        AddressModel updatedAddress = this.addressService.handleUserServiceAddressDelete(testAddress.getId(), idTobeRemoved);

        assertFalse(updatedAddress.containsUser(idTobeRemoved));
        assertTrue(updatedAddress.containsUser(testUser.getId()));
    }



    @Test(expected = AddressNotFoundException.class)
    public void givenIncorrectAddress_ExceptionShouldBeThrown() {
        this.addressService.handleUserServiceAddressUpdate(new ObjectId(), new AddressUpdate(), new ObjectId());
    }

    @Test
    public void givenCorrectIdAndHasNoOtherReferences_AddressShouldBeUpdatedAccordingly() {
        testUser.setId(new ObjectId());
        testAddress.addUserAddress(testUser.getId());

        this.addressService.save(testAddress);

        AddressUpdate update = new AddressUpdate().setHouseNumber("15");

        AddressModel updatedAddress = this.addressService.handleUserServiceAddressUpdate(testAddress.getId(), update, testUser.getId());

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

        this.addressService.handleUserServiceAddressUpdate(testAddress.getId(), update, testUser.getId());

        AddressModel withUserRef = this.addressService.getById(existingAddress.getId());

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

        AddressModel newAddress = this.addressService.handleUserServiceAddressUpdate(testAddress.getId(), update, testUser.getId());

        assertTrue(newAddress.containsUser(testUser.getId()) && newAddress.getUsers().size() == 1);

        AddressModel oldAddress = this.addressService.getById(testAddress.getId());

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

        this.addressService.handleUserServiceAddressUpdate(testAddress.getId(), update, testUser.getId());


        AddressModel updatedAddress = this.addressService.getById(existingAddress.getId());
        assertTrue(updatedAddress.containsUser(testUser.getId()));

        AddressModel oldAddress = this.addressService.getById(testAddress.getId());

        assertTrue(oldAddress.containsUser(secondUser.getId()));
        assertEquals(oldAddress.getUsers().size(), 1);
    }

    @Before
    public void clearCollection() {
        clear.clearDb();

    }

}
