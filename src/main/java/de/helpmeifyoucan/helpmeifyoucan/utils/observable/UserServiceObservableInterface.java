package de.helpmeifyoucan.helpmeifyoucan.utils.observable;

import de.helpmeifyoucan.helpmeifyoucan.models.UserModel;
import de.helpmeifyoucan.helpmeifyoucan.services.UserService;
import io.reactivex.Observer;

public interface UserServiceObservableInterface extends Observer<UserService> {

    void onDelete(UserModel deletedUser);

    void onUpdate(UserModel updatedUser);

}
