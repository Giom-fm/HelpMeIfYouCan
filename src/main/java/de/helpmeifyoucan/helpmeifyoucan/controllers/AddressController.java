package de.helpmeifyoucan.helpmeifyoucan.controllers;

import com.mongodb.MongoCommandException;
import de.helpmeifyoucan.helpmeifyoucan.services.AddressService;
import de.helpmeifyoucan.helpmeifyoucan.models.AddressModel;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.AddressUpdate;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;

@RestController
@RequestMapping("/address")
public class AddressController {

    AddressService addressModelController;

    public AddressController(AddressService addressModelController) {
        this.addressModelController = addressModelController;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public AddressModel save(@RequestBody AddressModel address) {
        return this.addressModelController.save(address);
    }

    @GetMapping("/{id}")
    public AddressModel get(@PathVariable ObjectId id) {
        return addressModelController.get(id);
    }


    @PatchMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public AddressModel update(@Valid @RequestBody AddressUpdate address, @PathVariable ObjectId id) {
        return addressModelController.updateAddress(id, address, getIdFromContext());

    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable ObjectId id) {
        this.addressModelController.delete(id);
    }

    @ExceptionHandler(value = {MongoCommandException.class})
    protected ResponseEntity<String> duplicateKey(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "hier";
        return new ResponseEntity<>(bodyOfResponse, HttpStatus.BAD_REQUEST);
    }

    private ObjectId getIdFromContext() {
        return new ObjectId(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
    }

}
