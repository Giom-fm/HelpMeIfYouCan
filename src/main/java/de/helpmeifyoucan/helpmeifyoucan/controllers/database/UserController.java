package de.helpmeifyoucan.helpmeifyoucan.controllers.database;

import org.bson.types.ObjectId;

import de.helpmeifyoucan.helpmeifyoucan.models.User;

public class UserController extends AbstractEntityController<User> {

    public User get(ObjectId id) {
        return this.get(id, User.class);
    }

}
