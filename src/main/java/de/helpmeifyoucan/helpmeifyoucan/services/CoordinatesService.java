package de.helpmeifyoucan.helpmeifyoucan.services;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import de.helpmeifyoucan.helpmeifyoucan.models.AbstractHelpModel;
import de.helpmeifyoucan.helpmeifyoucan.models.Coordinates;
import de.helpmeifyoucan.helpmeifyoucan.models.HelpRequestModel;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.CoordinatesUpdate;
import de.helpmeifyoucan.helpmeifyoucan.utils.errors.CoordinatesExceptions;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.set;

@Service
public class CoordinatesService extends AbstractService<Coordinates> {

    @Autowired
    public CoordinatesService(MongoDatabase dataBase) {
        super(dataBase);

        super.createCollection("coordinates", Coordinates.class);
        this.createIndex();
    }

    private void createIndex() {
        IndexOptions options = new IndexOptions();
        options.unique(true);

        super.createIndex(Indexes.ascending("latitude", "longitude"), options);
    }

    public Coordinates save(Coordinates coordinates) {
        return super.save(coordinates.calculateHashCode());
    }

    public List<Coordinates> getAll() {
        var exists = Filters.exists("id");
        return super.getAllByFilter(exists);
    }

    public List<Coordinates> getAllRequests() {
        var requestFilter = where("this.helpRequests.length > 0");
        return getAllByFilter(requestFilter);
    }

    public List<Coordinates> getAllOffers() {
        var offerFilter = where("this.helpOffers.length > 0");
        return getAllByFilter(offerFilter);
    }


    /**
     * We want to add new or existing coordinates to the collection and return the new object
     *
     * @param helpModel the model containing the coordinates to add
     * @return the added coordinates
     */
    public <T extends AbstractHelpModel> Coordinates handleHelpModelCoordinateAdd(T helpModel) {

        return addNewCoordinates(helpModel);
    }


    /**
     * We want to perform an update on the given coordinates. we do so by first checking if coordinates refer to the given model
     *
     * @param helpModel the model containing the coordinates
     * @param update    the update to perform
     * @param <T>       the runtime class
     * @return the updated coordinates
     */

    public <T extends AbstractHelpModel> Coordinates handleHelpModelCoordinateUpdate(T helpModel, CoordinatesUpdate update) {

        var coordsToUpdate = this.helpModelHasPermissionToUpdateCoordinates(helpModel.getCoordinates().getId(), helpModel.getId());
        return this.updateCoordinates(helpModel, coordsToUpdate, update);

    }


    /**
     * We want to delete a model containing coordinates, so we first check if the model has permission to do so and then proceed with deleting the coordinates
     *
     * @param helpModel the model containing the coordinates to be deleted
     * @param <T>       model runtime class
     * @return the deleted /updated coordinates
     */
    public <T extends AbstractHelpModel> Coordinates handleHelpModelCoordinateDelete(T helpModel) {

        var coordinatesToDelete = this.helpModelHasPermissionToUpdateCoordinates(helpModel.getCoordinates().getId(), helpModel.getId());

        return this.deleteModelFromCoordinates(coordinatesToDelete, helpModel);
    }

    /**
     * We want to update coordinates, as we cannot update coordinates other models refer to,  we first check for its references,
     * depending if it has other references we call different methods
     *
     * @param helpModel      the model containing the coordinates
     * @param coordsToUpdate the complete coodinates object retrieved from db
     * @param update         the update to perform
     * @param <T>            the models runtime class
     * @return the updated coordinates
     */
    private <T extends AbstractHelpModel> Coordinates updateCoordinates(T helpModel, Coordinates coordsToUpdate, CoordinatesUpdate update) {

        if (coordsToUpdate.noOtherRefsBesideId(helpModel.getId())) {
            return this.updateCoordinatesWithNoOtherRefs(coordsToUpdate, update, helpModel);
        } else {
            return this.updateCoordinatesWithOtherRefs(coordsToUpdate, update, helpModel);
        }

    }

    /**
     * We want to add new coordinates, we do so by checking if the coordinates to be added already exists, if yes, we will add the models to the coordinates refs,
     * if no we will save a new object
     *
     * @param helpModel the calling helpmodel
     * @param <T>       the helpmodels runtime class
     * @return the added coordinates
     */
    private <T extends AbstractHelpModel> Coordinates addNewCoordinates(T helpModel) {
        var coordinatesToSave = helpModel.getCoordinates();

        var addressFilter = eq("hashCode", coordinatesToSave.calculateHashCode().getHashCode());

        var existingAddress = this.getOptional(addressFilter);

        if (existingAddress.isPresent()) {
            return addHelpModelToCoordinates(existingAddress.get(), helpModel);
        } else {
            return this.saveNewCoordinates(helpModel);
        }
    }

    public <T extends AbstractHelpModel> Coordinates saveNewCoordinates(T helpModel) {

        var coordinatesToSave = helpModel.getCoordinates();

        coordinatesToSave.addHelpModel(helpModel);

        return this.save(coordinatesToSave);
    }


