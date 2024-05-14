package com.bezkoder.springjwt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RoleTest {

    private Role role;

    @BeforeEach
    public void setUp() {
        role = new Role();
    }

    @Test
    void testId() {
        role.setId(1);
        assertEquals(1, role.getId());
    }

    @Test
    void testName() {
        role.setName(ERole.ROLE_ADMIN);
        assertEquals(ERole.ROLE_ADMIN, role.getName());
    }

    @Test
    void testConstructor() {
        Role roleWithParam = new Role(ERole.ROLE_ADMIN);
        assertEquals(ERole.ROLE_ADMIN, roleWithParam.getName());
    }
}