package de.helpmeifyoucan.helpmeifyoucan;

import de.helpmeifyoucan.helpmeifyoucan.models.AbstractEntity;
import de.helpmeifyoucan.helpmeifyoucan.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;


@Service
public class StaticDbClear {

    @Autowired
    private AddressService addressService;

    @Autowired
    private CoordinatesService coordinatesService;

    @Autowired
    private HelpRequestModelService helpRequestModelService;

    @Autowired
    private HelpOfferModelService helpOfferModelService;

    @Autowired
    private UserService userService;

    public void clearDb() {

        Field[] fields = this.getClass().getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {

            try {
                AbstractService<? extends AbstractEntity> service = (AbstractService<? extends AbstractEntity>) fields[i].get(this);
                service.resetDB();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    }
}
