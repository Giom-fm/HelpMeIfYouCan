package de.helpmeifyoucan.helpmeifyoucan.utils;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import org.springframework.stereotype.Component;

import de.helpmeifyoucan.helpmeifyoucan.models.Coordinates;

@Component
public class EmbeddedCoordsMapper extends StdSerializer<Coordinates> {

    private static final long serialVersionUID = -397508863117016407L;

    public EmbeddedCoordsMapper() {
        this(null);
    }

    public EmbeddedCoordsMapper(Class<Coordinates> t) {
        super(t);
    }

    @Override
    public void serialize(
            Coordinates value, JsonGenerator jgen, SerializerProvider provider) throws IOException {

        jgen.writeStartObject();
        jgen.writeStringField("id", value.getId().toString());
        jgen.writeObjectField("helpOffers", value.getHelpOffers());
        jgen.writeObjectField("helpRequests", value.getHelpRequests());
        jgen.writeNumberField("latitude", value.getLatitude());
        jgen.writeNumberField("longitude", value.getLongitude());
        jgen.writeEndObject();
    }


}
