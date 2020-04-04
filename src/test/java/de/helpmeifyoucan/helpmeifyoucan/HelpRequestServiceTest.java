package de.helpmeifyoucan.helpmeifyoucan;


import de.helpmeifyoucan.helpmeifyoucan.models.Coordinates;
import de.helpmeifyoucan.helpmeifyoucan.models.HelpRequestModel;
import de.helpmeifyoucan.helpmeifyoucan.models.UserModel;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.HelpRequestUpdate;
import de.helpmeifyoucan.helpmeifyoucan.services.HelpRequestModelService;
import de.helpmeifyoucan.helpmeifyoucan.utils.HelpCategoryEnum;
import de.helpmeifyoucan.helpmeifyoucan.utils.PostStatusEnum;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class HelpRequestServiceTest {

    @Autowired
    private StaticDbClear clear;

    @Autowired
    private HelpRequestModelService requestService;

    private HelpRequestModel testRequest;

    private UserModel testUser;

    private Coordinates testCoordinates;

    @Before
    public void setUpTest() {
        testCoordinates = new Coordinates().setLatitude(50.00).setLongitude(20.00);
        testUser = new UserModel();
        testUser.setId(new ObjectId());
        testRequest = new HelpRequestModel().setUser(testUser.getId()).setCoordinates(testCoordinates).setCategory(HelpCategoryEnum.PersonalAssistance).setStatus(PostStatusEnum.ACTIVE).setDescription("Delphine sind schwule haie");
    }

    @Test
    public void givenOfferToSave_offerShouldBeSavedAndBeRetrieved() {
        this.requestService.saveNewRequest(testRequest, testUser.getId());

        assertEquals(testRequest, this.requestService.getById(testRequest.getId()));
    }

    @Test
    public void givenOfferAndUpdateToPerform_offerShouldBeUpdatedAccordingly() {
        this.requestService.saveNewRequest(testRequest, testUser.getId());

        HelpRequestUpdate requestUpdate = new HelpRequestUpdate().setStatus(PostStatusEnum.INACTIVE).setCategory(HelpCategoryEnum.Errands).setDateDue(new Date());

        var updatedRequest = this.requestService.update(testRequest.getId(), requestUpdate, testUser.getId());

        assertEquals(updatedRequest.getStatus(), PostStatusEnum.INACTIVE);
        assertEquals(updatedRequest.getCategory(), requestUpdate.category);
        assertEquals(updatedRequest.getDateDue(), requestUpdate.dateDue);
    }

    @Test
    public void givenTwoObjectsMatchingTheFilter_updateManyShouldUpdateTwo() {
        this.requestService.saveNewRequest(testRequest, testUser.getId());

        var newRequest = new HelpRequestModel().setUser(new ObjectId()).setCoordinates(testCoordinates).setCategory(HelpCategoryEnum.PersonalAssistance).setStatus(PostStatusEnum.ACTIVE).setDescription("Delphine sind schwule haie");

        this.requestService.saveNewRequest(newRequest, newRequest.getUser());

        long updatedOffers = this.requestService.updateEmbeddedCoordinates(testCoordinates.setLatitude(25.00));

        assertEquals(updatedOffers, 2);
    }


    @Before
    public void clearDb() {
        clear.clearDb();
    }
}
