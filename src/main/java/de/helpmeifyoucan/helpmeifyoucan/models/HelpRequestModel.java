package de.helpmeifyoucan.helpmeifyoucan.models;

import de.helpmeifyoucan.helpmeifyoucan.utils.HelpCategoryEnum;

import java.util.Date;
import java.util.Objects;

public class HelpRequestModel extends AbstractHelpModel<HelpRequestModel> {

    protected Date datePublished;

    protected Date dateDue;

    protected HelpCategoryEnum category;


    public Date getDatePublished() {
        return datePublished;
    }

    public HelpRequestModel setDatePublished(Date datePublished) {
        this.datePublished = datePublished;
        return this;
    }

    public Date getDateDue() {
        return dateDue;
    }

    public HelpRequestModel setDateDue(Date dateDue) {
        this.dateDue = dateDue;
        return this;
    }

    public HelpCategoryEnum getCategory() {
        return category;
    }

    public HelpRequestModel setCategory(HelpCategoryEnum category) {
        this.category = category;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        HelpRequestModel that = (HelpRequestModel) o;
        return getDatePublished().equals(that.getDatePublished()) &&
                Objects.equals(getDateDue(), that.getDateDue()) &&
                getCategory() == that.getCategory();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getDatePublished(), getDateDue(), getCategory());
    }
}
