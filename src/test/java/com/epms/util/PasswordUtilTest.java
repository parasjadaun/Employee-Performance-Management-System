package com.epms.util;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
/** Verifies BCrypt hashing behavior. */
class PasswordUtilTest {
    @Test void hashesAndMatchesPassword() {
        String hash = PasswordUtil.hash("secret");
        assertTrue(PasswordUtil.matches("secret", hash));
        assertFalse(PasswordUtil.matches("wrong", hash));
    }
}
