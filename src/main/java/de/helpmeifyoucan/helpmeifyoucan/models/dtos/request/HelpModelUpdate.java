package de.helpmeifyoucan.helpmeifyoucan.models.dtos.request;

import de.helpmeifyoucan.helpmeifyoucan.utils.PostStatusEnum;

public abstract class HelpModelUpdate extends ModelUpdate {

    protected PostStatusEnum status;

    protected String description;

    public void setStatus(PostStatusEnum status) {
        this.status = status;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
