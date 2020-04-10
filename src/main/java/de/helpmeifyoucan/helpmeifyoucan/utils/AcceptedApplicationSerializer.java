package de.helpmeifyoucan.helpmeifyoucan.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.helpmeifyoucan.helpmeifyoucan.models.HelpModelApplication;

import java.io.IOException;

public class AcceptedApplicationSerializer extends StdSerializer<HelpModelApplication> {


    protected AcceptedApplicationSerializer(Class<HelpModelApplication> t) {
        super(t);
    }

    protected AcceptedApplicationSerializer() {
        this(null);
    }

    @Override
    public void serialize(HelpModelApplication application, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("id", application.getId().toString());
        jsonGenerator.writeStringField("name", application.getName());
        jsonGenerator.writeStringField("lastName", application.getLastName());
        jsonGenerator.writeStringField("message", application.getMessage());
        jsonGenerator.writeEndObject();
    }

}
