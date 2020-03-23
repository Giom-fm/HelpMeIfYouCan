package de.helpmeifyoucan.helpmeifyoucan.controllers.rest;


import de.helpmeifyoucan.helpmeifyoucan.controllers.database.AddressModelController;
import de.helpmeifyoucan.helpmeifyoucan.models.AddressModel;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/addresses")
public class AddressRestController {

    AddressModelController controller = new AddressModelController();

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping(consumes = "application/json")
    public void save(@RequestBody AddressModel address) {
        controller.save(address);
    }

    @GetMapping("/get/{id}")
    public AddressModel find(@PathVariable ObjectId id) {
        return controller.get(id);
    }


    @PatchMapping(path = "/update/{id}", consumes = "application/json")
    public AddressModel update(@Valid @RequestBody AddressModel address, @PathVariable ObjectId id) {
        return controller.update(id, address);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/delete/{id}")
    public void delete(@PathVariable ObjectId id) {
        controller.delete(id);
    }

}
