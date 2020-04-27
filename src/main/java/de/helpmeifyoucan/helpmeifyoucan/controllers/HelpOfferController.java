package de.helpmeifyoucan.helpmeifyoucan.controllers;


import de.helpmeifyoucan.helpmeifyoucan.models.HelpOfferModel;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.HelpOfferUpdate;
import de.helpmeifyoucan.helpmeifyoucan.services.AbstractHelpModelService;
import de.helpmeifyoucan.helpmeifyoucan.utils.Role;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/offers")
public class HelpOfferController extends AbstractHelpModelController<HelpOfferModel> {

    @Autowired
    public HelpOfferController(@Qualifier("helpOfferModelService") AbstractHelpModelService<HelpOfferModel> modelService) {
        super(modelService);
    }

    @Secured({Role.ROLE_NAME_USER})
    @PatchMapping(path = "/{offerId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces =
            MediaType.APPLICATION_JSON_VALUE)
    public HelpOfferModel update(@PathVariable ObjectId offerId,
                                 @RequestBody HelpOfferUpdate update) {
        return super.modelService.update(offerId, update, super.getIdFromContext());
    }
}
