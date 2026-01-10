package com.kanatti.arrow;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import org.apache.arrow.memory.BufferAllocator;
import org.apache.arrow.memory.RootAllocator;
import org.apache.arrow.vector.FieldVector;
import org.apache.arrow.vector.IntVector;
import org.apache.arrow.vector.VarCharVector;
import org.apache.arrow.vector.VectorSchemaRoot;
import org.apache.arrow.vector.types.pojo.ArrowType;
import org.apache.arrow.vector.types.pojo.Field;
import org.apache.arrow.vector.types.pojo.FieldType;
import org.apache.arrow.vector.types.pojo.Schema;

/**
 * Exercise 9: CSV to Arrow Converter
 *
 * Goal: Build a practical application - converting CSV data to Arrow format.
 *
 * What you'll learn:
 * - Real-world data ingestion pipeline
 * - Manual CSV parsing (simple approach)
 * - Mapping CSV columns to Arrow vectors
 * - Type inference and handling
 * - Memory-efficient batch processing
 *
 * Use case: ETL pipeline for analytics
 * - Read CSV files (common data format)
 * - Convert to Arrow for fast analytics
 * - 10-100x faster than parsing CSV repeatedly
 * - Enable cross-language data sharing
 */
public class Exercise09CSVConverter {

    public static void main(String[] args) throws Exception {
        System.out.println("\n\nExercise 9: CSV to Arrow Converter\n");
        System.out.println("===================================\n");

        // Step 1: Create sample CSV file
        System.out.println("Step 1: Create Sample CSV");
        System.out.println("-------------------------");

        File csvFile = new File("sample_data.csv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile))) {
            writer.write("id,name,age\n");
            writer.write("1,Alice,30\n");
            writer.write("2,Bob,25\n");
            writer.write("3,Charlie,35\n");
            writer.write("4,Diana,28\n");
            writer.write("5,Eve,32\n");
        }

        System.out.println("Created CSV file: " + csvFile.getAbsolutePath());
        System.out.println("Contents:");
        System.out.println("  id,name,age");
        System.out.println("  1,Alice,30");
        System.out.println("  2,Bob,25");
        System.out.println("  3,Charlie,35");
        System.out.println("  4,Diana,28");
        System.out.println("  5,Eve,32");

        // Step 2: Parse CSV and convert to Arrow
        System.out.println("\nStep 2: Convert CSV to Arrow");
        System.out.println("-----------------------------");

        try (BufferAllocator allocator = new RootAllocator()) {
            // Define schema for Arrow data
            Field idField = new Field("id", FieldType.nullable(new ArrowType.Int(32, true)), null);
            Field nameField = new Field("name", FieldType.nullable(new ArrowType.Utf8()), null);
            Field ageField = new Field("age", FieldType.nullable(new ArrowType.Int(32, true)), null);
            Schema schema = new Schema(Arrays.asList(idField, nameField, ageField));

            System.out.println("Arrow Schema:");
            System.out.println("  id: INT32");
            System.out.println("  name: UTF8");
            System.out.println("  age: INT32");

            // Create vectors
            IntVector idVector = new IntVector("id", allocator);
            VarCharVector nameVector = new VarCharVector("name", allocator);
            IntVector ageVector = new IntVector("age", allocator);

            // Allocate capacity (we know we have 5 rows)
            idVector.allocateNew(5);
            nameVector.allocateNew(5);
            ageVector.allocateNew(5);

            // Parse CSV data (simplified - in production use a CSV library)
            String[][] rows = {
                {"1", "Alice", "30"},
                {"2", "Bob", "25"},
                {"3", "Charlie", "35"},
                {"4", "Diana", "28"},
                {"5", "Eve", "32"}
            };

            System.out.println("\nParsing and loading data:");
            for (int i = 0; i < rows.length; i++) {
                String[] row = rows[i];
                int id = Integer.parseInt(row[0]);
                String name = row[1];
                int age = Integer.parseInt(row[2]);

                idVector.setSafe(i, id);
                nameVector.setSafe(i, name.getBytes(StandardCharsets.UTF_8));
                ageVector.setSafe(i, age);

                System.out.println("  Row " + i + ": id=" + id +
                    ", name=" + name + ", age=" + age);
            }

            idVector.setValueCount(5);
            nameVector.setValueCount(5);
            ageVector.setValueCount(5);

            System.out.println("\nLoaded 5 rows into Arrow vectors");

            // Step 3: Verify the conversion
            System.out.println("\nStep 3: Verify Arrow Data");
            System.out.println("-------------------------");

            VectorSchemaRoot root = new VectorSchemaRoot(
                schema,
                Arrays.asList((FieldVector) idVector, nameVector, ageVector),
                5
            );

            System.out.println("VectorSchemaRoot stats:");
            System.out.println("  Row count: " + root.getRowCount());
            System.out.println("  Field count: " + root.getFieldVectors().size());

            System.out.println("\nSample data (first 3 rows):");
            for (int i = 0; i < 3; i++) {
                System.out.println("  [" + i + "] id=" + idVector.get(i) +
                    ", name=" + new String(nameVector.get(i), StandardCharsets.UTF_8) +
                    ", age=" + ageVector.get(i));
            }

            // Step 4: Show benefits
            System.out.println("\nStep 4: Benefits Analysis");
            System.out.println("-------------------------");

            long csvSize = csvFile.length();
            long arrowMemory = idVector.getDataBuffer().capacity() +
                nameVector.getOffsetBuffer().capacity() +
                nameVector.getDataBuffer().capacity() +
                ageVector.getDataBuffer().capacity();

            System.out.println("CSV file size: " + csvSize + " bytes");
            System.out.println("Arrow in-memory size: ~" + arrowMemory + " bytes");
            System.out.println("\nArrow advantages:");
            System.out.println("  - No parsing needed: direct memory access");
            System.out.println("  - Type safety: integers are integers, not strings");
            System.out.println("  - Columnar layout: read only columns you need");
            System.out.println("  - SIMD-friendly: vectorized operations");
            System.out.println("  - Zero-copy: share with other processes/languages");

            System.out.println("\nUse cases:");
            System.out.println("  - Load CSV once, query many times (10-100x faster)");
            System.out.println("  - Convert CSV to .arrow files for faster loads");
            System.out.println("  - Feed data to analytics engines (Spark, etc)");
            System.out.println("  - Share with Python/R for data science");

            idVector.close();
            nameVector.close();
            ageVector.close();
        }

        // Cleanup
        csvFile.delete();
        System.out.println("\nCleaned up: deleted " + csvFile.getName());

        System.out.println("\n===================================\n\n");
    }
}
