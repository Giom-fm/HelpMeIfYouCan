package de.helpmeifyoucan.helpmeifyoucan.services;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import de.helpmeifyoucan.helpmeifyoucan.models.Coordinates;
import de.helpmeifyoucan.helpmeifyoucan.models.HelpOfferModel;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.CoordinatesUpdate;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.HelpOfferUpdate;
import de.helpmeifyoucan.helpmeifyoucan.utils.errors.HelpOfferModelExceptions;
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

    private void createIndex() {
        IndexOptions options = new IndexOptions();
        options.unique(true);

        super.createIndex(Indexes.ascending("datePublished", "user"), options);
    }

    /**
     * We want to update a offer, we do so by first checking if the updating user has permission to do so, if yes we directly update the offer in the db
     *
     * @param offerToUpdate the offer to update
     * @param update        the update to perform
     * @param updatingUser  the user performing the update
     * @return the updated offer
     */
    public HelpOfferModel update(ObjectId offerToUpdate, HelpOfferUpdate update, ObjectId updatingUser) {

        var updateFilter = and(eq(offerToUpdate), in("user", updatingUser));


        return Optional.ofNullable(super.updateExistingFields(updateFilter, update.toFilter())).orElseThrow(() -> new HelpOfferModelExceptions.HelpOfferNotFoundException(offerToUpdate));

    }

    /**
     * We want to save a new helpOffer, so we first generate an id,  to then save the embedded coordinates and finally save the offer with a added ref to the added coordinates
     *
     * @param offer the new Offer to save
     * @param user  the user to save
     * @return the saved offer
     */
    public HelpOfferModel saveNewOffer(HelpOfferModel offer, ObjectId user) {

        offer.generateId();

        var addedCoords = this.coordinatesService.handleHelpModelCoordinateAdd(offer);

        this.updateEmbeddedCoordinates(addedCoords);

        return this.save(offer.setCoordinates(addedCoords).setUser(user));
    }

    /**
     * We want to update the embedded coordinates a offer has, so we first check if the updating user matches the user registered in the offers ref and then delegate the updating
     * to the embedded coordinates controller. to then update the db offers db model
     *
     * @param offerId      the offer to update
     * @param update       the update to perform
     * @param updatingUser the updating user to be verified
     * @return the updated offer
     */
    public HelpOfferModel handleCoordinatesUpdate(ObjectId offerId, CoordinatesUpdate update, ObjectId updatingUser) {
        var offerToUpdate = userIsAllowedToEditModel(offerId, updatingUser);

        var updatedCoordinates = this.coordinatesService.handleHelpModelCoordinateUpdate(offerToUpdate, update);

        this.updateEmbeddedCoordinates(updatedCoordinates);

        return offerToUpdate.setCoordinates(updatedCoordinates);

    }


    /**
     * We want to delete a offer, we do so by first checking if the user is permitted to do so by checking the offers user ref,
     * if permitted, the offer will be deleted and the deletion of its embedded coordinates will be handled by the embedded coordiantes controller
     * the coordinates returned by this controller will then be updated in the offer tables model refs
     *
     * @param requestToDelete the request id to be deleted
     * @param deletingUser    the user deleting
     * @return the deleted offer
     */
    public HelpOfferModel deleteRequest(ObjectId requestToDelete, ObjectId deletingUser) {

        var deleteFilter = and(eq(requestToDelete), in("user", deletingUser));

        var deletedOffer = Optional.ofNullable(this.findOneAndDelete(deleteFilter)).orElseThrow(() -> new HelpOfferModelExceptions.HelpOfferNotFoundException(requestToDelete));

        var deletedCoords = this.coordinatesService.handleHelpModelCoordinateDelete(deletedOffer);

        this.updateEmbeddedCoordinates(deletedCoords);

        return deletedOffer;

    }

    /**
     * We want the data integrity to be intact, so once we register an coordinates update, we will update the models holding refs to it
     *
     * @param updatedCoords the updated coords
     * @return number of objects updated
     */

    public long updateEmbeddedCoordinates(Coordinates updatedCoords) {
        var filter = in("coordinates._id", updatedCoords.getId());

        var update = set("coordinates", updatedCoords);

        return this.updateMany(filter, update).getModifiedCount();
    }

    private HelpOfferModel userIsAllowedToEditModel(ObjectId offerId, ObjectId userId) {

        return Optional.ofNullable(getByFilter(and(eq(offerId), in("user", userId)))).orElseThrow(() -> new HelpOfferModelExceptions.HelpOfferNotFoundException(offerId));


    }
}
