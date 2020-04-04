package de.helpmeifyoucan.helpmeifyoucan.security.filters;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class FilterUtils {

    private static ObjectMapper mapper = new ObjectMapper();

    public static void send(HttpServletResponse response, ResponseEntity<? extends Object> responseObject)
            throws IOException, ServletException {
        var json = FilterUtils.convertObjectToJson(responseObject.getBody());
        response.setStatus(responseObject.getStatusCode().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        var responseWriter = response.getWriter();
        responseWriter.write(json);
        responseWriter.flush();
    }

    public static String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        return mapper.writeValueAsString(object);
    }

}