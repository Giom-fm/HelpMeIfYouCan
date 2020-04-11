package de.helpmeifyoucan.helpmeifyoucan.services;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import de.helpmeifyoucan.helpmeifyoucan.models.HelpModelApplication;
import de.helpmeifyoucan.helpmeifyoucan.models.HelpRequestModel;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.HelpRequestUpdate;
import de.helpmeifyoucan.helpmeifyoucan.utils.errors.HelpRequestModelExceptions;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

@Service
public class HelpRequestModelService extends AbstractHelpModelService<HelpRequestModel> {

    @Autowired
    public HelpRequestModelService(MongoDatabase database, CoordinatesService coordinatesService,
                                   UserService userService) {
        super(database, coordinatesService, userService);
    }

    protected void createIndex() {
        IndexOptions options = new IndexOptions();
        options.unique(true);

        super.createIndex(Indexes.ascending("datePublished", "user"), options);
    }

    @Override
    protected Class<HelpRequestModel> getModel() {
        return HelpRequestModel.class;
    }


    /**
     * We want to update a request, we do so by first checking if the updating user has permission to do so, if yes we directly update the request in the db
     *
     * @param requestToUpdate the request to update
     * @param update          the update to perform
     * @param updatingUser    the user performing the updating
     * @return the updated request
     */

    public HelpRequestModel update(ObjectId requestToUpdate, HelpRequestUpdate update, ObjectId updatingUser) {

        var updateFilter = and(eq(requestToUpdate), in("user", updatingUser));

        return Optional.ofNullable(super.updateExistingFields(updateFilter, update.toFilter())).orElseThrow(() -> new HelpRequestModelExceptions.HelpRequestNotFoundException(requestToUpdate));

    }


    public void deleteApplication(ObjectId helpRequest, ObjectId deletingUser) {

        var idAndApplicationIdFilter = and(eq(helpRequest), or(elemMatch("applications", eq("user",
                deletingUser)), elemMatch("acceptedApplication", eq("user",
                deletingUser))));

        var pullApplication = pull("applications", in("user", deletingUser));
        var pullAcceptedApplication = pull("acceptedApplication", in("user", deletingUser));

        var deleteApplicationUpdate = combine(pullApplication, pullAcceptedApplication);

        Optional.ofNullable(super.updateExistingFields(idAndApplicationIdFilter, deleteApplicationUpdate).getApplications()).orElseThrow();

        super.userService.handleApplicationDelete(deletingUser, helpRequest);
    }


    public HelpModelApplication acceptApplication(ObjectId helpRequest, ObjectId application, ObjectId acceptingUser) {

        var idFilter = and(eq(helpRequest), eq("user", acceptingUser), elemMatch("applications", eq(application)));

        var offer = Optional.ofNullable(super.getByFilter(idFilter)).orElseThrow();

        var acceptedApplication = offer.acceptApplication(application);

        var addApplicationToAccepted = set("acceptedApplication", acceptedApplication);

        var removeApplicationFromApplications = pull("applications", eq(application));

        this.updateExistingFields(eq(helpRequest), combine(addApplicationToAccepted, removeApplicationFromApplications));

        return acceptedApplication;
    }


}
