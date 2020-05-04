package de.helpmeifyoucan.helpmeifyoucan.services.observable.subjects;

import de.helpmeifyoucan.helpmeifyoucan.services.UserService;
import org.bson.types.ObjectId;

import java.util.List;

public class CoordinatesServiceSubject extends ServiceSubject<UserService> {

    @Override
    public void onNext(UserService userService) {

    }

    public List<ObjectId> getModels(ObjectId user) {
        return observers.get(0).getModels(user);
    }
}
