package de.helpmeifyoucan.helpmeifyoucan.services;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import de.helpmeifyoucan.helpmeifyoucan.models.*;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.AbstractHelpModelUpdate;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.CoordinatesUpdate;
import de.helpmeifyoucan.helpmeifyoucan.utils.ApplicationExceptions;
import de.helpmeifyoucan.helpmeifyoucan.utils.PostStatusEnum;
import de.helpmeifyoucan.helpmeifyoucan.utils.errors.HelpModelExceptions;
import de.helpmeifyoucan.helpmeifyoucan.utils.errors.HelpOfferModelExceptions;
import de.helpmeifyoucan.helpmeifyoucan.utils.errors.HelpRequestModelExceptions;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

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

    /**
     * We want to update a request, we do so by first checking if the updating user has permission to do so, if yes we directly update the request in the db
     *
     * @param requestToUpdate the request to update
     * @param update          the update to perform
     * @param updatingUser    the user performing the updating
     * @return the updated request
     */

    public T update(ObjectId requestToUpdate, AbstractHelpModelUpdate update,
                    ObjectId updatingUser) {

        var updateFilter = and(eq(requestToUpdate), in("user", updatingUser));

        return Optional.ofNullable(super.updateExistingFields(updateFilter, update.toFilter())).orElseThrow(() -> new HelpRequestModelExceptions.HelpRequestNotFoundException(requestToUpdate));

    }


    public HelpModelApplication handleNewApplication(ObjectId helpModel,
                                                     HelpModelApplication application, ObjectId applyingUser) {
        var model =
                Optional.ofNullable(this.getById(helpModel)).orElseThrow(() -> new HelpModelExceptions.HelpModelNotFoundException(helpModel));

        if (model.getUser().equals(applyingUser)) {
            throw new ApplicationExceptions.OwnPostApplicationException(this.getModel());
        }

        if (model.userHasApplied(applyingUser)) {
            throw new ApplicationExceptions.DuplicateApplicationException(this.getModel(), helpModel);
        }

        return saveNewApplication(helpModel, application, applyingUser);
    }


    private HelpModelApplication saveNewApplication(ObjectId helpOffer,
                                                    HelpModelApplication application, ObjectId applyingUser) {

        var model =
                Optional.ofNullable(this.getById(helpOffer)).orElseThrow(() -> new HelpModelExceptions.HelpModelNotFoundException(helpOffer));

        application.setModelId(helpOffer).setHelpModelType(this.getModel().getSimpleName()).generateId();

        this.userService.handleApplicationAdd(applyingUser, application,
                model);

        var idFilter = eq(helpOffer);
        var addApplicationsUpdate = push("applications", application);

        super.updateExistingFields(idFilter, addApplicationsUpdate);

        return application;
    }


    public HelpModelApplication acceptApplication(ObjectId helpModel, ObjectId applicationId,
                                                  ObjectId acceptingUser) {
        var idFilter = and(eq(helpModel), eq("user", acceptingUser), elemMatch("applications", eq(applicationId)));

        var offer =
                Optional.ofNullable(super.getByFilter(idFilter)).orElseThrow(() -> new HelpModelExceptions.HelpModelNotFoundException(helpModel));

        var acceptedApplication = offer.acceptApplication(applicationId);

        this.userService.handleApplicationAccept(acceptedApplication, acceptingUser);

        var removeApplicationFromApplications = pull("applications", eq(applicationId));

        this.updateExistingFields(eq(helpModel), combine(buildAcceptedApplicationUpdate(acceptedApplication),
                removeApplicationFromApplications));

        return acceptedApplication;
    }

    private Bson buildAcceptedApplicationUpdate(HelpModelApplication acceptedApplication) {
        return this.getModel().equals(HelpOfferModel.class) ? push("acceptedApplications",
                acceptedApplication) : set("acceptedApplication", acceptedApplication);
    }


    public HelpModelApplication deleteApplication(ObjectId helpModel, ObjectId deletingUser) {
        var idAndApplicationIdFilter = and(eq(helpModel), or(elemMatch("applications", eq("user",
                deletingUser)), buildIdAndApplicationFilter(deletingUser)));

        var pullApplication = pull("applications", eq("user", deletingUser));
        var pullAcceptedApplication = buildDeleteApplicationFilter(deletingUser);

        var updateOptions = new FindOneAndUpdateOptions().returnDocument(ReturnDocument.BEFORE);

        var request = Optional.ofNullable(super.updateArrayFields(idAndApplicationIdFilter,
                combine(pullApplication, pullAcceptedApplication), updateOptions)).orElseThrow(() -> new HelpRequestModelExceptions.HelpRequestNotFoundException(helpModel));

        var userApplication = this.filterApplications(request, deletingUser);

        return this.userService.handleApplicationDelete(userApplication);
    }


    public Bson buildIdAndApplicationFilter(ObjectId deletingUser) {
        return this.getModel().equals(HelpOfferModel.class) ? elemMatch("acceptedApplications", eq(
                "user",
                deletingUser)) : eq("acceptedApplication.user", deletingUser);
    }

    public Bson buildDeleteApplicationFilter(ObjectId deletingUser) {
        return this.getModel().equals(HelpOfferModel.class) ? pull("acceptedApplications", in(
                "user", deletingUser)) : unset("acceptedApplication");

    }

    /**
     * We want to save a new helpModel, so we first generate an id, to then save the embedded
     * coordinates and finally save the request with a added ref to the added coordinates
     *
     * @param model      the request to update
     * @param savingUser the saving user
     * @return the saved request
     */
    public T saveNewModel(AbstractHelpModel model, ObjectId savingUser) {
        model.setStatus(PostStatusEnum.ACTIVE).generateId();

        UserModel userModel = this.userService.handleHelpModelAdd(model.getClass(), model.getId(),
                savingUser);

        model.setUserName(userModel.getName()).setUser(userModel.getId());

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

        this.userService.handleHelpModelDelete(deletedModel, deletingUser);

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

    private HelpModelApplication filterApplications(T offerModel, ObjectId userId) {
        return offerModel.getCombinedApplications().stream().filter(x -> x.getUser().equals(userId)).findFirst().orElseThrow(() -> new de.helpmeifyoucan.helpmeifyoucan.utils.errors.ApplicationExceptions.ApplicationNotFoundException(offerModel.getId()));
    }

    protected T getByModelIdAndUser(ObjectId modelId, ObjectId userId) {

        return Optional.ofNullable(getByFilter(and(eq(modelId), in("user", userId)))).orElseThrow(() -> new HelpRequestModelExceptions.HelpRequestNotFoundException(modelId));

    }


}
