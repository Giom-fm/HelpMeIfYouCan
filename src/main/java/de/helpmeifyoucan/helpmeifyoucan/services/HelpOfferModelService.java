package de.helpmeifyoucan.helpmeifyoucan.services;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import de.helpmeifyoucan.helpmeifyoucan.models.HelpOfferModel;
import de.helpmeifyoucan.helpmeifyoucan.models.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


    public void onDelete(UserModel deletedUser) {
        var userId = deletedUser.getId();
        deletedUser.getHelpOffers().forEach(x -> super.deleteModel(x, userId));
    }
}
