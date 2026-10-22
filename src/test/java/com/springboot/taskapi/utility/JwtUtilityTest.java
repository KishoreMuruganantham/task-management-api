package com.springboot.taskapi.utility;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilityTest {
    private JwtUtility jwtUtility;

    @BeforeEach
    public void init(){
        jwtUtility = new JwtUtility();
        ReflectionTestUtils.setField(jwtUtility, "secretKey", "hhjdsyuwtyskfhsdfjhsjfhsdjfh");
    }

    @Test
    public void generateTokenTest(){
        String token = jwtUtility.generateToken("employee1");

        Date expiration = jwtUtility.extractExpiration(token);

        assertNotNull(token);
        assertEquals("employee1", jwtUtility.extractUsername(token));
        assertNotNull(expiration);
    }

    @Test
    public void validateTokenTest(){
        String token = jwtUtility.generateToken("employee1");

        assertTrue(jwtUtility.validateToken(token, "employee1"));
    }

    @Test
    public void validateTokenTest_InvalidUsername(){
        String token = jwtUtility.generateToken("employee1");

        assertFalse(jwtUtility.validateToken(token, "employee2"));
    }
}
