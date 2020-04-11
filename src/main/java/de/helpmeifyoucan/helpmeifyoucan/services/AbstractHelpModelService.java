package de.helpmeifyoucan.helpmeifyoucan.services;

import com.mongodb.client.MongoDatabase;
import de.helpmeifyoucan.helpmeifyoucan.models.AbstractHelpModel;
import de.helpmeifyoucan.helpmeifyoucan.models.Coordinates;
import de.helpmeifyoucan.helpmeifyoucan.models.HelpModelApplication;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.CoordinatesUpdate;
import de.helpmeifyoucan.helpmeifyoucan.utils.PostStatusEnum;
import de.helpmeifyoucan.helpmeifyoucan.utils.errors.HelpOfferModelExceptions;
import de.helpmeifyoucan.helpmeifyoucan.utils.errors.HelpRequestModelExceptions;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.push;
import static com.mongodb.client.model.Updates.set;

@Service
public abstract class AbstractHelpModelService<T extends AbstractHelpModel> extends AbstractService<T> {


    protected CoordinatesService coordinatesService;

    protected UserService userService;

    @Autowired
    public AbstractHelpModelService(MongoDatabase database, CoordinatesService coordinatesService
            , UserService userService) {
        super(database);
        var modelClass = this.getModel();
        super.createCollection(modelClass.getSimpleName(), modelClass);
        this.createIndex();
        this.coordinatesService = coordinatesService;
        this.userService = userService;
    }

    protected abstract void createIndex();

    protected abstract Class<T> getModel();


    public HelpModelApplication saveNewApplication(ObjectId helpRequest, HelpModelApplication application, ObjectId user) {

        application.setHelpModelId(helpRequest).generateId();
        var savingUser = this.userService.handleApplicationAdd(user, application);
        application.addUserDetails(savingUser);

        var idFilter = eq(helpRequest);
        var addApplicationsUpdate = push("applications", application);
        super.updateExistingFields(idFilter, addApplicationsUpdate);

        return application;
    }


    /**
     * We want to save a new helpModel, so we first generate an id, to then save the embedded
     * coordinates and finally save the request with a added ref to the added coordinates
     *
     * @param model      the request to update
     * @param savingUser the saving user
     * @return the saved request
     */
    public T saveNewModel(T model, ObjectId savingUser) {
        model.setUser(savingUser).setStatus(PostStatusEnum.ACTIVE).generateId();

        this.userService.handleHelpModelAdd(model.getClass(), model.getId(), savingUser);

        var addedCords = this.coordinatesService.handleHelpModelCoordinateAdd(model);

        this.updateEmbeddedCoordinates(addedCords);

        return this.save(model.setCoordinates(addedCords));
    }


    /**
     * We want to delete a request, we do so by first checking if the user is permitted to do so by checking the request user ref,
     * if permitted, the request will be deleted and the deletion of its embedded coordinates will be handled by the embedded coordiantes controller
     * the coordinates returned by this controller will then be updated in the request tables model refs
     *
     * @param modelToDelete the model to delete
     * @param deletingUser  the user performing the delete
     * @return the deleted request
     */

    public T deleteModel(ObjectId modelToDelete, ObjectId deletingUser) {

        var deleteFilter = and(eq(modelToDelete), in("user", deletingUser));

        var deletedModel = Optional.ofNullable(this.findOneAndDelete(deleteFilter)).orElseThrow(() -> new HelpOfferModelExceptions.HelpOfferNotFoundException(modelToDelete));

        var deletedCoords = this.coordinatesService.handleHelpModelCoordinateDelete(deletedModel);

        this.userService.handleHelpModelDelete(deletedModel.getClass(), modelToDelete, deletingUser);

        this.updateEmbeddedCoordinates(deletedCoords);

        return deletedModel;
    }


    /**
     * We want to update the embedded coordinates a request has, so we first check if the updating user matches the user registered in the request ref and then delegate the updating
     * to the embedded coordinates controller. to then update the db request db model
     *
     * @param modelId      the request to update
     * @param update       the update to perform
     * @param updatingUser the user performing the update
     * @return the updated request
     */

    public T handleCoordinatesUpdate(ObjectId modelId, CoordinatesUpdate update,
                                     ObjectId updatingUser) {
        var modelToUpdate = getByModelIdAndUser(modelId, updatingUser);

        var updatedCoordinates = this.coordinatesService.handleHelpModelCoordinateUpdate(modelToUpdate, update);

        this.updateEmbeddedCoordinates(updatedCoordinates);

        return this.updateExistingFields(eq(modelId), set("coordinates", updatedCoordinates));
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

    protected T getByModelIdAndUser(ObjectId modelId, ObjectId userId) {

        return Optional.ofNullable(getByFilter(and(eq(modelId), in("user", userId)))).orElseThrow(() -> new HelpRequestModelExceptions.HelpRequestNotFoundException(modelId));

    }
}
