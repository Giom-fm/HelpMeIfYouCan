package de.helpmeifyoucan.helpmeifyoucan.controllers;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ErrorProperties.IncludeStacktrace;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import de.helpmeifyoucan.helpmeifyoucan.utils.errors.AbstractExceptions.ApiException;

@Controller
// @RequestMapping("${server.error.path:${error.path:/error}}")
public class ErrorController extends AbstractErrorController {

    private final ErrorProperties errorProperties;
    private static final String ERROR_ATTRIBUTE = DefaultErrorAttributes.class.getName() + ".ERROR";

    @Autowired
    public ErrorController(ErrorAttributes errorAttributes, ServerProperties serverProperties) {
        super(errorAttributes, Collections.emptyList());
        this.errorProperties = serverProperties.getError();
    }

    @RequestMapping("/error")
    public ResponseEntity<Map<String, Object>> handleException(HttpServletRequest request, HttpStatus status) {

        WebRequest webRequest = new ServletWebRequest(request);
        
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        var choosenStatus = this.getStatus(webRequest, status);
        this.addStatus(body, choosenStatus);
        this.addPath(body, webRequest);

        var includeTrace = this.errorProperties.getIncludeStacktrace().equals(IncludeStacktrace.ALWAYS);
        this.addError(body, webRequest, includeTrace);
      
        return new ResponseEntity<>(body, choosenStatus);
    }

    private HttpStatus getStatus(RequestAttributes attributes, HttpStatus status){
        Integer servletStatus = this.getAttribute(attributes, "javax.servlet.error.status_code");

        if(status != null){
            return status;
        }else if (servletStatus != null){
            return HttpStatus.valueOf(servletStatus);
        }else {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }

    }


    private void addError(Map<String, Object> body, WebRequest webRequest, boolean includeStacktrace) {
        var throwable = this.getError(webRequest);
        if(throwable != null){
            
            body.put("message", throwable.getMessage());
            if (throwable instanceof ApiException) {
                ApiException ex = (ApiException) throwable;
                body.put("code", ex.getCode());
            }
            if(includeStacktrace){
                body.put("exception", throwable.toString());
                body.put("trace", throwable.getStackTrace());
            }
        }
    }


    private void addStatus(Map<String, Object> body, HttpStatus status) {
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
    }



    private void addPath(Map<String, Object> body, RequestAttributes attributes) {
		String path = getAttribute(attributes, "javax.servlet.error.request_uri");
		if (path != null) {
			body.put("path", path);
		}
	}

    private Throwable getError(WebRequest webRequest) {
		Throwable exception = this.getAttribute(webRequest, ERROR_ATTRIBUTE);
		if (exception == null) {
			exception = getAttribute(webRequest, "javax.servlet.error.exception");
		}
		return exception;
	}


    @SuppressWarnings("unchecked")
    private <T> T getAttribute(RequestAttributes requestAttributes, String name) {
        return (T) requestAttributes.getAttribute(name, RequestAttributes.SCOPE_REQUEST);
    }

    @Override
    public String getErrorPath() {
        return this.errorProperties.getPath();
    }


    

}