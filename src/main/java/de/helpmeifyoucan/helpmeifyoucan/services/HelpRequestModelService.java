package de.helpmeifyoucan.helpmeifyoucan.services;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import de.helpmeifyoucan.helpmeifyoucan.models.Coordinates;
import de.helpmeifyoucan.helpmeifyoucan.models.HelpRequestModel;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.CoordinatesUpdate;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.HelpRequestUpdate;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.set;

@Service
public class HelpRequestModelService extends AbstractService<HelpRequestModel> {


    private CoordinatesService coordinatesService;

    @Autowired
    public HelpRequestModelService(MongoDatabase dataBase, CoordinatesService coordinatesService) {
        super(dataBase);
        super.createCollection("helpRequests", HelpRequestModel.class);
        this.createIndex();
        this.coordinatesService = coordinatesService;
    }

    public void createIndex() {
        IndexOptions options = new IndexOptions();
        options.unique(true);

        super.createIndex(Indexes.ascending("datePublished"), options);
    }

    public HelpRequestModel save(HelpRequestModel requestModel) {
        return super.save(requestModel);
    }

    public HelpRequestModel getById(ObjectId id) {
        var filter = eq(id);
        return super.getByFilter(filter);
    }

    public boolean exists(Bson filter) {
        return this.getOptional(filter).isPresent();
    }

    public Optional<HelpRequestModel> getOptional(Bson filter) {
        return super.getOptional(filter);
    }


    public HelpRequestModel update(ObjectId requestToUpdate, HelpRequestUpdate update, ObjectId updatingUser) {

        var updateFilter = and(eq(requestToUpdate), in("user", updatingUser));

        var updatedRequest = super.updateExistingFields(updateFilter, update.toFilter());
        if (updatedRequest == null) {
            //TODO exception
        }

        return updatedRequest;
    }

    public HelpRequestModel saveNewRequest(HelpRequestModel request, ObjectId user) {

        request.generateId();
        var requestWithCoordsAdded = request.setCoordinates(this.coordinatesService.handleHelpModelCoordinateAdd(request));

        return this.save(requestWithCoordsAdded);
    }

    public HelpRequestModel handleCoordinatesUpdate(ObjectId requestId, CoordinatesUpdate update, ObjectId updatingUser) {
        var requestToUpdate = getByRequestIdAndUser(requestId, updatingUser);

        var updatedCoordinates = this.coordinatesService.handleHelpModelCoordinateUpdate(requestToUpdate, update);

        return this.updateCoordinatesField(requestToUpdate, updatedCoordinates);
    }

    public void deleteRequest(ObjectId requestToDelete, ObjectId deletingUser) {

        var deleteFilter = and(eq(requestToDelete), in("user", deletingUser));

        if (super.delete(deleteFilter).getDeletedCount() == 0) {
            //TODO exception
        }

    }


    private HelpRequestModel updateCoordinatesField(HelpRequestModel request, Coordinates coordinates) {
        request.setCoordinates(coordinates);
        var updatedAddress = set("coordinates", coordinates);
        return super.updateExistingFields(eq(request.getId()), updatedAddress);
    }

    public HelpRequestModel getByRequestIdAndUser(ObjectId requestId, ObjectId userId) {

        var userHasPermission = super.getOptional(and(eq(requestId), in("user", userId)));

        //TODO
        return userHasPermission.orElseThrow();

    }


}
