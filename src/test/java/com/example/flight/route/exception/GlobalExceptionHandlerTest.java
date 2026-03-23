package com.example.flight.route.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleInvalidRouteException_InvalidRequestException_ReturnsBadRequestWithMessage() {
        String errorMessage = "Invalid request: origin and destination cannot be the same";
        InvalidRequest exception = new InvalidRequest(errorMessage);

        ResponseEntity<String> response = globalExceptionHandler.handleInvalidRouteException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
    }

    @Test
    void handleInvalidRouteException_InvalidRequestWithNullMessage_ReturnsBadRequestWithNull() {
        InvalidRequest exception = new InvalidRequest(null);

        ResponseEntity<String> response = globalExceptionHandler.handleInvalidRouteException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void handleInvalidRouteException_InvalidRequestWithEmptyMessage_ReturnsBadRequestWithEmptyString() {
        String errorMessage = "";
        InvalidRequest exception = new InvalidRequest(errorMessage);

        ResponseEntity<String> response = globalExceptionHandler.handleInvalidRouteException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
    }

    @Test
    void handleInvalidRouteException_InvalidRequestWithLongMessage_ReturnsBadRequestWithLongMessage() {
        String longErrorMessage = "This is a very long error message that contains detailed information about what went wrong with the request validation process and provides guidance on how to fix the issue";
        InvalidRequest exception = new InvalidRequest(longErrorMessage);

        ResponseEntity<String> response = globalExceptionHandler.handleInvalidRouteException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(longErrorMessage, response.getBody());
    }

    @Test
    void handleValidationExceptions_SingleFieldError_ReturnsBadRequestWithFieldError() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        org.springframework.validation.BindingResult bindingResult = mock(org.springframework.validation.BindingResult.class);
        FieldError fieldError = new FieldError("objectName", "username", "must not be empty");

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(java.util.Arrays.asList(fieldError));

        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleValidationExceptions(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("must not be empty", response.getBody().get("username"));
    }

    @Test
    void handleValidationExceptions_MultipleFieldErrors_ReturnsBadRequestWithAllFieldErrors() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        org.springframework.validation.BindingResult bindingResult = mock(org.springframework.validation.BindingResult.class);
        
        FieldError fieldError1 = new FieldError("objectName", "username", "must not be empty");
        FieldError fieldError2 = new FieldError("objectName", "password", "must be at least 8 characters");
        FieldError fieldError3 = new FieldError("objectName", "email", "must be a valid email address");

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(java.util.Arrays.asList(fieldError1, fieldError2, fieldError3));

        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleValidationExceptions(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().size());
        assertEquals("must not be empty", response.getBody().get("username"));
        assertEquals("must be at least 8 characters", response.getBody().get("password"));
        assertEquals("must be a valid email address", response.getBody().get("email"));
    }

    @Test
    void handleValidationExceptions_EmptyErrorList_ReturnsBadRequestWithEmptyMap() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        org.springframework.validation.BindingResult bindingResult = mock(org.springframework.validation.BindingResult.class);

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(java.util.Collections.emptyList());

        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleValidationExceptions(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void handleValidationExceptions_FieldErrorWithNullMessage_ReturnsBadRequestWithNullValue() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        org.springframework.validation.BindingResult bindingResult = mock(org.springframework.validation.BindingResult.class);
        FieldError fieldError = new FieldError("objectName", "username", null);

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(java.util.Arrays.asList(fieldError));

        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleValidationExceptions(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertNull(response.getBody().get("username"));
    }

    @Test
    void handleValidationExceptions_FieldErrorWithEmptyMessage_ReturnsBadRequestWithEmptyString() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        org.springframework.validation.BindingResult bindingResult = mock(org.springframework.validation.BindingResult.class);
        FieldError fieldError = new FieldError("objectName", "username", "");

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(java.util.Arrays.asList(fieldError));

        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleValidationExceptions(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("", response.getBody().get("username"));
    }

    @Test
    void handleValidationExceptions_DuplicateFieldNames_ReturnsBadRequestWithLastValue() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        org.springframework.validation.BindingResult bindingResult = mock(org.springframework.validation.BindingResult.class);
        
        FieldError fieldError1 = new FieldError("objectName", "username", "must not be empty");
        FieldError fieldError2 = new FieldError("objectName", "username", "must be at least 3 characters");

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(java.util.Arrays.asList(fieldError1, fieldError2));

        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleValidationExceptions(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("must be at least 3 characters", response.getBody().get("username"));
    }

    @Test
    void handleValidationExceptions_SpecialCharactersInFieldName_ReturnsBadRequestWithSpecialCharacters() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        org.springframework.validation.BindingResult bindingResult = mock(org.springframework.validation.BindingResult.class);
        FieldError fieldError = new FieldError("objectName", "user-name", "must not be empty");

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(java.util.Arrays.asList(fieldError));

        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleValidationExceptions(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("must not be empty", response.getBody().get("user-name"));
    }

    @Test
    void handleValidationExceptions_SpecialCharactersInErrorMessage_ReturnsBadRequestWithSpecialCharacters() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        org.springframework.validation.BindingResult bindingResult = mock(org.springframework.validation.BindingResult.class);
        FieldError fieldError = new FieldError("objectName", "username", "Error: Field cannot contain special characters like @#$%");

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(java.util.Arrays.asList(fieldError));

        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleValidationExceptions(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Error: Field cannot contain special characters like @#$%", response.getBody().get("username"));
    }

    @Test
    void handleValidationExceptions_LongFieldName_ReturnsBadRequestWithLongFieldName() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        org.springframework.validation.BindingResult bindingResult = mock(org.springframework.validation.BindingResult.class);
        String longFieldName = "verylongfieldnamethatexceedsnormallengthsandtestshandlingofsuchcases";
        FieldError fieldError = new FieldError("objectName", longFieldName, "must not be empty");

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(java.util.Arrays.asList(fieldError));

        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleValidationExceptions(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("must not be empty", response.getBody().get(longFieldName));
    }

    @Test
    void handleValidationExceptions_LongErrorMessage_ReturnsBadRequestWithLongErrorMessage() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        org.springframework.validation.BindingResult bindingResult = mock(org.springframework.validation.BindingResult.class);
        String longErrorMessage = "This is a very long validation error message that contains detailed information about what went wrong with the field validation process and provides comprehensive guidance on how to fix the issue and meet all the required validation criteria";
        FieldError fieldError = new FieldError("objectName", "username", longErrorMessage);

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(java.util.Arrays.asList(fieldError));

        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleValidationExceptions(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(longErrorMessage, response.getBody().get("username"));
    }

    @Test
    void handleValidationExceptions_NullBindingResult_HandlesGracefully() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getBindingResult()).thenReturn(null);

        assertThrows(NullPointerException.class, 
                () -> globalExceptionHandler.handleValidationExceptions(exception));
    }

    @Test
    void handleValidationExceptions_NullErrorList_HandlesGracefully() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        org.springframework.validation.BindingResult bindingResult = mock(org.springframework.validation.BindingResult.class);

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(null);

        assertThrows(NullPointerException.class, 
                () -> globalExceptionHandler.handleValidationExceptions(exception));
    }
}
