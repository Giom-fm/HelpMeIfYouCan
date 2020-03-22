package de.helpmeifyoucan.helpmeifyoucan.controllers.database;

import org.bson.types.ObjectId;

import de.helpmeifyoucan.helpmeifyoucan.models.Address;

public class AddressController extends AbstractEntityController<Address> {

    public Address get(ObjectId id) {
        return this.get(id, Address.class);
    }

}