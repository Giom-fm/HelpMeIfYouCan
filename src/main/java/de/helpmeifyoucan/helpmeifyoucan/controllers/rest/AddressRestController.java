package de.helpmeifyoucan.helpmeifyoucan.controllers.rest;


import de.helpmeifyoucan.helpmeifyoucan.controllers.database.AddressModelController;
import de.helpmeifyoucan.helpmeifyoucan.models.Address;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/addresses")
public class AddressRestController {

    AddressModelController controller = new AddressModelController();

    @PostMapping(consumes = "application/json")
    public void save(@RequestBody Address address) {
        controller.save(address);
    }

    @GetMapping("/get/{id}")
    public Address find(@PathVariable ObjectId id) {
        return controller.get(id);
    }


    @PostMapping(path = "/update/{id}", consumes = "application/json")
    public void update(@Valid @RequestBody Address address, @PathVariable ObjectId id) {
        controller.update(id, address);
    }

    @PostMapping(path = "/delete/{id}")
    public void delete(@PathVariable ObjectId id) {
        controller.delete(id);
    }

}
