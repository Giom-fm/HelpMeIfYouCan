package de.helpmeifyoucan.helpmeifyoucan.controllers.rest;

import de.helpmeifyoucan.helpmeifyoucan.controllers.database.AddressModelController;
import de.helpmeifyoucan.helpmeifyoucan.models.AddressModel;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.AddressUpdate;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/address")
public class AddressRestController {

    AddressModelController addressModelController;

    public AddressRestController(AddressModelController addressModelController) {
        this.addressModelController = addressModelController;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public AddressModel save(@RequestBody AddressModel address) {
        return this.addressModelController.save(address);
    }

    @GetMapping("/{id}")
    public AddressModel find(@PathVariable ObjectId id) {
        return addressModelController.get(id);
    }

    // FIXME
    @PatchMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public AddressModel update(@Valid @RequestBody AddressUpdate address, @PathVariable ObjectId id) {
        try {
            return addressModelController.updateAddress(id, address, getIdFromContext());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable ObjectId id) {
        this.addressModelController.delete(id);
    }

    private ObjectId getIdFromContext() {
        return new ObjectId(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
    }

}
