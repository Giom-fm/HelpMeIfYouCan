package de.helpmeifyoucan.helpmeifyoucan.controllers;


import de.helpmeifyoucan.helpmeifyoucan.models.AbstractHelpModel;
import de.helpmeifyoucan.helpmeifyoucan.models.HelpModelApplication;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.CoordinatesUpdate;
import de.helpmeifyoucan.helpmeifyoucan.services.AbstractHelpModelService;
import de.helpmeifyoucan.helpmeifyoucan.utils.Role;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

public abstract class AbstractHelpModelController<T extends AbstractHelpModel> {

    protected AbstractHelpModelService<T> modelService;

    @Autowired
    public AbstractHelpModelController(AbstractHelpModelService<T> modelService) {
        this.modelService = modelService;
    }

    @Secured({Role.ROLE_NAME_USER})
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public T save(@Valid @RequestBody T request) {
        return this.modelService.saveNewModel(request, getIdFromContext());
    }

    @Secured({Role.ROLE_NAME_USER})
    @PatchMapping(path = "/{modelId}/updateCoords", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public T updateCoords(@PathVariable ObjectId modelId, @RequestBody CoordinatesUpdate update) {
        return this.modelService.handleCoordinatesUpdate(modelId, update, getIdFromContext());
    }

    @Secured({Role.ROLE_NAME_USER})
    @DeleteMapping(path = "/{modelId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public T delete(@PathVariable ObjectId modelId) {
        return this.modelService.deleteModel(modelId, getIdFromContext());
    }

    @Secured({Role.ROLE_NAME_USER})
    @GetMapping(path = "/{modelId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public T get(@PathVariable ObjectId modelId) {
        return this.modelService.getById(modelId);
    }

    @Secured({Role.ROLE_NAME_USER})
    @GetMapping(path = "/getall", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<T> getAllById(@RequestBody List<ObjectId> ids) {
        return this.modelService.getAllById(ids);
    }

    @Secured({Role.ROLE_NAME_USER})
    @PostMapping(path = "/{modelId}/apply", consumes = MediaType.APPLICATION_JSON_VALUE, produces =
            MediaType.APPLICATION_JSON_VALUE)
    public HelpModelApplication applyToRequest(@PathVariable ObjectId modelId,
                                               @Valid @RequestBody HelpModelApplication application) {
        return this.modelService.handleNewApplication(modelId, application, getIdFromContext());
    }


    @Secured({Role.ROLE_NAME_USER})
    @DeleteMapping(path = "/{modelId}/unapply")
    public HelpModelApplication unApplyFromRequest(@PathVariable ObjectId modelId) {
        return this.modelService.deleteApplication(modelId, getIdFromContext());
    }

    @Secured({Role.ROLE_NAME_USER})
    @PatchMapping(path = "/{modelId}/{applicationId}/accept")
    public HelpModelApplication acceptApplication(@PathVariable ObjectId modelId,
                                                  @PathVariable ObjectId applicationId) {
        return this.modelService.acceptApplication(modelId, applicationId, getIdFromContext());
    }


    //TODO get by user

    //TODO get by status

    protected ObjectId getIdFromContext() {
        return (ObjectId) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
