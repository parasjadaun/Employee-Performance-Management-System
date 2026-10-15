package com.epms.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Owns the application-wide HikariCP PostgreSQL connection pool.
 */
public final class DBConnection {
    private static final int MAX_POOL_SIZE = 5;
    private static HikariDataSource dataSource;

    private DBConnection() {
    }

    /**
     * Initializes the pool from environment variables.
     */
    public static synchronized void initialize() {
        if (dataSource != null) {
            return;
        }
        String neonUrl = environment("DATABASE_URL");
        String url;
        String user;
        String password;
        if (neonUrl != null) {
            DatabaseSettings settings = parseNeonUrl(neonUrl);
            url = settings.jdbcUrl();
            user = settings.user();
            password = settings.password();
        } else {
            url = requireEnvironment("DB_URL");
            user = requireEnvironment("DB_USER");
            password = requireEnvironment("DB_PASSWORD");
        }
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setDriverClassName("org.postgresql.Driver");
        config.setUsername(user);
        config.setPassword(password);
        config.setMaximumPoolSize(MAX_POOL_SIZE);
        config.setPoolName("epms-pool");
        dataSource = new HikariDataSource(config);
    }

    /**
     * Returns a pooled database connection.
     *
     * @return database connection
     * @throws SQLException if a connection cannot be acquired
     */
    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            initialize();
        }
        return dataSource.getConnection();
    }

    /**
     * Closes the application-wide pool.
     */
    public static synchronized void close() {
        if (dataSource != null) {
            dataSource.close();
            dataSource = null;
        }
    }

    private static String requireEnvironment(String name) {
        return Objects.requireNonNull(environment(name),
                "Required environment variable is missing: " + name);
    }

    private static String environment(String name) {
        String value = System.getenv(name);
        return value == null || value.isBlank() ? null : value.trim().replaceAll("^\"|\"$", "");
    }

    private static DatabaseSettings parseNeonUrl(String rawUrl) {
        try {
            URI uri = new URI(rawUrl);
            if (uri.getUserInfo() == null || uri.getHost() == null) {
                throw new IllegalArgumentException("DATABASE_URL must include credentials and a host");
            }
            String[] credentials = uri.getUserInfo().split(":", 2);
            if (credentials.length != 2) {
                throw new IllegalArgumentException("DATABASE_URL must include a username and password");
            }
            String jdbcUrl = "jdbc:postgresql://" + uri.getRawAuthority().substring(uri.getRawUserInfo().length() + 1)
                    + uri.getRawPath() + (uri.getRawQuery() == null ? "" : "?" + uri.getRawQuery());
            return new DatabaseSettings(jdbcUrl, decode(credentials[0]), decode(credentials[1]));
        } catch (URISyntaxException | IllegalArgumentException exception) {
            throw new IllegalArgumentException("Invalid DATABASE_URL", exception);
        }
    }

    private static String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }

    private record DatabaseSettings(String jdbcUrl, String user, String password) {
    }
}
