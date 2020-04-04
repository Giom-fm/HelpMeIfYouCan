package de.helpmeifyoucan.helpmeifyoucan.models;

import de.helpmeifyoucan.helpmeifyoucan.utils.HelpCategoryEnum;
import de.helpmeifyoucan.helpmeifyoucan.utils.PostStatusEnum;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class HelpOfferModel extends AbstractHelpModel {

    protected List<HelpCategoryEnum> categories;

    public HelpOfferModel() {
        this.categories = new LinkedList<>();
    }

    public HelpOfferModel setUser(ObjectId user) {
        this.user = user;
        return this;
    }

    public HelpOfferModel setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
        return this;
    }


    public HelpOfferModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public HelpOfferModel setStatus(PostStatusEnum status) {
        this.status = status;
        return this;
    }

    public HelpOfferModel setDatePublished(Date date) {
        this.datePublished = date;
        return this;
    }

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


    public HelpOfferModel generateId() {
        this.setId(new ObjectId());
        this.datePublished = new Date();
        return this;
    }

    @Override
    public String toString() {
        return "HelpOfferModel{" +
                " id=" + id +
                ", categories=" + categories +
                ", user=" + user +
                ", coordinates=" + coordinates +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", datePublished=" + datePublished +

                '}';
    }
}
