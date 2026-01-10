# Apache Arrow Learning Setup

This directory contains exercises and documentation for learning Apache Arrow Java.

## Setup Complete ✓

The project has been configured with:
- Apache Arrow Vector 18.3.0
- Apache Arrow Memory (Netty) 18.3.0

Note: Upgraded to 18.3.0 for RunEndEncodedVector support (added in 18.2.0)

## Exercise Files

Located in `src/main/java/com/kanatti/arrow/`:

### Basic Vectors
- `Exercise01IntVector.java` - Basic fixed-width vector operations
- `Exercise02VarCharVector.java` - Variable-width vector operations (strings)

### Encoding Techniques
- `Exercise03DictionaryEncoding.java` - Dictionary compression for repeated values
- `Exercise04RunEndEncoding.java` - Run-length encoding for consecutive runs

### Complex Types
- `Exercise05StructVector.java` - Nested/structured data (records with multiple fields)
- `Exercise06ListVector.java` - Variable-length arrays

### Serialization & I/O
- `Exercise07ArrowFile.java` - Writing and reading Arrow IPC files

### Practical Applications
- `Exercise09CSVConverter.java` - Converting CSV data to Arrow format
- `Exercise10Analytics.java` - Analytical operations (sum, filter, transform)

## Running Exercises

Use the `runMain` Gradle task to run any exercise:

```bash
# From the just-java directory
./gradlew runMain -PmainClass=com.kanatti.arrow.Exercise01IntVector
./gradlew runMain -PmainClass=com.kanatti.arrow.Exercise02VarCharVector
./gradlew runMain -PmainClass=com.kanatti.arrow.Exercise03DictionaryEncoding
./gradlew runMain -PmainClass=com.kanatti.arrow.Exercise04RunEndEncoding
./gradlew runMain -PmainClass=com.kanatti.arrow.Exercise05StructVector
./gradlew runMain -PmainClass=com.kanatti.arrow.Exercise06ListVector
./gradlew runMain -PmainClass=com.kanatti.arrow.Exercise07ArrowFile
./gradlew runMain -PmainClass=com.kanatti.arrow.Exercise09CSVConverter
./gradlew runMain -PmainClass=com.kanatti.arrow.Exercise10Analytics
```

## Documentation

See `exercises.md` for the complete learning path with 10 exercises covering:
- Basic vector operations
- Memory management
- Complex types
- Serialization (IPC)
- Real-world applications

## Next Steps

1. Read `exercises.md` to understand the full learning path
2. Start with `Exercise01IntVector.java` - implement the TODOs
3. Run it to see the results
4. Move on to Exercise 2, then create your own for the remaining exercises

## Arrow Java Source

Keep the `arrow-java` repository open for reference:
- Test files are excellent examples
- Located in `vector/src/test/java/org/apache/arrow/vector/`

Happy learning!
