package de.helpmeifyoucan.helpmeifyoucan.controllers.rest;


import de.helpmeifyoucan.helpmeifyoucan.controllers.database.AddressController;
import de.helpmeifyoucan.helpmeifyoucan.models.Address;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/addresses")
public class AddressRestController {

    AddressController controller = new AddressController();

    @PostMapping(consumes = "application/json")
    public void save(@RequestBody Address address) {
        controller.save(address);
    }

    @GetMapping("/get/{id}")
    public Address find(@PathVariable String id) {
        return controller.get(new ObjectId(id));
    }


    @PostMapping(path = "/update/{id}", consumes = "application/json")
    public void update(@Valid @RequestBody Address address, @PathVariable String id) {
        controller.update(new ObjectId(id), address);
    }

}
