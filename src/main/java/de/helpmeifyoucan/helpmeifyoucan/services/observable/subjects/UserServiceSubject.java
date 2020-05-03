package de.helpmeifyoucan.helpmeifyoucan.services.observable.subjects;

import de.helpmeifyoucan.helpmeifyoucan.models.UserModel;
import de.helpmeifyoucan.helpmeifyoucan.services.AbstractHelpModelService;

public class UserServiceSubject extends ServiceSubject<AbstractHelpModelService<?>> {

    public void onUpdate(UserModel userModel) {
        observers.forEach(x -> x.onUpdate(userModel));
    }

    public void onDelete(UserModel userModel) {
        observers.forEach(x -> x.onDelete(userModel));
    }

    @Override
    public void onNext(AbstractHelpModelService<?> abstractHelpModelService) {

    }
}
