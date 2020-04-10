package de.helpmeifyoucan.helpmeifyoucan.controllers;

import com.mongodb.MongoCommandException;
import com.mongodb.MongoWriteException;
import de.helpmeifyoucan.helpmeifyoucan.models.AddressModel;
import de.helpmeifyoucan.helpmeifyoucan.models.UserModel;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.AddressUpdate;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.UserUpdate;
import de.helpmeifyoucan.helpmeifyoucan.services.UserService;
import de.helpmeifyoucan.helpmeifyoucan.utils.Role;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {
    UserService userModelController;

    @Autowired
    public UserController(UserService userModelController) {
        this.userModelController = userModelController;
    }

    // USER ENDPOINTS --------------------------------
    @Secured({ Role.ROLE_NAME_USER })
    @GetMapping("/me")
    public UserModel getMe() {
        var id = this.getIdFromContext();
        return this.userModelController.get(id);

    }

    @Secured({ Role.ROLE_NAME_USER })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/me")
    public void deleteMe() {
        this.userModelController.deleteById(this.getIdFromContext());
    }

    @Secured({ Role.ROLE_NAME_USER })
    @PatchMapping("/me")
    public UserModel updateMe(@Valid @RequestBody UserUpdate user) {
        var id = this.getIdFromContext();
        return this.userModelController.update(user, id);
    }

    @Secured({Role.ROLE_NAME_USER})
    @PostMapping(path = "/me/address", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public UserModel addUserAddress(@Valid @RequestBody AddressModel address, @RequestParam boolean lazy) {
        return this.userModelController.handleUserAddressAddRequest(getIdFromContext(), address, lazy);
    }

    @Secured({Role.ROLE_NAME_USER})
    @PatchMapping(path = "me/address")
    public UserModel updateUserAddress(@Valid @RequestBody AddressUpdate update, @RequestParam boolean lazy) {

        return this.userModelController.handleUserAddressUpdateRequest(getIdFromContext(), update, lazy);
    }

    @Secured({Role.ROLE_NAME_USER})
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(path = "/deleteaddress", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserModel deleteUserAddress(@PathVariable ObjectId addressId) {
        return this.userModelController.handleUserAddressDeleteRequest(getIdFromContext(), addressId);
    }

    // ADMIN ENDPOINTS --------------------------------
    @Secured({ Role.ROLE_NAME_ADMIN })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void create(@Valid @RequestBody UserModel user) {
        this.userModelController.save(user);
    }

    @Secured({ Role.ROLE_NAME_ADMIN })
    @PatchMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserModel update(@Valid @RequestBody UserUpdate user, @PathVariable ObjectId id) {
        return this.userModelController.update(user, id);
    }

    @Secured({ Role.ROLE_NAME_ADMIN })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable ObjectId id) {
        this.userModelController.deleteById(id);
    }

    // REVIEW
    @ExceptionHandler(value = { MongoWriteException.class })
    protected ResponseEntity<String> handleConflict(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "Email already taken!";
        return new ResponseEntity<>(bodyOfResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = { MongoCommandException.class })
    protected ResponseEntity<String> duplicateKey(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = ex.getLocalizedMessage();
        return new ResponseEntity<>(bodyOfResponse, HttpStatus.BAD_REQUEST);
    }

    private ObjectId getIdFromContext() {
        return (ObjectId) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
