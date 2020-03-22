package de.helpmeifyoucan.helpmeifyoucan.RestController;


import de.helpmeifyoucan.helpmeifyoucan.controllers.UserController;
import de.helpmeifyoucan.helpmeifyoucan.models.User;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserRestController {
    UserController controller = new UserController();

    @PostMapping(path = "/create", consumes = "application/json")
    public void createUser(@Valid @RequestBody User user) {
        controller.save(user);
    }


    @GetMapping("/get/{id}")
    public User get(@PathVariable String id) {
        ObjectId newId = new ObjectId(id);

        return controller.get(newId);
    }
}


