package de.helpmeifyoucan.helpmeifyoucan.unit.services;

import de.helpmeifyoucan.helpmeifyoucan.StaticDbClear;
import de.helpmeifyoucan.helpmeifyoucan.models.Coordinates;
import de.helpmeifyoucan.helpmeifyoucan.models.HelpOfferModel;
import de.helpmeifyoucan.helpmeifyoucan.models.HelpRequestModel;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.CoordinatesUpdate;
import de.helpmeifyoucan.helpmeifyoucan.services.CoordinatesService;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

import static com.mongodb.client.model.Filters.eq;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")

public class CoordinatesServiceTest {
    @Autowired
    private StaticDbClear clear;


    @Autowired
    private CoordinatesService coordinatesService;

    private Coordinates testCoords;

    @Before
    public void setUpTest() {
        testCoords = new Coordinates().setLatitude(50.00).setLongitude(20.00);
    }

    @Test
    public void givenCoordinates_ItShouldBeSavedAndRetrievedCorrectly() {
        this.coordinatesService.save(testCoords);

        assertEquals(testCoords, this.coordinatesService.getById(testCoords.getId()));
    }

    @Test
    public void givenObjectIdOfSavedObject_ExistsShouldBeTrue() {
        this.coordinatesService.save(testCoords);

        assertTrue(this.coordinatesService.exists(eq(testCoords.getId())));

    }

    @Test
    public void givenIdOfSavedObject_ItShouldBeDeleted() {
        this.coordinatesService.save(testCoords);
        this.coordinatesService.deleteById(testCoords.getId());
        assertFalse(this.coordinatesService.exists(eq(testCoords.getId())));
    }

    @Test
    public void givenIdOfExistingObjectAndUpdate_fieldsShouldBeUpdatedAccordingly() {
        var helpModel = new HelpRequestModel().setCoordinates(testCoords);

        this.coordinatesService.handleHelpModelCoordinateAdd(helpModel);


        CoordinatesUpdate update = new CoordinatesUpdate().setLatitude(11.22).setLongitude(22.11);


        this.coordinatesService.handleHelpModelCoordinateUpdate(helpModel, update);

        var updatedCoords = this.coordinatesService.getById(testCoords.getId());

        assertEquals(updatedCoords.getLatitude(), update.getLatitude(), 0.0);
    }

    @Test
    public void givenModelContainingCoordinatesNotExisting_theyShouldBeSaved() {
        HelpOfferModel testOffer = new HelpOfferModel().setCoordinates(testCoords).generateId();


        this.coordinatesService.handleHelpModelCoordinateAdd(testOffer);

        assertTrue(coordinatesService.exists(eq(testCoords.getId())));

        var savedCoords = this.coordinatesService.getById(testCoords.getId());
        assertTrue(savedCoords.hasRefToId(testOffer.getId()));

    }

    @Test
    public void givenRequestContainingAlreadySavedCoords_requestShouldBeAddedToCoords() {
        this.coordinatesService.save(testCoords);

        Coordinates duplicateCoordinates = new Coordinates().setLatitude(testCoords.getLatitude()).setLongitude(testCoords.getLongitude());

        HelpOfferModel testOffer = new HelpOfferModel().setCoordinates(duplicateCoordinates);

        Coordinates updatedExistingCoordinates = this.coordinatesService.handleHelpModelCoordinateAdd(testOffer);

        assertEquals(testCoords.getId(), updatedExistingCoordinates.getId());


    }

    @Test
    public void updatingSavedCoordsWithNoOtherRefs_coordsShouldBeUpdatedAccordingly() {
        HelpOfferModel testOffer = new HelpOfferModel().setCoordinates(testCoords).generateId();

        this.coordinatesService.handleHelpModelCoordinateAdd(testOffer);

        CoordinatesUpdate update = new CoordinatesUpdate().setLatitude(11.22).setLongitude(22.11);

        var updatedCoordinates = this.coordinatesService.handleHelpModelCoordinateUpdate(testOffer, update);

        assertEquals(testCoords.getId(), testOffer.getCoordinates().getId());

        assertEquals(updatedCoordinates.getLatitude(), update.getLatitude(), 0);
    }

    @Test
    public void addingHelpModelWithCoordinatesAlreadyExisting_modelShouldBeAddedToCoords() {
        this.coordinatesService.save(testCoords.addHelpModel(new HelpOfferModel().generateId()));

        var testCoordsObjId = testCoords.getId();

        HelpOfferModel testOffer = new HelpOfferModel().setCoordinates(testCoords.generateId()).generateId();

        this.coordinatesService.handleHelpModelCoordinateAdd(testOffer);

        var testCoordsWithTwoReferences = this.coordinatesService.getById(testCoordsObjId);

        assertEquals(testCoordsWithTwoReferences.getHelpOffers().size(), 2);
    }

