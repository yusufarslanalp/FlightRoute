package com.example.FlightRoute.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvalidRequestTest {

    @Test
    void InvalidRequest_WithMessage_SetsMessageCorrectly() {
        String message = "Invalid request message";

        InvalidRequest exception = new InvalidRequest(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    void InvalidRequest_WithNullMessage_HandlesNullCorrectly() {
        String message = null;

        InvalidRequest exception = new InvalidRequest(message);

        assertNull(exception.getMessage());
    }

    @Test
    void InvalidRequest_WithEmptyMessage_HandlesEmptyCorrectly() {
        String message = "";

        InvalidRequest exception = new InvalidRequest(message);

        assertEquals("", exception.getMessage());
    }

    @Test
    void InvalidRequest_WithLongMessage_HandlesLongMessageCorrectly() {
        String longMessage = "This is a very long error message that contains detailed information about what went wrong with the request validation process and provides guidance on how to fix the issue and meet all the required criteria";

        InvalidRequest exception = new InvalidRequest(longMessage);

        assertEquals(longMessage, exception.getMessage());
    }

    @Test
    void InvalidRequest_WithSpecialCharacters_HandlesSpecialCharactersCorrectly() {
        String messageWithSpecialChars = "Error: Invalid characters @#$%^&*() in request";

        InvalidRequest exception = new InvalidRequest(messageWithSpecialChars);

        assertEquals(messageWithSpecialChars, exception.getMessage());
    }

    @Test
    void InvalidRequest_WithUnicodeCharacters_HandlesUnicodeCorrectly() {
        String unicodeMessage = "错误信息：请求无效 émojis 🚀 ñiño";

        InvalidRequest exception = new InvalidRequest(unicodeMessage);

        assertEquals(unicodeMessage, exception.getMessage());
    }

    @Test
    void InvalidRequest_WithNewlinesAndTabs_HandlesWhitespaceCorrectly() {
        String messageWithWhitespace = "Error:\n\tInvalid request\n\tPlease check your input";

        InvalidRequest exception = new InvalidRequest(messageWithWhitespace);

        assertEquals(messageWithWhitespace, exception.getMessage());
    }

    @Test
    void InvalidRequest_IsRuntimeException_ReturnsTrue() {
        InvalidRequest exception = new InvalidRequest("test message");

        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void InvalidRequest_HasStackTrace_ReturnsStackTrace() {
        String message = "test message";

        InvalidRequest exception = new InvalidRequest(message);

        assertNotNull(exception.getStackTrace());
        assertTrue(exception.getStackTrace().length > 0);
    }

    @Test
    void InvalidRequest_CauseIsNull_ReturnsNullCause() {
        String message = "test message";

        InvalidRequest exception = new InvalidRequest(message);

        assertNull(exception.getCause());
    }

    @Test
    void InvalidRequest_ToString_ReturnsStringRepresentation() {
        String message = "test message";

        InvalidRequest exception = new InvalidRequest(message);

        String stringRepresentation = exception.toString();
        assertNotNull(stringRepresentation);
        assertTrue(stringRepresentation.contains("InvalidRequest"));
        assertTrue(stringRepresentation.contains(message));
    }

    @Test
    void InvalidRequest_MultipleInstances_AreIndependent() {
        String message1 = "First error message";
        String message2 = "Second error message";

        InvalidRequest exception1 = new InvalidRequest(message1);
        InvalidRequest exception2 = new InvalidRequest(message2);

        assertEquals(message1, exception1.getMessage());
        assertEquals(message2, exception2.getMessage());
        assertNotEquals(exception1, exception2);
    }

    @Test
    void InvalidRequest_SameMessage_EqualAndHashCode() {
        String message = "Same error message";

        InvalidRequest exception1 = new InvalidRequest(message);
        InvalidRequest exception2 = new InvalidRequest(message);

        assertEquals(exception1.getMessage(), exception2.getMessage());
        assertNotEquals(exception1, exception2);
    }

    @Test
    void InvalidRequest_WithNumericMessage_HandlesNumericCorrectly() {
        String numericMessage = "Error code: 404";

        InvalidRequest exception = new InvalidRequest(numericMessage);

        assertEquals(numericMessage, exception.getMessage());
    }

    @Test
    void InvalidRequest_WithHtmlTags_HandlesHtmlCorrectly() {
        String htmlMessage = "<error>Invalid request</error><details>Field cannot be empty</details>";

        InvalidRequest exception = new InvalidRequest(htmlMessage);

        assertEquals(htmlMessage, exception.getMessage());
    }

    @Test
    void InvalidRequest_WithJsonMessage_HandlesJsonCorrectly() {
        String jsonMessage = "{\"error\":\"Invalid request\",\"field\":\"username\",\"message\":\"must not be empty\"}";

        InvalidRequest exception = new InvalidRequest(jsonMessage);

        assertEquals(jsonMessage, exception.getMessage());
    }

    @Test
    void InvalidRequest_WithFormattedMessage_HandlesFormattingCorrectly() {
        String formattedMessage = String.format("Validation failed for field '%s': %s", "username", "must not be empty");

        InvalidRequest exception = new InvalidRequest(formattedMessage);

        assertEquals(formattedMessage, exception.getMessage());
    }
}
