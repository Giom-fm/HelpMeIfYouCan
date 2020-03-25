package de.helpmeifyoucan.helpmeifyoucan.controllers.rest;

import com.mongodb.MongoWriteException;
import de.helpmeifyoucan.helpmeifyoucan.controllers.database.UserModelController;
import de.helpmeifyoucan.helpmeifyoucan.models.AddressModel;
import de.helpmeifyoucan.helpmeifyoucan.models.UserModel;
import de.helpmeifyoucan.helpmeifyoucan.utils.ErrorMessages;
import de.helpmeifyoucan.helpmeifyoucan.utils.Roles;

import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserRestController {
    UserModelController userCollection = new UserModelController();

    // USER ENDPOINTS --------------------------------
    @Secured({ Roles.ROLE_NAME_USER })
    @GetMapping("/me")
    public UserModel getMe() {
        var id = this.getIdFromContext();
        var user = this.userCollection.get(id);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessages.USER_NOT_FOUND);
        }
        return user;
    }

    @Secured({ Roles.ROLE_NAME_USER })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/me")
    public void deleteMe() {
        var id = this.getIdFromContext();
        this.userCollection.delete(id);
    }

    @Secured({ Roles.ROLE_NAME_USER })
    @PatchMapping("/me")
    public UserModel updateMe(@Valid @RequestBody UserModel user) {
        var id = this.getIdFromContext();
        return userCollection.update(id, user);
    }

    // ADMIN ENDPOINTS --------------------------------
    @Secured({ Roles.ROLE_NAME_ADMIN })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void create(@Valid @RequestBody UserModel user) {
        userCollection.save(user);
    }

    @Secured({ Roles.ROLE_NAME_ADMIN })
    @PatchMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserModel update(@Valid @RequestBody UserModel user, @PathVariable ObjectId id) {
        return userCollection.update(id, user);
    }

    @Secured({ Roles.ROLE_NAME_ADMIN })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable ObjectId id) {
        userCollection.delete(id);
    }

    // FIXME
    @PostMapping(path = "/addaddress/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void addUserAddress(@PathVariable ObjectId id, @Valid @RequestBody AddressModel address) {
        userCollection.handleUserAddressAdd(id, address);
    }

    // FIXME
    @PostMapping(path = "/deleteaddress/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void deleteUserAddress(@PathVariable ObjectId id, @Valid @RequestBody AddressModel address) {
        userCollection.deleteUserAddress(id, address);
    }

    @ExceptionHandler(value = { MongoWriteException.class })
    protected ResponseEntity<String> handleConflict(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "Email already taken!";
        return new ResponseEntity<>(bodyOfResponse, HttpStatus.BAD_REQUEST);
    }

    private ObjectId getIdFromContext() {
        return new ObjectId(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
    }

}
