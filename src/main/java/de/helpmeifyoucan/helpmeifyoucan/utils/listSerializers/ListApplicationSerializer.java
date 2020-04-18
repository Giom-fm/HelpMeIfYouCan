package de.helpmeifyoucan.helpmeifyoucan.utils.listSerializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.helpmeifyoucan.helpmeifyoucan.models.HelpModelApplication;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class ListApplicationSerializer extends StdSerializer<List<HelpModelApplication>> {

    protected ListApplicationSerializer(Class<List<HelpModelApplication>> t) {
        super(t);
    }

    protected ListApplicationSerializer() {
        this(null);
    }

    @Override
    public void serialize(List<HelpModelApplication> helpModelApplications, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartArray();

        helpModelApplications.forEach(x -> {
            try {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeStringField("id", x.getId().toString());
                jsonGenerator.writeStringField("modelId", x.getModelId().toString());
                jsonGenerator.writeStringField("name", x.getName());
                jsonGenerator.writeStringField("message", x.getMessage());
                jsonGenerator.writeEndObject();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        jsonGenerator.writeEndArray();
    }
}
