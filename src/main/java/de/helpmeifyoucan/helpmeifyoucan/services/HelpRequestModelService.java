package de.helpmeifyoucan.helpmeifyoucan.services;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import de.helpmeifyoucan.helpmeifyoucan.models.HelpRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HelpRequestModelService extends AbstractService<HelpRequestModel> {


    @Autowired
    public HelpRequestModelService(MongoDatabase dataBase) {
        super(dataBase);
        super.createCollection("helpRequests", HelpRequestModel.class);
        this.createIndex();
    }

    public void createIndex() {
        IndexOptions options = new IndexOptions();
        options.unique(true);

        super.createIndex(Indexes.ascending("datePublished"), options);
    }
}
