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


    @PatchMapping(path = "/{offerId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces =
            MediaType.APPLICATION_JSON_VALUE)
    public HelpOfferModel update(@PathVariable ObjectId offerId,
                                 @RequestBody HelpOfferUpdate update) {
        return this.helpOfferModelService.update(offerId, update, getIdFromContext());
    }

    @PatchMapping(path = "/{offerId}/updateCoords", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public HelpOfferModel updateCoords(@PathVariable ObjectId offerId,
                                       @RequestBody CoordinatesUpdate update) {
        return this.helpOfferModelService.handleCoordinatesUpdate(offerId, update,
                getIdFromContext());
    }

    @DeleteMapping(path = "/{offerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public HelpOfferModel delete(@PathVariable ObjectId offerId) {
        return this.helpOfferModelService.deleteRequest(offerId, getIdFromContext());
    }

    @GetMapping(path = "/{offerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public HelpOfferModel get(@PathVariable ObjectId offerId) {
        return this.helpOfferModelService.getById(offerId);
    }

    @GetMapping(path = "/getall", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<HelpOfferModel> getAllById(@RequestBody List<ObjectId> ids) {
        return this.helpOfferModelService.getAllById(ids);
    }

    @PostMapping(path = "/{offerId}/apply", consumes = MediaType.APPLICATION_JSON_VALUE, produces =
            MediaType.APPLICATION_JSON_VALUE)
    public HelpModelApplication applyToRequest(@PathVariable ObjectId offerId,
                                               @RequestBody HelpModelApplication application) {
        return this.helpOfferModelService.saveNewApplication(offerId, application,
                getIdFromContext());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/{offerId}/unapply")
    public void unApplyFromRequest(@PathVariable ObjectId offerId) {
        this.helpOfferModelService.deleteApplication(offerId, getIdFromContext());
    }

    @PatchMapping(path = "/{offerId}/{applicationId}/accept")
    public HelpModelApplication acceptApplication(@PathVariable ObjectId offerId,
                                                  @PathVariable ObjectId applicationId) {
        return this.helpOfferModelService.acceptApplication(offerId, applicationId,
                getIdFromContext());
    }


    //TODO get by user

    //TODO get by status

    private ObjectId getIdFromContext() {
        return (ObjectId) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
