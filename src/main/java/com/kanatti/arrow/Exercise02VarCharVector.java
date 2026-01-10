package com.kanatti.arrow;

import org.apache.arrow.memory.BufferAllocator;
import org.apache.arrow.memory.RootAllocator;
import org.apache.arrow.vector.VarCharVector;

/**
 * Exercise 2: Variable-Width Vectors
 * 
 * Goal: Create a VarCharVector (UTF-8 strings) and understand variable-width storage.
 * 
 * What you'll learn:
 * - How variable-width data is stored (offsets + contiguous data buffer)
 * - The three-buffer structure: validity, offsets, data
 * - Memory efficiency differences between fixed and variable-width types
 * - Why offset buffers use cumulative positions
 * 
 * Success criteria:
 * - Store strings of different lengths
 * - Understand how to calculate string length from offset differences
 * - Manually inspect the three buffers
 * - Recognize when reallocation happens (capacity growth)
 */
public class Exercise02VarCharVector {

    public static void main(String[] args) {
        System.out.println("\n\nExercise 2: VarCharVector\n");
        System.out.println("=========================\n");

        try (BufferAllocator allocator = new RootAllocator()) {
            VarCharVector varCharVector = new VarCharVector("names", allocator);

            // Create vector: ["Alice", "Bob", NULL, "Diana"]
            varCharVector.allocateNew(4);
            varCharVector.setSafe(0, "Alice".getBytes());
            varCharVector.setSafe(1, "Bob".getBytes());
            varCharVector.setNull(2);
            varCharVector.setSafe(3, "Diana".getBytes());
            varCharVector.setValueCount(4);

            // Read values
            System.out.println("Values:");
            for (int i = 0; i < 4; i++) {
                System.out.println("  [" + i + "] " +
                    (varCharVector.isNull(i) ? "NULL" : new String(varCharVector.get(i))));
            }

            // Inspect offset buffer (n+1 int32 values - cumulative offsets)
            System.out.println("\nOffset Buffer (" + varCharVector.getOffsetBuffer().capacity() + " bytes):");
            for (int i = 0; i <= 4; i++) {
                int offset = varCharVector.getOffsetBuffer().getInt(i * 4);
                System.out.println("  [" + i + "] offset " + (i * 4) + ": " + offset);
            }

            // Inspect data buffer (contiguous UTF-8 bytes)
            System.out.println("\nData Buffer (" + varCharVector.getDataBuffer().capacity() + " bytes):");
            System.out.println("  Contiguous UTF-8 bytes: ");
            for (int i = 0; i < 4; i++) {
                if (!varCharVector.isNull(i)) {
                    int start = varCharVector.getOffsetBuffer().getInt(i * 4);
                    int end = varCharVector.getOffsetBuffer().getInt((i + 1) * 4);
                    int length = end - start;
                    System.out.println("    [" + i + "] bytes " + start + "-" + (end-1) +
                        " (length=" + length + "): " + new String(varCharVector.get(i)));
                }
            }

            // Inspect validity buffer (1 bit per value)
            System.out.println("\nValidity Buffer (" + varCharVector.getValidityBuffer().capacity() + " bytes):");
            byte validityByte = varCharVector.getValidityBuffer().getByte(0);
            String binary = String.format("%8s", Integer.toBinaryString(validityByte & 0xFF)).replace(' ', '0');
            System.out.println("  Byte 0: " + binary);
            for (int i = 0; i < 4; i++) {
                int bit = (validityByte >> i) & 0x01;
                System.out.println("    Bit " + i + ": " + bit + " = " + (bit == 1 ? "valid" : "null"));
            }

            System.out.println("\nKey Insight:");
            System.out.println("  - VarCharVector uses 3 buffers: validity, offsets, data");
            System.out.println("  - Offset buffer has n+1 entries (start/end positions)");
            System.out.println("  - Data buffer stores all strings contiguously");
            System.out.println("  - String length = offset[i+1] - offset[i]");

            varCharVector.close();
        }
        System.out.println("=========================\n\n");
    }
}
