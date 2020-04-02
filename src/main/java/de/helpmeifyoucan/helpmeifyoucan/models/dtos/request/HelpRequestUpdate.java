package de.helpmeifyoucan.helpmeifyoucan.models.dtos.request;

import de.helpmeifyoucan.helpmeifyoucan.utils.HelpCategoryEnum;
import org.bson.conversions.Bson;

import java.util.Date;

public class HelpRequestUpdate extends HelpModelUpdate {

    protected Date dateDue;

    protected HelpCategoryEnum categoryEnum;


    public void setDateDue(Date dateDue) {
        this.dateDue = dateDue;
    }

    public void setCategoryEnum(HelpCategoryEnum categoryEnum) {
        this.categoryEnum = categoryEnum;
    }

    public Bson toFilter() {
        return super.toFilter(this);
    }
}
