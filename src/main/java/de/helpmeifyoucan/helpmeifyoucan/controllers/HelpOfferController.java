package de.helpmeifyoucan.helpmeifyoucan.controllers;


import de.helpmeifyoucan.helpmeifyoucan.models.HelpModelApplication;
import de.helpmeifyoucan.helpmeifyoucan.models.HelpOfferModel;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.CoordinatesUpdate;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.HelpOfferUpdate;
import de.helpmeifyoucan.helpmeifyoucan.services.HelpOfferModelService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/offers")
public class HelpOfferController {

    private HelpOfferModelService helpOfferModelService;

    @Autowired
    public HelpOfferController(HelpOfferModelService helpOfferModelService) {
        this.helpOfferModelService = helpOfferModelService;
    }


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HelpOfferModel save(@RequestBody HelpOfferModel request) {
        return this.helpOfferModelService.saveNewOffer(request, getIdFromContext());
    }


    @PatchMapping(path = "/{requestId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HelpOfferModel update(@PathVariable ObjectId requestId, @RequestBody HelpOfferUpdate update) {
        return this.helpOfferModelService.update(requestId, update, getIdFromContext());
    }

    @PatchMapping(path = "/{requestId}/updateCoords", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HelpOfferModel updateCoords(@PathVariable ObjectId requestId, @RequestBody CoordinatesUpdate update) {
        return this.helpOfferModelService.handleCoordinatesUpdate(requestId, update, getIdFromContext());
    }

    @DeleteMapping(path = "/{requestId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public HelpOfferModel delete(@PathVariable ObjectId requestId) {
        return this.helpOfferModelService.deleteRequest(requestId, getIdFromContext());
    }

    @GetMapping(path = "/{requestId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public HelpOfferModel get(@PathVariable ObjectId requestId) {
        return this.helpOfferModelService.getById(requestId);
    }

    @GetMapping(path = "/getall", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<HelpOfferModel> getAllById(@RequestBody List<ObjectId> ids) {
        return this.helpOfferModelService.getAllById(ids);
    }

    @PostMapping(path = "/{requestId}/apply", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HelpModelApplication applyToRequest(@PathVariable ObjectId requestId, @RequestBody HelpModelApplication application) {
        return this.helpOfferModelService.saveNewApplication(requestId, application, getIdFromContext());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/{requestId}/unapply")
    public void unApplyFromRequest(@PathVariable ObjectId requestId) {
        this.helpOfferModelService.deleteApplication(requestId, getIdFromContext());
    }

    @PatchMapping(path = "/{requestId}/{applicationId}/accept")
    public HelpModelApplication acceptApplication(@PathVariable ObjectId requestId, @PathVariable ObjectId applicationId) {
        return this.helpOfferModelService.acceptApplication(requestId, applicationId, getIdFromContext());
    }


    //TODO get by user

    //TODO get by status

    private ObjectId getIdFromContext() {
        return (ObjectId) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
