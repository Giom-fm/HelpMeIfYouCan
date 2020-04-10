package de.helpmeifyoucan.helpmeifyoucan.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.helpmeifyoucan.helpmeifyoucan.models.HelpModelApplication;

import java.io.IOException;
import java.util.List;

public class UserApplicationsSerializer extends StdSerializer<List<HelpModelApplication>> {

    protected UserApplicationsSerializer(Class<List<HelpModelApplication>> t) {
        super(t);
    }

    protected UserApplicationsSerializer() {
        this(null);
    }

    @Override
    public void serialize(List<HelpModelApplication> helpModelApplications, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartArray();

        helpModelApplications.forEach(x -> {
            try {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeStringField("id", x.getId().toString());
                jsonGenerator.writeStringField("message", x.getMessage());
                jsonGenerator.writeEndObject();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        jsonGenerator.writeEndArray();
    }
}
