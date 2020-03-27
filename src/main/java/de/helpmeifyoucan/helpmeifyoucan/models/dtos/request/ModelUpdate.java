package de.helpmeifyoucan.helpmeifyoucan.models.dtos.request;

import de.helpmeifyoucan.helpmeifyoucan.utils.ErrorMessages;
import org.bson.conversions.Bson;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

public abstract class ModelUpdate {

    protected Bson toFilter(Object obj) {

        try {
            List<Bson> filter = new ArrayList<>();

            Field[] runtimeFields = obj.getClass().getDeclaredFields();

            for (Field f : runtimeFields) {
                Optional<Object> notNull = Optional.ofNullable(f.get(obj));
                if (notNull.isPresent()) {
                    filter.add(set(f.getName(), notNull.get()));
                }

            }
            return combine(filter);
        } catch (IllegalAccessException e) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.UPDATE_FAILED);
        }
    }
}
