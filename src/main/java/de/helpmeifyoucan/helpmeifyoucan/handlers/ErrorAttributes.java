
package de.helpmeifyoucan.helpmeifyoucan.handlers;

import java.util.Map;

import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import de.helpmeifyoucan.helpmeifyoucan.utils.errors.AbstractExceptions.ApiException;

@Component
public class ErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {

        Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, includeStackTrace);
        Throwable throwable = this.getError(webRequest);

        if (throwable instanceof ApiException) {
            ApiException ex = (ApiException) throwable;
            errorAttributes.put("code", ex.getCode());
        } 

        if(includeStackTrace){
            errorAttributes.put("trace", throwable.getStackTrace());
        }
     
        return errorAttributes;
    }
}
