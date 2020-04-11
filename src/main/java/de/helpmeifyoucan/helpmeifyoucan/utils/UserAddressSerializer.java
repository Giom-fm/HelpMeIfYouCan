package de.helpmeifyoucan.helpmeifyoucan.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.helpmeifyoucan.helpmeifyoucan.models.AddressModel;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class UserAddressSerializer extends StdSerializer<AddressModel> {


    protected UserAddressSerializer(Class<AddressModel> t) {
        super(t);
    }

    protected UserAddressSerializer() {
        this(null);
    }

    @Override
    public void serialize(AddressModel addressModel, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("street", addressModel.getStreet());
        jsonGenerator.writeStringField("district", addressModel.getDistrict());
        jsonGenerator.writeStringField("zipCode", addressModel.getZipCode());
        jsonGenerator.writeStringField("houseNumber", addressModel.getHouseNumber());
        jsonGenerator.writeStringField("country", addressModel.getCountry());
        jsonGenerator.writeEndObject();


    }


}
