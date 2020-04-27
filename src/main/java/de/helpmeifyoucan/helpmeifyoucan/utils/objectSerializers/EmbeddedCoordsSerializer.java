package de.helpmeifyoucan.helpmeifyoucan.utils.objectSerializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.helpmeifyoucan.helpmeifyoucan.models.Coordinates;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class EmbeddedCoordsSerializer extends StdSerializer<Coordinates> {

    private static final long serialVersionUID = -397508863117016407L;

    public EmbeddedCoordsSerializer() {
        this(null);
    }

    public EmbeddedCoordsSerializer(Class<Coordinates> t) {
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
