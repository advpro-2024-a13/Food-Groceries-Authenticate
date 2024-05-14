package com.bezkoder.springjwt.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ERoleTest {

    @Test
    void testEnumValues() {
        assertEquals(0, ERole.ROLE_PEMBELI.ordinal());
        assertEquals(1, ERole.ROLE_PENGELOLA.ordinal());
        assertEquals(2, ERole.ROLE_ADMIN.ordinal());
    }
}