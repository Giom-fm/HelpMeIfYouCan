package de.helpmeifyoucan.helpmeifyoucan.utils.objectSerializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.helpmeifyoucan.helpmeifyoucan.models.HelpModelApplication;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class UserAcceptedApplicationSerializer extends StdSerializer<HashMap<String,
        List<HelpModelApplication>>> {
    private JsonGenerator jsonGenerator;

    protected UserAcceptedApplicationSerializer(Class<HashMap<String, List<HelpModelApplication>>> t) {
        super(t);
    }

    protected UserAcceptedApplicationSerializer() {
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

    private void writeApplicationSend(List<HelpModelApplication> applications) throws IOException {
        jsonGenerator.writeStartArray();
        applications.forEach(x -> {
            try {
                jsonGenerator.writeStartObject();
                writeCoreApplication(x);
                jsonGenerator.writeEndObject();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        jsonGenerator.writeEndArray();

    }

    private void writeApplicationReceived(List<HelpModelApplication> applications) throws IOException {
        jsonGenerator.writeStartArray();
        applications.forEach(x -> {
            try {
                jsonGenerator.writeStartObject();
                writeCoreApplication(x);
                jsonGenerator.writeBooleanField("read", x.isRead());
                jsonGenerator.writeEndObject();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        jsonGenerator.writeEndArray();

    }

    private void writeCoreApplication(HelpModelApplication application) throws IOException {
        jsonGenerator.writeStringField("id", application.getId().toString());
        jsonGenerator.writeStringField("modelId", application.getModelId().toString());
        jsonGenerator.writeObjectField("created", application.getCreated());
        jsonGenerator.writeStringField("name", application.getName());
        jsonGenerator.writeStringField("lastName", application.getLastName());
        jsonGenerator.writeStringField("message", application.getMessage());
        jsonGenerator.writeStringField("phoneNr", application.getTelephoneNr());
        jsonGenerator.writeStringField("helpModelType", application.getHelpModelType());
    }

}
