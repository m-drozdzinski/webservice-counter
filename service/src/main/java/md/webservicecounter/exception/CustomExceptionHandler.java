/*
 * The MIT License
 *
 * Copyright 2022 Marcin Drozdziński.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package md.webservicecounter.exception;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public void handleNotFound(HttpServletResponse response) throws IOException {
        createJSONErrorResponse(HttpServletResponse.SC_NOT_FOUND, "Resource does not exists", response);
    }
    
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public void handleAlreadyExists(HttpServletResponse response) throws IOException {
        createJSONErrorResponse(HttpServletResponse.SC_BAD_REQUEST, "Resource already exists", response);
    }
    
    @ExceptionHandler(NotImplementedException.class)
    public void handleNotImplemented(HttpServletResponse response) throws IOException {
        createJSONErrorResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Service not implemented", response);
    }
    
        @ExceptionHandler(RegistrationException.class)
    public void handleRegistrationException(HttpServletResponse response) throws IOException {
        createJSONErrorResponse(HttpServletResponse.SC_BAD_REQUEST, "User already exists", response);
    }
    
    public static String createErrorJSON(int errorCode, String errorMessage) {
        ObjectNode errorNode = JsonNodeFactory.instance.objectNode();
        errorNode.put("code", errorCode);
        errorNode.put("message", errorMessage);
        ObjectNode root = JsonNodeFactory.instance.objectNode();
        root.set("error", errorNode);
        return root.toString();
    }

    public static void createJSONErrorResponse(int errorCode, String errorMessage, HttpServletResponse response)
            throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(errorCode);
        response.getWriter().write(createErrorJSON(errorCode, errorMessage));
    }
   

}
