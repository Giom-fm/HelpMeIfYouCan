package de.helpmeifyoucan.helpmeifyoucan.services;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import de.helpmeifyoucan.helpmeifyoucan.models.Coordinates;
import de.helpmeifyoucan.helpmeifyoucan.models.HelpOfferModel;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.CoordinatesUpdate;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.HelpOfferUpdate;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.set;

public class HelpOfferModelService extends AbstractService<HelpOfferModel> {

    private CoordinatesService coordinatesService;

    @Autowired
    public HelpOfferModelService(MongoDatabase dataBase, CoordinatesService coordinatesService) {
        super(dataBase);
        super.createCollection("helpRequests", HelpOfferModel.class);
        this.createIndex();
        this.coordinatesService = coordinatesService;
    }

    public void createIndex() {
        IndexOptions options = new IndexOptions();
        options.unique(true);

        super.createIndex(Indexes.ascending("datePublished"), options);
    }

    public HelpOfferModel save(HelpOfferModel requestModel) {
        return super.save(requestModel);
    }

    public HelpOfferModel getById(ObjectId id) {
        var filter = eq(id);
        return super.getByFilter(filter);
    }

    public boolean exists(Bson filter) {
        return this.getOptional(filter).isPresent();
    }

    public Optional<HelpOfferModel> getOptional(Bson filter) {
        return super.getOptional(filter);
    }

    public void deleteById(ObjectId id) {
        super.delete(eq(id));
    }

    public HelpOfferModel update(ObjectId requestToUpdate, HelpOfferUpdate update, ObjectId updatingUser) {

        var updateFilter = and(eq(requestToUpdate), in("user", updatingUser));

        var updatedRequest = super.updateExistingFields(updateFilter, update.toFilter());
        if (updatedRequest == null) {
            //TODO exception
        }

        return updatedRequest;
    }

    public HelpOfferModel saveNewRequest(HelpOfferModel request, ObjectId user) {

        request.generateId();
        var requestWithCoordsAdded = request.setCoordinates(this.coordinatesService.handleHelpModelCoordinateAdd(request));

        return this.save(requestWithCoordsAdded);
    }

    public HelpOfferModel handleCoordinatesUpdate(ObjectId offerId, CoordinatesUpdate update, ObjectId updatingUser) {
        var offerToUpdate = getByRequestIdAndUser(offerId, updatingUser);

        var updatedCoordinates = this.coordinatesService.handleHelpModelCoordinateUpdate(offerToUpdate, update);

        return this.updateCoordinatesField(offerToUpdate, updatedCoordinates);
    }

    public void deleteRequest(ObjectId requestToDelete, ObjectId deletingUser) {

        var deleteFilter = and(eq(requestToDelete), in("user", deletingUser));

        if (super.delete(deleteFilter).getDeletedCount() == 0) {
            //TODO exception
        }

    }

    private HelpOfferModel updateCoordinatesField(HelpOfferModel offer, Coordinates coordinates) {
        offer.setCoordinates(coordinates);
        var updatedAddress = set("coordinates", coordinates);
        return super.updateExistingFields(eq(offer.getId()), updatedAddress);
    }

    public HelpOfferModel getByRequestIdAndUser(ObjectId requestId, ObjectId userId) {

        var userHasPermission = super.getOptional(and(eq(requestId), in("user", userId)));

        //TODO
        return userHasPermission.orElseThrow();

    }
}
