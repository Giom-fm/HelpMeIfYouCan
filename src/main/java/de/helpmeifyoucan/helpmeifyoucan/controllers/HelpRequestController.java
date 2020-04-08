package de.helpmeifyoucan.helpmeifyoucan.controllers;

import de.helpmeifyoucan.helpmeifyoucan.models.HelpModelApplication;
import de.helpmeifyoucan.helpmeifyoucan.models.HelpRequestModel;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.CoordinatesUpdate;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.HelpRequestUpdate;
import de.helpmeifyoucan.helpmeifyoucan.services.HelpRequestModelService;
import de.helpmeifyoucan.helpmeifyoucan.services.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/requests")
public class HelpRequestController {

    private HelpRequestModelService helpRequestModelService;

    private UserService userService;

    @Autowired
    public HelpRequestController(HelpRequestModelService helpRequestModelService, UserService userService) {
        this.helpRequestModelService = helpRequestModelService;
        this.userService = userService;
    }


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HelpRequestModel save(@RequestBody HelpRequestModel request) {
        return this.helpRequestModelService.saveNewRequest(request, getIdFromContext());
    }


    @PatchMapping(path = "/{requestId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HelpRequestModel update(@PathVariable ObjectId requestId, @RequestBody HelpRequestUpdate update) {
        return this.helpRequestModelService.update(requestId, update, getIdFromContext());
    }

    @PatchMapping(path = "/updateCoords", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HelpRequestModel updateCoords(@PathVariable ObjectId requestId, @RequestBody CoordinatesUpdate update) {
        return this.helpRequestModelService.handleCoordinatesUpdate(requestId, update, getIdFromContext());
    }

    @DeleteMapping(path = "/{requestId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public HelpRequestModel delete(@PathVariable ObjectId requestId) {
        return this.helpRequestModelService.deleteRequest(requestId, getIdFromContext());
    }

    @GetMapping(path = "/{requestId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public HelpRequestModel get(@PathVariable ObjectId requestId) {
        return this.helpRequestModelService.getById(requestId);
    }


    @PostMapping(path = "/apply/{requestId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HelpModelApplication applyToRequest(@PathVariable ObjectId requestId, @RequestBody HelpModelApplication application) {
        return this.helpRequestModelService.saveNewApplication(requestId, application, getIdFromContext());
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/unapply/{requestId}={applicationId}")
    public void unApplyFromRequest(@PathVariable ObjectId requestId, @PathVariable ObjectId applicationId) {
        this.helpRequestModelService.deleteApplication(requestId, applicationId, getIdFromContext());
    }

    @PatchMapping(path = "/accept/{requestId}={applicationId}")
    public HelpModelApplication acceptApplication(@PathVariable ObjectId requestId, @PathVariable ObjectId applicationId) {
        return this.helpRequestModelService.acceptApplication(requestId, applicationId, getIdFromContext());
    }


    //TODO get by user

    //TODO get by status

    private ObjectId getIdFromContext() {
        return (ObjectId) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
