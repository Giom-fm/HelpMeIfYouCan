package de.helpmeifyoucan.helpmeifyoucan.models.dtos.request;

import org.bson.conversions.Bson;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

public abstract class ModelUpdate {

    protected Bson toFilter(Object obj) {
        Field[] runtimeFields = obj.getClass().getDeclaredFields();
        List<Bson> filter = new ArrayList<>();

        Arrays.stream(runtimeFields).filter(x -> {
            try {
                return Optional.ofNullable(x.get(obj)).isPresent();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return false;
            }
        }).forEach(x -> {
            try {
                filter.add(set(x.getName(), x.get(obj)));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        return combine(filter);
    }
}
