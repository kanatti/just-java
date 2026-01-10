package com.kanatti.arrow;

import org.apache.arrow.memory.ArrowBuf;
import org.apache.arrow.memory.BufferAllocator;
import org.apache.arrow.memory.RootAllocator;
import org.apache.arrow.vector.IntVector;

/**
 * Exercise 1: IntVector Basics
 *
 * Learn: Two-buffer system (data + validity), vector lifecycle
 */
public class Exercise01IntVector {

    public static void main(String[] args) {
        System.out.println("\n=== Exercise 1: IntVector ===\n");
        Display d = new Display();

        try (BufferAllocator allocator = new RootAllocator()) {
            IntVector vector = new IntVector("id", allocator);

            // Create vector
            vector.allocateNew(4);
            vector.setSafe(0, 100);
            vector.setSafe(1, 200);
            vector.setNull(2);
            vector.setSafe(3, 400);
            vector.setValueCount(4);

            // Read values
            d.show("vector.get(0)", vector.get(0));
            d.show("vector.get(1)", vector.get(1));
            d.show("vector.isNull(2)", vector.isNull(2));
            d.show("vector.get(3)", vector.get(3));

            // Inspect data buffer
            System.out.println();
            d.section("Data Buffer", () -> {
                ArrowBuf dataBuf = vector.getDataBuffer();
                d.show("capacity", dataBuf.capacity() + " bytes");
                d.show("getInt(0)", dataBuf.getInt(0));
                d.show("getInt(4)", dataBuf.getInt(4));
                d.show("getInt(8)", dataBuf.getInt(8));
                d.show("getInt(12)", dataBuf.getInt(12));
            });

            // Inspect validity buffer
            System.out.println();
            d.section("Validity Buffer", () -> {
                ArrowBuf validityBuf = vector.getValidityBuffer();
                byte validityByte = validityBuf.getByte(0);
                d.show("capacity", validityBuf.capacity() + " bytes");
                d.show("getByte(0) binary", Display.toBinary(validityByte));
                d.show("bit 0", ((validityByte >> 0) & 0x01) == 1);
                d.show("bit 1", ((validityByte >> 1) & 0x01) == 1);
                d.show("bit 2", ((validityByte >> 2) & 0x01) == 1);
                d.show("bit 3", ((validityByte >> 3) & 0x01) == 1);
            });

            vector.close();
        }
    }
}
