package de.helpmeifyoucan.helpmeifyoucan.controllers;

import de.helpmeifyoucan.helpmeifyoucan.models.Coordinates;
import de.helpmeifyoucan.helpmeifyoucan.services.CoordinatesService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/coordinates")
public class CoordinatesController {

    private CoordinatesService coordinatesService;


    @Autowired
    public CoordinatesController(CoordinatesService coordinatesService) {
        this.coordinatesService = coordinatesService;
    }

    @GetMapping(path = "/getall", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Coordinates> getAll() {
        return this.coordinatesService.getAll();
    }

    @GetMapping(path = "getall/requests", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Coordinates> getAllRequests() {
        return this.coordinatesService.getAllRequests(20, 50, 2);
    }

    @GetMapping(path = "/getall/offers", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Coordinates> getAllOffers() {
        return this.coordinatesService.getAllOffers(20, 50, 2);
    }

    @GetMapping(path = "/{id}")
    public Coordinates getById(@PathVariable ObjectId id) {
        return this.coordinatesService.getById(id);
    }

}
