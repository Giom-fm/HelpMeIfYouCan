package de.helpmeifyoucan.helpmeifyoucan.controllers;

import de.helpmeifyoucan.helpmeifyoucan.models.Coordinates;
import de.helpmeifyoucan.helpmeifyoucan.services.CoordinatesService;
import de.helpmeifyoucan.helpmeifyoucan.utils.Role;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coordinates")
public class CoordinatesController {

    private CoordinatesService coordinatesService;

    @Autowired
    public CoordinatesController(CoordinatesService coordinatesService) {
        this.coordinatesService = coordinatesService;
    }

    @Secured({Role.ROLE_NAME_USER})
    @GetMapping(path = "/getall", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Coordinates> getAll() {
        return this.coordinatesService.getAll();
    }

    @Secured({Role.ROLE_NAME_USER})
    @GetMapping(path = "/requests", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Coordinates> getAllRequests(@RequestParam double longitude, @RequestParam double latitude,
                                            @RequestParam double radius) {
        return this.coordinatesService.getAllRequests(longitude, latitude, radius);
    }

    @Secured({Role.ROLE_NAME_USER})
    @GetMapping(path = "/offers", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Coordinates> getAllOffers(@RequestParam double longitude, @RequestParam double latitude,
                                          @RequestParam double radius) {
        return this.coordinatesService.getAllOffers(longitude, latitude, radius);
    }

    @Secured({Role.ROLE_NAME_USER})
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Coordinates getById(@PathVariable ObjectId id) {
        return this.coordinatesService.getById(id);
    }

    @GetMapping(path = "/public", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Coordinates> getAllPublic(@RequestParam double longitude, @RequestParam double latitude,
                                          @RequestParam double radius) {
        return this.coordinatesService.getAllCombined(longitude, latitude, radius);
    }

}
