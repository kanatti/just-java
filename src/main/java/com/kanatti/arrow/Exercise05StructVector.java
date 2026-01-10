package com.kanatti.arrow;

import java.nio.charset.StandardCharsets;
import org.apache.arrow.memory.BufferAllocator;
import org.apache.arrow.memory.RootAllocator;
import org.apache.arrow.vector.IntVector;
import org.apache.arrow.vector.VarCharVector;
import org.apache.arrow.vector.complex.StructVector;
import org.apache.arrow.vector.complex.impl.NullableStructWriter;
import org.apache.arrow.vector.complex.writer.IntWriter;
import org.apache.arrow.vector.complex.writer.VarCharWriter;
import org.apache.arrow.vector.types.Types.MinorType;
import org.apache.arrow.vector.types.pojo.FieldType;

/**
 * Exercise 5: Struct Vectors (Nested Data)
 *
 * Goal: Understand how to work with structured/nested data in Arrow.
 *
 * What you'll learn:
 * - How to create vectors with multiple child fields
 * - Working with NullableStructWriter to populate nested data
 * - Reading structured data back using child vectors
 * - Memory layout of struct vectors (validity buffer + child vectors)
 * - Real-world use case: representing records/rows with multiple columns
 *
 * Use case: Store person records with {id: int, name: string}
 * - Each struct represents one complete record
 * - Fields are stored in separate child vectors for efficiency
 * - Null struct = entire record is missing
 */
public class Exercise05StructVector {

    public static void main(String[] args) {
        System.out.println("\n\nExercise 5: StructVector\n");
        System.out.println("========================\n");

        try (BufferAllocator allocator = new RootAllocator()) {
            // Step 1: Create StructVector with child fields
            System.out.println("Step 1: Create StructVector");
            System.out.println("---------------------------");
            StructVector structVector = StructVector.empty("person", allocator);

            // Add child fields: id (int) and name (varchar)
            structVector.addOrGet(
                "id",
                FieldType.nullable(MinorType.INT.getType()),
                IntVector.class
            );
            structVector.addOrGet(
                "name",
                FieldType.nullable(MinorType.VARCHAR.getType()),
                VarCharVector.class
            );
            structVector.allocateNew();

            System.out.println("Created struct with fields:");
            System.out.println("  - id: INT");
            System.out.println("  - name: VARCHAR");

            // Step 2: Write structured data
            System.out.println("\nStep 2: Write Data");
            System.out.println("------------------");

            NullableStructWriter structWriter = structVector.getWriter();
            IntWriter idWriter = structWriter.integer("id");
            VarCharWriter nameWriter = structWriter.varChar("name");

            // Record 0: {id: 101, name: "Alice"}
            structWriter.setPosition(0);
            structWriter.start();
            idWriter.writeInt(101);
            nameWriter.writeVarChar("Alice");
            structWriter.end();

            // Record 1: {id: 102, name: "Bob"}
            structWriter.setPosition(1);
            structWriter.start();
            idWriter.writeInt(102);
            nameWriter.writeVarChar("Bob");
            structWriter.end();

            // Record 2: NULL (entire record is missing)
            structWriter.setPosition(2);
            // Don't call start/end - leaves it null

            // Record 3: {id: 104, name: "Diana"}
            structWriter.setPosition(3);
            structWriter.start();
            idWriter.writeInt(104);
            nameWriter.writeVarChar("Diana");
            structWriter.end();

            structWriter.setValueCount(4);

            System.out.println("Wrote 4 records:");
            System.out.println("  [0] {id: 101, name: \"Alice\"}");
            System.out.println("  [1] {id: 102, name: \"Bob\"}");
            System.out.println("  [2] NULL");
            System.out.println("  [3] {id: 104, name: \"Diana\"}");

            // Step 3: Read data back using child vectors
            System.out.println("\nStep 3: Read Data");
            System.out.println("-----------------");

            IntVector idVector = (IntVector) structVector.getChild("id");
            VarCharVector nameVector = (VarCharVector) structVector.getChild("name");

            System.out.println("Reading via child vectors:");
            for (int i = 0; i < 4; i++) {
                if (structVector.isNull(i)) {
                    System.out.println("  [" + i + "] NULL");
                } else {
                    int id = idVector.get(i);
                    String name = new String(nameVector.get(i), StandardCharsets.UTF_8);
                    System.out.println("  [" + i + "] {id: " + id + ", name: \"" + name + "\"}");
                }
            }

            // Step 4: Inspect memory layout
            System.out.println("\nStep 4: Memory Layout");
            System.out.println("---------------------");

            System.out.println("Struct validity buffer: " +
                structVector.getValidityBuffer().capacity() + " bytes");
            byte validityByte = structVector.getValidityBuffer().getByte(0);
            String binary = String.format("%8s", Integer.toBinaryString(validityByte & 0xFF))
                .replace(' ', '0');
            System.out.println("  Byte 0: " + binary);
            for (int i = 0; i < 4; i++) {
                int bit = (validityByte >> i) & 0x01;
                System.out.println("    Bit " + i + ": " + bit +
                    " = " + (bit == 1 ? "valid" : "null"));
            }

            System.out.println("\nChild vector buffers:");
            System.out.println("  id (IntVector):");
            System.out.println("    - Data buffer: " +
                idVector.getDataBuffer().capacity() + " bytes");
            System.out.println("  name (VarCharVector):");
            System.out.println("    - Offset buffer: " +
                nameVector.getOffsetBuffer().capacity() + " bytes");
            System.out.println("    - Data buffer: " +
                nameVector.getDataBuffer().capacity() + " bytes");

            System.out.println("\nKey Insight:");
            System.out.println("  - StructVector stores fields in separate child vectors");
            System.out.println("  - Columnar storage: all ids together, all names together");
            System.out.println("  - Struct validity marks entire record as null/valid");
            System.out.println("  - Excellent for analytics: can read just the columns you need");

            structVector.close();
        }

        System.out.println("\n========================\n\n");
    }
}
