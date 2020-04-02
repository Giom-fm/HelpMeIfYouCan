package de.helpmeifyoucan.helpmeifyoucan.models.dtos.request;

import de.helpmeifyoucan.helpmeifyoucan.utils.HelpCategoryEnum;
import org.bson.conversions.Bson;

import java.util.List;

public class HelpOfferUpdate extends HelpModelUpdate {

    protected List<HelpCategoryEnum> categories;

    public void setCategories(List<HelpCategoryEnum> categories) {
        this.categories = categories;
    }

    public Bson toFilter() {
        return super.toFilter(this);
    }
}
