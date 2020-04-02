package de.helpmeifyoucan.helpmeifyoucan.services;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import de.helpmeifyoucan.helpmeifyoucan.models.AbstractHelpModel;
import de.helpmeifyoucan.helpmeifyoucan.models.Coordinates;
import de.helpmeifyoucan.helpmeifyoucan.models.HelpRequestModel;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.CoordinatesUpdate;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

@Service
public class CoordinatesService extends AbstractService<Coordinates> {

    @Autowired
    public CoordinatesService(MongoDatabase dataBase) {
        super(dataBase);

        super.createCollection("coordinates", Coordinates.class);
        this.createIndex();
    }

    public void createIndex() {
        IndexOptions options = new IndexOptions();
        options.unique(true);

        super.createIndex(Indexes.ascending("latitude", "longitude"), options);
    }

    public Coordinates save(Coordinates coordinates) {
        return super.save(coordinates.calculateHashCode());
    }

    public Coordinates get(ObjectId id) {
        return super.getById(id);
    }

    public boolean exists(Bson filter) {
        return this.getOptional(filter).isPresent();
    }

    public boolean deleteById(ObjectId id) {
        return super.delete(eq(id)).wasAcknowledged();
    }

    public Optional<Coordinates> getOptional(Bson filter) {
        return super.getOptional(filter);
    }

    public Coordinates updateExistingField(ObjectId id, Bson updatedFields) {
        var filter = eq(id);

        var updatedCoordinates = super.updateExistingFields(filter, updatedFields);

        //todo exception

        return updatedCoordinates;

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


        if (helpModel.getCoordinates() == null) {
            //todo exception
        }

        return this.updateCoordinates(helpModel, update);

    }

    public <T extends AbstractHelpModel> Coordinates handleHelpModelCoordinateDelete(T helpModel) {
        var coordinatesToDelete = this.getById(helpModel.getCoordinates().getId());

        coordinatesToDelete.removeHelpModel(helpModel);

        var isRequest = getModelClass(helpModel);

        if (coordinatesToDelete.noHelpModelRefs()) {
            deleteById(coordinatesToDelete.getId());
            return coordinatesToDelete;
        } else {
            return this.updateClassCorrespondingField(coordinatesToDelete, isRequest);
        }


    }

    public <T extends AbstractHelpModel> Coordinates updateCoordinates(T helpModel, CoordinatesUpdate update) {

        var coordsToUpdate = this.getOptional(eq(helpModel.getCoordinates().getId()));

        if (coordsToUpdate.isEmpty() || !coordsToUpdate.get().hasRefToId(helpModel.getId())) {
            //TODO EXCEPTION
        }

        var existingCoordsToUpdate = coordsToUpdate.get();
        var mergedCoords = existingCoordsToUpdate.mergeWithUpdate(update);

        var filter = eq("hashCode", mergedCoords.getHashCode());
        var updatedCoordinatesExistingInDb = this.getOptional(filter);

        if (existingCoordsToUpdate.noOtherRefsBesideId(helpModel.getId())) {
            return this.updateCoordinatesWithNoOtherRefs(update, existingCoordsToUpdate, updatedCoordinatesExistingInDb, helpModel);
        } else {
            return this.updateCoordinatesWithOtherRefs(mergedCoords, existingCoordsToUpdate, updatedCoordinatesExistingInDb, helpModel);
        }

    }


    public <T extends AbstractHelpModel> Coordinates updateCoordinatesWithOtherRefs(Coordinates mergedAddress, Coordinates existingAddress, Optional<Coordinates> updatedAddressExistingInDB, T helpModel) {
        this.deleteModelFromCoordinates(existingAddress, helpModel);

        if (updatedAddressExistingInDB.isPresent()) {
            var existingAddressInDb = updatedAddressExistingInDB.get();
            //TODO return to calling class to exchange fields
            return this.addHelpModelToCoordinates(existingAddressInDb, helpModel);
        } else {
            return this.save(mergedAddress);
        }
    }


    public <T extends AbstractHelpModel> Coordinates updateCoordinatesWithNoOtherRefs(CoordinatesUpdate coordinatesUpdate, Coordinates existingCoordinates, Optional<Coordinates> updatedDbAddressIfExisting, T helpModel) {
        if (updatedDbAddressIfExisting.isPresent()) {
            this.deleteModelFromCoordinates(existingCoordinates, helpModel);
            return this.addHelpModelToCoordinates(updatedDbAddressIfExisting.get(), helpModel);
        } else {
            return this.updateExistingField(existingCoordinates.getId(), coordinatesUpdate.toFilter());
        }
    }

    public <T extends AbstractHelpModel> Coordinates addHelpModelToCoordinates(Coordinates coordinates, T helpModel) {

        var isRequest = this.getModelClass(helpModel);
        coordinates.addHelpModel(helpModel);
        return this.updateClassCorrespondingField(coordinates, isRequest);
    }

    public <T extends AbstractHelpModel> void deleteModelFromCoordinates(Coordinates coordinates, T helpModel) {
        var isRequest = this.getModelClass(helpModel);
        coordinates.removeHelpModel(helpModel);
        this.updateClassCorrespondingField(coordinates, isRequest);
    }

    public <T extends AbstractHelpModel> Coordinates saveNewCoordinates(T helpModel) {

        var coordinatesToSave = helpModel.getCoordinates();

        coordinatesToSave.addHelpModel(helpModel);

        return this.save(coordinatesToSave);
    }

    private Coordinates updateClassCorrespondingField(Coordinates coordinates, boolean isRequest) {
        Bson updatedFields;
        if (isRequest) {
            updatedFields = set("requests", coordinates.getHelpRequests());
        } else {
            updatedFields = set("offers", coordinates.getHelpOffers());
        }
        return this.updateExistingField(coordinates.getId(), updatedFields);

    }

    private <T extends AbstractHelpModel> boolean getModelClass(T helpModel) {
        return helpModel instanceof HelpRequestModel;
    }


}
