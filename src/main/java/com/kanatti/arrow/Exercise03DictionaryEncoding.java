package com.kanatti.arrow;

import java.nio.charset.StandardCharsets;
import org.apache.arrow.memory.BufferAllocator;
import org.apache.arrow.memory.RootAllocator;
import org.apache.arrow.vector.IntVector;
import org.apache.arrow.vector.ValueVector;
import org.apache.arrow.vector.VarCharVector;
import org.apache.arrow.vector.dictionary.Dictionary;
import org.apache.arrow.vector.dictionary.DictionaryEncoder;
import org.apache.arrow.vector.types.pojo.ArrowType;
import org.apache.arrow.vector.types.pojo.DictionaryEncoding;

/**
 * Exercise 3: Dictionary Encoding
 *
 * Goal: Understand how dictionary encoding compresses repeated values.
 *
 * What you'll learn:
 * - How dictionary encoding works (separate dictionary + indices)
 * - Space savings when you have many repeated values
 * - How to encode and decode vectors
 * - The trade-off: compression vs random access complexity
 *
 * Use case: Column with city names ["New York", "New York", "Paris", "New York", "Paris"]
 * - Without encoding: 5 strings (variable length, lots of bytes)
 * - With encoding: Dictionary ["New York", "Paris"] + Indices [0, 0, 1, 0, 1] (much smaller!)
 */
public class Exercise03DictionaryEncoding {

    public static void main(String[] args) {
        System.out.println("\n\nExercise 3: Dictionary Encoding\n");
        System.out.println("=================================\n");

        try (BufferAllocator allocator = new RootAllocator()) {
            // Step 1: Create the dictionary (unique values)
            System.out.println("Step 1: Create Dictionary");
            System.out.println("-------------------------");
            VarCharVector dictionaryVector = new VarCharVector(
                "cities_dict",
                allocator
            );
            dictionaryVector.allocateNew(3);
            dictionaryVector.set(
                0,
                "New York".getBytes(StandardCharsets.UTF_8)
            );
            dictionaryVector.set(1, "Paris".getBytes(StandardCharsets.UTF_8));
            dictionaryVector.set(2, "Tokyo".getBytes(StandardCharsets.UTF_8));
            dictionaryVector.setValueCount(3);

            System.out.println("Dictionary values:");
            for (int i = 0; i < dictionaryVector.getValueCount(); i++) {
                System.out.println(
                    "  Index " + i + ": " + new String(dictionaryVector.get(i))
                );
            }

            // Wrap in Dictionary object with encoding metadata
            DictionaryEncoding encoding = new DictionaryEncoding(
                1L, // dictionary id
                false, // not ordered
                new ArrowType.Int(32, true) // use 32-bit int for indices
            );
            Dictionary dictionary = new Dictionary(dictionaryVector, encoding);

            // Step 2: Create original data (many repeated values)
            System.out.println("\nStep 2: Create Original Data");
            System.out.println("-----------------------------");
            VarCharVector originalVector = new VarCharVector(
                "cities",
                allocator
            );
            originalVector.allocateNew(10);
            originalVector.set(0, "New York".getBytes(StandardCharsets.UTF_8));
            originalVector.set(1, "New York".getBytes(StandardCharsets.UTF_8));
            originalVector.set(2, "Paris".getBytes(StandardCharsets.UTF_8));
            originalVector.set(3, "New York".getBytes(StandardCharsets.UTF_8));
            originalVector.set(4, "Tokyo".getBytes(StandardCharsets.UTF_8));
            originalVector.set(5, "Paris".getBytes(StandardCharsets.UTF_8));
            originalVector.set(6, "New York".getBytes(StandardCharsets.UTF_8));
            originalVector.set(7, "Tokyo".getBytes(StandardCharsets.UTF_8));
            originalVector.set(8, "Paris".getBytes(StandardCharsets.UTF_8));
            originalVector.set(9, "New York".getBytes(StandardCharsets.UTF_8));
            originalVector.setValueCount(10);

            System.out.println("Original values (10 strings):");
            for (int i = 0; i < originalVector.getValueCount(); i++) {
                System.out.println(
                    "  [" + i + "] " + new String(originalVector.get(i))
                );
            }
            System.out.println(
                "Original data buffer size: " +
                    originalVector.getDataBuffer().capacity() +
                    " bytes"
            );

            // Step 3: Encode using dictionary
            System.out.println("\nStep 3: Dictionary Encode");
            System.out.println("-------------------------");
            ValueVector encodedVector = DictionaryEncoder.encode(
                originalVector,
                dictionary
            );
            IntVector indices = (IntVector) encodedVector;

            System.out.println("Encoded as indices:");
            for (int i = 0; i < indices.getValueCount(); i++) {
                System.out.println("  [" + i + "] index=" + indices.get(i));
            }
            System.out.println(
                "\nEncoded indices buffer size: " +
                    indices.getDataBuffer().capacity() +
                    " bytes"
            );
            System.out.println(
                "Dictionary buffer size: " +
                    dictionaryVector.getDataBuffer().capacity() +
                    " bytes"
            );
            System.out.println(
                "Total encoded size: " +
                    (indices.getDataBuffer().capacity() +
                        dictionaryVector.getDataBuffer().capacity()) +
                    " bytes"
            );

            // Step 4: Decode back to original values
            System.out.println("\nStep 4: Decode Back");
            System.out.println("-------------------");
            ValueVector decodedVector = DictionaryEncoder.decode(
                indices,
                dictionary
            );
            VarCharVector decoded = (VarCharVector) decodedVector;

            System.out.println("Decoded values (should match original):");
            for (int i = 0; i < decoded.getValueCount(); i++) {
                System.out.println(
                    "  [" + i + "] " + new String(decoded.get(i))
                );
            }

            // Step 5: Space savings analysis
            System.out.println("\nStep 5: Space Analysis");
            System.out.println("----------------------");
            long originalSize = originalVector.getDataBuffer().capacity();
            long encodedSize =
                indices.getDataBuffer().capacity() +
                dictionaryVector.getDataBuffer().capacity();
            System.out.println(
                "Space saved: " + (originalSize - encodedSize) + " bytes"
            );
            System.out.println(
                "Compression ratio: " +
                    String.format(
                        "%.2f%%",
                        (1.0 - (double) encodedSize / originalSize) * 100
                    )
            );

            System.out.println("\nKey Insight:");
            System.out.println(
                "- The more repetition, the better the compression!"
            );
            System.out.println(
                "- Dictionary encoding is perfect for categorical data"
            );
            System.out.println(
                "- Trade-off: Reading requires dictionary lookup"
            );

            // Cleanup
            originalVector.close();
            dictionaryVector.close();
            encodedVector.close();
            decodedVector.close();
        }

        System.out.println("\n=================================\n\n");
    }
}
