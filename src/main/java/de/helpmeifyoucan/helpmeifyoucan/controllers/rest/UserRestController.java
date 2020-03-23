package de.helpmeifyoucan.helpmeifyoucan.controllers.rest;


import de.helpmeifyoucan.helpmeifyoucan.controllers.database.UserModelController;
import de.helpmeifyoucan.helpmeifyoucan.models.Address;
import de.helpmeifyoucan.helpmeifyoucan.models.UserModel;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserRestController {
    UserModelController controller = new UserModelController();

    @PostMapping(consumes = "application/json")
    public void createUser(@Valid @RequestBody UserModel user) {
        controller.save(user);
    }

    @GetMapping("/get/{id}")
    public UserModel get(@PathVariable ObjectId id) {
        return controller.get(id);
    }

    @PostMapping(path = "/update/{id}", consumes = "application/json")
    public void update(@Valid @RequestBody UserModel user, @PathVariable ObjectId id) {
        controller.update(id, user);
    }

    @GetMapping("/delete/{id}")
    public void delete(@PathVariable ObjectId id) {
        controller.delete(id);
    }

    @PostMapping(path = "/addaddress/{id}", consumes = "application/json")
    public void addUserAddress(@PathVariable ObjectId id, @Valid @RequestBody Address address) {
        controller.handleUserAddressAdd(id, address);
    }

    @PostMapping(path = "/deleteaddress/{id}", consumes = "application/json")
    public void deleteUserAddress(@PathVariable ObjectId id, @Valid @RequestBody Address address) {
        controller.deleteUserAddress(id, address);
    }
}
