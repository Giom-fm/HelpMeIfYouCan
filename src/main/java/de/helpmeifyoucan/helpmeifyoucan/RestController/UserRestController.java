package de.helpmeifyoucan.helpmeifyoucan.RestController;


import de.helpmeifyoucan.helpmeifyoucan.controllers.EntityController;
import de.helpmeifyoucan.helpmeifyoucan.models.User;
import de.helpmeifyoucan.helpmeifyoucan.utils.ClassName;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserRestController {
    EntityController controller = new EntityController();

    @PostMapping(path = "/create", consumes = "application/json")
    public void createUser(@Valid @RequestBody User user) {
        controller.saveEntity(user);
    }


    @GetMapping("/get/{id}")
    public User get(@PathVariable String id) {
        ObjectId newId = new ObjectId(id);

        return controller.getEntityByIdAndClass(newId, ClassName.User);
    }
}


