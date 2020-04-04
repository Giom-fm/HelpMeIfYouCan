package de.helpmeifyoucan.helpmeifyoucan.services;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import de.helpmeifyoucan.helpmeifyoucan.models.AbstractHelpModel;
import de.helpmeifyoucan.helpmeifyoucan.models.Coordinates;
import de.helpmeifyoucan.helpmeifyoucan.models.HelpRequestModel;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.CoordinatesUpdate;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


    public <T extends AbstractHelpModel> Coordinates handleHelpModelCoordinateAdd(T helpModel) {
        var coordinatesToSave = helpModel.getCoordinates();

        var addressFilter = eq("hashCode", coordinatesToSave.calculateHashCode().getHashCode());

        var existingAddress = this.getOptional(addressFilter);

        if (existingAddress.isPresent()) {
            return addHelpModelToCoordinates(existingAddress.get(), helpModel);
        } else {
            return this.saveNewCoordinates(helpModel);
        }

    }


    public <T extends AbstractHelpModel> Coordinates handleHelpModelCoordinateUpdate(T helpModel, CoordinatesUpdate update) {

        var modelIsAllowedToEditCoordinates = and(eq(helpModel.getCoordinates().getId()), or(in("helpRequests", helpModel.getId()), in("helpOffers", helpModel.getId())));


        //TODO exception
        var coordsToUpdate = this.getOptional(modelIsAllowedToEditCoordinates).orElseThrow();
        return this.updateCoordinates(helpModel, coordsToUpdate, update);

    }

    public <T extends AbstractHelpModel> Coordinates handleHelpModelCoordinateDelete(T helpModel) {

        var modelIsAllowedToDeleteCoordinates = and(eq(helpModel.getCoordinates().getId()), or(in("helpRequests", helpModel.getId()), in("helpOffers", helpModel.getId())));

        //TODO
        var coordinatesToDelete = Optional.ofNullable(this.getByFilter(modelIsAllowedToDeleteCoordinates)).orElseThrow();

        return this.deleteModelFromCoordinates(coordinatesToDelete, helpModel);
    }

    private <T extends AbstractHelpModel> Coordinates updateCoordinates(T helpModel, Coordinates coordsToUpdate, CoordinatesUpdate update) {

        if (coordsToUpdate.noOtherRefsBesideId(helpModel.getId())) {
            return this.updateCoordinatesWithNoOtherRefs(coordsToUpdate, update, helpModel);
        } else {
            return this.updateCoordinatesWithOtherRefs(coordsToUpdate, update, helpModel);
        }

    }


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


    private <T extends AbstractHelpModel> Coordinates updateCoordinatesWithNoOtherRefs(Coordinates coordinatesToUpdate, CoordinatesUpdate update, T helpModel) {

        var updatedCoordinatesExistingInDb = this.mergeCoordsWithUpdateAndCheckDbForMatchingAddress(coordinatesToUpdate, update);

        if (updatedCoordinatesExistingInDb.isPresent()) {
            this.deleteModelFromCoordinates(coordinatesToUpdate, helpModel);
            return this.addHelpModelToCoordinates(updatedCoordinatesExistingInDb.get(), helpModel);
        } else {
            return super.updateExistingFields(eq(coordinatesToUpdate.getId()), update.toFilter());
        }
    }

    private <T extends AbstractHelpModel> Coordinates addHelpModelToCoordinates(Coordinates coordinates, T helpModel) {

        var isRequest = this.getModelClass(helpModel);
        coordinates.addHelpModel(helpModel);
        return this.updateClassCorrespondingField(coordinates, isRequest);
    }

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

    public <T extends AbstractHelpModel> Coordinates saveNewCoordinates(T helpModel) {

        var coordinatesToSave = helpModel.getCoordinates();

        coordinatesToSave.addHelpModel(helpModel);

        return this.save(coordinatesToSave);
    }

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
