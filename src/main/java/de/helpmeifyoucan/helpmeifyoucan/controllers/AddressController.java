package de.helpmeifyoucan.helpmeifyoucan.controllers;

import de.helpmeifyoucan.helpmeifyoucan.models.AddressModel;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.AddressUpdate;
import de.helpmeifyoucan.helpmeifyoucan.services.AddressService;
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
@RequestMapping("/address")
public class AddressController {

   private AddressService addressModelController;

    @Autowired
    public AddressController(AddressService addressModelController) {
        this.addressModelController = addressModelController;
    }

    @Secured({Role.ROLE_NAME_USER})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public AddressModel save(@RequestBody AddressModel address) {
        return this.addressModelController.save(address);
    }

    @Secured({Role.ROLE_NAME_USER})
    @GetMapping("/{id}")
    public AddressModel get(@PathVariable ObjectId id) {
        return addressModelController.getById(id);
    }


    //TODO
    @PatchMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public AddressModel update(@Valid @RequestBody AddressUpdate address, @PathVariable ObjectId id) {
        return addressModelController.handleUserServiceAddressUpdate(id, address, getIdFromContext());

    }

    @Secured({Role.ROLE_NAME_USER})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable ObjectId id) {
        this.addressModelController.deleteById(id);
    }

    private ObjectId getIdFromContext() {
        return (ObjectId) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
