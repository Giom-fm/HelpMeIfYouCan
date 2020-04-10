package de.helpmeifyoucan.helpmeifyoucan.utils.listSerializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.helpmeifyoucan.helpmeifyoucan.models.HelpModelApplication;

import java.io.IOException;
import java.util.List;

public class ListAcceptedApplicationSerializer extends StdSerializer<List<HelpModelApplication>> {
    protected ListAcceptedApplicationSerializer(Class<List<HelpModelApplication>> t) {
        super(t);
    }

    protected ListAcceptedApplicationSerializer() {
        this(null);
    }

    @Override
    public void serialize(List<HelpModelApplication> helpModelApplications, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartArray();

        helpModelApplications.forEach(x -> {
            try {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeStringField("id", x.getId().toString());
                jsonGenerator.writeStringField("name", x.getName());
                jsonGenerator.writeStringField("lastName", x.getLastName());
                jsonGenerator.writeStringField("message", x.getMessage());
                jsonGenerator.writeStringField("user", x.getUser().toString());
                jsonGenerator.writeStringField("phoneNr", x.getTelephoneNr());
                jsonGenerator.writeEndObject();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        jsonGenerator.writeEndArray();
    }
}
