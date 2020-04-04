package de.helpmeifyoucan.helpmeifyoucan.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.ModelUpdate;
import de.helpmeifyoucan.helpmeifyoucan.utils.ObjectIdMapping;
import org.bson.types.ObjectId;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;

public abstract class AbstractEntity {
    @JsonSerialize(converter = ObjectIdMapping.class)
    protected ObjectId id;


    public ObjectId setId(ObjectId id) {
        this.id = id;
        return this.id;
    }

    public ObjectId getId() {
        return id;
    }


    protected <T extends AbstractEntity> void mergeWithUpdate(ModelUpdate update, T subClass) {
        Field[] updateRuntimeFields = update.getClass().getDeclaredFields();


        Arrays.stream(updateRuntimeFields).filter(x -> {
            try {
                return Optional.ofNullable(x.get(update)).isPresent();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return false;
            }
        }).forEach(x -> {
            try {
                subClass.getClass().getDeclaredField(x.getName()).set(subClass, x.get(update));

            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });

    }
}
