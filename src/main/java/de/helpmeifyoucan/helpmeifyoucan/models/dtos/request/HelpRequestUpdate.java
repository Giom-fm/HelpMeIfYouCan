package de.helpmeifyoucan.helpmeifyoucan.models.dtos.request;

import de.helpmeifyoucan.helpmeifyoucan.utils.HelpCategoryEnum;
import de.helpmeifyoucan.helpmeifyoucan.utils.PostStatusEnum;
import org.bson.conversions.Bson;

import java.util.Date;

public class HelpRequestUpdate extends ModelUpdate {

    public Date dateDue;

    public HelpCategoryEnum category;

    public PostStatusEnum status;

    public String description;


    public HelpRequestUpdate setDateDue(Date dateDue) {
        this.dateDue = dateDue;
        return this;
    }

    public HelpRequestUpdate setStatus(PostStatusEnum status) {
        this.status = status;
        return this;
    }

    public HelpRequestUpdate setCategory(HelpCategoryEnum categoryEnum) {
        this.category = categoryEnum;
        return this;
    }

    public HelpRequestUpdate setPostStatusEnum(PostStatusEnum postStatus) {
        this.status = postStatus;
        return this;
    }

    public HelpRequestUpdate setDescription(String description) {
        this.description = description;
        return this;
    }

    public Bson toFilter() {
        return super.toFilter(this);
    }
}
