package de.helpmeifyoucan.helpmeifyoucan.controllers;

import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ErrorProperties.IncludeStacktrace;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
//@RequestMapping("${server.error.path:${error.path:/error}}")
public class ErrorController extends AbstractErrorController {

    private final ErrorProperties errorProperties;

    @Autowired
    public ErrorController(ErrorAttributes errorAttributes, ServerProperties serverProperties) {
        super(errorAttributes, Collections.emptyList());
        this.errorProperties = serverProperties.getError();
    }

    @RequestMapping("/error}")
	public ResponseEntity<Map<String, Object>> handleException(HttpServletRequest request, HttpStatus status) {
        var includeTrace = this.errorProperties.getIncludeStacktrace().equals(IncludeStacktrace.ALWAYS);
        Map<String, Object> body = this.getErrorAttributes(request,includeTrace);
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
		return new ResponseEntity<>(body, status);
	}

    @Override
    public String getErrorPath() {
        return this.errorProperties.getPath();
    }

}