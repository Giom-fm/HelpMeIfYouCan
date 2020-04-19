package de.helpmeifyoucan.helpmeifyoucan.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.helpmeifyoucan.helpmeifyoucan.models.HelpModelApplication;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Component
public class UserApplicationsSerializer extends StdSerializer<HashMap<String,
        List<HelpModelApplication>>> {
    private JsonGenerator jsonGenerator;

    protected UserApplicationsSerializer(Class<HashMap<String, List<HelpModelApplication>>> t) {
        super(t);
    }

    protected UserApplicationsSerializer() {
        this(null);
    }

    @Override
    public void serialize(HashMap<String, List<HelpModelApplication>> helpModelApplications,
                          JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        this.jsonGenerator = jsonGenerator;

        jsonGenerator.writeStartObject();

        jsonGenerator.writeFieldName("received");
        writeApplicationReceived(helpModelApplications.get("received"));

        jsonGenerator.writeFieldName("send");
        writeApplicationSend(helpModelApplications.get("send"));
        jsonGenerator.writeEndObject();

    }

    private void writeApplicationReceived(List<HelpModelApplication> applications) throws IOException {
        jsonGenerator.writeStartArray();
        applications.forEach(x -> {
            try {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeStringField("id", x.getId().toString());
                jsonGenerator.writeStringField("modelId", x.getModelId().toString());
                jsonGenerator.writeStringField("name", x.getName());
                jsonGenerator.writeStringField("message", x.getMessage());
                jsonGenerator.writeStringField("helpModelType", x.getHelpModelType());
                jsonGenerator.writeBooleanField("read", x.isRead());
                jsonGenerator.writeEndObject();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        jsonGenerator.writeEndArray();

    }

    private void writeApplicationSend(List<HelpModelApplication> applications) throws IOException {
        jsonGenerator.writeStartArray();
        applications.forEach(x -> {
            try {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeStringField("id", x.getId().toString());
                jsonGenerator.writeStringField("modelId", x.getModelId().toString());
                jsonGenerator.writeStringField("message", x.getMessage());
                jsonGenerator.writeStringField("helpModelType", x.getHelpModelType());
                jsonGenerator.writeEndObject();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        jsonGenerator.writeEndArray();

    }
}
