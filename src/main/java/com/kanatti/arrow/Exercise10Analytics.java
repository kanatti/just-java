package com.kanatti.arrow;

import org.apache.arrow.memory.BufferAllocator;
import org.apache.arrow.memory.RootAllocator;
import org.apache.arrow.vector.IntVector;

/**
 * Exercise 10: Vector Analytics Operations
 *
 * Goal: Perform analytical operations on Arrow vectors.
 *
 * What you'll learn:
 * - How to iterate over vectors efficiently
 * - Implementing aggregations (sum, avg, min, max)
 * - Filtering operations (creating new vectors from predicates)
 * - Working with null values in calculations
 * - Why Arrow's columnar format makes analytics fast
 *
 * Use case: Data analytics pipeline
 * - Calculate statistics on large datasets
 * - Filter data based on conditions
 * - Build aggregation functions
 * - Understand performance benefits of columnar format
 */
public class Exercise10Analytics {

    public static void main(String[] args) {
        System.out.println("\n\nExercise 10: Vector Analytics\n");
        System.out.println("==============================\n");

        try (BufferAllocator allocator = new RootAllocator()) {
            // Step 1: Create sample data
            System.out.println("Step 1: Create Sample Data");
            System.out.println("--------------------------");

            IntVector salesVector = new IntVector("daily_sales", allocator);
            salesVector.allocateNew(10);

            // Daily sales: [100, 150, 200, NULL, 180, 220, 190, 210, NULL, 250]
            salesVector.setSafe(0, 100);
            salesVector.setSafe(1, 150);
            salesVector.setSafe(2, 200);
            salesVector.setNull(3);
            salesVector.setSafe(4, 180);
            salesVector.setSafe(5, 220);
            salesVector.setSafe(6, 190);
            salesVector.setSafe(7, 210);
            salesVector.setNull(8);
            salesVector.setSafe(9, 250);
            salesVector.setValueCount(10);

            System.out.println("Daily sales data (10 days):");
            for (int i = 0; i < 10; i++) {
                System.out.println("  Day " + i + ": " +
                    (salesVector.isNull(i) ? "NULL" : salesVector.get(i)));
            }

            // Step 2: Calculate statistics
            System.out.println("\nStep 2: Calculate Statistics");
            System.out.println("-----------------------------");

            int count = 0;
            long sum = 0;
            int min = Integer.MAX_VALUE;
            int max = Integer.MIN_VALUE;

            for (int i = 0; i < salesVector.getValueCount(); i++) {
                if (!salesVector.isNull(i)) {
                    int value = salesVector.get(i);
                    count++;
                    sum += value;
                    min = Math.min(min, value);
                    max = Math.max(max, value);
                }
            }

            double average = count > 0 ? (double) sum / count : 0;

            System.out.println("Statistics:");
            System.out.println("  Count (non-null): " + count);
            System.out.println("  Sum: " + sum);
            System.out.println("  Average: " + String.format("%.2f", average));
            System.out.println("  Min: " + min);
            System.out.println("  Max: " + max);

            // Step 3: Filter operation
            System.out.println("\nStep 3: Filter Operation");
            System.out.println("------------------------");

            // Filter: sales > 200
            System.out.println("Filter: daily_sales > 200");

            IntVector filteredVector = new IntVector("filtered_sales", allocator);
            filteredVector.allocateNew(10); // Max possible size

            int filteredIndex = 0;
            for (int i = 0; i < salesVector.getValueCount(); i++) {
                if (!salesVector.isNull(i) && salesVector.get(i) > 200) {
                    filteredVector.setSafe(filteredIndex, salesVector.get(i));
                    System.out.println("  Day " + i + ": " + salesVector.get(i) + " (included)");
                    filteredIndex++;
                }
            }
            filteredVector.setValueCount(filteredIndex);

            System.out.println("\nFiltered result (" + filteredIndex + " days):");
            for (int i = 0; i < filteredVector.getValueCount(); i++) {
                System.out.println("  [" + i + "] " + filteredVector.get(i));
            }

            // Step 4: Transformation
            System.out.println("\nStep 4: Transformation");
            System.out.println("----------------------");

            // Apply 10% discount (multiply by 0.9)
            System.out.println("Apply 10% discount to all sales:");

            IntVector discountedVector = new IntVector("discounted_sales", allocator);
            discountedVector.allocateNew(10);

            for (int i = 0; i < salesVector.getValueCount(); i++) {
                if (salesVector.isNull(i)) {
                    discountedVector.setNull(i);
                } else {
                    int discounted = (int) (salesVector.get(i) * 0.9);
                    discountedVector.setSafe(i, discounted);
                }
            }
            discountedVector.setValueCount(10);

            System.out.println("Original vs Discounted:");
            for (int i = 0; i < 5; i++) { // Show first 5 days
                String original = salesVector.isNull(i) ? "NULL" : String.valueOf(salesVector.get(i));
                String discounted = discountedVector.isNull(i) ? "NULL" : String.valueOf(discountedVector.get(i));
                System.out.println("  Day " + i + ": " + original + " -> " + discounted);
            }

            // Step 5: Performance insights
            System.out.println("\nStep 5: Performance Benefits");
            System.out.println("----------------------------");

            System.out.println("Why Arrow is fast for analytics:");
            System.out.println("  1. Columnar layout:");
            System.out.println("     - All values contiguous in memory");
            System.out.println("     - CPU cache-friendly access patterns");
            System.out.println("     - No pointer chasing (unlike row-based)");
            System.out.println("\n  2. SIMD vectorization:");
            System.out.println("     - Process multiple values per CPU instruction");
            System.out.println("     - Modern CPUs can do 4-8 operations at once");
            System.out.println("     - Arrow's layout enables auto-vectorization");
            System.out.println("\n  3. Null handling:");
            System.out.println("     - Separate validity buffer");
            System.out.println("     - Bit-packed for memory efficiency");
            System.out.println("     - Can skip nulls in batch operations");
            System.out.println("\n  4. Zero-copy:");
            System.out.println("     - Share vectors across functions");
            System.out.println("     - No serialization overhead");
            System.out.println("     - Memory-map large datasets");

            System.out.println("\nCommon analytics patterns:");
            System.out.println("  - Aggregations: sum, avg, min, max, count");
            System.out.println("  - Filtering: create new vectors from predicates");
            System.out.println("  - Transformations: map operations (discount, tax, etc)");
            System.out.println("  - Joins: combine multiple vectors");
            System.out.println("  - Group-by: partition and aggregate");

            salesVector.close();
            filteredVector.close();
            discountedVector.close();
        }

        System.out.println("\n==============================\n\n");
    }
}
