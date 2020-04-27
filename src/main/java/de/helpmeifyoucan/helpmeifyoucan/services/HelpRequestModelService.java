package de.helpmeifyoucan.helpmeifyoucan.services;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import de.helpmeifyoucan.helpmeifyoucan.models.HelpRequestModel;
import de.helpmeifyoucan.helpmeifyoucan.models.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void onDelete(UserModel deletedUser) {
        var userId = deletedUser.getId();
        deletedUser.getHelpRequests().forEach(x -> super.deleteModel(x, userId));
    }


}
