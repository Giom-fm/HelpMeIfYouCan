package de.helpmeifyoucan.helpmeifyoucan.models.dtos.request;

import org.bson.conversions.Bson;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

public class ModelUpdate {


    protected Bson toFilter(Object obj) {

        Field[] runtimeFields = obj.getClass().getDeclaredFields();

        List<Bson> filter = new ArrayList<>();

        Arrays.stream(runtimeFields).filter(x -> {
            try {
                if (x.getModifiers() == Modifier.PRIVATE) {
                    return false;
                }
                return Optional.ofNullable(x.get(obj)).isPresent();
            } catch (IllegalAccessException e) {
                e.printStackTrace();

                return false;
            }
        }).forEach(x -> {
            try {
                String fieldName = x.getName();
                if (obj instanceof UserUpdate && fieldName.equals("password") && x.getType() == String.class) {
                    var passwordEncoder = new BCryptPasswordEncoder();
                    filter.add(set(fieldName, passwordEncoder.encode((String) x.get(obj))));
                } else {
                    filter.add(set(fieldName, x.get(obj)));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });


        return combine(filter);
    }

}
