package de.helpmeifyoucan.helpmeifyoucan.services;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import de.helpmeifyoucan.helpmeifyoucan.models.HelpModelApplication;
import de.helpmeifyoucan.helpmeifyoucan.models.HelpOfferModel;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.HelpOfferUpdate;
import de.helpmeifyoucan.helpmeifyoucan.utils.errors.HelpOfferModelExceptions;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

@Service
public class HelpOfferModelService extends AbstractHelpModelService<HelpOfferModel> {

    @Autowired
    public HelpOfferModelService(MongoDatabase database, CoordinatesService coordinatesService, UserService userService) {
        super(database, coordinatesService, userService);
    }

    protected void createIndex() {
        IndexOptions options = new IndexOptions();
        options.unique(true);

        super.createIndex(Indexes.ascending("datePublished", "user"), options);
    }

    @Override
    protected Class<HelpOfferModel> getModel() {
        return HelpOfferModel.class;
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


    public void deleteApplication(ObjectId helpOffer, ObjectId deletingUser) {

        var idAndApplicationIdFilter = and(eq(helpOffer), or(elemMatch("applications", eq("user",
                deletingUser)), elemMatch("acceptedApplications", eq("user",
                deletingUser))));

        var pullApplication = pull("applications", in("user", deletingUser));
        var pullAcceptedApplication = pull("acceptedApplications", in("user", deletingUser));

        var deleteApplicationUpdate = combine(pullApplication, pullAcceptedApplication);

        Optional.ofNullable(super.updateExistingFields(idAndApplicationIdFilter, deleteApplicationUpdate).getApplications()).orElseThrow();

        this.userService.handleApplicationDelete(deletingUser, helpOffer);

    }

    public HelpModelApplication acceptApplication(ObjectId helpOffer, ObjectId application, ObjectId acceptingUser) {

        var idFilter = and(eq(helpOffer), eq("user", acceptingUser), elemMatch("applications", eq(application)));

        var offer = Optional.ofNullable(super.getByFilter(idFilter)).orElseThrow();

        var acceptedApplication = offer.acceptApplication(application);

        this.userService.handleApplicationAccept(acceptedApplication, acceptingUser);

        var addApplicationToAccepted = push("acceptedApplications", acceptedApplication);

        var removeApplicationFromApplications = pull("applications", eq(application));

        this.updateExistingFields(eq(helpOffer), combine(addApplicationToAccepted, removeApplicationFromApplications));

        return acceptedApplication;
    }


}
