package de.helpmeifyoucan.helpmeifyoucan.models.dtos.request;

import de.helpmeifyoucan.helpmeifyoucan.utils.HelpCategoryEnum;
import de.helpmeifyoucan.helpmeifyoucan.utils.PostStatusEnum;
import org.bson.conversions.Bson;

import java.util.List;

public class HelpOfferUpdate extends AbstractHelpModelUpdate {

    public PostStatusEnum status;

    public String description;

    public List<HelpCategoryEnum> categories;

    public HelpOfferUpdate setCategories(List<HelpCategoryEnum> categories) {
        this.categories = categories;
        return this;
    }

    public HelpOfferUpdate setDescription(String description) {
        this.description = description;
        return this;
    }

    public HelpOfferUpdate setStatus(PostStatusEnum status) {
        this.status = status;
        return this;
    }

    @Override
    public Bson toFilter() {
        return super.toFilter(this);
    }

    @Override
    public String toString() {
        return "HelpOfferUpdate{" +
                "categories=" + categories +
                ", status=" + status +
                ", description='" + description + '\'' +
                '}';
    }
}
