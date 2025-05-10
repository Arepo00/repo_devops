package com.example.classroom;

import com.example.classroom.util.PasswordUtil;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

public class PasswordUtilTest { // Renamed class

    @Test
    public void testGenerateSalt() {
        byte[] salt1 = PasswordUtil.generateSalt();
        byte[] salt2 = PasswordUtil.generateSalt();

        assertNotNull("Salt1 should not be null", salt1);
        assertEquals("Salt should be 16 bytes long", 16, salt1.length);

        assertNotNull("Salt2 should not be null", salt2);
        assertEquals("Salt should be 16 bytes long", 16, salt2.length);

        assertFalse("Two generated salts should be different", Arrays.equals(salt1, salt2));
    }

    @Test
    public void testHashPassword() {
        String password = "mySecretPassword123";
        byte[] salt = PasswordUtil.generateSalt();
        String hashedPassword = PasswordUtil.hashPassword(password, salt);

        assertNotNull("Hashed password should not be null", hashedPassword);
        assertNotEquals("Hashed password should not be the same as plain password", password, hashedPassword);

        // Test that the same password with a different salt produces a different hash
        byte[] salt2 = PasswordUtil.generateSalt();
        String hashedPassword2 = PasswordUtil.hashPassword(password, salt2);
        assertNotEquals("Hashing with different salt should produce different hash", hashedPassword, hashedPassword2);

        // Test that the same password with the same salt produces the same hash
        String hashedPasswordAgain = PasswordUtil.hashPassword(password, salt);
        assertEquals("Hashing with the same salt should produce the same hash", hashedPassword, hashedPasswordAgain);
    }

    @Test
    public void testVerifyPassword() {
        String password = "testPassword";
        byte[] salt = PasswordUtil.generateSalt();
        String hashedPassword = PasswordUtil.hashPassword(password, salt);

        // Correct password and salt
        assertTrue("Verification should succeed with correct password and salt",
                PasswordUtil.verifyPassword(password, hashedPassword, salt));

        // Incorrect password, correct salt
        assertFalse("Verification should fail with incorrect password",
                PasswordUtil.verifyPassword("wrongPassword", hashedPassword, salt));

        // Correct password, incorrect salt
        byte[] wrongSalt = PasswordUtil.generateSalt();
        assertFalse("Verification should fail with incorrect salt",
                PasswordUtil.verifyPassword(password, hashedPassword, wrongSalt));

        // Empty password
        String emptyPassword = "";
        byte[] saltForEmpty = PasswordUtil.generateSalt();
        String hashedEmptyPassword = PasswordUtil.hashPassword(emptyPassword, saltForEmpty);
        assertTrue("Verification should succeed for empty password with correct salt",
                PasswordUtil.verifyPassword(emptyPassword, hashedEmptyPassword, saltForEmpty));
        assertFalse("Verification should fail for empty password with wrong salt",
                PasswordUtil.verifyPassword(emptyPassword, hashedEmptyPassword, wrongSalt)); // using wrongSalt from above
    }

    @Test
    public void testHashingConsistency() {
        String password = "consistentPassword";
        byte[] salt = PasswordUtil.generateSalt(); // Generate salt once

        String hash1 = PasswordUtil.hashPassword(password, salt);
        String hash2 = PasswordUtil.hashPassword(password, salt); // Use the exact same salt

        assertEquals("Hashing the same password with the same salt twice should yield the same result", hash1, hash2);
    }
}
