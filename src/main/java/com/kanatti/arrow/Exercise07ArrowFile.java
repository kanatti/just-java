package com.kanatti.arrow;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import org.apache.arrow.memory.BufferAllocator;
import org.apache.arrow.memory.RootAllocator;
import org.apache.arrow.vector.FieldVector;
import org.apache.arrow.vector.IntVector;
import org.apache.arrow.vector.VarCharVector;
import org.apache.arrow.vector.VectorSchemaRoot;
import org.apache.arrow.vector.ipc.ArrowFileReader;
import org.apache.arrow.vector.ipc.ArrowFileWriter;
import org.apache.arrow.vector.types.pojo.ArrowType;
import org.apache.arrow.vector.types.pojo.Field;
import org.apache.arrow.vector.types.pojo.FieldType;
import org.apache.arrow.vector.types.pojo.Schema;

/**
 * Exercise 7: Arrow IPC (Inter-Process Communication)
 *
 * Goal: Learn how to serialize and deserialize Arrow data to files.
 *
 * What you'll learn:
 * - How to create a Schema for your data
 * - Working with VectorSchemaRoot (tabular data container)
 * - Writing Arrow data to .arrow files (IPC format)
 * - Reading Arrow data back from files
 * - Zero-copy data sharing between processes/languages
 * - The Arrow IPC format structure (schema + record batches)
 *
 * Use case: Save analytics results to disk for later use
 * - Language-agnostic format (read in Python, R, etc)
 * - Fast serialization (zero-copy where possible)
 * - Self-describing format (schema included)
 */
public class Exercise07ArrowFile {

    public static void main(String[] args) throws Exception {
        System.out.println("\n\nExercise 7: Arrow File (IPC)\n");
        System.out.println("============================\n");

        File arrowFile = new File("people.arrow");

        // Step 1: Write data to Arrow file
        System.out.println("Step 1: Write to Arrow File");
        System.out.println("----------------------------");

        try (BufferAllocator allocator = new RootAllocator()) {
            // Define schema
            Field idField = new Field("id", FieldType.nullable(new ArrowType.Int(32, true)), null);
            Field nameField = new Field("name", FieldType.nullable(new ArrowType.Utf8()), null);
            Schema schema = new Schema(Arrays.asList(idField, nameField));

            System.out.println("Schema:");
            System.out.println("  Field 0: id (INT32)");
            System.out.println("  Field 1: name (UTF8)");

            // Create vectors
            IntVector idVector = new IntVector("id", allocator);
            VarCharVector nameVector = new VarCharVector("name", allocator);

            // Populate data: 3 records
            idVector.allocateNew(3);
            nameVector.allocateNew(3);

            idVector.setSafe(0, 101);
            nameVector.setSafe(0, "Alice".getBytes(StandardCharsets.UTF_8));

            idVector.setSafe(1, 102);
            nameVector.setSafe(1, "Bob".getBytes(StandardCharsets.UTF_8));

            idVector.setSafe(2, 103);
            nameVector.setSafe(2, "Charlie".getBytes(StandardCharsets.UTF_8));

            idVector.setValueCount(3);
            nameVector.setValueCount(3);

            System.out.println("\nData to write:");
            for (int i = 0; i < 3; i++) {
                System.out.println("  [" + i + "] id=" + idVector.get(i) +
                    ", name=" + new String(nameVector.get(i), StandardCharsets.UTF_8));
            }

            // Create VectorSchemaRoot and write to file
            VectorSchemaRoot root = new VectorSchemaRoot(
                schema,
                Arrays.asList((FieldVector) idVector, nameVector),
                3
            );

            try (FileOutputStream fileOutputStream = new FileOutputStream(arrowFile);
                 ArrowFileWriter writer = new ArrowFileWriter(
                     root,
                     null,
                     fileOutputStream.getChannel())) {

                writer.start();
                writer.writeBatch();
                writer.end();

                System.out.println("\nWrote to file: " + arrowFile.getAbsolutePath());
                System.out.println("File size: " + arrowFile.length() + " bytes");
            }

            idVector.close();
            nameVector.close();
        }

        // Step 2: Read data from Arrow file
        System.out.println("\nStep 2: Read from Arrow File");
        System.out.println("-----------------------------");

        try (BufferAllocator allocator = new RootAllocator();
             FileInputStream fileInputStream = new FileInputStream(arrowFile);
             ArrowFileReader reader = new ArrowFileReader(
                 fileInputStream.getChannel(),
                 allocator)) {

            System.out.println("Schema from file:");
            Schema schema = reader.getVectorSchemaRoot().getSchema();
            for (Field field : schema.getFields()) {
                System.out.println("  " + field.getName() + ": " + field.getType());
            }

            // Read the batch
            reader.loadNextBatch();
            VectorSchemaRoot root = reader.getVectorSchemaRoot();

            System.out.println("\nData read from file:");
            IntVector idVector = (IntVector) root.getVector("id");
            VarCharVector nameVector = (VarCharVector) root.getVector("name");

            for (int i = 0; i < root.getRowCount(); i++) {
                System.out.println("  [" + i + "] id=" + idVector.get(i) +
                    ", name=" + new String(nameVector.get(i), StandardCharsets.UTF_8));
            }

            System.out.println("\nRow count: " + root.getRowCount());
        }

        // Step 3: Key insights
        System.out.println("\nStep 3: Arrow IPC Benefits");
        System.out.println("--------------------------");
        System.out.println("  - Self-describing: schema embedded in file");
        System.out.println("  - Fast: zero-copy reads where possible");
        System.out.println("  - Language-agnostic: read in Python, R, C++, etc");
        System.out.println("  - Efficient: columnar format, good compression");
        System.out.println("  - Streaming: can write/read batches incrementally");

        System.out.println("\nFile format:");
        System.out.println("  1. Magic bytes (ARROW1)");
        System.out.println("  2. Schema metadata");
        System.out.println("  3. Record batches (can be multiple)");
        System.out.println("  4. Footer with batch offsets");
        System.out.println("  5. Magic bytes (ARROW1)");

        // Cleanup
        arrowFile.delete();
        System.out.println("\nCleaned up: deleted " + arrowFile.getName());

        System.out.println("\n============================\n\n");
    }
}
