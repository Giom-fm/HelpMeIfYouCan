package de.helpmeifyoucan.helpmeifyoucan.controllers.rest;


import com.mongodb.MongoWriteException;
import de.helpmeifyoucan.helpmeifyoucan.controllers.database.UserModelController;
import de.helpmeifyoucan.helpmeifyoucan.models.AddressModel;
import de.helpmeifyoucan.helpmeifyoucan.models.UserModel;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserRestController {
    UserModelController controller = new UserModelController();


    //TODO responses
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping(consumes = "application/json")
    public void createUser(@Valid @RequestBody UserModel user) {

        controller.save(user);
    }


    @PatchMapping(path = "/{id}", consumes = "application/json")
    public UserModel update(@Valid @RequestBody UserModel user, @PathVariable ObjectId id) {
        return controller.update(id, user);
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable ObjectId id) {
        controller.delete(id);
    }


    @GetMapping("/me")
    public UserModel getMe() {
        //tokenid
        var id = new ObjectId();
        return controller.get(id);
    }

    //FIXME
    @PostMapping(path = "/addaddress/{id}", consumes = "application/json")
    public void addUserAddress(@PathVariable ObjectId id, @Valid @RequestBody AddressModel address) {
        controller.handleUserAddressAdd(id, address);
    }

    //FIXME
    @PostMapping(path = "/deleteaddress/{id}", consumes = "application/json")
    public void deleteUserAddress(@PathVariable ObjectId id, @Valid @RequestBody AddressModel address) {
        controller.deleteUserAddress(id, address);
    }


    @ExceptionHandler(value = {MongoWriteException.class})
    protected ResponseEntity<String> handleConflict(
            RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "Email already taken!";
        return new ResponseEntity<>(bodyOfResponse, HttpStatus.BAD_REQUEST);
    }

}
