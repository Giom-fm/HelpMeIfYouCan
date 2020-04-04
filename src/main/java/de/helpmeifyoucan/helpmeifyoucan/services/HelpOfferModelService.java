package de.helpmeifyoucan.helpmeifyoucan.services;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import de.helpmeifyoucan.helpmeifyoucan.models.Coordinates;
import de.helpmeifyoucan.helpmeifyoucan.models.HelpOfferModel;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.CoordinatesUpdate;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.HelpOfferUpdate;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.set;

@Service
public class HelpOfferModelService extends AbstractService<HelpOfferModel> {

    private CoordinatesService coordinatesService;

    @Autowired
    public HelpOfferModelService(MongoDatabase dataBase, CoordinatesService coordinatesService) {
        super(dataBase);
        super.createCollection("helpOffers", HelpOfferModel.class);
        this.createIndex();
        this.coordinatesService = coordinatesService;
    }

    public void createIndex() {
        IndexOptions options = new IndexOptions();
        options.unique(true);

        super.createIndex(Indexes.ascending("datePublished", "user"), options);
    }

    public HelpOfferModel update(ObjectId requestToUpdate, HelpOfferUpdate update, ObjectId updatingUser) {

        var updateFilter = and(eq(requestToUpdate), in("user", updatingUser));

        var updatedRequest = super.updateExistingFields(updateFilter, update.toFilter());
        if (updatedRequest == null) {
            //TODO exception
        }

        return updatedRequest;
    }

    public HelpOfferModel saveNewOffer(HelpOfferModel offer, ObjectId user) {

        offer.generateId();

        var addedCoords = this.coordinatesService.handleHelpModelCoordinateAdd(offer);

        this.updateEmbeddedCoordinates(addedCoords);

        return this.save(offer.setCoordinates(addedCoords).setUser(user));
    }

    public HelpOfferModel handleCoordinatesUpdate(ObjectId offerId, CoordinatesUpdate update, ObjectId updatingUser) {
        var offerToUpdate = userIsAllowedToEditModel(offerId, updatingUser);

        var updatedCoordinates = this.coordinatesService.handleHelpModelCoordinateUpdate(offerToUpdate, update);

        this.updateEmbeddedCoordinates(updatedCoordinates);

        return offerToUpdate.setCoordinates(updatedCoordinates);

    }

    public HelpOfferModel deleteRequest(ObjectId requestToDelete, ObjectId deletingUser) {

        var deleteFilter = and(eq(requestToDelete), in("user", deletingUser));

        //TODO
        var deletedOffer = Optional.ofNullable(this.findOneAndDelete(deleteFilter)).orElseThrow();

        var deletedCoords = this.coordinatesService.handleHelpModelCoordinateDelete(deletedOffer);

        this.updateEmbeddedCoordinates(deletedCoords);

        return deletedOffer;

    }

    public long updateEmbeddedCoordinates(Coordinates updatedCoords) {
        var filter = in("coordinates._id", updatedCoords.getId());

        var update = set("coordinates", updatedCoords);

        return this.updateMany(filter, update).getModifiedCount();
    }

    public HelpOfferModel userIsAllowedToEditModel(ObjectId requestId, ObjectId userId) {

        return Optional.ofNullable(getByFilter(and(eq(requestId), in("user", userId)))).orElseThrow();


    }
}
