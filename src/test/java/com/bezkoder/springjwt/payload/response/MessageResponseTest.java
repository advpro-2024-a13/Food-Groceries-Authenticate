package com.bezkoder.springjwt.payload.response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MessageResponseTest {

    private MessageResponse messageResponse;

    @BeforeEach
    public void setUp() {
        messageResponse = new MessageResponse("Test message");
    }

    @Test
    void testMessage() {
        messageResponse.setMessage("New message");
        assertEquals("New message", messageResponse.getMessage());
    }
}