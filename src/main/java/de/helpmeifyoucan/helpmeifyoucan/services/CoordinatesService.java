package de.helpmeifyoucan.helpmeifyoucan.services;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import de.helpmeifyoucan.helpmeifyoucan.models.AbstractHelpModel;
import de.helpmeifyoucan.helpmeifyoucan.models.Coordinates;
import de.helpmeifyoucan.helpmeifyoucan.models.HelpOfferModel;
import de.helpmeifyoucan.helpmeifyoucan.models.HelpRequestModel;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

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
        return super.delete(eq(id));
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

    //TODO
    public <T extends AbstractHelpModel> Coordinates handleHelpModelCoordinateUpdate(T helpModel) {
        return null;
    }

    public <T extends AbstractHelpModel> Coordinates handleHelpModelCoordinateDelete(T helpModel) {
        var coordinatesToDelete = this.getById(helpModel.getCoordinates().getId());

        if (helpModel instanceof HelpRequestModel) {
            coordinatesToDelete.deleteHelpRequest(helpModel.getId());
        } else if (helpModel instanceof HelpOfferModel) {
            coordinatesToDelete.deleteHelpOffer(helpModel.getId());
        } else {
            //todo exception
        }

        if (coordinatesToDelete.noHelpModelRefs()) {
            deleteById(coordinatesToDelete.getId());
            return coordinatesToDelete;
        } else {
            return this.updateClassCorrespondingField(coordinatesToDelete, helpModel);
        }


    }


    public <T extends AbstractHelpModel> Coordinates addHelpModelToCoordinates(Coordinates coordinates, T helpModel) {

        Bson updatedFields = null;
        if (helpModel instanceof HelpRequestModel) {
            coordinates.addHelpRequest(helpModel.getId());
        } else if (helpModel instanceof HelpOfferModel) {
            coordinates.addHelpOffer(helpModel.getId());
        } else {
            //TODO EXCEPTION
        }

        return this.updateClassCorrespondingField(coordinates, helpModel);
    }

    public <T extends AbstractHelpModel> Coordinates saveNewCoordinates(T helpModel) {

        var coordinatesToSave = helpModel.getCoordinates();

        if (helpModel instanceof HelpOfferModel) {
            coordinatesToSave.addHelpOffer(helpModel.getId());

        } else if (helpModel instanceof HelpRequestModel) {
            coordinatesToSave.addHelpRequest(helpModel.getId());

        } else {
            //todo exception

        }

        return this.save(coordinatesToSave);
    }

    private <T extends AbstractHelpModel> Coordinates updateClassCorrespondingField(Coordinates coordinates, T helpModel) {
        Bson updatedFields = null;
        if (helpModel instanceof HelpRequestModel) {
            updatedFields = set("requests", coordinates.getHelpRequests());

        } else if (helpModel instanceof HelpOfferModel) {
            updatedFields = set("offers", coordinates.getHelpOffers());
        } else {
            //TODO EXCEPTION
        }
        return this.updateExistingField(coordinates.getId(), updatedFields);

    }

}
