package com.epms.util;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

/** Writes report rows as RFC-friendly CSV. */
public final class CsvExporter {
    private CsvExporter() { }
    /** Writes a header and escaped rows. */
    public static void write(Writer writer, List<String[]> rows) throws IOException {
        for (String[] row : rows) {
            for (int i = 0; i < row.length; i++) {
                if (i > 0) writer.write(',');
                writer.write('"');
                writer.write(row[i] == null ? "" : row[i].replace("\"", "\"\""));
                writer.write('"');
            }
            writer.write("\r\n");
        }
    }
}
