package de.helpmeifyoucan.helpmeifyoucan.controllers;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.helpmeifyoucan.helpmeifyoucan.models.Coordinates;
import de.helpmeifyoucan.helpmeifyoucan.services.CoordinatesService;

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

    @GetMapping(path = "/requests", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Coordinates> getAllRequests(@RequestParam double longitude, @RequestParam double latitude,
            @RequestParam double radius) {
        return this.coordinatesService.getAllRequests(longitude, latitude, radius);
    }

    @GetMapping(path = "/offers", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Coordinates> getAllOffers(@RequestParam double longitude, @RequestParam double latitude,
            @RequestParam double radius) {
        return this.coordinatesService.getAllOffers(longitude, latitude, radius);
    }

    @GetMapping(path = "/{id}")
    public Coordinates getById(@PathVariable ObjectId id) {
        return this.coordinatesService.getById(id);
    }

}
