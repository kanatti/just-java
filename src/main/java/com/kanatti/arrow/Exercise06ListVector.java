package com.kanatti.arrow;

import java.util.ArrayList;
import org.apache.arrow.memory.BufferAllocator;
import org.apache.arrow.memory.RootAllocator;
import org.apache.arrow.vector.IntVector;
import org.apache.arrow.vector.complex.ListVector;
import org.apache.arrow.vector.complex.impl.UnionListWriter;

/**
 * Exercise 6: List Vectors (Arrays of Values)
 *
 * Goal: Understand how to store variable-length arrays in Arrow.
 *
 * What you'll learn:
 * - How ListVector stores arrays of varying lengths
 * - Working with UnionListWriter to add elements
 * - The offset buffer pattern (similar to VarChar but for arrays)
 * - Reading lists back as Java objects
 * - Use cases for array data (tags, time series, nested collections)
 *
 * Use case: Store tags for items (variable-length arrays)
 * - Each row contains a different number of tags
 * - Tags stored contiguously in child vector
 * - Offset buffer tracks where each array starts/ends
 */
public class Exercise06ListVector {

    public static void main(String[] args) {
        System.out.println("\n\nExercise 6: ListVector\n");
        System.out.println("======================\n");

        try (BufferAllocator allocator = new RootAllocator()) {
            // Step 1: Create ListVector
            System.out.println("Step 1: Create ListVector");
            System.out.println("-------------------------");

            ListVector listVector = ListVector.empty("tags", allocator);
            UnionListWriter writer = listVector.getWriter();
            writer.allocate();

            System.out.println("Created ListVector for arrays of integers");

            // Step 2: Write array data
            System.out.println("\nStep 2: Write Arrays");
            System.out.println("--------------------");

            // List 0: [1, 2, 3]
            writer.setPosition(0);
            writer.startList();
            writer.integer().writeInt(1);
            writer.integer().writeInt(2);
            writer.integer().writeInt(3);
            writer.endList();

            // List 1: [10, 20]
            writer.setPosition(1);
            writer.startList();
            writer.integer().writeInt(10);
            writer.integer().writeInt(20);
            writer.endList();

            // List 2: NULL (entire list is missing)
            writer.setPosition(2);
            // Don't call startList/endList

            // List 3: [] (empty list)
            writer.setPosition(3);
            writer.startList();
            writer.endList();

            // List 4: [100, 200, 300, 400]
            writer.setPosition(4);
            writer.startList();
            writer.integer().writeInt(100);
            writer.integer().writeInt(200);
            writer.integer().writeInt(300);
            writer.integer().writeInt(400);
            writer.endList();

            writer.setValueCount(5);

            System.out.println("Wrote 5 lists:");
            System.out.println("  [0] [1, 2, 3]");
            System.out.println("  [1] [10, 20]");
            System.out.println("  [2] NULL");
            System.out.println("  [3] []");
            System.out.println("  [4] [100, 200, 300, 400]");

            // Step 3: Read lists back
            System.out.println("\nStep 3: Read Lists");
            System.out.println("------------------");

            System.out.println("Reading via getObject():");
            for (int i = 0; i < 5; i++) {
                if (listVector.isNull(i)) {
                    System.out.println("  [" + i + "] NULL");
                } else {
                    Object obj = listVector.getObject(i);
                    ArrayList<?> list = (ArrayList<?>) obj;
                    System.out.println("  [" + i + "] " + list +
                        " (length=" + list.size() + ")");
                }
            }

            // Step 4: Inspect buffers
            System.out.println("\nStep 4: Memory Layout");
            System.out.println("---------------------");

            // Validity buffer
            System.out.println("Validity buffer: " +
                listVector.getValidityBuffer().capacity() + " bytes");
            byte validityByte = listVector.getValidityBuffer().getByte(0);
            String binary = String.format("%8s", Integer.toBinaryString(validityByte & 0xFF))
                .replace(' ', '0');
            System.out.println("  Byte 0: " + binary);
            for (int i = 0; i < 5; i++) {
                int bit = (validityByte >> i) & 0x01;
                System.out.println("    Bit " + i + ": " + bit +
                    " = " + (bit == 1 ? "valid" : "null"));
            }

            // Offset buffer (n+1 entries like VarChar)
            System.out.println("\nOffset buffer (" +
                listVector.getOffsetBuffer().capacity() + " bytes):");
            for (int i = 0; i <= 5; i++) {
                int offset = listVector.getOffsetBuffer().getInt(i * 4);
                System.out.println("  [" + i + "] offset " + (i * 4) + ": " + offset);
            }

            // Data vector (the actual integers)
            IntVector dataVector = (IntVector) listVector.getDataVector();
            System.out.println("\nData vector (child IntVector):");
            System.out.println("  Total elements: " + dataVector.getValueCount());
            System.out.println("  Data buffer: " + dataVector.getDataBuffer().capacity() + " bytes");
            System.out.print("  Values: [");
            for (int i = 0; i < dataVector.getValueCount(); i++) {
                if (i > 0) System.out.print(", ");
                System.out.print(dataVector.get(i));
            }
            System.out.println("]");

            System.out.println("\nKey Insight:");
            System.out.println("  - ListVector uses offsets like VarCharVector");
            System.out.println("  - All array elements stored contiguously in child vector");
            System.out.println("  - offset[i+1] - offset[i] = number of elements in list i");
            System.out.println("  - NULL list vs empty list [] are different");
            System.out.println("  - Great for variable-length arrays (tags, time series, etc)");

            listVector.close();
        }

        System.out.println("\n======================\n\n");
    }
}
