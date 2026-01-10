package com.kanatti.arrow;

import org.apache.arrow.memory.BufferAllocator;
import org.apache.arrow.memory.RootAllocator;
import org.apache.arrow.vector.BigIntVector;
import org.apache.arrow.vector.IntVector;
import org.apache.arrow.vector.complex.RunEndEncodedVector;
import org.apache.arrow.vector.types.Types;
import org.apache.arrow.vector.types.pojo.ArrowType;
import org.apache.arrow.vector.types.pojo.Field;
import org.apache.arrow.vector.types.pojo.FieldType;

/**
 * Exercise 4: Run-Length Encoding (RLE)
 *
 * Goal: Understand how RLE compresses consecutive repeated values.
 *
 * What you'll learn:
 * - How RLE stores runs of same value efficiently
 * - The two-vector structure: run_ends + values
 * - Space savings for data with long runs
 * - The trade-off: random access requires binary search
 *
 * Use case: Time series with stable periods
 * - Without RLE: [100, 100, 100, 100, 200, 200, 200, 300, 300] (9 values)
 * - With RLE: run_ends=[4, 7, 9], values=[100, 200, 300] (3 runs!)
 */
public class Exercise04RunEndEncoding {

    public static void main(String[] args) {
        System.out.println("\n\nExercise 4: Run-End Encoding (RLE)\n");
        System.out.println("==================================\n");

        try (BufferAllocator allocator = new RootAllocator()) {
            // Step 1: Create RLE vector structure
            System.out.println("Step 1: Setup RLE Vector");
            System.out.println("------------------------");

            // Define the field type for RLE
            FieldType reeFieldType = FieldType.notNullable(
                ArrowType.RunEndEncoded.INSTANCE
            );
            Field reeField = new Field("temperature_rle", reeFieldType, null);

            RunEndEncodedVector reeVector = new RunEndEncodedVector(
                reeField,
                allocator,
                null
            );
            reeVector.allocateNew();

            System.out.println("RLE Vector created with:");
            System.out.println(
                "  - Run ends vector (IntVector): stores cumulative end positions"
            );
            System.out.println(
                "  - Values vector (BigIntVector): stores the actual values"
            );

            // Step 2: Populate RLE vector representing:
            // [100, 100, 100, 100, 100, 200, 200, 200, 200, 300, 300, 300]
            System.out.println("\nStep 2: Encode Data");
            System.out.println("-------------------");

            System.out.println(
                "Logical data: [100, 100, 100, 100, 100, 200, 200, 200, 200, 300, 300, 300]"
            );
            System.out.println("12 total values with 3 distinct runs\n");

            // Get the child vectors
            IntVector runEndsVector = (IntVector) reeVector.getRunEndsVector();
            BigIntVector valuesVector =
                (BigIntVector) reeVector.getValuesVector();

            // Run 1: Value 100 appears 5 times (positions 0-4)
            valuesVector.set(0, 100);
            runEndsVector.set(0, 5); // Run ends at position 5 (exclusive)

            // Run 2: Value 200 appears 4 times (positions 5-8)
            valuesVector.set(1, 200);
            runEndsVector.set(1, 9); // Run ends at position 9 (exclusive)

            // Run 3: Value 300 appears 3 times (positions 9-11)
            valuesVector.set(2, 300);
            runEndsVector.set(2, 12); // Run ends at position 12 (exclusive)

            // Set counts
            valuesVector.setValueCount(3);
            runEndsVector.setValueCount(3);
            reeVector.setValueCount(12); // Logical value count

            System.out.println("Encoded as 3 runs:");
            for (int i = 0; i < 3; i++) {
                System.out.println(
                    "  Run " +
                        i +
                        ": value=" +
                        valuesVector.get(i) +
                        ", ends_at=" +
                        runEndsVector.get(i)
                );
            }

            // Step 3: Read values back (demonstrates random access)
            System.out.println("\nStep 3: Random Access");
            System.out.println("---------------------");
            System.out.println("Reading individual positions:");

            int[] testPositions = { 0, 2, 4, 5, 7, 9, 11 };
            for (int pos : testPositions) {
                Object value = reeVector.getObject(pos);
                System.out.println("  Position " + pos + ": " + value);
            }

            // Step 4: Compare storage
            System.out.println("\nStep 4: Storage Analysis");
            System.out.println("------------------------");

            int logicalCount = 12;
            int physicalRuns = 3;

            System.out.println("Without RLE:");
            System.out.println("  - 12 BigInt values × 8 bytes = 96 bytes");
            System.out.println("\nWith RLE:");
            System.out.println("  - 3 run_ends (Int) × 4 bytes = 12 bytes");
            System.out.println("  - 3 values (BigInt) × 8 bytes = 24 bytes");
            System.out.println("  - Total: 36 bytes");
            System.out.println("\nSpace saved: 60 bytes (62.5% compression!)");

            // Step 5: Real-world example
            System.out.println("\nStep 5: Real-World Example");
            System.out.println("--------------------------");
            System.out.println("RLE is excellent for:");
            System.out.println("  - Time series with stable periods");
            System.out.println("  - Boolean flags (long runs of true/false)");
            System.out.println(
                "  - Status codes (pending → processing → complete)"
            );
            System.out.println("  - Sparse data (many consecutive zeros)");

            System.out.println("\nKey Insight:");
            System.out.println(
                "  - RLE compresses runs of consecutive identical values"
            );
            System.out.println("  - Best when data has long runs (10+)");
            System.out.println(
                "  - Trade-off: Random access requires binary search in run_ends"
            );

            reeVector.close();
        }

        System.out.println("\n==================================\n\n");
    }
}
