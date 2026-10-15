package com.epms.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

/**
 * Creates the initial PostgreSQL schema when the users table is absent.
 */
public final class SchemaInitializer {
    private static final Logger LOGGER = Logger.getLogger(SchemaInitializer.class.getName());

    private SchemaInitializer() {
    }

    /**
     * Initializes the schema once when the application starts.
     *
     * @throws SQLException if schema inspection or creation fails
     * @throws IOException if schema.sql cannot be read
     */
    public static void initialize() throws SQLException, IOException {
        try (Connection connection = DBConnection.getConnection()) {
            if (usersTableExists(connection)) {
                return;
            }
            String schema = readSchema();
            try (Statement statement = connection.createStatement()) {
                executeStatements(statement, schema);
                String demoSeed = readResource("/demo-seed.sql");
                executeStatements(statement, demoSeed);
            }
            LOGGER.info("Database schema initialized");
        }
    }

    private static boolean usersTableExists(Connection connection) throws SQLException {
        try (ResultSet resultSet = connection.getMetaData().getTables(null, null, "users", null)) {
            return resultSet.next();
        }
    }

    private static String readSchema() throws IOException {
        return readResource("/schema.sql");
    }

    private static String readResource(String resourceName) throws IOException {
        try (InputStream stream = SchemaInitializer.class.getResourceAsStream(resourceName)) {
            if (stream == null) {
                throw new IOException(resourceName + " resource is missing");
            }
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    private static void executeStatements(Statement statement, String sql) throws SQLException {
        for (String query : sql.split(";")) {
            if (!query.trim().isEmpty()) {
                statement.execute(query);
            }
        }
    }
}
