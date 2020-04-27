package de.helpmeifyoucan.helpmeifyoucan.controllers;

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
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {
    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // USER ENDPOINTS --------------------------------
    @Secured({Role.ROLE_NAME_USER})
    @GetMapping(path = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserModel getMe(@RequestParam boolean lazy) {
        return this.userService.getWithAddress(getIdFromContext(), lazy);
    }

    @Secured({Role.ROLE_NAME_USER})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/me", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void deleteMe(@RequestBody UserUpdate update) {
        this.userService.handleUserMeDelete(this.getIdFromContext(), update);
    }

    @Secured({Role.ROLE_NAME_USER})
    @PatchMapping(path = "/me", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserModel updateMe(@Valid @RequestBody UserUpdate user) {
        var id = this.getIdFromContext();
        return this.userService.update(user, id);
    }

    // ADDRESS ENDPOINTS --------------------------------

    @Secured({Role.ROLE_NAME_USER})
    @GetMapping(path = "/me/address", produces = MediaType.APPLICATION_JSON_VALUE)
    public AddressModel getUserAddress() {
        return this.userService.getUserAddress(getIdFromContext());
    }

    @Secured({Role.ROLE_NAME_USER})
    @PostMapping(path = "/me/address", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserModel addUserAddress(@Valid @RequestBody AddressModel address, @RequestParam boolean lazy) {
        return this.userService.handleUserAddressAddRequest(getIdFromContext(), address, lazy);
    }

    @Secured({Role.ROLE_NAME_USER})
    @PatchMapping(path = "me/address", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserModel updateUserAddress(@Valid @RequestBody AddressUpdate update, @RequestParam boolean lazy) {

        return this.userService.handleUserAddressUpdateRequest(getIdFromContext(), update, lazy);
    }

    @Secured({Role.ROLE_NAME_USER})
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(path = "/me/address", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserModel deleteUserAddress() {
        return this.userService.handleUserAddressDeleteRequest(getIdFromContext());
    }


    @Secured({Role.ROLE_NAME_USER})
    @PatchMapping(path = "me/applications/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserModel readApplication(@PathVariable ObjectId id) {
        return this.userService.handleApplicationRead(getIdFromContext(), id);
    }


    // ADMIN ENDPOINTS --------------------------------
    @Secured({Role.ROLE_NAME_ADMIN})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void create(@Valid @RequestBody UserModel user) {
        this.userService.save(user);
    }

    @Secured({Role.ROLE_NAME_ADMIN})
    @PatchMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserModel update(@Valid @RequestBody UserUpdate user, @PathVariable ObjectId id) {
        return this.userService.update(user, id);
    }

    @Secured({Role.ROLE_NAME_ADMIN})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable ObjectId id) {
        this.userService.deleteById(id);
    }



    private ObjectId getIdFromContext() {
        return (ObjectId) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
