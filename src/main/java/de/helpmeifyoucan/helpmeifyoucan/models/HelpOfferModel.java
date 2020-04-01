package de.helpmeifyoucan.helpmeifyoucan.models;

import de.helpmeifyoucan.helpmeifyoucan.utils.HelpCategoryEnum;

import java.util.List;

public class HelpOfferModel extends AbstractHelpModel<HelpOfferModel> {

    protected List<HelpCategoryEnum> categories;


    public List<HelpCategoryEnum> getCategories() {
        return categories;
    }

    public HelpOfferModel setCategories(List<HelpCategoryEnum> categories) {
        this.categories = categories;
        return this;
    }

    public HelpOfferModel addCategory(HelpCategoryEnum category) {
        this.categories.add(category);

        return this;
    }
}
