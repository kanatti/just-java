package com.kanatti.antlr.sql;

import com.kanatti.antlr.SimpleSqlLexer;
import com.kanatti.antlr.SimpleSqlParser;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import java.util.ArrayList;
import java.util.List;

/**
 * Example demonstrating how to use ANTLR to parse SQL and transform it into domain objects.
 *
 * This shows the complete flow:
 * 1. Create a Lexer to tokenize the input
 * 2. Create a Parser to build a parse tree
 * 3. Use a Visitor to walk the tree and create domain objects
 */
public class SqlParserExample {

    public static void main(String[] args) {
        System.out.println("=== ANTLR SQL Parser Example ===\n");

        // Example SQL queries to parse
        String[] queries = {
            "SELECT * FROM users",
            "SELECT name, email FROM users WHERE age > 18",
            "SELECT u.id, u.name FROM users WHERE status = 'active' AND age >= 21",
            "SELECT * FROM products WHERE price > 100 OR category = 'electronics'",
            // Error examples
            "SELECT * FROM users WHERE age > 18%",  // Lexer error: % not recognized
            "SELECT FROM users"  // Parser error: missing column list
        };

        for (String sql : queries) {
            System.out.println("Input SQL:");
            System.out.println("  " + sql);
            System.out.println();

            try {
                // Parse the SQL and get domain object
                SelectStatement statement = parseSQL(sql);

                // Display the parsed structure
                System.out.println("Parsed Structure:");
                System.out.println("  Table: " + statement.getTableName());
                System.out.println("  Select All: " + statement.isSelectAll());

                if (!statement.isSelectAll()) {
                    System.out.println("  Columns:");
                    for (Column col : statement.getColumns()) {
                        System.out.println("    - " + col);
                    }
                }

                if (statement.getWhereCondition() != null) {
                    System.out.println("  Where: " + statement.getWhereCondition());
                }

                System.out.println("\nReconstructed SQL:");
                System.out.println("  " + statement);

            } catch (Exception e) {
                System.out.println("PARSE FAILED:");
                System.out.println("  " + e.getMessage());
            }

            System.out.println("\n" + "=".repeat(60) + "\n");
        }

        // Demonstrate accessing domain objects programmatically
        demonstrateDomainObjectAccess();
    }

    /**
     * Parse SQL string into a SelectStatement domain object
     *
     * This is the core ANTLR workflow:
     * 1. CharStreams.fromString() - Convert input to character stream
     * 2. SimpleSqlLexer - Tokenize the character stream
     * 3. CommonTokenStream - Buffer the tokens
     * 4. SimpleSqlParser - Parse tokens into parse tree
     * 5. SqlDomainVisitor - Walk tree to create domain objects
     */
    public static SelectStatement parseSQL(String sql) {
        // Step 1: Create a character stream from the input
        var input = CharStreams.fromString(sql);

        // Step 2: Create a lexer that feeds off the input stream
        SimpleSqlLexer lexer = new SimpleSqlLexer(input);
        ErrorCollector lexerErrors = new ErrorCollector("LEXER");
        lexer.removeErrorListeners();  // Remove default console error listener
        lexer.addErrorListener(lexerErrors);

        // Step 3: Create a buffer of tokens pulled from the lexer
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // Step 4: Create a parser that feeds off the token stream
        SimpleSqlParser parser = new SimpleSqlParser(tokens);
        ErrorCollector parserErrors = new ErrorCollector("PARSER");
        parser.removeErrorListeners();  // Remove default console error listener
        parser.addErrorListener(parserErrors);

        // Step 5: Begin parsing at the 'query' rule (the root of our grammar)
        ParseTree tree = parser.query();

        // Check for errors
        List<String> allErrors = new ArrayList<>();
        allErrors.addAll(lexerErrors.getErrors());
        allErrors.addAll(parserErrors.getErrors());

        if (!allErrors.isEmpty()) {
            throw new RuntimeException(String.join("\n  ", allErrors));
        }

        // Step 6: Walk the parse tree with our custom visitor
        SqlDomainVisitor visitor = new SqlDomainVisitor();
        return (SelectStatement) visitor.visit(tree);
    }

    /**
     * Demonstrate how to work with the domain objects programmatically
     */
    private static void demonstrateDomainObjectAccess() {
        System.out.println("=== Programmatic Access Example ===\n");

        String sql = "SELECT id, name, email FROM users WHERE age > 21 AND status = 'active'";
        SelectStatement stmt = parseSQL(sql);

        // Access properties
        System.out.println("Query Analysis:");
        System.out.println("  - Querying table: " + stmt.getTableName());
        System.out.println("  - Number of columns: " + stmt.getColumns().size());

        // Iterate through columns
        System.out.println("  - Column details:");
        for (Column col : stmt.getColumns()) {
            System.out.println("      * " + col.getColumnName() +
                (col.getTableName() != null ? " (from " + col.getTableName() + ")" : ""));
        }

        // Analyze WHERE condition
        if (stmt.getWhereCondition() instanceof AndCondition) {
            AndCondition andCond = (AndCondition) stmt.getWhereCondition();
            System.out.println("  - WHERE clause uses AND condition");
            System.out.println("      Left: " + andCond.getLeft());
            System.out.println("      Right: " + andCond.getRight());
        }

        System.out.println();
    }

    /**
     * Custom error listener to capture and format error messages
     */
    static class ErrorCollector extends BaseErrorListener {
        private final List<String> errors = new ArrayList<>();
        private final String stage;  // "LEXER" or "PARSER"

        public ErrorCollector(String stage) {
            this.stage = stage;
        }

        @Override
        public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                                int line, int charPositionInLine,
                                String msg, RecognitionException e) {
            errors.add(String.format("[%s ERROR] Line %d:%d - %s",
                                     stage, line, charPositionInLine, msg));
        }

        public boolean hasErrors() {
            return !errors.isEmpty();
        }

        public List<String> getErrors() {
            return errors;
        }
    }
}
