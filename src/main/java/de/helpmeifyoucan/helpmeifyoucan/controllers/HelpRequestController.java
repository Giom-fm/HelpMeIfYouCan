package de.helpmeifyoucan.helpmeifyoucan.controllers;

import de.helpmeifyoucan.helpmeifyoucan.models.HelpRequestModel;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.HelpRequestUpdate;
import de.helpmeifyoucan.helpmeifyoucan.services.AbstractHelpModelService;
import de.helpmeifyoucan.helpmeifyoucan.utils.Role;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/requests")
public class HelpRequestController extends AbstractHelpModelController<HelpRequestModel> {

    @Autowired
    public HelpRequestController(@Qualifier("helpRequestModelService") AbstractHelpModelService<HelpRequestModel> modelService) {
        super(modelService);
    }

    @Secured({Role.ROLE_NAME_USER})
    @PatchMapping(path = "/{requestId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces =
            MediaType.APPLICATION_JSON_VALUE)
    public HelpRequestModel update(@PathVariable ObjectId requestId,
                                   @RequestBody HelpRequestUpdate update) {
        return super.modelService.update(requestId, update, super.getIdFromContext());
    }
}
