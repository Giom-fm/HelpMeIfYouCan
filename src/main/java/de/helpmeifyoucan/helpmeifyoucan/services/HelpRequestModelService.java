package de.helpmeifyoucan.helpmeifyoucan.services;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import de.helpmeifyoucan.helpmeifyoucan.models.HelpModelApplication;
import de.helpmeifyoucan.helpmeifyoucan.models.HelpRequestModel;
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
