package de.helpmeifyoucan.helpmeifyoucan;

import de.helpmeifyoucan.helpmeifyoucan.models.Coordinates;
import de.helpmeifyoucan.helpmeifyoucan.services.CoordinatesService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.mongodb.client.model.Filters.eq;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class CoordinatesServiceTest {

    @Autowired
    private CoordinatesService coordinatesService;

    private Coordinates testCoords;

    @Before
    public void setUpTest() {
        testCoords = new Coordinates().setLatitude(50.00).setLatitude(20.00);
    }

    @Test
    public void givenCoordinates_ItShouldBeSavedAndRetrievedCorrectly() {
        this.coordinatesService.save(testCoords);

        assertEquals(testCoords, this.coordinatesService.get(testCoords.getId()));
    }

    @Test
    public void givenObjectIdOfSavedObject_ExistsShouldBeTrue() {
        this.coordinatesService.save(testCoords);

        assertTrue(this.coordinatesService.exists(eq(testCoords.getId())));

    }

    
    @Before
    public void clearDb() {
        this.coordinatesService.getCollection().drop();
        this.coordinatesService.createIndex();
    }


}