    /**
     * We want to update coordinates, which do have other refs beside the updating model. so we delete the model from this coordinates refs and
     * then check if a coordinates obj equalsing the updated coordinates exists. if yes we add a ref to and from this coordinates, if no we just save the updated coordinates
     *
     * @param coordinatesToUpdate the coords to update
     * @param update              the uodate to perfrom
     * @param helpModel           the calling helpmodel
     * @param <T>                 the helpmodels runtime class
     * @return the updated coordinates
     */
    private <T extends AbstractHelpModel> Coordinates updateCoordinatesWithOtherRefs(Coordinates coordinatesToUpdate, CoordinatesUpdate update, T helpModel) {

        this.deleteModelFromCoordinates(coordinatesToUpdate, helpModel);
        var updatedCoordinatesExistingInDb = this.mergeCoordsWithUpdateAndCheckDbForMatchingAddress(coordinatesToUpdate, update);

        if (updatedCoordinatesExistingInDb.isPresent()) {
            var existingAddressInDb = updatedCoordinatesExistingInDb.get();
            return this.addHelpModelToCoordinates(existingAddressInDb, helpModel);
        } else {
            return this.save(coordinatesToUpdate.clearRefsAndAddId(helpModel).generateId());
        }
    }


    /**
     * We want to update coordinates without any other refs, so we check the db if the updated address already exists, if yes we add the models ref to these coordinates,
     * if no, we just update the given coordinates
     *
     * @param coordinatesToUpdate the coordinates to update
     * @param update              the update to perform
     * @param helpModel           the calling helpmodel
     * @param <T>                 the models runtime class
     * @return the updated coordinates
     */
    private <T extends AbstractHelpModel> Coordinates updateCoordinatesWithNoOtherRefs(Coordinates coordinatesToUpdate, CoordinatesUpdate update, T helpModel) {

        var updatedCoordinatesExistingInDb = this.mergeCoordsWithUpdateAndCheckDbForMatchingAddress(coordinatesToUpdate, update);

        if (updatedCoordinatesExistingInDb.isPresent()) {
            this.deleteModelFromCoordinates(coordinatesToUpdate, helpModel);
            return this.addHelpModelToCoordinates(updatedCoordinatesExistingInDb.get(), helpModel);
        } else {
            return super.updateExistingFields(eq(coordinatesToUpdate.getId()), update.toFilter());
        }
    }

    /**
     * We want to update the coordinates model refs, we do so by adding a field into the coordinates model refs
     *
     * @param coordinates the coordinates to add the model ref to
     * @param helpModel   the model to be added
     * @param <T>         models the runtime class
     * @return the updated coordinates
     */
    private <T extends AbstractHelpModel> Coordinates addHelpModelToCoordinates(Coordinates coordinates, T helpModel) {

        var isRequest = this.getModelClass(helpModel);
        coordinates.addHelpModel(helpModel);
        return this.updateClassCorrespondingField(coordinates, isRequest);
    }

    /**
     * We want to delete a model ref from coordinates refs, we do so by retrieving it from the db, removing the ref and then check if the object should be saved again
     * depending if it has any more ref, if no, we will delete it
     *
     * @param coordinates the coordinates to delete a ref from
     * @param helpModel   the calling helpmodel
     * @param <T>         the models runtime class
     * @return the updated coordinates
     */
    private <T extends AbstractHelpModel> Coordinates deleteModelFromCoordinates(Coordinates coordinates, T helpModel) {
        var isRequest = this.getModelClass(helpModel);
        coordinates.removeHelpModel(helpModel);
        if (coordinates.noHelpModelRefs()) {
            return super.findOneAndDelete(eq(coordinates.getId()));
        } else {
            return this.updateClassCorrespondingField(coordinates, isRequest);
        }
    }

    private Optional<Coordinates> mergeCoordsWithUpdateAndCheckDbForMatchingAddress(Coordinates coordsToMerge, CoordinatesUpdate update) {
        var mergedCoords = coordsToMerge.mergeWithUpdate(update);

        var filter = eq("hashCode", mergedCoords.getHashCode());
        return this.getOptional(filter);
    }

    /**
     * A model needs to be in a coordinates refs to be able to perform any updating actions on it
     *
     * @param coordinatesId the coordinates to perform actions on
     * @param modelId       the model perfroming the actions
     * @return the coordinates matching the filter
     */

    private Coordinates helpModelHasPermissionToUpdateCoordinates(ObjectId coordinatesId, ObjectId modelId) {
        var modelIsAllowedToEditCoordinates = and(eq(coordinatesId), or(in("helpRequests", modelId), in("helpOffers", modelId)));

        return Optional.ofNullable(this.getByFilter(modelIsAllowedToEditCoordinates)).orElseThrow(() -> new CoordinatesExceptions.CoordinatesNotFoundException(coordinatesId));
    }

    /**
     * Due to the helpmodels polymorphism we need to determine its runtime class to know what ref field we want to update
     *
     * @param coordinates the coordinates to update
     * @param isRequest   true if helpmodels runtime class is HelpRequestModel, false if its HelpOfferModel
     * @return the updated coordinates
     */
    private Coordinates updateClassCorrespondingField(Coordinates coordinates, boolean isRequest) {
        Bson updatedFields;
        if (isRequest) {
            updatedFields = set("helpRequests", coordinates.getHelpRequests());
        } else {
            updatedFields = set("helpOffers", coordinates.getHelpOffers());
        }
        return super.updateExistingFields(eq(coordinates.getId()), updatedFields);

    }

    private <T extends AbstractHelpModel> boolean getModelClass(T helpModel) {
        return helpModel instanceof HelpRequestModel;
    }


}
