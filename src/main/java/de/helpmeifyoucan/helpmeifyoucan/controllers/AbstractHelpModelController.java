package de.helpmeifyoucan.helpmeifyoucan.controllers;


import de.helpmeifyoucan.helpmeifyoucan.models.AbstractHelpModel;
import de.helpmeifyoucan.helpmeifyoucan.models.HelpModelApplication;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.CoordinatesUpdate;
import de.helpmeifyoucan.helpmeifyoucan.services.AbstractHelpModelService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public T save(@Valid @RequestBody T request) {
        return this.modelService.saveNewModel(request, getIdFromContext());
    }


    @PatchMapping(path = "/{modelId}/updateCoords", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public T updateCoords(@PathVariable ObjectId modelId, @RequestBody CoordinatesUpdate update) {
        return this.modelService.handleCoordinatesUpdate(modelId, update, getIdFromContext());
    }

    @DeleteMapping(path = "/{modelId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public T delete(@PathVariable ObjectId modelId) {
        return this.modelService.deleteModel(modelId, getIdFromContext());
    }

    @GetMapping(path = "/{modelId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public T get(@PathVariable ObjectId modelId) {
        return this.modelService.getById(modelId);
    }

    @GetMapping(path = "/getall", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<T> getAllById(@RequestBody List<ObjectId> ids) {
        return this.modelService.getAllById(ids);
    }

    @PostMapping(path = "/{modelId}/apply", consumes = MediaType.APPLICATION_JSON_VALUE, produces =
            MediaType.APPLICATION_JSON_VALUE)
    public HelpModelApplication applyToRequest(@PathVariable ObjectId modelId,
                                               @RequestBody HelpModelApplication application) {
        return this.modelService.saveNewApplication(modelId, application, getIdFromContext());
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/{modelId}/unapply")
    public void unApplyFromRequest(@PathVariable ObjectId modelId) {
        this.modelService.deleteApplication(modelId, getIdFromContext());
    }

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
