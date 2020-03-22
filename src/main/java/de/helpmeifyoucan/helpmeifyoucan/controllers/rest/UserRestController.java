package de.helpmeifyoucan.helpmeifyoucan.controllers.rest;

import de.helpmeifyoucan.helpmeifyoucan.controllers.database.UserController;
import de.helpmeifyoucan.helpmeifyoucan.models.Address;
import de.helpmeifyoucan.helpmeifyoucan.models.User;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserRestController {
    UserController controller = new UserController();

    @PostMapping(consumes = "application/json")
    public void createUser(@Valid @RequestBody User user) {
        controller.save(user);
    }

    @GetMapping("/get/{id}")
    public User get(@PathVariable String id) {
        ObjectId newId = new ObjectId(id);

        return controller.get(newId);
    }

    @PostMapping(path = "/update/{id}", consumes = "application/json")
    public void update(@Valid @RequestBody User user, @PathVariable String id) {
        controller.update(new ObjectId(id), user);
    }

    @GetMapping("/delete/{id}")
    public void delete(@PathVariable String id) {
        controller.delete(new ObjectId(id));
    }

    @PostMapping(path = "/addaddress/{id}", consumes = "application/json")
    public void addUserAddress(@PathVariable String id, @Valid @RequestBody Address address) {
        controller.handleUserAddressAdd(new ObjectId(id), address);
    }

    @PostMapping(path = "/deleteaddress/{id}", consumes = "application/json")
    public void deleteUserAddress(@PathVariable String id, @Valid @RequestBody Address address) {
        controller.deleteUserAddress(new ObjectId(id), address);
    }
}
