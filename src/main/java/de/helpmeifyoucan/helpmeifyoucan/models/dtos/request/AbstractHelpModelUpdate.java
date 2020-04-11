package de.helpmeifyoucan.helpmeifyoucan.models.dtos.request;

import org.bson.conversions.Bson;

public abstract class AbstractHelpModelUpdate extends ModelUpdate {

    public abstract Bson toFilter();
}