    @Test
    public void updatingCoordsWithResultExisting_modelShouldBeAddedToExistingCoordsAndOldCoordsShouldBeDeleted() {
        HelpOfferModel testOffer = new HelpOfferModel().setCoordinates(testCoords).generateId();

        this.coordinatesService.handleHelpModelCoordinateAdd(testOffer);

        CoordinatesUpdate update = new CoordinatesUpdate().setLatitude(11.22).setLongitude(22.11);

        var existingCoords = this.coordinatesService.save(new Coordinates().setLatitude(11.22).setLongitude(22.11));

        var updatedAddress = this.coordinatesService.handleHelpModelCoordinateUpdate(testOffer, update);

        assertEquals(existingCoords.getId(), updatedAddress.getId());

        assertFalse(this.coordinatesService.exists(eq(testCoords.getId())));
    }


    @Test
    public void updatingCoordsWithMultipleRefsAndUpdatedCoordsNotExisting_newCoordsShouldBeSavedAndModelShouldBeAdded() {
        testCoords.setHelpRequests(Collections.singletonList(new HelpRequestModel().setCoordinates(testCoords).generateId().getId()));

        HelpOfferModel testOffer = new HelpOfferModel().setCoordinates(testCoords).generateId();
        testCoords.setHelpOffers(Collections.singletonList(testOffer.getId()));

        this.coordinatesService.save(testCoords);

        CoordinatesUpdate update = new CoordinatesUpdate().setLatitude(11.22).setLongitude(22.11);

        var updatedAddress = this.coordinatesService.handleHelpModelCoordinateUpdate(testOffer, update);

        assertTrue(updatedAddress.noOtherRefsBesideId(testOffer.getId()));

        assertTrue(this.coordinatesService.getById(testCoords.getId()).getHelpOffers().isEmpty());

    }

    @Test
    public void updatingCoordsWithMultipleRefsAndUpdateExisting_refShouldBeDeletedInOldCoordsAndAddedToExistingOne() {
        testCoords.setHelpRequests(Collections.singletonList(new HelpRequestModel().setCoordinates(testCoords).generateId().getId()));

        HelpOfferModel testOffer = new HelpOfferModel().setCoordinates(testCoords).generateId();
        testCoords.setHelpOffers(Collections.singletonList(testOffer.getId()));

        this.coordinatesService.save(testCoords);

        CoordinatesUpdate update = new CoordinatesUpdate().setLatitude(11.22).setLongitude(22.11);

        var existingCoords = new Coordinates().setLatitude(11.22).setLongitude(22.11);

        this.coordinatesService.save(existingCoords);

        var updatedAddress = this.coordinatesService.handleHelpModelCoordinateUpdate(testOffer, update);

        assertEquals(existingCoords.getId(), updatedAddress.getId());
        assertTrue(updatedAddress.hasRefToId(testOffer.getId()));
        assertTrue(this.coordinatesService.getById(testCoords.getId()).getHelpOffers().isEmpty());
    }

    @Test
    public void deletingModelWithCoordsWithMultipleReferences_refShouldBeRemoved() {
        testCoords.setHelpRequests(Collections.singletonList(new ObjectId()));

        HelpOfferModel testOffer = new HelpOfferModel().setCoordinates(testCoords).generateId();
        testCoords.setHelpOffers(Collections.singletonList(testOffer.getId()));

        this.coordinatesService.save(testCoords);

        this.coordinatesService.handleHelpModelCoordinateDelete(testOffer);

        var editedCoords = this.coordinatesService.getById(testCoords.getId());

        assertTrue(editedCoords.getHelpOffers().isEmpty());

        assertEquals(editedCoords.getHelpRequests().size(), 1);

    }

    @Test
    public void deletingModelWithCoordsWithoutOtherRefs_coordsShouldBeDeleted() {

        HelpOfferModel testOffer = new HelpOfferModel().setCoordinates(testCoords).generateId();
        testCoords.setHelpOffers(Collections.singletonList(testOffer.getId()));

        this.coordinatesService.save(testCoords);

        this.coordinatesService.handleHelpModelCoordinateDelete(testOffer);

        assertFalse(this.coordinatesService.exists(eq(testCoords.getId())));

    }


    @Before
    public void clearDb() {
        clear.clearDb();
    }


}
