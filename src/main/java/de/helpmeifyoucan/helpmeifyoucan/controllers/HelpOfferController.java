package de.helpmeifyoucan.helpmeifyoucan.controllers;


import de.helpmeifyoucan.helpmeifyoucan.models.HelpOfferModel;
import de.helpmeifyoucan.helpmeifyoucan.services.HelpOfferModelService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/offers")
public class HelpOfferController {

    private HelpOfferModelService helpOfferModelService;

    @Autowired
    public HelpOfferController(HelpOfferModelService helpOfferModelService) {
        this.helpOfferModelService = helpOfferModelService;
    }

    @GetMapping("/{id}")
    public HelpOfferModel get(@PathVariable ObjectId id) {
        return helpOfferModelService.getById(id);
    }
}
