package com.epms.util;

import org.mindrot.jbcrypt.BCrypt;

/** Provides BCrypt password hashing and verification. */
public final class PasswordUtil {
    private PasswordUtil() { }
    /** Hashes a raw password using BCrypt. */
    public static String hash(String rawPassword) { return BCrypt.hashpw(rawPassword, BCrypt.gensalt(12)); }
    /** Verifies a raw password against a BCrypt hash. */
    public static boolean matches(String rawPassword, String hash) {
        return rawPassword != null && hash != null && BCrypt.checkpw(rawPassword, hash);
    }
}
